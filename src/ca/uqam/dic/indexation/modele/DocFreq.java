package ca.uqam.dic.indexation.modele;

public class DocFreq implements Comparable<DocFreq>{

	private String tfidf;
	private String nroDoc;
	private String freq;
	private String tf;
	private String idf;
	private String score;
	
	public DocFreq(String n, String f){
		this.setNroDoc(n);
		this.setFreq(f);
	}
	
	public DocFreq(String n){
		this.setNroDoc(n);
	}
	
	public int compareTo(DocFreq compareObject){
        if (Double.parseDouble(getTfidf()) < Double.parseDouble(compareObject.getTfidf()))
            return -1;
        else if (Double.parseDouble(getTfidf()) == Double.parseDouble(compareObject.getTfidf()))
            return 0;
        else
            return 1;
    }
	
	public String getTfidf(){
		return this.tfidf;
	}
	
	public void setTfidf(String tfidf){
		this.tfidf = tfidf;
	}
	
	public String getNroDoc(){
		return this.nroDoc;
	}
	
	public void setNroDoc(String nroDoc){
		this.nroDoc = nroDoc;
	}
	
	public String getFreq(){
		return this.freq;
	}
	
	public void setFreq(String freq){
		this.freq = freq;
	}
	
	public String getTf(){
		return this.tf;
	}
	
	public void setTf(String tf){
		this.tf = tf;
	}
	
	public String getIdf(){
		return this.idf;
	}
	
	public void setIdf(String idf){
		this.idf = idf;
	}
	
	public String getScore(){
		return this.score;
	}
	
	public void setScore(String score){
		this.score = score;
	}
}