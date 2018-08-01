package PreProcessData;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

/*
* Class			: 	OpenCSV
* Author		: 	AlbanDev
* Version		:	2.0
* Date			: 	17/04/2018
* Main features	:
* 					vers2.0-used opencsv lib and removed vars and functions causing memory leak
*/

public class openCSV {

	List<String[]> docLine;
	public PreProcess pp;

	// constructor class
	public openCSV() throws IOException, InterruptedException {

		// initialize the store var
		docLine = new ArrayList<String[]>();

		// initialize the preprocess class
		pp = new PreProcess();

		// check if the preprocessed file exists
		if (Files.exists(Paths.get("utils/preProcessedData.csv"))) {

			System.out.println("Pre-Processed File Found.");
			readDoc();

		} else {

			System.out.println("Pre-Processed File Not Found.");
			// create the pre processed doc file
			preProcessDoc();
			writePreProcessedToCSV();

		}
		//the syn file is used to improve the software prediction based on the search made
		if(!Files.exists(Paths.get("utils/syn.csv"))) {
			createSynFile();
		}

	}

	private void createSynFile() throws IOException {
		// TODO Auto-generated method stub
		
		CSVWriter writer = new CSVWriter(new FileWriter("utils/syn.csv"), CSVWriter.DEFAULT_SEPARATOR,
				CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.NO_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);

		String[] s = {""};
		
		for(int i=0; i<docLine.size();i++)
			writer.writeNext(s);

		// close the write
		writer.flush();
		writer.close();

	}

	// preprocess the data
	void preProcessDoc() throws IOException, InterruptedException {

		// open the pre processed doc file
		BufferedReader reader = new BufferedReader(new FileReader("utils/FILTER_FOR_CON_D_ORIG_FOOD1.csv"));

		/*
		 * STANDARDIZED CHARACTERS SEPARATOR the csvSplitBy String is used to smartly
		 * separate the data inside a csv file by columns. Nb the csv file has not
		 * columns separator but instead use specific characters to denote them.
		 */
		String csvSplitBy = ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)";

		int count = 0;
		String line = "";
		String[] items;
		String[] ppItems;

		System.out.println("Start to preprocess the data ...");

		// jump the header
		reader.readLine();

		// label loop
		loopEachRecord: while ((line = reader.readLine()) != null) {

			items = line.split(csvSplitBy);
			ppItems = new String[2];

			// nextLine[] is an array of values from the line

			/*
			 * ERROR STD CHARACTER DETECTOR: The value indicates where the user should write
			 * again the description or the name of the related food because of some
			 * incoherences. This would help to have a table all with standardised
			 * occurrences that are more easy to read and analysed with optimized
			 * algorithms.
			 */

			for (int i = 0; i < items.length; i++) {
				try {
					if (items[i] == null) {
						System.out.println("Error at line: " + count);
						break loopEachRecord;
					}
				} catch (Exception e) {
					System.out.println("Error at line: " + count);
				}
			}

			// preprocess the brand and append the streamlined text
			ppItems[0] = pp.textCleaner(items[1]);

			// preprocess the description and append the streamlined text
			ppItems[1] = pp.textCleaner(items[2]);

			// shuld be use for frequency word needs
			// append frequency word for description
			// lineItem[4] = PreProcess.termDocMatrix(lineItem[3]);
			// remove duplicates words
			// lineItem[3] = PreProcess.duplicatesRemover(lineItem[3]);

			// System.out.println("Line "+count +" - "+ lineItem.length);
			docLine.add(ppItems);

			count = count + 1;

		}

		System.out.println("PreProcess Done.\n");
		System.out.println("Total rows analyzied: " + count + "\nThe file has been analyzed correctly.");

		reader.close();

	}

	// read the data
	void readDoc() throws IOException, InterruptedException {

		System.out.println("Start to read the data ...");
		// open the pre processed doc file
		CSVReader reader = new CSVReader(new FileReader("utils/preProcessedData.csv"));

		docLine = reader.readAll();
		System.out.println("Analyzed " + docLine.size() + " of rows.");
		
		reader.close();
	}

	// write the preprocessed data to a new file
	void writePreProcessedToCSV() throws IOException {

		CSVWriter writer = new CSVWriter(new FileWriter("utils/preProcessedData.csv"), CSVWriter.DEFAULT_SEPARATOR,
				CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.NO_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);

		// Write the record to file
		writer.writeAll(docLine);

		// close the write
		writer.flush();
		writer.close();

	}

	public List<String[]> getDocLine() {
		return docLine;
	}
}
