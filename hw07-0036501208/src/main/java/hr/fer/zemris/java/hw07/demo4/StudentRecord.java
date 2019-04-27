package hr.fer.zemris.java.hw07.demo4;

import java.util.Objects;

/**
 * Razred koji modelira zapis jednog studenta.
 *
 * @author Filip Nemec
 */
public class StudentRecord {
	/** JMBAG studenta. */
	private String jmbag;
	
	/** Prezime studenta. */
	private String prezime;
	
	/** Ime studenta. */
	private String ime;
	
	/** Broj bodova na međuispitu koje je student ostvario. */
	private double bodMI;
	
	/** Broj bodova na završnom ispitu koje je student ostvario. */
	private double bodZI;
	
	/** Broj bodova na laboratorijskim vježbama koje je student ostvario. */
	private double bodLAB;
	
	/** Krajnja ocjena studenta. */
	private int ocjena;
	
	/** Ukupan broj bodova studenta. */
	private double ukupnoBod;
	
	/**
	 * Konstruira novi zapis studenta.
	 *
	 * @param jmbag JMBAG studenta
	 * @param prezime prezime studenta
	 * @param ime ime studenta
	 * @param bodMI broj bodova ostvarenih na međuispitu
	 * @param bodZI broj bodova ostvarenih na završnom ispitu
	 * @param bodLAB broj bodova ostvarenih na laboratorijskim vježbama
	 * @param ocjena krajnja ocjena studenta
	 */
	public StudentRecord(String jmbag, String prezime, String ime, double bodMI, double bodZI, double bodLAB, int ocjena) {
		this.jmbag = jmbag;
		this.prezime = prezime;
		this.ime = ime;
		this.bodMI = bodMI;
		this.bodZI = bodZI;
		this.bodLAB = bodLAB;
		this.ocjena = ocjena;
		this.ukupnoBod = bodMI + bodZI + bodLAB;
	}

	@Override
	public int hashCode() {
		return Objects.hash(jmbag);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof StudentRecord))
			return false;
		StudentRecord other = (StudentRecord) obj;
		return Objects.equals(jmbag, other.jmbag);
	}

	@Override
	public String toString() {
		return jmbag   + "\t" +
			   prezime + "\t" +
			   ime 	   + "\t" +
			   bodMI   + "\t" +
			   bodZI   + "\t" +
			   bodLAB  + "\t" +
			   ocjena;
	}
	
	//------------------------------------------------------------------
	// 							Getters
	//------------------------------------------------------------------
	
	/**
	 * @return JMBAG studenta
	 */
	public String getJmbag() {
		return jmbag;
	}
	
	/**
	 * @return prezime studenta
	 */
	public String getPrezime() {
		return prezime;
	}
	
	/**
	 * @return ime studenta
	 */
	public String getIme() {
		return ime;
	}
	
	/**
	 * @return broj bodova ostvarenih na međuispitu
	 */
	public double getBodMI() {
		return bodMI;
	}
	
	/**
	 * @return broj bodova ostvarenih na završnom ispitu
	 */
	public double getBodZI() {
		return bodZI;
	}
	
	/**
	 * @return broj bodova ostvarenih na laboratorijskim vježbama
	 */
	public double getBodLAB() {
		return bodLAB;
	}
	
	/**
	 * @return krajnja ocjena studenta
	 */
	public int getOcjena() {
		return ocjena;
	}
	
	/**
	 * @return ukupan broj bodova studenta
	 */
	public double getUkupnoBod() {
		return ukupnoBod;
	}
}
