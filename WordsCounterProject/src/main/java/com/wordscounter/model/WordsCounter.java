package com.wordscounter.model;

import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;

import com.wordscounter.io.CommandLine;
import com.wordscounter.util.CommandLineUtils;
import com.wordscounter.util.LogUtils;


/**
 * This is the main class of the program.
 * 
 * <p>It consists in a command line dialog with the user that lets generate a set of
 * random text files, parse them to calculate the frequency of every word present in it
 * (with single thread and multithread implementations), and review the information of
 * the executions done.
 * 
 * @author Arekku
 *
 */

public class WordsCounter {

	private static FrequencyMap wordsMap = new FrequencyMap();
	private static LinkedList<ExecutionData> executionDataList = new LinkedList<ExecutionData>();

	private static CommandLine io = CommandLineUtils.getDefaultCommandLineIO();

	private static WordsCounterService service = new WordsCounterService();


	public static void main(String[] args) {

		printWelcomeMessage();

		createInitialWorkingFiles();

		runMainMenu();

	}

	/**
	 * Prints some words about the program and how to use it.
	 */
	private static void printWelcomeMessage() {

		LogUtils.emptyLine();
		LogUtils.info("Welcome to WordsCounter!");
		LogUtils.emptyLine();
		LogUtils.info("This program calculates the frequency of every word present in a set of files.");
		LogUtils.emptyLine();
		LogUtils.info("It will generate a set of random files to work with.");
		LogUtils.emptyLine();
		LogUtils.info("The number and size of this working files is configurable at any time.");
		LogUtils.emptyLine();
		LogUtils.info("All the files generated will be removed from disc once you exit the program.");
		LogUtils.emptyLine();

	}

	/**
	 * Creates the initial set of working files.
	 */
	private static void createInitialWorkingFiles() {

		boolean workingFilesCreated = false;

		while (!workingFilesCreated) {

			try {

				LogUtils.info("Let's generate the initial working files;");
				LogUtils.emptyLine();

				createWorkingFiles();

				LogUtils.emptyLine();
				LogUtils.info("Well done! Now you're ready to work.");
				LogUtils.emptyLine();

				workingFilesCreated = true;

			} catch (Exception e) {

				LogUtils.error("Ops! An unexpected error has ocurred while trying to create the working files.");
				LogUtils.emptyLine();
				LogUtils.error("Error info:");
				LogUtils.error(e.toString());
				LogUtils.emptyLine();
				LogUtils.info("We can't continue without working files. What do you want to do?");
				LogUtils.info("Press enter to try again, or insert 'exit' to terminate the program now:");

				if (io.readLine().equals("exit")) {
					LogUtils.info("Exiting program now. Thank you for your patience!");
					System.exit(0);
				}

			}

		}

	}

	/**
	 * Prints the menu options and keeps on waiting for user requests until option 6
	 * (delete working files and exit) is requested.
	 */
	private static void runMainMenu() {

		boolean keepOn = true;

		String menuOptions = "%nInsert the number of the option you want to execute:%n"
				+ "(valid values: 1, 2, 3, 4, 5, 6)%n"
				+ "- Option 1: Create new working files%n"
				+ "- Option 2: Count words (single thread)%n"
				+ "- Option 3: Count words (multithread)%n"
				+ "- Option 4: Print historic of executions%n"
				+ "- Option 5: Clean historic of executions%n"
				+ "- Option 6: Exit%n";

		try {

			while (keepOn) {

				try {

					io.format(menuOptions);

					switch (io.readLine()) {
					case "1":
						createWorkingFiles();
						break;
					case "2":
						countWordsSingleThread();
						break;
					case "3":
						countWordsMultithread();
						break;
					case "4":
						printExecutionsHistoric();
						break;
					case "5":
						cleanExecutionsHistoric();
						break;
					case "6":
						keepOn = false;
						break;
					default:
						LogUtils.error("Wrong option number. Please insert a valid option number.");
						break;
					}

				} catch (IOException e) {

					LogUtils.error("Ops! There was an error trying to read or write a working file.");
					LogUtils.emptyLine();
					LogUtils.error("Error info:");
					LogUtils.error(e.toString());
					LogUtils.emptyLine();
					LogUtils.info("Creating working files again with option 1 could fix the problem.");

				} catch (Exception e) {

					LogUtils.error("Ops! An unexpected error has ocurred.");
					LogUtils.emptyLine();
					LogUtils.error("Error info:");
					LogUtils.error(e.toString());
					LogUtils.emptyLine();
					LogUtils.info("Please report the error to the developer. Thank you for your patience!");

				}

			}

		} finally {
			deleteWorkingFiles();
		}

	}

	/**
	 * Creates a set of working files and saves it on disc.
	 * 
	 * @throws IOException if there is a problem while trying to read or write a
	 *         working file.
	 */
	private static void createWorkingFiles() throws IOException {

		int workingFilesNumber = askWorkingFilesNumber();
		int workingFilesSize = askWorkingFilesSize();

		service.setWorkingFilesNumber(workingFilesNumber);
		service.setWorkingFilesSize(workingFilesSize);

		service.createWorkingFiles();

	}

	/**
	 * Asks the user for the number of working files.
	 * 
	 * @return the number of working files introduced by the user.
	 */
	private static int askWorkingFilesNumber() {

		int workingFilesNumber;

		LogUtils.info("Insert the number of files to generate, or 0 to apply the default value:");
		LogUtils.info("(default value: 100)");
		workingFilesNumber = io.askForIntegerValue();

		return workingFilesNumber;

	}

	/**
	 * Asks the user for the size of working files.
	 * 
	 * @return the size of working files introduced by the user.
	 */
	private static int askWorkingFilesSize() {

		int workingFilesSize;

		LogUtils.info("Insert the size (in kb) of each file, or 0 to apply the default value:");
		LogUtils.info("(default value: 256)");
		workingFilesSize = io.askForIntegerValue();

		return workingFilesSize;

	}	

	/**
	 * Counts the frequency of every word present in the set of working files and saves
	 * it in a map.
	 * 
	 * <p>Implementation note: It parses all the working files, 1 by 1, using a single
	 * thread for all of them.
	 * 
	 * @throws IOException if there is a problem while trying to read or write a
	 *         working file.
	 */
	private static void countWordsSingleThread() throws IOException {

		wordsMap = new FrequencyMap();
		service.countWordsSingleThread(wordsMap, executionDataList);
		service.printExecutionData(executionDataList.getLast());

	}

	/**
	 * Counts the frequency of every word present in the set of working files and saves
	 * it in a map.
	 * 
	 * <p>Implementation note: It parses all the working files, 1 by 1, using a new
	 * thread for every file.
	 * 
	 * @throws IOException if there is a problem while trying to read or write a
	 *         working file.
	 * @throws InterruptedException if one of the threads created by this method is
	 *         interrupted.
	 * @throws ExecutionException if one of the threads created by this method has an
	 *         execution error.
	 */
	private static void countWordsMultithread() throws IOException, InterruptedException, ExecutionException {

		wordsMap = new FrequencyMap();
		service.countWordsMultithread(wordsMap, executionDataList);
		service.printExecutionData(executionDataList.getLast());

	}

	/**
	 * Prints the execution information stored in the list of executions.
	 */
	private static void printExecutionsHistoric() {

		if (executionDataList.size() > 0) {
			service.printExecutionDataList(executionDataList);
		} else {
			LogUtils.info("There is no data to print.");
		}

	}

	/**
	 * Cleans the execution information stored in the list of executions.
	 */
	private static void cleanExecutionsHistoric() {

		if (executionDataList.size() > 0) {
			executionDataList = new LinkedList<ExecutionData>();
			LogUtils.info("Historic of executions cleaned successfully.");
		} else {
			LogUtils.info("There is no data to clean.");
		}

	}

	/**
	 * Deletes the set of working files from disc.
	 */
	private static void deleteWorkingFiles() {

		service.deleteWorkingFiles();
		LogUtils.info("Thank you. See you soon!");

	}

}

