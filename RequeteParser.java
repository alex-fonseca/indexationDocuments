package ca.uqam.dic.indexation.controle;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import java.text.ParseException;
import org.json.JSONObject;

import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import java.net.MalformedURLException;
import java.io.InputStreamReader;

import org.apache.commons.digester.Digester;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.shingle.ShingleAnalyzerWrapper;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryParser.analyzing.AnalyzingQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockFactory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.store.SimpleFSLockFactory;
import org.apache.lucene.util.Version;

import ca.uqam.dic.indexation.modele.*;


public class RequeteParser {
		
	private static IndexWriter writer;
	private static String contextPath = "C:\\indexationAFF\\indexationDocuments\\web\\";
	public static final String DIR_QUERY_ENGLISH = "query\\queryEnglish\\";
	public static final String DIR_QUERY_FRENCH = "query\\queryFrench\\";
	public static final String DIR_QUERY_PORT = "query\\queryPortuguese\\";
	public static final String DIR_QUERY_FRENCH_TRANS = "query\\queryFrenchTranslatedMM\\";
	public static final String DIR_QUERY_PORT_TRANS = "query\\queryPortugueseTranslatedMM\\";
	
	public static final String INDEX_QUERYS_ENGLISH_DIR = "index\\indexQueryEnglish";	
	public static final String INDEX_QUERYS_FRENCH_DIR = "index\\indexQueryFrench";	
	public static final String INDEX_QUERYS_PORT_DIR = "index\\indexQueryPort";		
	public static final String INDEX_QUERYS_FRENCH_TRANS = "index\\indexQueryFrenchTranslatedMM";
	public static final String INDEX_QUERYS_PORT_TRANS = "index\\indexQueryPortTranslatedMM";
	
	public static final String FIELD_TOP = "BEGIN/top"; 
	public static final String FIELD_NUM = "BEGIN/top/num";
	public static final String FIELD_TITLE = "BEGIN/top/title"; 
	public static final String FIELD_DESC = "BEGIN/top/desc";
	
	public static final String INDEX_SYNONIMS = "index\\indexSyns";
	public static final String API_WORDREFERENCE = "93f69";
	//http://api.wordreference.com/0.8/93f69/json/fren/
	
	/* STANDARD STOP WORDS
	 		"a","and", "are", "as",  
			"at", "be", "but", "by", "for", "if", "in", "into", "is","no", "not", "of", "on", "or", 
			"s", "such", "t", "that", "the", "their", "then", "there", "these", "they", 
			"this", "to", "was", "will", "with", "inc","incorporated","co.","ltd","ltd.", 
	 
	 */
	
	public Set<String> my_stop_words = new HashSet<String>(Arrays.asList("a","and", "are", "as",  
			"at", "be", "but", "by", "for", "if", "in", "into", "is","no", "not", "of", "on", "or", 
			"s", "such", "t", "that", "the", "their", "then", "there", "these", "they", 
			"this", "to", "was", "will", "with", "inc","incorporated","co.","ltd","ltd.",
			"it","had","said","from","he","have","a","year","were","who","been","which",
			"after","more","than","all","most","them","did","those","who","10","100","1000",
			"1","2","3","4","5","6","7","8","9","among","former","give","remain","without",
			"number","becom","total","begin","sinc","call","under","end","up"));
	
	public RequeteParser(){
		
	}
	
	//Read the queries from a file and creates an index with them
	public void readFileQueries(String pathFiles, String pathIndex) throws Exception{
	    
		File   fileDir  = new File(pathFiles);
		File   indexDir = new File(pathIndex);
		File[] textFiles  = fileDir.listFiles();
		Directory directory = new SimpleFSDirectory(indexDir);
		LockFactory simpleLock = new SimpleFSLockFactory(pathIndex);
		simpleLock.makeLock(pathIndex + ".lock");
		
		Analyzer analyzer = new EnglishAnalyzer(Version.LUCENE_36,my_stop_words);
		
		/*ShingleAnalyzerWrapper shingleAnalyzerWrapper = new ShingleAnalyzerWrapper(analyzer);
		shingleAnalyzerWrapper.setOutputUnigrams(false);
		IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_36, shingleAnalyzerWrapper);*/
		IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_36, analyzer);
		writer = new IndexWriter(directory, iwc);
		
        Digester digester = new Digester();
        digester.setValidating(false);

        digester.addObjectCreate("BEGIN", RequeteParser.class);
        digester.addObjectCreate(FIELD_TOP, RequeteAP.class);

        digester.addCallMethod(FIELD_NUM, "setNum", 0);
        digester.addCallMethod(FIELD_TITLE, "setTitle", 0);
        digester.addCallMethod(FIELD_DESC, "setDesc", 0);

        digester.addSetNext(FIELD_TOP, "addDocumentQuery" );
        
        for(int i = 0; i < textFiles.length; i++){
        	if(textFiles[i].isFile()){
        		RequeteParser documentQuery = (RequeteParser) digester.parse(textFiles[i]);
	        }
        }
        writer.close();
		simpleLock.clearLock(pathIndex + ".lock");
	}
	
	public void readFileQueriesEnglish(String path) throws Exception{
		String pathFiles = path + DIR_QUERY_ENGLISH;
		String pathIndex = path + INDEX_QUERYS_ENGLISH_DIR;
		readFileQueries(pathFiles, pathIndex);
	}
	
	public void readFileQueriesFrench(String path) throws Exception{
		String pathFiles = path + DIR_QUERY_FRENCH;
		String pathIndex = path + INDEX_QUERYS_FRENCH_DIR;
		readFileQueries(pathFiles, pathIndex);
	}
	
	public void readFileQueriesPortuguese(String path) throws Exception{
		String pathFiles = path + DIR_QUERY_PORT;
		String pathIndex = path + INDEX_QUERYS_PORT_DIR;
		readFileQueries(pathFiles, pathIndex);
	}
	
	public void readFileQueriesFrenchTrans(String path) throws Exception{
		String pathFiles = path + DIR_QUERY_FRENCH_TRANS;
		String pathIndex = path + INDEX_QUERYS_FRENCH_TRANS;
		readFileQueries(pathFiles, pathIndex);
	}
	
	public void readFileQueriesPortugueseTrans(String path) throws Exception{
		String pathFiles = path + DIR_QUERY_PORT_TRANS;
		String pathIndex = path + INDEX_QUERYS_PORT_TRANS;
		readFileQueries(pathFiles, pathIndex);
	}
	
	public void addDocumentQuery(RequeteAP doc) throws Exception{
		Document document = new Document();
		document.add(new Field(FIELD_NUM, doc.getNum(),Field.Store.YES, Field.Index.NO));
		document.add(new Field(FIELD_TITLE, doc.getTitle(), Field.Store.YES,Field.Index.ANALYZED));
		document.add(new Field(FIELD_DESC, doc.getDesc(), Field.Store.YES,Field.Index.ANALYZED));
		writer.addDocument(document);
	}
	
	//Reads the queries from the index created
	public List<Requete> readQueries(String indexPath, boolean expandQuery) throws Exception{
		IndexReader indexReader = this.createIndexReader(indexPath);
		List<Requete> requetes = new ArrayList<Requete>();
		
		for (int i = 0; i < 50; i++){
			Document d = indexReader.document(i);
			String num = d.get(FIELD_NUM);
			String title = "";
			String desc = "";
			if (expandQuery){			
				title = expandQuery(contextPath, d.get(FIELD_TITLE));
				desc = expandQuery(contextPath, d.get(FIELD_DESC));
			}
			else{
				title = d.get(FIELD_TITLE);
				desc = d.get(FIELD_DESC);
			}
			Requete requete = new Requete();
			requete.setNum(num);
			requete.setTitle(title);
			requete.setDesc(desc);
			requetes.add(requete);
		}
		return requetes;
	}
	
	public String expandQuery(String path, String query)throws Exception{
		String queryExpanded = "";
		//Analyzer analyzer = new EnglishAnalyzer(Version.LUCENE_36,my_stop_words);
		Analyzer analyzer = new WhitespaceAnalyzer(Version.LUCENE_36);
		TokenStream ts = analyzer.tokenStream(null, new StringReader(query));
		CharTermAttribute charTermAttribute = ts.addAttribute(CharTermAttribute.class);
		ts.reset();
		while (ts.incrementToken()){
			String token = charTermAttribute.toString();
			queryExpanded+= " " + getExpandedQueryWordNet(path,token);			
		}
		analyzer.close();
		return queryExpanded;
	}
	
	public List<String> getSynonimsWordNet(String word) throws Exception{
		List<String> synonims = new ArrayList<String>();
		IndexSearcher searcher = createIndexSearcher(INDEX_SYNONIMS);
		Query wordTokenized = queryTokenizer_q(word);
		if (wordTokenized != null){
			TopDocs topDocs = searcher.search(wordTokenized, 5);
		    ScoreDoc[] scoreDocs = topDocs.scoreDocs;
		    int maxWords = scoreDocs.length;
		    if (maxWords > 5) maxWords = 5;
		    for (int i = 0; i < maxWords; i++){
			    int idDoc = scoreDocs[i].doc;
			    Document doc = searcher.doc(idDoc);
			    String syn = doc.get("syn");
			    synonims.add(syn);
		    }
		}
		return synonims;
	}
	
	public String getExpandedQueryWordNet(String path, String word) throws Exception{
		String qExpanded = word;
		IndexSearcher searcher = createIndexSearcher(path + INDEX_SYNONIMS);
		Query wordTokenized = queryTokenizer_q(word);
		if (wordTokenized != null){
			TopDocs topDocs = searcher.search(wordTokenized, 5);
		    ScoreDoc[] scoreDocs = topDocs.scoreDocs;
		    int maxWords = scoreDocs.length;
		    if (maxWords > 5) maxWords = 5;
		    for (int i = 0; i < maxWords; i++){
			    int idDoc = scoreDocs[i].doc;
			    Document doc = searcher.doc(idDoc);
			    String syn = doc.get("syn");
			    qExpanded+= " " + syn;
		    }
		}
		return qExpanded;
	}
	
	public List<Requete> readQueryEnglish(String path) throws Exception{
		String indexPath = path + INDEX_QUERYS_ENGLISH_DIR;
		List<Requete> requetes = readQueries(indexPath,false);
		return requetes;
	}
	
	public List<Requete> readExpandedQueryEnglish(String path) throws Exception{
		String indexPath = path + INDEX_QUERYS_ENGLISH_DIR;
		List<Requete> requetes = readQueries(indexPath,true);
		return requetes;
	}
	
	public List<Requete> readQueryFrench(String path) throws Exception{
		String indexPath = path + INDEX_QUERYS_FRENCH_DIR;
		List<Requete> requetes = readQueries(indexPath,false);
		return requetes;
	}
	
	public List<Requete> readQueryPortuguese(String path) throws Exception{
		String indexPath = path + INDEX_QUERYS_PORT_DIR;
		List<Requete> requetes = readQueries(indexPath,false);
		return requetes;
	}
	
	public List<Requete> readQueryFrenchTranslated(String path) throws Exception{
		String indexPath = path + INDEX_QUERYS_FRENCH_TRANS;
		List<Requete> requetes = readQueries(indexPath,false);
		return requetes;
	}
	
	public List<Requete> readQueryPortugueseTranslated(String path) throws Exception{
		String indexPath = path + INDEX_QUERYS_PORT_TRANS;
		List<Requete> requetes = readQueries(indexPath,false);
		return requetes;
	}
	
	//Read the queries from the index, translate them, write them to a file
	public String translateQueries(String indexPath, String fileQueriesTrans, String lang) throws Exception{
		List<Requete> requetes = readQueries(indexPath,false);
		List<Requete> requetesTraduites = translateQueriesMyMemory(requetes,lang);
		writeQueriesFile(requetesTraduites,fileQueriesTrans);
		return "Queries Translated";
	}
	
	public String translateQueriesFrench(String path) throws Exception{
		String indexPath = path + INDEX_QUERYS_FRENCH_DIR;
		String fileQueries = path + DIR_QUERY_FRENCH_TRANS + "queriesFrenchTranslated.txt";
		String lang = "fr";
		String ret = translateQueries(indexPath,fileQueries,lang);
		return ret;
	}
	
	public String translateQueriesPort(String path) throws Exception{
		String indexPath = path + INDEX_QUERYS_PORT_DIR;
		String fileQueries = path + DIR_QUERY_PORT_TRANS + "queriesPortTranslated.txt";
		String lang = "pt";
		String ret = translateQueries(indexPath,fileQueries,lang);
		return ret;
	}
	
	public List<Requete> translateQueriesMyMemory(List<Requete> queries, String lang) throws Exception{
		List<Requete> requetesTraduites = new ArrayList<Requete>();
		for (int i = 0; i < queries.size(); i++){
			Requete query = queries.get(i);
			String title = query.getTitle();
			String titleTrans = translateMyMemory(title, lang);
			Requete queryTrans = new Requete();
			queryTrans.setNum(query.getNum());
			queryTrans.setTitle(titleTrans);
			requetesTraduites.add(queryTrans);
		}
		return requetesTraduites;
	}
	
	private static String convertStreamToString(InputStreamReader is) {
	    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
	    return s.hasNext() ? s.next() : "";
	}
	
	public String translateMyMemory(String text, String lang) throws Exception{
		
		String urlMyMemory = "http://mymemory.translated.net/api/get?q=";
		String langPairSt = "&langpair=";
		String langPair = lang + "|en";
		String textEncoded = URLEncoder.encode(text, "utf-8");
		String langPairEnc = URLEncoder.encode(langPair, "utf-8");
		String de = "&de=affonseca@gmail.com";
		
		String query = urlMyMemory + textEncoded + langPairSt + langPairEnc + de;
		
		String textTranslated = "";
		try {
			URL url = new URL(query);
			URLConnection conn = url.openConnection();
		
			conn.setDoOutput(true);
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			//conn.setRequestMethod("GET");
		
			InputStreamReader content = new InputStreamReader(conn.getInputStream());
			String contentString = convertStreamToString(content); 
			JSONObject json = new JSONObject(contentString);
			if (json != null && !json.equals("")){
				JSONObject jsonObj = (JSONObject)json.get("responseData");
				textTranslated = (String)jsonObj.get("translatedText");
			}
		}
		catch (MalformedURLException e) { 
			// new URL() failed
			System.out.println(e.toString());
			System.out.print("Malformed URL");
		} 
		catch (IOException e) {   
			System.out.println(e.toString());
			System.out.print("openConnection() failed");
		}
		catch (ParseException e) {   
			System.out.println(e.toString());
			System.out.print("parse failed");
		}
		return textTranslated;
	}
	
	private void writeQueriesFile(List<Requete> requetes, String fileQueriesTrans){
		
		File file = new File(fileQueriesTrans);
        FileWriter fr = null;
        BufferedWriter br = null;
        String separator = System.getProperty("line.separator");
        try{
            fr = new FileWriter(file);
            br = new BufferedWriter(fr);
            br.write("<BEGIN>" + separator);
            int qtdQueries = requetes.size();
            for (int i = 0; i < qtdQueries; i++){
	            Requete requete = requetes.get(i);
	            String desc = requete.getDesc();
	            if (desc == null) desc = "";
	            br.write("<top>" + separator);
	            br.write("<num>" + requete.getNum() + "</num>" + separator);
	            br.write("<title>" + requete.getTitle() + "</title>" + separator);
	            br.write("<desc>" + desc + "</desc>" + separator);
	            br.write("</top>" + separator);
            }
            br.write("</BEGIN>");
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                br.close();
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
	}
	
	public List<String> getShortQueries(List<Requete> listQueries) throws Exception{
		List<String> shortQueries = new ArrayList<String>();
		for (int i = 0; i < listQueries.size(); i++){
			String q = listQueries.get(i).getTitle();
			shortQueries.add(q);
		}
		return shortQueries;
	}
	
	public List<String> getLongQueries(List<Requete> listQueries) throws Exception{
		List<String> longQueries = new ArrayList<String>();
		for (int i = 0; i < listQueries.size(); i++){
			//String q = listQueries.get(i).getTitle() + " " + listQueries.get(i).getDesc();
			String q = listQueries.get(i).getDesc();
			longQueries.add(q);
		}
		return longQueries;
	}
	
	public String queryTokenizer(String query) throws Exception{
		Analyzer analyzer = new EnglishAnalyzer(Version.LUCENE_36,my_stop_words);
		AnalyzingQueryParser qp = new AnalyzingQueryParser(Version.LUCENE_36,"",analyzer);
		Query q = qp.parse(query);
		String s = q.toString();
		return s;
	}
	
	public Query queryTokenizer_q(String query) throws Exception{
		Analyzer analyzer = new EnglishAnalyzer(Version.LUCENE_36,my_stop_words);
		AnalyzingQueryParser qp = new AnalyzingQueryParser(Version.LUCENE_36,"word",analyzer);
		Query q = qp.parse(query);
		return q;
	}
	
	public String queryTokenizerBigrams(String query, boolean withUnigrams) throws Exception{
		Analyzer analyzer = new EnglishAnalyzer(Version.LUCENE_36,my_stop_words);
		ShingleAnalyzerWrapper shingleAnalyzerWrapper = new ShingleAnalyzerWrapper(analyzer);
		shingleAnalyzerWrapper.setOutputUnigrams(withUnigrams);
		AnalyzingQueryParser qp = new AnalyzingQueryParser(Version.LUCENE_36,"",shingleAnalyzerWrapper);
		Query q = qp.parse(query);
		String s = q.toString();
		return s;
	}
	
	private IndexSearcher createIndexSearcher(String dir) throws Exception{
		IndexReader indexReader = createIndexReader(dir);
		IndexSearcher searcher = new IndexSearcher(indexReader);
		return searcher;
	}
	
	private IndexReader createIndexReader(String dir) throws Exception{
		File   indexDir = new File(dir);
		Directory directory = new SimpleFSDirectory(indexDir);
		IndexReader indexReader = IndexReader.open(directory);
		return indexReader;
	}
		
	public static class RequeteAP{
		
		private String num;
		private String title;
		private String desc;
		
		public String getNum(){
			return this.num;
		}
		public void setNum(String num){
			this.num = num;
		}
		
		public String getTitle(){
			return this.title;
		}
		public void setTitle(String title){
			this.title = title;
		}
		
		public String getDesc(){
			return this.desc;
		}
		public void setDesc(String desc){
			this.desc = desc;
		}
	}
}
