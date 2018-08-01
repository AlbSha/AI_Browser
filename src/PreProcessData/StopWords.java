package PreProcessData;
/*
 	* Class			: 	StopWords
 	* Author		: 	AlbanDev
 	* Version		:	1.4
 	* Date			: 	17/04/2018
 	* Main features	:
 	* 					vers1.0 -> numbers and ENGLISH stop-words
 	* 					vers1.1 -> +Punctuation
 	* 					vers1.2 -> +signs
 	* 					vers1.3 -> +customized EFSA stop words
 	* 					vers1.4 -> +clearer code and removed unused vars and methods
 	* 
*/

import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;

import com.opencsv.CSVReader;

public class StopWords {

	private Hashtable<String, Boolean> h;
	private CSVReader enDictReader;

	public boolean readDict() {
		try {
			// find the eng dict.
			enDictReader = new CSVReader(new FileReader("utils/stop_words_en.csv"));
		} catch (IOException e) {
			System.out.println("Dictionary CSV file not found!");
			return false;
		}
		return true;
	}

	public boolean isStopWord(String s) {
		boolean ret = h.get(s) == null ? false : true;

		if (s.length() == 1)
			ret = true;

		return ret;
	}

	public StopWords() throws IOException {

		String[] nextRecord = null;

		if (readDict()) {

			h = new Hashtable<String, Boolean>();

			// Reading values in an array of string
			while ((nextRecord = enDictReader.readNext()) != null) {
				// System.out.println(nextRecord[0]);
				h.put(nextRecord[0], true);
			}
		}
	}
}