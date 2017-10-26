package ca.uqam.dic.indexation.controle;

import ca.uqam.dic.indexation.modele.*;
import com.opensymphony.xwork2.ActionSupport;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;


public class IndexationAction extends ActionSupport implements ServletRequestAware {
	
	public HttpServletRequest request;
	@Override
	  public void setServletRequest(HttpServletRequest request) {
	    this.request = request;  
	    }
	
	public String getPath(){
		return "C:\\indexationAFF\\indexationDocuments\\web\\";
		//return request.getContextPath();
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -4858141372274555307L;
	
	public static final int LANGUAGE_ENGLISH = 1;
	public static final int LANGUAGE_FRENCH = 2;
	public static final int LANGUAGE_PORTUGUESE = 3;
	public static final int LANGUAGE_ENGLISH_EXPANDED = 4;
	
	public static final int QUERY_LENGTH_SHORT = 1;
	public static final int QUERY_LENGTH_LONG = 2;
	
	private List<String> bigrams;
	private String teste;
	private List<String> termFreq;
	
	private String docNO;
	private DocAP docAP;
	private String termName;
	private List<DocFreq> listDocFreq;
	private List<Terme> termList;
	private Terme terme;
	private List<Requete> requeteList;

	public IndexationAction() {
		
	}
	
	public String execute() throws Exception {
        return "resultatIndexation";
	}
	
	public String createIndex() throws Exception {
		DocumentAPParser documentAPParser = new DocumentAPParser();
		documentAPParser.geraIndex(getPath());
		return "indexCreated";
	}
	
	public String restart() throws Exception {
		return "indexBegin";
	}
	
	public String readIndex() throws Exception {
		DocumentAPParser documentAPParser = new DocumentAPParser();
		List<String> l = documentAPParser.getTerms(this.getPath());
		this.setDocNO(Integer.toString(l.size()));
		this.setBigrams(l);
		return "indexRead";
	}
	
	public String getDocFreqByTerm() throws Exception {
		DocumentAPParser documentAPParser = new DocumentAPParser();
		Terme t = documentAPParser.getDocFrequency(this.getPath(),this.getTermName());
		this.setTerme(t);
		return "vectorDocFreqTerm";
	}
	
	public String searchTerm() throws Exception {
		DocumentAPParser documentAPParser = new DocumentAPParser();
		Terme t = documentAPParser.searchTermIndex(this.getPath(), this.getTermName());
		this.setTerme(t);
		return "searchResultTerm";
	}
	
	public String getTermFreqByDoc() throws Exception {
		DocumentAPParser documentAPParser = new DocumentAPParser();
		List<Terme> lts = documentAPParser.getTermFrequency(this.getPath(), this.getDocNO());
		this.setTermList(lts);
		return "vectorTermFreqDoc";
	}
	
	public String readFileEnglish() throws Exception {
		RequeteParser parser = new RequeteParser();
		parser.readFileQueriesEnglish(getPath());
		this.setTeste("Index de requetes cree");
		return "fileEnglishRead";
	}
	
	public String readFileFrench() throws Exception {
		RequeteParser parser = new RequeteParser();
		parser.readFileQueriesFrench(getPath());
		this.setTeste("Index de requetes cree");
		return "fileFrenchRead";
	}
	
	public String readFilePortuguese() throws Exception {
		RequeteParser parser = new RequeteParser();
		parser.readFileQueriesPortuguese(getPath());
		this.setTeste("Index de requetes cree");
		return "filePortRead";
	}
	
	public String readQueryEnglish() throws Exception {
		RequeteParser parser = new RequeteParser();
		List<Requete> listQuery = parser.readQueryEnglish(getPath());
		this.setRequeteList(listQuery);
		this.setTeste("Anglais");
		return "listQueries";
	}
	
	public String readExpandedQueryEnglish() throws Exception {
		RequeteParser parser = new RequeteParser();
		List<Requete> listQuery = parser.readExpandedQueryEnglish(getPath());
		this.setRequeteList(listQuery);
		this.setTeste("Anglais");
		return "listQueries";
	}
	
	public String readQueryFrench() throws Exception {
		RequeteParser parser = new RequeteParser();
		List<Requete> listQuery = parser.readQueryFrench(getPath());
		this.setRequeteList(listQuery);
		this.setTeste("Francais");
		return "listQueries";
	}
	
	public String readQueryPort() throws Exception {
		RequeteParser parser = new RequeteParser();
		List<Requete> listQuery = parser.readQueryPortuguese(getPath());
		this.setRequeteList(listQuery);
		this.setTeste("Portugais");
		return "listQueries";
	}
	
	public String readQueryFrenchTranslated() throws Exception {
		RequeteParser parser = new RequeteParser();
		List<Requete> listQuery = parser.readQueryFrenchTranslated(getPath());
		this.setRequeteList(listQuery);
		this.setTeste("Francais");
		return "listQueries";
	}
	
	public String readQueryPortTranslated() throws Exception {
		RequeteParser parser = new RequeteParser();
		List<Requete> listQuery = parser.readQueryPortugueseTranslated(getPath());
		this.setRequeteList(listQuery);
		this.setTeste("Portugais");
		return "listQueries";
	}
	
	public String translateQueriesFrench() throws Exception {
		RequeteParser parser = new RequeteParser();
		//String res = parser.translateQueriesFrench(); //Read the queries from the index, translate them, write them to a file
		parser.readFileQueriesFrenchTrans(getPath()); //Read the translated queries from the file, create an index
		//this.setTeste(res);
		return "queriesTranslated";
	}
	
	public String translateQueriesPort() throws Exception {
		RequeteParser parser = new RequeteParser();
		//String res = parser.translateQueriesPort(); //Read the queries from the index, translate them, write them to a file
		parser.readFileQueriesPortugueseTrans(getPath()); //Read the translated queries from the file, create an index
		//this.setTeste(res);
		return "queriesTranslated";
	}
	
	public String searchShortQueriesEnglish() throws Exception {
		List<Terme> termes = searchQueries(LANGUAGE_ENGLISH,QUERY_LENGTH_SHORT);
		this.setTermList(termes);
		return "searchResultQuery";
	}
	
	public String searchLongQueriesEnglish() throws Exception {
		List<Terme> termes = searchQueries(LANGUAGE_ENGLISH,QUERY_LENGTH_LONG);
		this.setTermList(termes);
		return "searchResultQuery";
	}
	
	public String searchShortQueriesFrench() throws Exception {
		List<Terme> termes = searchQueries(LANGUAGE_FRENCH,QUERY_LENGTH_SHORT);
		this.setTermList(termes);
		return "searchResultQuery";
	}
	
	public String searchShortQueriesPort() throws Exception {
		List<Terme> termes = searchQueries(LANGUAGE_PORTUGUESE,QUERY_LENGTH_SHORT);
		this.setTermList(termes);
		return "searchResultQuery";
	}
	
	public String searchExpandedShortQueriesEnglish() throws Exception {
		List<Terme> termes = searchQueries(LANGUAGE_ENGLISH_EXPANDED, QUERY_LENGTH_SHORT);
		this.setTermList(termes);
		return "searchResultQuery";
	}
	
	public String searchExpandedLongQueriesEnglish() throws Exception {
		List<Terme> termes = searchQueries(LANGUAGE_ENGLISH_EXPANDED, QUERY_LENGTH_LONG);
		this.setTermList(termes);
		return "searchResultQuery";
	}
	
	public String generateTrecEnglishShort() throws Exception {
		DocumentAPParser documentAPParser = new DocumentAPParser();
		List<Terme> termes = searchQueries(LANGUAGE_ENGLISH,QUERY_LENGTH_SHORT,DocumentAPParser.MAX_HITS_TREC);
		String qtdDoc = documentAPParser.generateTrecFile(termes, getPath() + DocumentAPParser.TREC_ENGLISH_SHORT_Q);
		this.setDocNO(qtdDoc);
		return "trecGenerationResult";
	}
	
	public String generateTrecEnglishLong() throws Exception {
		DocumentAPParser documentAPParser = new DocumentAPParser();
		List<Terme> termes = searchQueries(LANGUAGE_ENGLISH,QUERY_LENGTH_LONG,DocumentAPParser.MAX_HITS_TREC);
		String qtdDoc = documentAPParser.generateTrecFile(termes, getPath() + DocumentAPParser.TREC_ENGLISH_LONG_Q);
		this.setDocNO(qtdDoc);
		return "trecGenerationResult";
	}
	
	public String generateTrecFrenchShort() throws Exception {
		DocumentAPParser documentAPParser = new DocumentAPParser();
		List<Terme> termes = searchQueries(LANGUAGE_FRENCH,QUERY_LENGTH_SHORT,DocumentAPParser.MAX_HITS_TREC);
		String qtdDoc = documentAPParser.generateTrecFile(termes, getPath() + DocumentAPParser.TREC_FRENCH_SHORT_Q);
		this.setDocNO(qtdDoc);
		return "trecGenerationResult";
	}
	
	public String generateTrecPortShort() throws Exception {
		DocumentAPParser documentAPParser = new DocumentAPParser();
		List<Terme> termes = searchQueries(LANGUAGE_PORTUGUESE,QUERY_LENGTH_SHORT,DocumentAPParser.MAX_HITS_TREC);
		String qtdDoc = documentAPParser.generateTrecFile(termes, getPath() + DocumentAPParser.TREC_PORT_SHORT_Q);
		this.setDocNO(qtdDoc);
		return "trecGenerationResult";
	}
	
	public String generateTrecExpandedEnglishShort() throws Exception {
		DocumentAPParser documentAPParser = new DocumentAPParser();
		List<Terme> termes = searchQueries(LANGUAGE_ENGLISH_EXPANDED,QUERY_LENGTH_SHORT,DocumentAPParser.MAX_HITS_TREC);
		String qtdDoc = documentAPParser.generateTrecFile(termes, getPath() + DocumentAPParser.TREC_ENGLISH_SHORT_Q);
		this.setDocNO(qtdDoc);
		return "trecGenerationResult";
	}
	
	public String generateTrecExpandedEnglishLong() throws Exception {
		DocumentAPParser documentAPParser = new DocumentAPParser();
		List<Terme> termes = searchQueries(LANGUAGE_ENGLISH_EXPANDED,QUERY_LENGTH_LONG,DocumentAPParser.MAX_HITS_TREC);
		String qtdDoc = documentAPParser.generateTrecFile(termes, getPath() + DocumentAPParser.TREC_ENGLISH_LONG_Q);
		this.setDocNO(qtdDoc);
		return "trecGenerationResult";
	}
	
	private List<Terme> searchQueries(int language, int length) throws Exception {
		return searchQueries(language,length,DocumentAPParser.MAX_HITS_SEARCH);
	}
	
	private List<Terme> searchQueries(int language, int length, int qtdHits) throws Exception {
		List<Terme> termes = new ArrayList<Terme>();
		List<Requete> listObjQuery = new ArrayList<Requete>();
		List<String> listShortQueries = new ArrayList<String>();
		List<String> listLongQueries = new ArrayList<String>();
		RequeteParser requeteParser = new RequeteParser();
		DocumentAPParser documentAPParser = new DocumentAPParser();
		switch (language){
		case LANGUAGE_ENGLISH:
			listObjQuery = requeteParser.readQueryEnglish(getPath());
			break;
		case LANGUAGE_FRENCH: 
			listObjQuery = requeteParser.readQueryFrenchTranslated(getPath());
			break;
		case LANGUAGE_PORTUGUESE:
			listObjQuery = requeteParser.readQueryPortugueseTranslated(getPath());
			break;	
		case LANGUAGE_ENGLISH_EXPANDED:
			listObjQuery = requeteParser.readExpandedQueryEnglish(getPath());
			break;
		}
		switch (length){
		case QUERY_LENGTH_SHORT:
			listShortQueries = requeteParser.getShortQueries(listObjQuery);
			break;
		case QUERY_LENGTH_LONG:
			listShortQueries = requeteParser.getShortQueries(listObjQuery);
			listLongQueries = requeteParser.getLongQueries(listObjQuery);
			break;
		}
		termes = documentAPParser.searchMultipleQueriesIndex(this.getPath(),listShortQueries, listLongQueries, qtdHits);
		return termes;
	}
	
	public String userQuery() throws Exception {
		String query = this.getTeste();
		DocumentAPParser documentAPParser = new DocumentAPParser();
		Terme terme = documentAPParser.searchQueryIndex(this.getPath(),query,"", DocumentAPParser.MAX_HITS_SEARCH);
		List<Terme> termes = new ArrayList<Terme>();
		termes.add(terme);
		this.setTermList(termes);
		return "searchResultQuery";
	}
	
	public String userQueryExpanded() throws Exception {
		RequeteParser parser = new RequeteParser();
		String query = parser.expandQuery(getPath(),this.getTeste());
		DocumentAPParser documentAPParser = new DocumentAPParser();
		Terme terme = documentAPParser.searchQueryIndex(getPath(),query,"", DocumentAPParser.MAX_HITS_SEARCH);
		List<Terme> termes = new ArrayList<Terme>();
		termes.add(terme);
		this.setTermList(termes);
		return "searchResultQuery";
	}
	
	public String showDocument() throws Exception {
		DocAP d = new DocAP(this.getDocNO());
		DocumentAPParser documentAPParser = new DocumentAPParser();
		d = documentAPParser.getDocument(getPath(),this.getDocNO());
		this.setDocAP(d);
		return "documentVisualization";
	}
	
	public String translateQueryFrench() throws Exception {
		RequeteParser parser = new RequeteParser();
		String text = parser.translateMyMemory(this.getTeste(), "fr");
		this.setTeste(text);
		return "testResult";
	}
	
	public String tester() throws Exception {
		createIndex();
		this.setTeste("Index created");
		return "testResult";
	}
    
    public List<String> getBigrams(){
    	return this.bigrams;
    }
    
    public void setBigrams(List<String> bigrams){
    	this.bigrams = bigrams;
    }
    
    public String getTeste(){
    	return this.teste;
    }
    
    public void setTeste(String teste){
    	this.teste = teste;
    }
    
    public List<Terme> getTermList(){
    	return this.termList;
    }
    
    public void setTermList(List<Terme> termList){
    	this.termList = termList;
    }
    
    public List<String> getTermFreq(){
    	return this.termFreq;
    }
    
    public void setTermFreq(List<String> termFreq){
    	this.termFreq = termFreq;
    }
    
    public String getDocNO(){
    	return this.docNO;
    }
    
    public void setDocNO(String docNO){
    	this.docNO = docNO;
    }
    
    public DocAP getDocAP(){
    	return this.docAP;
    }
    
    public void setDocAP(DocAP docAP){
    	this.docAP = docAP;
    }
    
    public String getTermName(){
    	return this.termName;
    }
    
    public void setTermName(String termName){
    	this.termName = termName;
    } 
    
    public List<DocFreq> getListDocFreq(){
    	return this.listDocFreq;
    }
    
    public void setListDocFreq(List<DocFreq> listDocFreq){
    	this.listDocFreq = listDocFreq;
    }
    
    public Terme getTerme(){
    	return this.terme;
    }
    
    public void setTerme(Terme terme){
    	this.terme = terme;
    }
    
    public List<Requete> getRequeteList(){
    	return this.requeteList;
    }
    
    public void setRequeteList(List<Requete> requeteList){
    	this.requeteList = requeteList;
    }
}