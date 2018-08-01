package PreProcessData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.Map.Entry;

/*
* Class			: 	PreProcess
* Author		: 	AlbanDev
* Version		:	2.0
* Date			: 	17/04/2018
* Main features	:
* 					vers2.0-grouped methods in textCleaner method with improvements on var and class initialization, less memory leak.
*/

public class PreProcess {

	StopWords sw;
	Stemmer stemmer;

	public PreProcess() throws IOException {
		sw = new StopWords();
		stemmer = new Stemmer();
	}

	public String textCleaner(String text) {

		List<String> tokens = new ArrayList<String>();
		String res = "";

		////////////////////////////////////// TEXT
		////////////////////////////////////// CLEAR///////////////////////////////////
		// 1-trasform in lower case, 2-remove irrelevant punctation (not numbers!)
		text = text.replaceAll("\\p{Punct}", " ").toLowerCase();

		// 2-trim all group of spaces generated with a single one
		text = text.trim().replaceAll("\\s{2,}", " ");

		////////////////////////////////////// STOP-WORDS///////////////////////////////////
		// tokenize
		tokens.addAll(Arrays.asList(text.split("\\s+")));

		////////////////////////////////////// STOP-WORDS &&
		////////////////////////////////////// STEMMER////////////////////////
		for (ListIterator<String> it = tokens.listIterator(); it.hasNext();) {
			String word = it.next();
			if (sw.isStopWord(word)) {
				it.remove();
				// System.out.println("removed " + word);
			} else {
				it.set(stemmer.stem(word));
			}
		}

		////////////////////////////////////// REMOVE
		////////////////////////////////////// DUPLICATES////////////////////////////
		List<String> tokensNoDup = tokens.stream().distinct().collect(Collectors.toList());// without duplicates

		// save the result in a temp string so to clear the tokens list
		res = String.join(" ", tokensNoDup);

		////////////////////////////////////// CLEAR
		////////////////////////////////////// VAR///////////////////////////////////
		tokens.clear();
		tokensNoDup.clear();

		return res;

	}

	public static String duplicatesRemover(String text) {

		Set<String> tokens = new LinkedHashSet<String>();

		// Remove duplicates
		tokens.addAll(Arrays.asList(text.split("\\s+")));

		return String.join(" ", tokens);
	}

	// the function build a term-doc matrix in which for every line is counted the
	// most frequency word
	public static String termDocMatrix(String text) {

		String[] tokens = text.split("\\s+");
		// it contains the weight and the best similar string
		TreeMap<String, Integer> map = new TreeMap<>();

		for (String w : tokens) {
			Integer count = map.get(w);
			if (count == null)
				count = 1;
			else
				count = count + 1;
			map.put(w, count);
		}

		// System.out.println(map);
		String res = "";

		for (Entry<String, Integer> entry : map.descendingMap().entrySet()) {
			if (entry.getValue() > 1)
				res = res + " " + entry.getKey();
		}

		if (!res.equals(""))
			return res;

		return "undef";

		/*
		 * based on high freq. int maxFreq =
		 * map.values().stream().max(Integer::compare).get();
		 * //System.out.println(maxFreq);
		 * 
		 * String res="";
		 * 
		 * if (maxFreq>1) { //so there is at least a word which appear more then once
		 * for (Entry<String, Integer> entry : map.descendingMap().entrySet()) { if
		 * (entry.getValue()==maxFreq) res=res+" "+entry.getKey(); } return res; } else
		 * { return "undefined"; }
		 */
	}

	/*
	 * DEBUGGING public static void main (String[] args) throws IOException {
	 * 
	 * PreProcess pp = new
	 * PreProcess("This is my new  , made with . flake sugar and natural-part  grain 75.5."
	 * );
	 * 
	 * System.out.println(pp.returnPreProcessedText() + " && " +
	 * pp.returnStreamlinedText()); }
	 */
}
