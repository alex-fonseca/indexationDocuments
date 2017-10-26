/*
Copyright 2007 David Spencer.

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
*/
package ca.uqam.dic.indexation.controle;

import java.io.*;
import java.util.*;

import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
//import org.apache.lucene.analysis.standard.*;
import org.apache.lucene.index.*;
import org.apache.lucene.store.Directory;
//import org.apache.lucene.store.LockFactory;
import org.apache.lucene.store.SimpleFSDirectory;
//import org.apache.lucene.store.SimpleFSLockFactory;
import org.apache.lucene.util.Version;
import org.apache.lucene.document.*;

/**
 * Convert the prolog file wn_s.pl from the wordnet prolog download
 * into a Lucene index suitable for looking up synonyms.
 * The index is named 'syn_index' and has fields named "word"
 * and "syn".
 * 
 * The source word (such as 'big') can be looked up in the
 * "word" field, and if present there will be fields named "syn"
 * for every synonym.
 *
 * <p>
 *
 * While the wordnet file distinguishes groups of synonyms with
 * related meanings we don't do that here.
 *
 * <p>
 * By default, with no args, we expect the prolog
 * file to be at 'c:/proj/wordnet/prolog/wn_s.pl' and will
 * write to an index named 'syn_index' in the current dir.
 * See constants at the bottom of this file to change these.
 */
public final class Syns2Index
{
	/**
	 * Take optional arg of prolog file name.
	 */
	public static void main(  String[] a)
		throws Throwable
	{
		String fn = PROLOG;
		if ( a.length == 1)
			fn = a[ 0];
		o.println( "Opening " + fn);
		final FileInputStream fis = new FileInputStream( fn);
		final DataInputStream dis = new DataInputStream( fis);
		String line;

		// maps a word to all the "groups" it's in
		final Map word2Nums = new HashMap();
		// maps a group to all the words in it
		final Map num2Words = new HashMap();
		int ndecent =0; // number of rejected words

		// status output
		int mod = 1;
		int row = 1;		
		// parse prolog file
		while ( ( line = dis.readLine()) != null)
		{
			String oline = line;

			// occasional progress
			if ( (++row) % mod == 0)
			{
				mod *= 2;
				o.println( "" + row + " " +line + " " + word2Nums.size() + " "+ num2Words.size() + " ndecent=" +ndecent);
			}

			// syntax check
			if ( ! line.startsWith( "s("))
			{
				err.println( "OUCH: "+ line);
				System.exit( 1);
			}

			// parse line
			line = line.substring( 2);
			int comma = line.indexOf( ',');
			String num = line.substring( 0, comma);
			int q1 = line.indexOf( '\'');
			line = line.substring( q1+1);
			int q2 = line.indexOf( '\'');
			String word = line.substring( 0, q2).toLowerCase();

			// make sure is a normal word
			if ( ! isDecent( word))
			{
				ndecent++;
				continue; // don't store words w/ spaces
			}

			// 1/2: word2Nums map
			// append to entry or add new one
			List lis =(List) word2Nums.get( word);
			if ( lis == null)
			{
				lis = new LinkedList();
				lis.add( num);
				word2Nums.put( word, lis);
			}
			else
				lis.add( num);

			// 2/2: num2Words map 
			lis = (List) num2Words.get( num);
			if ( lis == null)
			{
				lis =new LinkedList();
				lis.add( word);
				num2Words.put( num, lis); 
			}
			else
				lis.add( word);
		}

		// form the index
		index( word2Nums, num2Words);
	}

	/**
	 * Check to see if a word is alphabetic.
	 */
	private static boolean isDecent( String s)
	{
		int len = s.length();
		for ( int i = 0; i < len; i++)
			if ( ! Character.isLetter( s.charAt( i)))
				return false;
		return true;
	}

	/**
	 * Form a lucene index based on the 2 maps.
	 */
	private static void index( Map word2Nums, Map num2Words)
		throws Throwable
	{
		int row = 0;
		int mod = 1;
		
		File   indexDir = new File(INDEX_DIRECTORY);
		Directory directory = new SimpleFSDirectory(indexDir);
		//LockFactory simpleLock = new SimpleFSLockFactory(INDEX_DIRECTORY);
		//simpleLock.makeLock(INDEX_DIRECTORY + ".lock");

		Analyzer analyzer = new EnglishAnalyzer(Version.LUCENE_36, my_stop_words);
		
		IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_36, analyzer);
		IndexWriter writer = new IndexWriter(directory, iwc);
		
		//IndexWriter writer = new IndexWriter( INDEX, ana, true);
		Iterator i1 = word2Nums.keySet().iterator();
		while ( i1.hasNext()) // for each word
		{
			String g = (String) i1.next();
			Document doc = new Document();
			
			int n = index( word2Nums, num2Words, g, doc);
			if ( n > 0)
			{
				doc.add( new Field("word",g,Field.Store.YES, 
						Field.Index.ANALYZED));
				if ( (++row % mod) == 0)
				{
					o.println( "row=" +row + " doc= " + doc);
					mod *= 2;
				}
				writer.addDocument( doc);
			} // else degenerate
		}
		//writer.optimize();
		writer.close();		
	}
	
	/**
	 * Given the 2 maps fill a document for 1 word.
	 */
	private static int index( Map word2Nums, Map num2Words, String g, Document doc)
		throws Throwable
	{
		List keys = (List) word2Nums.get( g); // get list of key#'s
		Iterator i2 = keys.iterator();

		Set already = new TreeSet(); // keep them sorted

		// pass 1: fill up 'already' with all words
		while ( i2.hasNext()) // for each key#
		{
			already.addAll( (List) num2Words.get( i2.next())); // get list of words
		}
		int num = 0;		
		//StringBuffer res = new StringBuffer();
		already.remove( g); // of course a word is it's own syn
		Iterator it = already.iterator();
		while ( it.hasNext())
		{
			String cur = (String) it.next();
			if ( ! isDecent( cur)) continue; // don't store things like 'pit bull' -> 'american pit bull'
			num++;
			
			doc.add( new Field("syn",cur,Field.Store.YES, 
					Field.Index.NO));
		}
		return num;
	}

	//private static Analyzer ana = new StandardAnalyzer();
	private static final PrintStream o = System.out;
	private static final PrintStream err = System.err;
	private static final String PROLOG = "C:\\Users\\Public\\Documents\\Prolog\\wn_s.pl";
	//private static final String INDEX = "syn_index";
	public static final String INDEX_DIRECTORY = "C:\\Users\\Public\\Documents\\indexSyns";
	public static final Set<String> my_stop_words = new HashSet<String>(Arrays.asList("a","and", "are", "as",  
			"at", "be", "but", "by", "for", "if", "in", "into", "is","no", "not", "of", "on", "or", 
			"s", "such", "t", "that", "the", "their", "then", "there", "these", "they", 
			"this", "to", "was", "will", "with", "inc","incorporated","co.","ltd","ltd.",
			"it","had","said","from","he","have","a","year","were","who","been","which",
			"after","more","than","all","most","them","did","those","who","10","100","1000",
			"1","2","3","4","5","6","7","8","9","among","former","give","remain","without",
			"number","becom","total","begin","sinc","call","under","end","up","an","ha","would"));
}
