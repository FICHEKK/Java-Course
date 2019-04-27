package hr.fer.zemris.java.hw07.demo4;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Jednostavan program koji pokazuje mogućnosti Java
 * Stream API-a.
 *
 * @author Filip Nemec
 */
public class StudentDemo {
	
	/**
	 * Program počinje izviđenje od ovdje.
	 *
	 * @param args ne primaju se nikakvi argumenti
	 */
	public static void main(String[] args) {
		try {
			List<String> linije = Files.readAllLines(Paths.get("./studenti.txt"));
			List<StudentRecord> zapisi = napraviZapise(linije);
			
			ispisiZadatke(zapisi, 1, 2, 3, 4, 5, 6, 7, 8);
			
		} catch (IOException e) {
			System.err.println("Pogreška pri čitanju datoteke.");
			
		} catch(NumberFormatException e) {
			System.err.println("Pogreška pri formatiranju.");
		}
	}
	
	/**
	 * Ispisuje odabrane zadatke.
	 *
	 * @param zapisi zapisi koji se obrađuju
	 * @param brojeviZadataka nabrojani redni brojevi zadataka koji se žele ispisati
	 */
	private static void ispisiZadatke(List<StudentRecord> zapisi, int ... brojeviZadataka) {
		for(int brojZadatka : brojeviZadataka) {
			System.out.println("Zadatak " + brojZadatka);
			System.out.println("=========");
			
			switch (brojZadatka) {
				case 1: ispisiZadatak1(zapisi); break;
				case 2: ispisiZadatak2(zapisi); break;
				case 3: ispisiZadatak3(zapisi); break;
				case 4: ispisiZadatak4(zapisi); break;
				case 5: ispisiZadatak5(zapisi); break;
				case 6: ispisiZadatak6(zapisi); break;
				case 7: ispisiZadatak7(zapisi); break;
				case 8: ispisiZadatak8(zapisi); break;
				default: System.out.println("Zadatak ne postoji."); break;
			}
			
			System.out.println();
		}
	}
	
	/**
	 * Ispisuje zadatak 8: razvrstavanje studenata po prolazu.
	 */
	private static void ispisiZadatak8(List<StudentRecord> zapisi) {
		System.out.println("Razvrstani studenti po prolazu: ");
		Map<Boolean, List<StudentRecord>> mapa = razvrstajProlazPad(zapisi);
		
		for(Map.Entry<Boolean, List<StudentRecord>> e : mapa.entrySet()) {
			System.out.println("Prošli: " + e.getKey());
			System.out.println("=========");
			
			List<StudentRecord> studenti = e.getValue();
			studenti.forEach(System.out::println);
			System.out.println();
		}
	}
	
	/**
	 * Ispisuje zadatak 7: broj studenata po ocjenama.
	 */
	private static void ispisiZadatak7(List<StudentRecord> zapisi) {
		System.out.println("Brojevi konkretnih ocjena: ");
		Map<Integer, Integer> mapa = vratiBrojStudenataPoOcjenama(zapisi);
		
		for(Map.Entry<Integer, Integer> e : mapa.entrySet()) {
			System.out.println("Studenata s ocjenom " + e.getKey() + " ima " + e.getValue());
		}
	}

	/**
	 * Ispisuje zadatak 6: studenti razvrstani po ocjenama.
	 */
	private static void ispisiZadatak6(List<StudentRecord> zapisi) {
		System.out.println("Razvrstani studenti po ocjenama: ");
		Map<Integer, List<StudentRecord>> mapa = razvrstajStudentePoOcjenama(zapisi);
		
		for(Map.Entry<Integer, List<StudentRecord>> e : mapa.entrySet()) {
			System.out.println("Ocjena " + e.getKey());
			System.out.println("=========");
			
			List<StudentRecord> studenti = e.getValue();
			studenti.forEach(System.out::println);
			System.out.println();
		}
	}

	/**
	 * Ispisuje zadatak 5: studenti razvrstani po ocjenama.
	 */
	private static void ispisiZadatak5(List<StudentRecord> zapisi) {
		System.out.println("JMBAG-ovi nepoloženih studenata: ");
		vratiPopisNepolozenih(zapisi).forEach(System.out::println);
	}
	
	/**
	 * Ispisuje zadatak 4: sortirani odlikaši.
	 */
	private static void ispisiZadatak4(List<StudentRecord> zapisi) {
		System.out.println("Sortirani odlikaši po broju bodova: ");
		vratiSortiranuListuOdlikasa(zapisi).forEach(System.out::println);
	}
	
	/**
	 * Ispisuje zadatak 3: odlikaši.
	 */
	private static void ispisiZadatak3(List<StudentRecord> zapisi) {
		System.out.println("Odlikaši: ");
		vratiListuOdlikasa(zapisi).forEach(System.out::println);
	}
	
	/**
	 * Ispisuje zadatak 2: broj odlikaša.
	 */
	private static void ispisiZadatak2(List<StudentRecord> zapisi) {
		System.out.println("Broj odlikaša je " + vratiBrojOdlikasa(zapisi));
	}
	
	/**
	 * Ispisuje zadatak 1: broj studenata s više od 25 bodova.
	 */
	private static void ispisiZadatak1(List<StudentRecord> zapisi) {
		System.out.println("Broj studenata s više od 25 bodova je " + vratiBodovaViseOd25(zapisi));
	}
	
	/**
	 * Pretvara {@code String} linije u zapise studenata.
	 *
	 * @param linije lista linija koje obrađujemo
	 * @return lista student zapisa
	 * @throws NumberFormatException ako se atribut ne može pretvoriti u broj
	 */
	private static List<StudentRecord> napraviZapise(List<String> linije) throws NumberFormatException {
		final int TRAZENI_BROJ_ARGUMENATA = 7;
		List<StudentRecord> zapisi = new LinkedList<>();
		
		for(String linija : linije) {
			String[] atributi = linija.split("\t");
			
			if(atributi.length != TRAZENI_BROJ_ARGUMENATA) continue;
			
			String jmbag   = atributi[0];
			String prezime = atributi[1];
			String ime 	   = atributi[2];
			double bodMI   = Double.parseDouble(atributi[3]);
			double bodZI   = Double.parseDouble(atributi[4]);
			double bodLAB  = Double.parseDouble(atributi[5]);
			int ocjena 	   = Integer.parseInt(atributi[6]);
			
			zapisi.add(new StudentRecord(jmbag, prezime, ime, bodMI, bodZI, bodLAB, ocjena));
		}
		
		return zapisi;
	}

	//------------------------------------------------------------------
	// 							Zadatak 1.
	//------------------------------------------------------------------
	
	/**
	 * Vraća broj studenata koji su ostvarili više od 25 bodova.
	 *
	 * @param records izvor zapisa studenata
	 * @return broj studenata koji su ostvarili više od 25 bodova
	 */
	private static long vratiBodovaViseOd25(List<StudentRecord> zapisi) {
		return zapisi.stream()
					  .filter(r -> r.getUkupnoBod() > 25)
					  .count();
	}
	
	//------------------------------------------------------------------
	// 							Zadatak 2.
	//------------------------------------------------------------------
	
	/**
	 * Vraća broj odlikaša.
	 *
	 * @param zapisi izvor zapisa studenata
	 * @return broj odlikaša
	 */
	private static long vratiBrojOdlikasa(List<StudentRecord> zapisi) {
		return zapisi.stream()
					 .filter(r -> r.getOcjena() == 5)
					 .count();
	}
	
	//------------------------------------------------------------------
	// 							Zadatak 3.
	//------------------------------------------------------------------
	
	/**
	 * Vraća listu student zapisa svih odlikaša.
	 *
	 * @param zapisi izvor zapisa studenata
	 * @return lista student zapisa svih odlikaša
	 */
	private static List<StudentRecord> vratiListuOdlikasa(List<StudentRecord> zapisi) {
		return zapisi.stream()
					 .filter(r -> r.getOcjena() == 5)
					 .collect(Collectors.toList());
	}
	
	//------------------------------------------------------------------
	// 							Zadatak 4.
	//------------------------------------------------------------------
	
	/**
	 * Vraća sortiranu listu student zapisa svih odlikaša. Sortiranje se
	 * obavlja po bodovima.
	 *
	 * @param zapisi izvor zapisa studenata
	 * @return sortirana lista student zapisa svih odlikaša, sortirano po bodovima
	 */
	private static List<StudentRecord> vratiSortiranuListuOdlikasa(List<StudentRecord> zapisi) {
		final var PO_BODOVIMA = new Comparator<StudentRecord>() {
			@Override
			public int compare(StudentRecord o1, StudentRecord o2) {
				double prviUkupno  = o1.getUkupnoBod();
				double drugiUkupno = o2.getUkupnoBod();
				
				if(prviUkupno > drugiUkupno) return 1;
				if(prviUkupno < drugiUkupno) return -1;
				return 0;
			}
		}.reversed();
		
		return zapisi.stream()
					 .filter(r -> r.getOcjena() == 5)
					 .sorted(PO_BODOVIMA)
					 .collect(Collectors.toList());
	}
	
	//------------------------------------------------------------------
	// 							Zadatak 5.
	//------------------------------------------------------------------
	
	/**
	 * Vraća listu svih JMBAG-ova nepoloženih studenata.
	 *
	 * @param zapisi izvor zapisa studenata
	 * @return lista svih JMBAG-ova nepoloženih studenata
	 */
	private static List<String> vratiPopisNepolozenih(List<StudentRecord> zapisi) {
		return zapisi.stream()
					 .filter(r -> r.getOcjena() == 1)
					 .map(r -> r.getJmbag())
					 .sorted()
					 .collect(Collectors.toList());
	}
	
	//------------------------------------------------------------------
	// 							Zadatak 6.
	//------------------------------------------------------------------
	
	/**
	 * Vraća mapu koja razvrstava studente po ocjenama.
	 *
	 * @param zapisi izvor zapisa studenata
	 * @return mapa koja razvrstava studente po ocjenama
	 */
	private static Map<Integer, List<StudentRecord>> razvrstajStudentePoOcjenama(List<StudentRecord> zapisi) {
		return zapisi.stream()
					 .collect(Collectors.groupingBy(StudentRecord::getOcjena));
	}
	
	//------------------------------------------------------------------
	// 							Zadatak 7.
	//------------------------------------------------------------------
	
	/**
	 * Vraća mapu koja mapira svaku ocjenu s brojem studenata koji imaju tu ocjenu.
	 *
	 * @param zapisi izvor zapisa studenata
	 * @return mapa koja mapira svaku ocjenu s brojem studenata koji imaju tu ocjenu
	 */
	private static Map<Integer, Integer> vratiBrojStudenataPoOcjenama(List<StudentRecord> zapisi) {
		return zapisi.stream()
					 .collect(Collectors.toMap(StudentRecord::getOcjena,
											   r -> 1,
											   (trenutniBroj, ocjena) -> ++trenutniBroj));
	}
	
	//------------------------------------------------------------------
	// 							Zadatak 8.
	//------------------------------------------------------------------
	
	/**
	 * Vraća mapu koja razvrstava studente po prolazu.
	 *
	 * @param zapisi izvor zapisa studenata
	 * @return mapa koja razvrstava studente po prolazu
	 */
	private static Map<Boolean, List<StudentRecord>> razvrstajProlazPad(List<StudentRecord> zapisi) {
		return zapisi.stream()
					 .collect(Collectors.partitioningBy(r -> r.getOcjena() != 1));
	}
}
