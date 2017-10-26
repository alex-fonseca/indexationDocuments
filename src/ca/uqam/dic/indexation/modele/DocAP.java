package ca.uqam.dic.indexation.modele;

public class DocAP {
	private String nroDoc;
	private String text;
	
	public DocAP(String nroDoc){
		this.setNroDoc(nroDoc);
	}
	
	public String getNroDoc(){
		return this.nroDoc;
	}
	
	public void setNroDoc(String nroDoc){
		this.nroDoc = nroDoc;
	}
	
	public String getText(){
		return this.text;
	}
	
	public void setText(String text){
		this.text = text;
	}
}
