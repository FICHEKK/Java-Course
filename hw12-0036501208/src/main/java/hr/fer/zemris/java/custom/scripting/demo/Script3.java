package hr.fer.zemris.java.custom.scripting.demo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.webserver.RequestContext;
import hr.fer.zemris.java.webserver.RequestContext.RCCookie;

/**
 * Script 3 demonstrates the duplication function and
 * parameter manipulation.
 *
 * @author Filip Nemec
 */
public class Script3 {
	
	/**
	 * Program starts from here.
	 *
	 * @param args none are used
	 */
	public static void main(String[] args) {
		String documentBody = readFromDisk("brojPoziva.smscr");
		
		Map<String, String> parameters = new HashMap<String, String>();
		Map<String, String> persistentParameters = new HashMap<String, String>();
		List<RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();
		
		persistentParameters.put("brojPoziva", "3");
		RequestContext rc = new RequestContext(System.out, parameters, persistentParameters, cookies);
		
		new SmartScriptEngine(new SmartScriptParser(documentBody).getDocumentNode(), rc).execute();
		
		System.out.println("Vrijednost u mapi: " + rc.getPersistentParameter("brojPoziva"));
	}
	
	/**
	 * Returns the text stored in the given file as a single
	 * {@code String}.
	 * 
	 * @param filename file path
	 * @return {@code String} of the file contents, or {@code null}
	 * 		   if exception occurred
	 */
	private static String readFromDisk(String filePath) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		
		try (InputStream is = Files.newInputStream(Paths.get(filePath))) {
			byte[] buffer = new byte[1024];
			while (true) {
				int r = is.read(buffer);
				if (r < 1) break;
				bos.write(buffer, 0, r);
			}
			return new String(bos.toByteArray(), StandardCharsets.UTF_8);
			
		} catch (IOException ex) {
			return null;
			
		}
	}
}
