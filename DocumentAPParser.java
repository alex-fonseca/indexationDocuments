package ca.uqam.dic.indexation.controle;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.digester.Digester;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.ngram.NGramTokenizer;
import org.apache.lucene.analysis.shingle.ShingleAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermDocs;
import org.apache.lucene.index.TermFreqVector;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.queryParser.analyzing.AnalyzingQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Similarity;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockFactory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.store.SimpleFSLockFactory;
import org.apache.lucene.util.Version;

import ca.uqam.dic.indexation.modele.*;


public class DocumentAPParser {
	
	
	private static IndexWriter writer;
	public static final String FILES_TO_INDEX_DIRECTORY = "index\\filesToIndex";
	//public static final String INDEX_DIRECTORY = "index\\indexDocBigrams"; //with bigrams
	public static final String INDEX_DIRECTORY = "index\\indexDocSWBigger"; //SW bigger
	//public static final String INDEX_DIRECTORY = "index\\indexDocTotal"; //SW standard
	//public static final String INDEX_DIRECTORY = "index\\indexDocTeste";
	//public static final String INDEX_DIRECTORY = "index\\indexDocSWBiggerHead"; 
	//public static final String INDEX_DIRECTORY = "index\\indexDocNgrams2_4"; //ngrams2-4, empty stop words
	//public static final String INDEX_DIRECTORY = "index\\indexDocNgrams1_4"; //ngrams1-4, with stop words
	
	public static final int MAX_HITS_TREC = 1000;
	public static final int MAX_HITS_SEARCH = 10;
	public static final float THRESHOLD_IDF = 3.5f;
	
	public static final String TREC_ENGLISH_SHORT_Q = "trec_english_short";
	public static final String TREC_ENGLISH_LONG_Q = "trec_english_long";
	public static final String TREC_FRENCH_SHORT_Q = "trec_french_short";
	public static final String TREC_PORT_SHORT_Q = "trec_port_short";
	
	public static final String FIELD_DOC = "BEGIN/DOC"; 
	public static final String FIELD_DOCNO = "BEGIN/DOC/DOCNO";
	public static final String FIELD_FILEID = "BEGIN/DOC/FILEID"; 
	public static final String FIELD_FIRST = "BEGIN/DOC/FIRST";
	public static final String FIELD_SECOND = "BEGIN/DOC/SECOND"; 
	public static final String FIELD_HEAD = "BEGIN/DOC/HEAD";
	public static final String FIELD_DATELINE = "BEGIN/DOC/DATELINE"; 
	public static final String FIELD_TEXT = "BEGIN/DOC/TEXT";
	
	public static final int QUERY_LENGTH_SHORT = 1;
	public static final int QUERY_LENGTH_LONG = 2;
	
	public Set<String> my_stop_words = new HashSet<String>(Arrays.asList("a","and", "are", "as",  
			"at", "be", "but", "by", "for", "if", "in", "into", "is","no", "not", "of", "on", "or", 
			"s", "such", "t", "that", "the", "their", "then", "there", "these", "they", 
			"this", "to", "was", "will", "with", "inc","incorporated","co.","ltd","ltd.",
			"it","had","said","from","he","have","a","year","were","who","been","which",
			"after","more","than","all","most","them","did","those","who","10","100","1000",
			"1","2","3","4","5","6","7","8","9","among","former","give","remain","without",
			"number","becom","total","begin","sinc","call","under","end","up","an","ha","would"));
	
	public DocumentAPParser(){
		
	}
	
	public void geraIndex(String path) throws Exception{
	    

		File   fileDir  = new File(path + FILES_TO_INDEX_DIRECTORY);
		File   indexDir = new File(path + INDEX_DIRECTORY);
		File[] textFiles  = fileDir.listFiles();
		Directory directory = new SimpleFSDirectory(indexDir);
		LockFactory simpleLock = new SimpleFSLockFactory(path + INDEX_DIRECTORY);
		simpleLock.makeLock(path + INDEX_DIRECTORY + ".lock");

		//Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
		//Analyzer analyzer = new WhitespaceAnalyzer(Version.LUCENE_36);
		Analyzer analyzer = new EnglishAnalyzer(Version.LUCENE_36, my_stop_words);
		//Analyzer analyzer = new EnglishAnalyzer(Version.LUCENE_36, new HashSet<String>());
		
		ShingleAnalyzerWrapper shingleAnalyzerWrapper = new ShingleAnalyzerWrapper(analyzer,2,4);
		//shingleAnalyzerWrapper.setOutputUnigrams(false);
		IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_36, shingleAnalyzerWrapper);
		
		//IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_36, analyzer);
		writer = new IndexWriter(directory, iwc);
		
        Digester digester = new Digester();
        digester.setValidating(false);

        digester.addObjectCreate("BEGIN", DocumentAPParser.class);
        digester.addObjectCreate(FIELD_DOC, DocumentAP.class);

        digester.addCallMethod(FIELD_DOCNO, "setDocno", 0);
        digester.addCallMethod(FIELD_FILEID, "setFileid", 0);
        digester.addCallMethod(FIELD_FIRST, "setFirst", 0);
        digester.addCallMethod(FIELD_SECOND, "setSecond", 0);
        digester.addCallMethod(FIELD_HEAD, "setHead", 0);
        digester.addCallMethod(FIELD_DATELINE, "setDateline", 0);
        digester.addCallMethod(FIELD_TEXT, "setText", 0);

        digester.addSetNext(FIELD_DOC, "addDocumentAP" );
  
        for(int i = 0; i < textFiles.length; i++){
        	if(textFiles[i].isFile()){
        		DocumentAPParser documentAP = (DocumentAPParser) digester.parse(textFiles[i]);
	        }
        }
		writer.close();
		simpleLock.clearLock(path + INDEX_DIRECTORY + ".lock");
	}
	
	public void addDocumentAP(DocumentAP doc) throws Exception{
		Document document = new Document();
		document.add(new Field(FIELD_DOCNO, doc.getDocno(),Field.Store.YES, Field.Index.NO));
		//Field head = new Field(FIELD_HEAD, doc.getHead(),Field.Store.YES,Field.Index.ANALYZED, Field.TermVector.YES);
		Field text = new Field(FIELD_TEXT, doc.getText(), Field.Store.YES,Field.Index.ANALYZED, Field.TermVector.YES);
		//head.setBoost(0.8f);
		//text.setBoost(0.2f);
		//document.add(head);
		document.add(text);
		writer.addDocument(document);
	}
	
	public List<Terme> getTermFrequency(String path, String nroDocument) throws Exception{
	    IndexReader indexReader = createIndexReader(path);
	    IndexSearcher searcher = new IndexSearcher(indexReader);
	    List<Terme> listTerms = new ArrayList<Terme>();
	    DecimalFormat twoDForm = new DecimalFormat("#.##");
	    int idDoc = getIdDocByNumber(searcher, nroDocument);
	    TermFreqVector tfv = indexReader.getTermFreqVector(idDoc,FIELD_TEXT);
	    for (int j = 0; j < tfv.getTerms().length; j++) {
            String item = tfv.getTerms()[j];
            
            String stParsed = queryWordTokenizer(item);
    		Term t = new Term(FIELD_TEXT,stParsed);
    		Query q = new TermQuery(t);
    		TopDocs topDocs = searcher.search(q, 1);
    		
    		int tf = tfv.getTermFrequencies()[j];
    		double idf = 0;
    		if (topDocs.totalHits > 0)
    			idf = 1 + Math.log10(indexReader.maxDoc()/(topDocs.totalHits+1));
    		double tfidf = tf*idf;
    		
    		Terme terme = new Terme(item, Integer.toString(tf));
    		terme.setDf(Integer.toString(topDocs.totalHits));
    		terme.setIdf(twoDForm.format(idf));
    		terme.setTfidf(twoDForm.format(tfidf));
            listTerms.add(terme);
        }
	    searcher.close();
	    Collections.sort(listTerms, new Comparator<Terme>(){
	        public int compare(Terme t1, Terme t2){
	            if(Double.parseDouble(t1.getTfidf()) == Double.parseDouble(t2.getTfidf()))
	                return 0;
	            return Double.parseDouble(t1.getTfidf()) > Double.parseDouble(t2.getTfidf()) ? -1 : 1;
	        }
	    });
	    /*Collections.sort(listTerms, new Comparator<Terme>(){
	        public int compare(Terme t1, Terme t2){
	            if(Integer.parseInt(t1.getDf()) == Integer.parseInt(t2.getDf()))
	                return 0;
	            return Integer.parseInt(t1.getDf()) > Integer.parseInt(t2.getDf()) ? -1 : 1;
	        }
	    });*/
		return listTerms;
	}
	
	public Terme getDocFrequency(String path, String term) throws Exception{
		
	    IndexReader indexReader = createIndexReader(path);
	    IndexSearcher searcher = new IndexSearcher(indexReader);
	    DecimalFormat twoDForm = new DecimalFormat("#.##");
	    Terme terme = new Terme(term);
	    
	    List<DocFreq> listDocs = new ArrayList<DocFreq>();
	    String stParsed = queryWordTokenizer(term);
		Term t = new Term(FIELD_TEXT,stParsed);
		TermDocs termDocs = indexReader.termDocs(t);
		Query q = new TermQuery(t);
		TopDocs topDocs = searcher.search(q, 1);
		while (termDocs.next()){
			int idDoc = termDocs.doc();
			int freq = termDocs.freq();
			Document d = indexReader.document(idDoc);
			String nroDoc = d.get(FIELD_DOCNO);
			listDocs.add(new DocFreq(nroDoc,Integer.toString(freq)));
		}
		double idf = 0;
		if (topDocs.totalHits > 0)
			idf = 1 + Math.log10(indexReader.maxDoc()/(topDocs.totalHits+1));
	    listDocs = calculateTfidf(listDocs,idf);
	    
	    Collections.sort(listDocs, new Comparator<DocFreq>(){
	        public int compare(DocFreq doc1, DocFreq doc2){
	            if(Double.parseDouble(doc1.getTfidf()) == Double.parseDouble(doc2.getTfidf()))
	                return 0;
	            return Double.parseDouble(doc1.getTfidf()) > Double.parseDouble(doc2.getTfidf()) ? -1 : 1;
	        }
	   });
	    
	    terme.setDf(Integer.toString(topDocs.totalHits));
	    terme.setIdf(twoDForm.format(idf));
	    terme.setDocFreqList(listDocs);
	    searcher.close();
		return terme;
	}
	
	public DocAP getDocument(String path, String nroDoc) throws Exception{
		DocAP doc = new DocAP(nroDoc);
		IndexSearcher searcher = this.createIndexSearcher(path);
		Document d = getDocByNumber(searcher, nroDoc);
		if (d != null)
			doc.setText(d.get(FIELD_TEXT));
		return doc;
	}
	
	private Document getDocByNumber(IndexSearcher searcher, String nroDocument) throws Exception{
		Document doc = new Document();
		boolean docFound = false;
		for (int i = 0; i < searcher.maxDoc() && !docFound; i++){
			Document d = searcher.doc(i);
			String dl = d.get(FIELD_DOCNO);
			if (nroDocument.equals(dl)){
				docFound = true;
				doc = d;
			}
		}
		return doc;
	}
	
	private int getIdDocByNumber(IndexSearcher searcher, String nroDocument) throws Exception{
		boolean docFound = false;
		int idDoc = 0;
		for (int i = 0; i < searcher.maxDoc() && !docFound; i++){
			Document d = searcher.doc(i);
			String dl = d.get(FIELD_DOCNO);
			if (nroDocument.equals(dl)){
				docFound = true;
				idDoc = i;
			}
		}
		return idDoc;
	}
	
	public List<Terme> searchMultipleQueriesIndex(String path, List<String> shortQueries, List<String> longQueries, int qtdHits) throws Exception{
		List<Terme> termes = new ArrayList<Terme>();
		List<String> newShortQueries = filterQueriesByIDF(path,THRESHOLD_IDF,shortQueries);
		List<String> newLongQueries = filterQueriesByIDF(path,THRESHOLD_IDF,longQueries);
		//List<String> newShortQueries = shortQueries;
		//List<String> newLongQueries = longQueries;
		if (newLongQueries.size() == 0) {
			for (int i = 0; i < newShortQueries.size(); i++){
				Terme t = searchQueryIndex(path, newShortQueries.get(i), "", qtdHits);
				termes.add(t);
			}
		}
		else{
			for (int i = 0; i < newShortQueries.size(); i++){
				Terme t = searchQueryIndex(path, newShortQueries.get(i), newLongQueries.get(i), qtdHits);
				termes.add(t);
			}
		}
		return termes;
	}
	
	private List<String> filterQueriesByIDF(String path, float threshold, List<String> queries) throws Exception{
		List newQueries = new ArrayList<String>();
		IndexSearcher searcher = createIndexSearcher(path);
		Analyzer analyzer = new WhitespaceAnalyzer(Version.LUCENE_36);
	    Iterator sQueries = queries.iterator();
	    int i = 1;
		while (sQueries.hasNext()){
			String newQuery = "";
			double idfTotalQuery = 0;
			double idfMeanQuery = 0;
			List idfTerms = new ArrayList<String>();
			List terms = new ArrayList<String>();
			String query = (String)sQueries.next();
			TokenStream ts = analyzer.tokenStream(null, new StringReader(query));
			CharTermAttribute charTermAttribute = ts.addAttribute(CharTermAttribute.class);
			ts.reset();
			while (ts.incrementToken()) {
				String word = charTermAttribute.toString();
				String stParsed = queryWordTokenizer(word);
				Term term = new Term(FIELD_TEXT,stParsed);
				Query q = new TermQuery(term);
				TopDocs topDocs = searcher.search(q, 1); 
				double idf = 0;
				if (topDocs.totalHits > 0)
					idf = 1 + Math.log10(searcher.maxDoc()/(topDocs.totalHits+1));
				idfTotalQuery+= idf;
				terms.add(word);
				idfTerms.add(Double.toString(idf));
			}
			idfMeanQuery = idfTotalQuery/terms.size();
			for (int j = 0; j < terms.size(); j++){
				double idfTerm = Double.parseDouble((String)idfTerms.get(j));
				if (idfTerm >= idfMeanQuery)
					newQuery += " " + terms.get(j);
			}
			if (newQuery.equals(""))
				newQuery = query;
			System.out.println("QUERY" + i + " = " + newQuery);
			i++;
			newQueries.add(newQuery);
		}
		return newQueries;
	}
	
	public Terme searchQueryIndex(String path, String shortQuery, String longQuery, int qtdHits) throws Exception{
		
	    IndexSearcher searcher = createIndexSearcher(path);
	    Terme terme = new Terme(shortQuery + longQuery);
	    List<DocFreq> lts = new ArrayList<DocFreq>();
	    //Query q = queryTokenizerBigramsShortLong(shortQuery,longQuery);
	    Query q = queryTokenizerShortLong(shortQuery,longQuery);
	    
	    int qtdDocIndex = searcher.maxDoc();
	    TopDocs topDocs = searcher.search(q, qtdDocIndex);
	    ScoreDoc[] scoreDocs = topDocs.scoreDocs;
	    int totalHits = topDocs.totalHits;
	    if (totalHits > qtdHits) totalHits = qtdHits;
	    for (int i = 0; i < totalHits; i++){
		    int idDoc = scoreDocs[i].doc;
		    float score = scoreDocs[i].score;
		    Document doc = searcher.doc(idDoc);
		    DocFreq docFreq = new DocFreq(doc.get(FIELD_DOCNO));
		    docFreq.setScore(Float.toString(score));
		    lts.add(docFreq);
	    }
	    terme.setDocFreqList(lts);
	    terme.setDf(Integer.toString(topDocs.totalHits));
		return terme;
	}
	
	public Terme searchTermIndex(String path, String query) throws Exception{
		
	    IndexSearcher searcher = createIndexSearcher(path);
	    Terme terme = new Terme(query);
	    List<DocFreq> lts = new ArrayList<DocFreq>(); 
	    String stParsed = queryWordTokenizer(query);
	    Term t = new Term(FIELD_TEXT, stParsed);
	    Query q = new TermQuery(t);
	    
	    TopDocs topDocs = searcher.search(q, 10);
	    ScoreDoc[] scoreDocs = topDocs.scoreDocs;
	    for (int i = 0; i < 10; i++){
		    int idDoc = scoreDocs[i].doc;
		    float score = scoreDocs[i].score;
		    Document doc = searcher.doc(idDoc);
		    DocFreq docFreq = new DocFreq(doc.get(FIELD_DOCNO));
		    docFreq.setScore(Float.toString(score));
		    lts.add(docFreq);
	    }
	    terme.setDocFreqList(lts);
	    terme.setDf(Integer.toString(topDocs.totalHits));
		return terme;
	}
	
	public List<DocFreq> calculateTfidf(List<DocFreq> listDocs, double idf){
		List<DocFreq> lts = new ArrayList<DocFreq>();
		DecimalFormat twoDForm = new DecimalFormat("#.##");
		for (int i = 0; i < listDocs.size();i++){
			DocFreq docF = listDocs.get(i);
			int freq = Integer.parseInt(docF.getFreq());
			double tfidf = freq*idf;
			docF.setTfidf(twoDForm.format(tfidf));
			lts.add(docF);
		}
		return lts;
	}
	
	public String generateTrecFile(List<Terme> queryResult, String path){
		String qtdDoc = writeTrecFile(queryResult, path);
		return qtdDoc;
	}
	
	public String queryWordTokenizer(String word) throws Exception{
		Analyzer analyzer = new EnglishAnalyzer(Version.LUCENE_36,my_stop_words);
		AnalyzingQueryParser q = new AnalyzingQueryParser(Version.LUCENE_36,"",analyzer);
		Query query = q.parse(word);
		String s = query.toString();
		return s;
	}
	
	public Query queryTokenizerBigrams(String line, boolean withUnigrams) throws Exception{
		Analyzer analyzer = new EnglishAnalyzer(Version.LUCENE_36,my_stop_words);
		ShingleAnalyzerWrapper shingleAnalyzerWrapper = new ShingleAnalyzerWrapper(analyzer,2,4);
		//shingleAnalyzerWrapper.setOutputUnigrams(withUnigrams);
		AnalyzingQueryParser q = new AnalyzingQueryParser(Version.LUCENE_36,FIELD_TEXT,shingleAnalyzerWrapper);
		//q.setDefaultOperator(QueryParser.Operator.AND);
		Query query = q.parse(line);
		return query;
	}
	
	public Query queryTokenizerBigramsShortLong(String shortQuery, String longQuery) throws Exception{
		Analyzer analyzer = new EnglishAnalyzer(Version.LUCENE_36,my_stop_words);
		ShingleAnalyzerWrapper shingleAnalyzerWrapper = new ShingleAnalyzerWrapper(analyzer,2,4);
		//shingleAnalyzerWrapper.setOutputUnigrams(false);
		//AnalyzingQueryParser qHead = new AnalyzingQueryParser(Version.LUCENE_36,FIELD_HEAD,shingleAnalyzerWrapper);
		AnalyzingQueryParser qText = new AnalyzingQueryParser(Version.LUCENE_36,FIELD_TEXT,shingleAnalyzerWrapper);
		
		
		//Query queryShortHead = qHead.parse(shortQuery); //queryShortHead.setBoost(10f);
		//Query queryLongHead = qHead.parse(shortQuery + " " + longQuery);   //queryLongHead.setBoost(5f);
		//Query queryShortText = qText.parse(shortQuery); //queryShortText.setBoost(5f);
		Query queryLongText = qText.parse(shortQuery + " " + longQuery); //queryLongText.setBoost(1f);
		
		//BooleanQuery combined = new BooleanQuery();
		//combined.add(queryShortHead, BooleanClause.Occur.SHOULD);
		//combined.add(queryLongHead, BooleanClause.Occur.SHOULD);
		//combined.add(queryShortText, BooleanClause.Occur.SHOULD);
		//combined.add(queryLongText, BooleanClause.Occur.SHOULD);
		
		//return combined;
		return queryLongText;
	}
	
	public Query queryTokenizer(String line) throws Exception{
		Analyzer analyzer = new EnglishAnalyzer(Version.LUCENE_36,my_stop_words);
		//AnalyzingQueryParser qHead = new AnalyzingQueryParser(Version.LUCENE_36,FIELD_HEAD,analyzer);
		AnalyzingQueryParser qText = new AnalyzingQueryParser(Version.LUCENE_36,FIELD_TEXT,analyzer);
		//q.setDefaultOperator(QueryParser.Operator.AND);
		//Query queryHead = qHead.parse(line);
		Query queryText = qText.parse(line);
		//BooleanQuery combined = new BooleanQuery();
		//combined.add(queryText, BooleanClause.Occur.SHOULD);
		//combined.add(queryHead, BooleanClause.Occur.SHOULD);
		//return combined;
		return queryText;
	}
	
	public Query queryTokenizerShortLong(String shortQuery, String longQuery) throws Exception{
		Analyzer analyzer = new EnglishAnalyzer(Version.LUCENE_36,my_stop_words);
		//AnalyzingQueryParser qHead = new AnalyzingQueryParser(Version.LUCENE_36,FIELD_HEAD,analyzer);
		AnalyzingQueryParser qText = new AnalyzingQueryParser(Version.LUCENE_36,FIELD_TEXT,analyzer);
		
		//Query queryShortHead = qHead.parse(shortQuery); //queryShortHead.setBoost(10f);
		//Query queryLongHead = qHead.parse(shortQuery + " " + longQuery);   //queryLongHead.setBoost(5f);
		//Query queryShortText = qText.parse(shortQuery); //queryShortText.setBoost(5f);
		Query queryLongText = qText.parse(shortQuery + " " + longQuery); //queryLongText.setBoost(1f);
		
		//BooleanQuery combined = new BooleanQuery();
		//combined.add(queryShortHead, BooleanClause.Occur.SHOULD);
		//combined.add(queryLongHead, BooleanClause.Occur.SHOULD);
		//combined.add(queryShortText, BooleanClause.Occur.SHOULD);
		//combined.add(queryLongText, BooleanClause.Occur.SHOULD);
		
		//return combined;
		return queryLongText;
	}
	
	private IndexSearcher createIndexSearcher(String path) throws Exception{
		IndexReader indexReader = createIndexReader(path);
		IndexSearcher searcher = new IndexSearcher(indexReader);
		return searcher;
	}
	
	private IndexReader createIndexReader(String path) throws Exception{
		File   indexDir = new File(path + INDEX_DIRECTORY);
		Directory directory = new SimpleFSDirectory(indexDir);
		IndexReader indexReader = IndexReader.open(directory);
		return indexReader;
	}
	
	//
	private static String writeTrecFile(List<Terme> resultQueries, String path) {
        int totalDocs = 0;
		File file = new File(path);
        FileWriter fr = null;
        BufferedWriter br = null;
        String separator = System.getProperty("line.separator");
        try{
            fr = new FileWriter(file);
            br = new BufferedWriter(fr);
            int qtdQueries = resultQueries.size();
            for (int i = 0; i < qtdQueries; i++){
	            Terme query = resultQueries.get(i);
	            List <DocFreq> docs = query.getDocFreqList();
	            int qtdDocs = docs.size();
	            totalDocs+= qtdDocs;
	            if (qtdDocs > 0){
	            	for (int j = 0; j < qtdDocs; j++){
	            		DocFreq doc = docs.get(j);
		            	br.write(Integer.toString(i+1) + " Q0 "); //Field queryNumber
		            	br.write(doc.getNroDoc() + " "); //Field doc_number
		            	br.write(Integer.toString(j+1) + " "); //Field rank
		            	br.write(doc.getScore() + " STAND " + separator); //Field score
	            	}
	            }
            }
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
        return Integer.toString(totalDocs);
    }
	
	public List<String> getTerms(String path) throws Exception{
		
	    List<String> listTerms = new ArrayList<String>();
		IndexSearcher searcher = createIndexSearcher(path);
		for (int i = 0; i < searcher.maxDoc(); i++){
			Document d = searcher.doc(i);
			String dl = d.get(FIELD_DOCNO);
			listTerms.add(dl);
		}
		searcher.close();
		return listTerms;
	}	

public static class DocumentAP{
	
	private String docno;
	private String fileid;
	private String first;
	private String second;
	private String head;
	private String dateline;
	private String text;
	
	public String getDocno(){
		return this.docno;
	}
	public void setDocno(String docno){
		this.docno = docno;
	}
	
	public String getFileid(){
		return this.fileid;
	}
	public void setFileid(String fileid){
		this.fileid = fileid;
	}
	
	public String getFirst(){
		return this.first;
	}
	public void setFirst(String first){
		this.first = first;
	}
	
	public String getSecond(){
		return this.second;
	}
	public void setSecond(String second){
		this.second = second;
	}
	
	public String getHead(){
		if (this.head == null) this.head = "";
		return this.head;
	}
	public void setHead(String head){
		this.head = head;
	}
	
	public String getDateline(){
		return this.dateline;
	}
	public void setDateline(String dateline){
		this.dateline = dateline;
	}
	
	public String getText(){
		return this.text;
	}
	public void setText(String text){
		this.text = text;
	}
}
}
