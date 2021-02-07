package lse;

import java.io.*;
import java.util.*;

/**
 * This class builds an index of keywords. Each keyword maps to a set of pages in
 * which it occurs, with frequency of occurrence in each page.
 *
 */
public class LittleSearchEngine {
	
	/**
	 * This is a hash table of all keywords. The key is the actual keyword, and the associated value is
	 * an array list of all occurrences of the keyword in documents. The array list is maintained in 
	 * DESCENDING order of frequencies.
	 */
	HashMap<String,ArrayList<Occurrence>> keywordsIndex;
	
	/**
	 * The hash set of all noise words.
	 */
	HashSet<String> noiseWords;
	
	/**
	 * Creates the keyWordsIndex and noiseWords hash tables.
	 */
	public LittleSearchEngine() {
		keywordsIndex = new HashMap<String,ArrayList<Occurrence>>(1000,2.0f);
		noiseWords = new HashSet<String>(100,2.0f);
	}
	
	/**
	 * Scans a document, and loads all keywords found into a hash table of keyword occurrences
	 * in the document. Uses the getKeyWord method to separate keywords from other words.
	 * 
	 * @param docFile Name of the document file to be scanned and loaded
	 * @return Hash table of keywords in the given document, each associated with an Occurrence object
	 * @throws FileNotFoundException If the document file is not found on disk
	 */
	public HashMap<String,Occurrence> loadKeywordsFromDocument(String docFile) 
	throws FileNotFoundException {
		/** COMPLETE THIS METHOD **/
		if (docFile == null) {
			throw new FileNotFoundException();
		}
		
		HashMap <String, Occurrence> map = new HashMap<String, Occurrence>();
		Scanner sc = new Scanner(new File(docFile));
		
		while(sc.hasNext()) {
			String word = sc.nextLine();
			StringTokenizer token = new StringTokenizer(word);
			
			while(token.hasMoreTokens()) {
				String reader = token.nextToken();
				reader = getKeyword(reader);
				
				if(reader == null) {
					continue;
				} else {
					
					
					if (map.containsKey(reader)) {
						map.get(reader).frequency++;
						
					}
					else {
						Occurrence temp = new Occurrence(docFile, 1);
						map.put(reader, temp);
					}
					
				}
				
			}
			
		}
		
		return map;
	}
	
	/**
	 * Merges the keywords for a single document into the master keywordsIndex
	 * hash table. For each keyword, its Occurrence in the current document
	 * must be inserted in the correct place (according to descending order of
	 * frequency) in the same keyword's Occurrence list in the master hash table. 
	 * This is done by calling the insertLastOccurrence method.
	 * 
	 * @param kws Keywords hash table for a document
	 */
	public void mergeKeywords(HashMap<String,Occurrence> kws) {
		/** COMPLETE THIS METHOD **/
		for(String x : kws.keySet()) {
			ArrayList<Occurrence> oc = new ArrayList<Occurrence>();

			if (keywordsIndex.containsKey(x))
				oc = keywordsIndex.get(x);
			
			oc.add(kws.get(x));
			insertLastOccurrence(oc);
			keywordsIndex.put(x, oc);
			
		}
	}
	
	/**
	 * Given a word, returns it as a keyword if it passes the keyword test,
	 * otherwise returns null. A keyword is any word that, after being stripped of any
	 * trailing punctuation(s), consists only of alphabetic letters, and is not
	 * a noise word. All words are treated in a case-INsensitive manner.
	 * 
	 * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!'
	 * NO OTHER CHARACTER SHOULD COUNT AS PUNCTUATION
	 * 
	 * If a word has multiple trailing punctuation characters, they must all be stripped
	 * So "word!!" will become "word", and "word?!?!" will also become "word"
	 * 
	 * See assignment description for examples
	 * 
	 * @param word Candidate word
	 * @return Keyword (word without trailing punctuation, LOWER CASE)
	 */
	public String getKeyword(String word) {
		/** COMPLETE THIS METHOD **/
		if(word == null) {
			return null;
		}
		
		word = word.toLowerCase();
		
		for(int i = word.length()-1; i>=0; i--) {
			if(Character.isLetter(word.charAt(i))) {
				break;
			} else {
				if(word.charAt(i) == '.' || word.charAt(i) == ',' || word.charAt(i) == '?' || word.charAt(i) == ':' || word.charAt(i) == ';' || word.charAt(i) == '!') {
					word = word.substring(0,i);
				} else {
					return null;
				}
				
				
				
			}
		}
		
		for(int i = 0; i < word.length(); i++) {
			if(Character.isLetter(word.charAt(i))) {
				
			}else {
				return null;
			}
			
		}
		
		if(noiseWords.contains(word)) {
			return null;
		}
		
		return word;
	}
	
	/**
	 * Inserts the last occurrence in the parameter list in the correct position in the
	 * list, based on ordering occurrences on descending frequencies. The elements
	 * 0..n-2 in the list are already in the correct order. Insertion is done by
	 * first finding the correct spot using binary search, then inserting at that spot.
	 * 
	 * @param occs List of Occurrences
	 * @return Sequence of mid point indexes in the input list checked by the binary search process,
	 *         null if the size of the input list is 1. This returned array list is only used to test
	 *         your code - it is not used elsewhere in the program.
	 */
	public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) {
		/** COMPLETE THIS METHOD **/
		if(occs.size() == 1) {
			return null;
		}
		
		ArrayList<Integer> arr = new ArrayList<Integer>();
		
		for(int i = 0; i<occs.size()-1; i++) {
			arr.add(occs.get(i).frequency);
		}
		
		int min = 0;
		int max = arr.size()-1;
		ArrayList<Integer> result = new ArrayList<Integer>(); 
		int target = occs.get(occs.size()-1).frequency;
		
		
		while(min <= max) {
			int mid = (min+max)/2;
			result.add(mid);
			
			if(arr.get(mid) > target) {
				min = mid + 1;
			}
			
			else if(arr.get(mid) < target) {
				max = mid - 1;
			}
			else {
				break;
			}
			
		}
		
		
		
		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code
		return result;
	}
	
	/**
	 * This method indexes all keywords found in all the input documents. When this
	 * method is done, the keywordsIndex hash table will be filled with all keywords,
	 * each of which is associated with an array list of Occurrence objects, arranged
	 * in decreasing frequencies of occurrence.
	 * 
	 * @param docsFile Name of file that has a list of all the document file names, one name per line
	 * @param noiseWordsFile Name of file that has a list of noise words, one noise word per line
	 * @throws FileNotFoundException If there is a problem locating any of the input files on disk
	 */
	public void makeIndex(String docsFile, String noiseWordsFile) 
	throws FileNotFoundException {
		// load noise words to hash table
		Scanner sc = new Scanner(new File(noiseWordsFile));
		while (sc.hasNext()) {
			String word = sc.next();
			noiseWords.add(word);
		}
		
		// index all keywords
		sc = new Scanner(new File(docsFile));
		while (sc.hasNext()) {
			String docFile = sc.next();
			HashMap<String,Occurrence> kws = loadKeywordsFromDocument(docFile);
			mergeKeywords(kws);
		}
		sc.close();
	}
	
	/**
	 * Search result for "kw1 or kw2". A document is in the result set if kw1 or kw2 occurs in that
	 * document. Result set is arranged in descending order of document frequencies. 
	 * 
	 * Note that a matching document will only appear once in the result. 
	 * 
	 * Ties in frequency values are broken in favor of the first keyword. 
	 * That is, if kw1 is in doc1 with frequency f1, and kw2 is in doc2 also with the same 
	 * frequency f1, then doc1 will take precedence over doc2 in the result. 
	 * 
	 * The result set is limited to 5 entries. If there are no matches at all, result is null.
	 * 
	 * See assignment description for examples
	 * 
	 * @param kw1 First keyword
	 * @param kw1 Second keyword
	 * @return List of documents in which either kw1 or kw2 occurs, arranged in descending order of
	 *         frequencies. The result size is limited to 5 documents. If there are no matches, 
	 *         returns null or empty array list.
	 */
	public ArrayList<String> top5search(String kw1, String kw2) {
		/** COMPLETE THIS METHOD **/
		if(kw1 == null && kw2 == null || keywordsIndex.isEmpty() ) {
			return null;
		}
		
		ArrayList<String> result = new ArrayList<String>();
		kw1 = kw1.toLowerCase();
		kw2 = kw2.toLowerCase();
		ArrayList<Occurrence> l1 = keywordsIndex.get(kw1);
		ArrayList<Occurrence> l2 = keywordsIndex.get(kw2);
		int count = 0;
		
		if(!(keywordsIndex.containsKey(kw1)) && !(keywordsIndex.containsKey(kw2)) ) {
			return null;
		}
		
		if(keywordsIndex.containsKey(kw2) && !(keywordsIndex.containsKey(kw1))) {
			while(result.size() < 5) {
				int a = 0;
				int b = 1;
				boolean change = false;
				Occurrence temp = l2.get(a);
				if(l2.size() == 1) {
					result.add(temp.document);
					return result;
				}
				while(b <= l2.size()-1) {
					
					if(l2.get(b).frequency > l2.get(a).frequency || l2.get(b).frequency == l2.get(a).frequency) {
						temp = l2.get(b);
						a = b;
						change = true;
					} else {
						
						temp = l2.get(a);
						
					}
					b++;
				}
				
				
				if(change == true) {
					l2.remove(a);
					result.add(temp.document);
				} else {
					l2.remove(a);
					result.add(temp.document);
					
				}	
			}
			return result;
		}
		
		if(keywordsIndex.containsKey(kw1) && !(keywordsIndex.containsKey(kw2))) {
			while(result.size() < 5) {
				int a = 0;
				int b = 1;
				boolean change = false;
				Occurrence temp = l1.get(a);
				if(l1.size() == 1) {
					result.add(temp.document);
					return result;
				}
				while(b <= l1.size()-1) {
					
					if(l1.get(b).frequency > l1.get(a).frequency || l1.get(b).frequency == l1.get(a).frequency) {
						temp = l1.get(b);
						a = b;
						change = true;
					} else {
						
						temp = l1.get(a);
						
					}
					b++;
				}
				
				
				if(change == true) {
					l1.remove(a);
					result.add(temp.document);
				} else {
					l1.remove(a);
					result.add(temp.document);
					
				}	
			}
			return result;
		}
		
		if(keywordsIndex.containsKey(kw1) && keywordsIndex.containsKey(kw2)) {
			ArrayList<Occurrence> temp = new ArrayList<Occurrence>();
			temp.addAll(keywordsIndex.get(kw1));
			temp.addAll(keywordsIndex.get(kw2));
			
			while(count < 5 && !temp.isEmpty()){
				int ptr = 0;
				int prev = -1;
				
				while(ptr < temp.size() && temp.get(ptr) != null){
					if (prev == -1){
						if (!result.contains(temp.get(ptr).document)) 
							prev = ptr;
					} else if (temp.get(ptr).frequency > temp.get(prev).frequency){
						if(!result.contains(temp.get(ptr).document)) 
							prev = ptr;
					} else if (temp.get(ptr).frequency == temp.get(prev).frequency){
						if(keywordsIndex.get(kw1).contains(temp.get(ptr))){
							if(!result.contains(temp.get(ptr).document)) 
								prev = ptr;
						}
					}
					ptr++;
				}
				
				if (prev != -1) {
					result.add(temp.remove(prev).document);
				}
				
				count++;
			}
			return result;
			
			
		}
		
	
	return result;
	}
}
