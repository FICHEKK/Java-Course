package hr.fer.zemris.java.hw17.trazilica;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

/**
 * Models a search engine for the collection of the textual documents.
 * The search engine first needs to prepare and store some information:
 * <ol>
 * <li> Load the stop-words from the predefined file. </li>
 * <li> Build the vocabulary, taking the stop-words in the consideration. </li>
 * <li> Build the <i>idf</i> vector which is used for creation of all the document vectors. </li>
 * <li> Build all of the document vectors. </li>
 * </ol>
 * 
 * After all the steps are done, the search is a simple process:
 * <ol>
 * <li> Convert the list of query words into a document vector ("query vector"). </li>
 * <li> Check the similarity of the "query vector" against all of the document vectors. </li>
 * <li> Sort the similarities by their value, and those values (associated with their document path) are the query results. </li>
 * </ol>
 *
 * @author Filip Nemec
 */
public class SearchEngine {
	
	/** The maximum number of query results printed by the {@link #printQueryResults()}. */
	private static final int QUERY_RESULTS_PRINT_LIMIT = 10;
	
	/** A set of stop-words. */
	private Set<String> stopwords = new HashSet<>();
	
	/** 
	 * The collection that maps all of the words from all of the documents to the
	 * word information container {@link WordInfo}.
	 * <br>
	 * <br> Can be used to check if a certain word is in the vocabulary, in O(1) time.
	 * <br> Can be used to check the number of different documents specified word is in, in O(1) time.
	 * <br> Can be used to check the number of total number of word occurrences, in O(1) time.
	 */
	private Map<String, WordInfo> vocabulary = new HashMap<>();
	
	/** The <i>inverse document frequency</i> vector. */
	private Vector<Double> idf;
	
	/** A list that holds and caches the processed documents. */
	private List<Document> documents = new LinkedList<>();
	
	/** The number of documents this search engine is processing. */
	private int documentCount;
	
	/**
	 * A tree set containing the query results. The results must be sorted
	 * by similarity, hence the {@code TreeSet} is used.
	 */
	private TreeSet<QueryResult> results;
	
	//------------------------------------------------------------------
	//							CONSTRUCTOR
	//------------------------------------------------------------------
	
	/**
	 * Constructs a new search engine that operates over all of the documents in
	 * located in the hierarchy with root {@link #rootPath}. It also requires a file
	 * containing all of the frequent words (so called "stop-words") that are not
	 * taken in the search calculations.
	 *
	 * @param root the root of the document hierarchy
	 * @param stopwordPath the path of the "stop-word file"
	 */
	public SearchEngine(Path root, Path stopwordPath) {
		loadStopwords(stopwordPath);
		buildVocabulary(root);
		buildIdfVector(root);
		buildDocuments(root);
	}
	
	//------------------------------------------------------------------
	//						FETCHING STOP-WORDS
	//------------------------------------------------------------------
	
	/**
	 * Loads all of the stop-words from the given path.
	 * 
	 * @param stopwordPath the path of the "stop-word file"
	 */
	private void loadStopwords(Path stopwordPath) {
		try(BufferedReader br = Files.newBufferedReader(stopwordPath)) {
			for(String line; (line = br.readLine()) != null; ) {
				stopwords.add(line);
		    }
		} catch(IOException ioe) {
			System.err.println(ioe.getMessage());
		}
		
		printInfo("Fetched " + stopwords.size() + " stop-words from '" + stopwordPath + "'.", 1);
	}
	
	//------------------------------------------------------------------
	//					  BUILDING THE VOCABULARY
	//------------------------------------------------------------------

	/**
	 * Builds the vocabulary by traversing the document hierarchy.
	 * 
	 * @param root the root of the hierarchy
	 */
	private void buildVocabulary(Path root) {
		try {
			Files.walkFileTree(root, new VocabularyBuildingVisitor());
			printInfo("Vocabulary building finished. Vocabulary contains " +  vocabulary.size() + " words.", 1);
			
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}
	
	/**
	 * A simple {@code FileVisitor} that visits all of the documents in
	 * the document hierarchy and converts the text in those documents
	 * to the collection of vocabulary words.
	 *
	 * @author Filip Nemec
	 */
	private class VocabularyBuildingVisitor extends SimpleFileVisitor<Path> { 
		
		@Override
		public FileVisitResult visitFile(Path document, BasicFileAttributes attrs) throws IOException {
			convertDocumentToVocabulary(document);
			return FileVisitResult.CONTINUE;
		}
	}
	
	/**
	 * Converts the given document to a collection of words which are then
	 * added to the vocabulary, unless the word is a "stop-word".
	 *
	 * @param document the document to be converted and added to the vocabulary
	 * @throws IOException if the document could not be read
	 */
	private void convertDocumentToVocabulary(Path document) throws IOException {
		List<String> words = Util.convertTextToWords(new String(Files.readAllBytes(document)));
		
		for(String word : words) {
			if(stopwords.contains(word)) continue;

			WordInfo wordInfo = vocabulary.get(word);
			
			if(wordInfo == null) {
				vocabulary.put(word, new WordInfo(1, 1, document));
			} else {
				wordInfo.occurrences++;
				
				if(document != wordInfo.previousOccurrenceDocument) {
					wordInfo.parentDocuments++;
					wordInfo.previousOccurrenceDocument = document;
				}
			}
		}
	}
	
	//------------------------------------------------------------------
	//					  BUILDING THE IDF-VECTOR
	//------------------------------------------------------------------
	
	/**
	 * Builds the <i>inverse document frequency</i> vector.
	 * 
	 * @param root the root of the hierarchy
	 */
	private void buildIdfVector(Path root) {
		// The total number of documents in the hierarchy.
		documentCount = Util.getDocumentCount(root.toFile());
		idf = new Vector<Double>(vocabulary.size());
		
		for(WordInfo wordInfo : vocabulary.values()) {
			idf.add(Math.log((double) documentCount / wordInfo.parentDocuments));
		}
		
		printInfo("Inverse document frequency (idf) vector was successfully built.", 1);
	}
	
	//------------------------------------------------------------------
	//					  BUILDING THE DOCUMENTS
	//------------------------------------------------------------------
	
	/**
	 * Builds the document vectors by traversing the document hierarchy and
	 * processing each document separately using the previously created vocabulary.
	 * 
	 * @param root the root of the hierarchy
	 */
	private void buildDocuments(Path root) {
		try {
			Files.walkFileTree(root, new DocumentBuildingVisitor());
			printInfo("Finished building tf-idf vectors for " + documentCount + " document(s).", 2);
		
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}
	
	/**
	 * A simple {@code FileVisitor} that visits all of the documents in
	 * the document hierarchy and builds the documents in memory. A
	 * single document is described by its path and its vector.
	 * <p>
	 * In order for this visitor to properly do the job,
	 * the vocabulary already needs to be built beforehand.
	 *
	 * @author Filip Nemec
	 */
	private class DocumentBuildingVisitor extends SimpleFileVisitor<Path> {
		
		@Override
		public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
			buildDocument(path);
			return FileVisitResult.CONTINUE;
		}
	}
	
	/**
	 * Builds a single document by processing the words from the given document and
	 * using that information to build the document's <i>tfidf</i> vector. Created
	 * document is then saved to the {@linkplain #documents} list.
	 *
	 * @param docPath the path of the document on the disk
	 * @throws IOException if an IO error occurred
	 */
	private void buildDocument(Path docPath) throws IOException {
		List<String> words = Util.convertTextToWords(new String(Files.readAllBytes(docPath)));
		Vector<Double> vector = buildDocumentVector( buildTermFrequencyMap(words) );
		
		documents.add(new Document(docPath, vector));
	}
	
	/**
	 * Builds and returns the map that maps each word in the {@code words} list to the 
	 * number of times that word is in the list. That map is called "term-frequency" map.
	 *
	 * @param words the list of words
	 * @return the term-frequency map
	 */
	private Map<String, Integer> buildTermFrequencyMap(List<String> words) {
		var termFrequencyMap = new HashMap<String, Integer>();
		
		for(String word : words) {
			termFrequencyMap.merge(word, 1, (prev, value) -> prev + 1);
		}
		
		return termFrequencyMap;
	}
	
	/**
	 * Builds and returns the <i>tf-idf</i> vector for the the given term-frequency map.
	 *
	 * @param termFrequencyMap the term-frequency map
	 * @return the <i>tf-idf</i> document vector
	 */
	private Vector<Double> buildDocumentVector(Map<String, Integer> termFrequencyMap) {
		var vector = new Vector<Double>(vocabulary.size());
		
		int index = 0;
		for(String word : vocabulary.keySet()) {
			// The "term frequency" - the number of times a word was found in this document.
			Integer tf = termFrequencyMap.get(word);
			
			if(tf == null) {
				vector.add(0.0);
			} else {
				vector.add(tf * idf.get(index));
			}
			
			index++;
		}
		
		return vector;
	}
	
	//------------------------------------------------------------------
	//							PUBLIC API
	//------------------------------------------------------------------
	
	/**
	 * Performs the search query for the given list of words.
	 *
	 * @param words the search words
	 */
	public void query(List<String> words) {
		// Remove all the words that are not in the vocabulary.
		words.removeIf(word -> !vocabulary.containsKey(word));
		
		if(words.isEmpty()) {
			System.out.println("Query ignored: No words from the query are in the vocabulary.");
			return;
		}
		
		System.out.println("Query is: " + words);

		var queryVector = buildDocumentVector( buildTermFrequencyMap(words) );		
		results = new TreeSet<QueryResult>();
		
		for(Document document : documents) {
			double similarity = VectorN.similarity(queryVector, document.vector);
			
			if(similarity > 0.0) {
				results.add(new QueryResult(document, similarity));
			}
		}
		
		System.out.println("The closest " + QUERY_RESULTS_PRINT_LIMIT + " matches:");
		printQueryResults();
	}
	
	/**
	 * Displays the results of the last query. If the query was not
	 * yet been performed, appropriate message will be displayed.
	 */
	public void printQueryResults() {
		if(results == null) {
			System.out.println("There were no queries performed.\r\n");
			return;
		}
			
		int index = 0;
		for(QueryResult result : results) {
			System.out.println(String.format("[%d] (%.4f) " + result.document.path, index, result.similarity));
			
			if(++index == QUERY_RESULTS_PRINT_LIMIT) break;
		}
		
		System.out.println();
	}
	
	/**
	 * Prints the document represented by the query result with the
	 * given number {@code resultNumber}.
	 *
	 * @param resultNumber the number of the result
	 */
	public void type(int resultNumber) {
		if(results == null) {
			System.out.println("There were no queries performed.");
			
		} else if(resultNumber < 0) {
			System.out.println("The result number must be a positive integer.");
			
		} else if(resultNumber >= results.size()) {
			System.out.println("The result [" + resultNumber + "] does not exist.");
			
		} else {
			int index = 0;
			
			for(QueryResult result : results) {
				if(index == resultNumber) {
					try {
						String documentPath = result.document.path.toString();
						System.out.println("Document: " + documentPath);
						System.out.println("=".repeat(documentPath.length() * 2));
						System.out.println(new String(Files.readAllBytes(result.document.path)));
						System.out.println("=".repeat(documentPath.length() * 2));
						
					} catch(IOException e) {
						System.out.println("Could not load the document.");
						
					}
					
					break;
				}
				index++;
			}
		}
		
		System.out.println();
	}
	
	//------------------------------------------------------------------
	//							HELPER METHODS
	//------------------------------------------------------------------
	
	/**
	 * Prints the info message to the output stream
	 *
	 * @param message the message to be printed
	 * @param n the number of new line characters after the message
	 */
	private static void printInfo(String message, int n) {
		System.out.print("[INFO] " + message + "\r\n".repeat(n));
	}
	
	//------------------------------------------------------------------
	//							DOCUMENT
	//------------------------------------------------------------------
	
	/**
	 * Encapsulates a single document. A single document in the search
	 * engine should have a location of the document on the disk and an
	 * <i>tf-idf</i> vector that describes that document.
	 *
	 * @author Filip Nemec
	 */
	private static class Document {
		
		/** The path of the document on disk. */
		Path path;
		
		/** The <i>tf-idf</i> vector that describes this document. */
		Vector<Double> vector;
		
		/**
		 * Constructs a new document with the given path and <i>tf-idf</i> vector.
		 *
		 * @param path the path
		 * @param vector the <i>tf-idf</i> vector
		 */
		public Document(Path path, Vector<Double> vector) {
			this.path = path;
			this.vector = vector;
		}
		
		@Override
		public String toString() {
			return "'" + path + "' -> " + vector;
		}
	}
	
	//------------------------------------------------------------------
	//							WORD INFO
	//------------------------------------------------------------------
	
	/**
	 * Encapsulates the word information. This class is used as a helper
	 * container for the vocabulary.
	 *
	 * @author Filip Nemec
	 */
	private static class WordInfo {
		
		/** The total number of times a word was found in the document hierarchy. */
		int occurrences;
		
		/** The number of different documents a word was found in. */
		int parentDocuments;
		
		/**
		 * The previous document a word was found in. This helper "flag"
		 * is used during the vocabulary creation; if a word was found twice
		 * in the same document, the second occurrence will not increment the
		 * {@code parentDocuments} counter, as the word was already found in
		 * the document.
		 */
		Path previousOccurrenceDocument;

		/**
		 * Constructs a new word information container.
		 *
		 * @param occurrences the number of word occurrences
		 * @param parentDocuments the number of parent documents
		 * @param previousOccurrenceDocument the document a word was last found in
		 */
		public WordInfo(int occurrences, int parentDocuments, Path previousOccurrenceDocument) {
			this.occurrences = occurrences;
			this.parentDocuments = parentDocuments;
			this.previousOccurrenceDocument = previousOccurrenceDocument;
		}
		
		@Override
		public String toString() {
			return "[" + occurrences + ", " + parentDocuments + "]";
		}
	}
	
	//------------------------------------------------------------------
	//							QUERY RESULT
	//------------------------------------------------------------------
	
	/**
	 * Models a single query result. It simply encapsulates the resulting
	 * document and its similarity factor relative to the query.
	 *
	 * @author Filip Nemec
	 */
	private static class QueryResult implements Comparable<QueryResult> {
		
		/** The query document. */
		Document document;
		
		/** The similarity factor relative to the query. */
		double similarity;
		
		/**
		 * Constructs a new query result.
		 *
		 * @param document the query result document
		 * @param similarity the query similarity factor
		 */
		public QueryResult(Document document, double similarity) {
			this.document = document;
			this.similarity = similarity;
		}

		@Override
		public int compareTo(QueryResult o) {
			if(this.similarity > o.similarity) {
				return -1;
				
			} else if(this.similarity == o.similarity) {
				return 0;
			
			} else {
				return 1;
				
			}
		}
	}
}
