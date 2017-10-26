package ca.uqam.dic.indexation.modele;

import java.util.List;

public class Terme {
	
	public Terme(String n, String f){
		this.setNom(n);
		this.setFreq(f);
	}
	
	public Terme(String n){
		this.setNom(n);
	}
	
	private String nom;
	private String freq;
	private String df;
	private String idf;
	private String tfidf;
	private List<DocFreq> docFreqList;
	
	public String getNom(){
		return this.nom;
	}
	
	public void setNom(String nom){
		this.nom = nom;
	}
	
	public String getFreq(){
		return this.freq;
	}
	
	public void setFreq(String freq){
		this.freq = freq;
	}
	
	public String getDf(){
		return this.df;
	}
	
	public void setDf(String df){
		this.df = df;
	}
	
	public String getIdf(){
		return this.idf;
	}
	
	public void setIdf(String idf){
		this.idf = idf;
	}
	
	public String getTfidf(){
		return this.tfidf;
	}
	
	public void setTfidf(String tfidf){
		this.tfidf = tfidf;
	}
	
	public List<DocFreq> getDocFreqList(){
		return this.docFreqList;
	}
	
	public void setDocFreqList(List<DocFreq> docFreqList){
		this.docFreqList = docFreqList;
	}
}
