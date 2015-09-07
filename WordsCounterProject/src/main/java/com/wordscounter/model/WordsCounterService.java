package com.wordscounter.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.wordscounter.util.FileUtils;
import com.wordscounter.util.LogUtils;
import com.wordscounter.util.Utils;


/**
 * This class contains all the operations used by the main class <i>WordsCounter</i>
 * 
 * @author Arekku
 *
 */
public class WordsCounterService {

	// Constants
	private static final String FOLDER_PATH = "text/";
	private static final String FILE_NAME = "TextFile_";
	private static final String FILE_EXTENSION = ".txt";
	private static final String FILE_TMP_SUFFIX = "_tmp";
	private static final String FILE_ENCODING = "UTF-8";

	private static final int BASE_FILES_NUMBER = 25;
	private static final int BASE_FILES_SIZE = 4;
	private static final int TOP_WORDS_NUMBER = 5;

	private static final int DEFAULT_WORKING_FILES_NUMBER = 100;
	private static final int DEFAULT_WORKING_FILES_SIZE = 256;


	// Attributes
	private int workingFilesNumber;
	private int workingFilesSize;


	// Constructors
	/**
	 * Creates a WordsCounterService with default values.
	 */
	public WordsCounterService() {
		workingFilesNumber = DEFAULT_WORKING_FILES_NUMBER;
		workingFilesSize = DEFAULT_WORKING_FILES_SIZE;
	}

	/**
	 * Creates a WordsCounterService with the given values.
	 * 
	 * @param workingFilesNumber the number of workings files to set.
	 * @param workingFilesSize the size of the working files to set.
	 */
	public WordsCounterService(int workingFilesNumber, int workingFilesSize) {
		setWorkingFilesNumber(workingFilesNumber);
		setWorkingFilesSize(workingFilesSize);
	}


	// Getters / Setters
	/**
	 * Getter
	 * 
	 * @return the number of working files.
	 */
	public int getWorkingFilesNumber() {
		return workingFilesNumber;
	}
	/**
	 * Setter
	 * 
	 * @param workingFilesNumber the number of working files.
	 * If workingFilesNumber is 0, it sets the default value.
	 */
	public void setWorkingFilesNumber(int workingFilesNumber) {

		if (workingFilesNumber != 0) {
			this.workingFilesNumber = workingFilesNumber;			
		} else {
			this.workingFilesNumber = DEFAULT_WORKING_FILES_NUMBER;
		}

		LogUtils.info("Number of working files set to: " + getWorkingFilesNumber());

	}

	/**
	 * Getter
	 * 
	 * @return the size of working files.
	 */
	public int getWorkingFilesSize() {
		return workingFilesSize;
	}
	/**
	 * Setter
	 * 
	 * @param workingFilesSize the size of working files.
	 * If workingFilesSize is 0, it sets the default value.
	 */
	public void setWorkingFilesSize(int workingFilesSize) {

		if (workingFilesSize != 0) {
			this.workingFilesSize = workingFilesSize;	
		} else {
			this.workingFilesSize = DEFAULT_WORKING_FILES_SIZE;
		}

		LogUtils.info("Size of working files set to: " + getWorkingFilesSize());

	}


	// Public Methods
	/**
	 * Creates a set of working files and saves it on disc.
	 * 
	 * <p>Every working file is created with a combination of a number of random base files,
	 * depending on the file size defined.
	 * 
	 * @throws IOException if there is a problem while trying to read or write a
	 *         working file.
	 */
	public void createWorkingFiles() throws IOException {

		long startTime = System.currentTimeMillis();
		LogUtils.infoStart("createWorkingFiles");

		int baseFilesPerWorkingFile = workingFilesSize / BASE_FILES_SIZE;

		String[] baseFiles = new String[BASE_FILES_NUMBER];
		Random randomizer = new Random();

		for (int i = 1; i <= BASE_FILES_NUMBER; i++) {
			baseFiles[i-1] = FileUtils.read(FOLDER_PATH + FILE_NAME + i + FILE_EXTENSION, FILE_ENCODING);
		}

		for (int i = 1; i <= workingFilesNumber; i++) {

			LogUtils.infoProgress("Preparing working file", i, workingFilesNumber);

			String fileText = "";

			for (int j = 0; j < baseFilesPerWorkingFile; j++) {
				fileText += baseFiles[randomizer.nextInt(BASE_FILES_NUMBER)];
			}

			FileUtils.write(FOLDER_PATH + FILE_NAME + i + FILE_TMP_SUFFIX + FILE_EXTENSION, FILE_ENCODING, fileText);

		}

		LogUtils.info("Working files created successfully.");

		long endTime = System.currentTimeMillis();
		LogUtils.infoEnd("createWorkingFiles", startTime, endTime);

	}

	/**
	 * Counts the frequency of every word present in the set of working files and saves
	 * it in a map.
	 * 
	 * <p>It parses all the working files, 1 by 1, using a single thread for all of them.
	 * 
	 * @param wordsMap the map that will store the frequency of every word.
	 * @param executionDataList the list of executions. 
	 * @throws IOException if there is a problem while trying to read or write a
	 *         working file.
	 */
	public void countWordsSingleThread(FrequencyMap wordsMap, List<ExecutionData> executionDataList) throws IOException {

		long startTime = System.currentTimeMillis();
		LogUtils.infoStart("countWordsSingleThread");

		for (int i = 1; i <= workingFilesNumber; i++) {

			LogUtils.infoProgress("Processing file", i, workingFilesNumber);

			String fileText = FileUtils.read(FOLDER_PATH + FILE_NAME + i + FILE_TMP_SUFFIX + FILE_EXTENSION, FILE_ENCODING);
			Utils.countWords(fileText, wordsMap.getMap());

		}

		long endTime = System.currentTimeMillis();
		LogUtils.infoEnd("countWordsSingleThread", startTime, endTime);

		int executionID = executionDataList.size() + 1;
		ExecutionTypeEnum executionType = ExecutionTypeEnum.SINGLETHREAD;
		long executionTime = endTime - startTime;
		List<Entry<String, Frequency>> mostUsedWords = Utils.getFirstElements(wordsMap.getSortedList(OrderEnum.DESC), TOP_WORDS_NUMBER);
		List<Entry<String, Frequency>> lessUsedWords = Utils.getLastElements(wordsMap.getSortedList(OrderEnum.DESC), TOP_WORDS_NUMBER);

		executionDataList.add(new ExecutionData(executionID, executionType, workingFilesNumber, workingFilesSize, executionTime, mostUsedWords, lessUsedWords));

	}

	/**
	 * Counts the frequency of every word present in the set of working files and saves
	 * it in a map.
	 * 
	 * <p>It parses all the working files, 1 by 1, using a new thread for every file.
	 * 
	 * @param wordsMap the map that will store the frequency of every word.
	 * @param executionDataList the list of executions. 
	 * @throws IOException if there is a problem while trying to read or write a
	 *         working file.
	 * @throws InterruptedException if one of the threads created by this method is
	 *         interrupted.
	 * @throws ExecutionException if one of the threads created by this method has an
	 *         execution error.
	 */
	public void countWordsMultithread(FrequencyMap wordsMap, List<ExecutionData> executionDataList) throws IOException, InterruptedException, ExecutionException {

		ExecutorService exec = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

		try {

			long startTime = System.currentTimeMillis();
			LogUtils.infoStart("countWordsMultithread");

			LogUtils.info("Available Processors: " + Runtime.getRuntime().availableProcessors());
			List<Future<String>> futures = new ArrayList<Future<String>>();

			for (int i = 1; i <= workingFilesNumber; i++) {
				String fileText = FileUtils.read(FOLDER_PATH + FILE_NAME + i + FILE_TMP_SUFFIX + FILE_EXTENSION, FILE_ENCODING);
				futures.add(exec.submit(new TextParser(fileText, i, workingFilesNumber, wordsMap.getMap())));
			}

			for (Future<String> future : futures) {
				future.get();
			}

			long endTime = System.currentTimeMillis();
			LogUtils.infoEnd("countWordsMultithread", startTime, endTime);

			int executionID = executionDataList.size() + 1;
			ExecutionTypeEnum executionType = ExecutionTypeEnum.MULTITHREAD;
			long executionTime = endTime - startTime;
			List<Entry<String, Frequency>> mostUsedWords = Utils.getFirstElements(wordsMap.getSortedList(OrderEnum.DESC), TOP_WORDS_NUMBER);
			List<Entry<String, Frequency>> lessUsedWords = Utils.getLastElements(wordsMap.getSortedList(OrderEnum.DESC), TOP_WORDS_NUMBER);

			executionDataList.add(new ExecutionData(executionID, executionType, workingFilesNumber, workingFilesSize, executionTime, mostUsedWords, lessUsedWords));

		} finally {
			exec.shutdown();
		}

	}

	/**
	 * Prints the content of a given ExecutionData object.
	 * @param execData the ExecutionData object to print.
	 */
	public void printExecutionData(ExecutionData execData) {

		LogUtils.info("### EXECUTION INFO ############################################################");
		printExecutionDataInfo(execData);
		LogUtils.info("###############################################################################");
		LogUtils.emptyLine();

	}

	/**
	 * Prints the content of all the ExecutionData objects of a given list.
	 * @param executionDataList the list of ExecutionData objects to print.
	 */
	public void printExecutionDataList(LinkedList<ExecutionData> executionDataList) {

		LogUtils.info("### HISTORIC OF EXECUTIONS ####################################################");

		long singleThreadAverageTime = 0;
		long multithreadAverageTime = 0;

		int singleThreadExecutions = 0;
		int multithreadExecutions = 0;

		long singleThreadExecutionTime = 0;
		long multithreadExecutionTime = 0;

		ListIterator<ExecutionData> it = executionDataList.listIterator();
		ExecutionData execData;

		while (it.hasNext()) {

			execData = it.next();

			if (execData.getExecutionType().equals(ExecutionTypeEnum.SINGLETHREAD)) {
				singleThreadExecutions++;
				singleThreadExecutionTime += execData.getExecutionTime();
			} else {
				multithreadExecutions++;
				multithreadExecutionTime += execData.getExecutionTime();
			}

			printExecutionDataInfo(execData);
			LogUtils.info("###############################################################################");
		}

		LogUtils.emptyLine();
		LogUtils.info("### AVERAGE EXECUTION TIME ####################################################");

		if (singleThreadExecutions > 0) {
			singleThreadAverageTime = singleThreadExecutionTime / singleThreadExecutions;
		}

		if (multithreadExecutions > 0) {
			multithreadAverageTime = multithreadExecutionTime / multithreadExecutions;
		}

		LogUtils.info("  " + ExecutionTypeEnum.SINGLETHREAD + " average execution time: " + Utils.formatTime(singleThreadAverageTime) + " seconds");
		LogUtils.info("  " + ExecutionTypeEnum.MULTITHREAD + " average execution time:  " + Utils.formatTime(multithreadAverageTime) + " seconds");

		LogUtils.info("###############################################################################");
		LogUtils.emptyLine();

	}

	/**
	 * Deletes the working files from the disc.
	 * It checks the suffix of the file names to identify the working files.
	 */
	public void deleteWorkingFiles() {

		long startTime = System.currentTimeMillis();
		LogUtils.infoStart("deleteWorkingFiles");

		File folder = new File(FOLDER_PATH);

		File[] files = folder.listFiles();
		if (files != null) {
			for (File f : files) {
				if (f.getName().endsWith(FILE_TMP_SUFFIX + FILE_EXTENSION)) {
					f.delete();
				}
			}
		}

		LogUtils.info("Working files deleted successfully.");

		long endTime = System.currentTimeMillis();
		LogUtils.infoEnd("deleteWorkingFiles", startTime, endTime);

	}


	// Private Methods
	/**
	 * Prints the attribute values of a given ExecutionData object.
	 * @param execData the ExecutionData object to print.
	 */
	private void printExecutionDataInfo(ExecutionData execData) {

		LogUtils.info("  Execution ID:     " + execData.getId());
		LogUtils.info("  Execution type:   " + execData.getExecutionType().name());
		LogUtils.info("  Number of files:  " + execData.getWorkingFilesNumber());
		LogUtils.info("  Size of files:    " + execData.getWorkingFilesSize() + "kb");
		LogUtils.info("  Execution time:   " + Utils.formatTime(execData.getExecutionTime()) + " seconds");
		LogUtils.info("  Most used words:  " + execData.getMostUsedWords().toString());
		LogUtils.info("  Less used words:  " + execData.getLessUsedWords().toString());

	}

}

