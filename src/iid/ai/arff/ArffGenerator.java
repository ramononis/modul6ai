package iid.ai.arff;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.TextDirectoryLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class ArffGenerator {
	public static void main(String[] args) {
		//generateArff("blogstest", "blogstrain");
		generateArff("spammail");
	}

	public static void generateArff(String directory) {
		try {
			TextDirectoryLoader loader = new TextDirectoryLoader();
			loader.setDirectory(new File(directory));
			Instances dataRaw = loader.getDataSet();
			StringToWordVector filter = new StringToWordVector();
			filter.setStopwords(new File("stopwords_en.txt"));
			filter.setOptions(Utils.splitOptions("-L"));
			filter.setInputFormat(dataRaw);
			Instances dataFiltered = Filter.useFilter(dataRaw, filter);
			writeInstances(directory + ".arff", dataFiltered);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void generateArff(String test, String train) {
		try {
			String trainIn = train + "_raw.arff";
			String testIn = test + "_raw.arff";
			String trainOut = train + ".arff";
			String testOut = test + ".arff";
			TextDirectoryLoader testLoader = new TextDirectoryLoader();
			testLoader.setDirectory(new File(test));
			Instances testDataRaw = testLoader.getDataSet();
			writeInstances(testIn, testDataRaw);

			TextDirectoryLoader trainLoader = new TextDirectoryLoader();
			trainLoader.setDirectory(new File(train));
			Instances trainDataRaw = trainLoader.getDataSet();
			writeInstances(trainIn, trainDataRaw);

			StringToWordVector filter = new StringToWordVector();
			filter.setStopwords(new File("stopwords_en.txt"));

			Filter.batchFilterFile(
					filter,
					("-i " + trainIn + " -o " + trainOut
							+ " -r " + testIn + " -s " + testOut + " -L").split(" "));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void writeInstances(String file, Instances data)
			throws FileNotFoundException {
		PrintWriter pw = new PrintWriter(new FileOutputStream(file), true);
		pw.println("" + data);
		pw.close();
	}
}
