package com.wordscounter.model;


import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentMap;

import com.wordscounter.util.LogUtils;
import com.wordscounter.util.Utils;


public class TextParser implements Callable<String> {

	private String fileText;
	private int fileNumber;
	private int workingFilesNumber;
	private ConcurrentMap<String, Frequency> wordsMap;

	public TextParser(String fileText, int fileNumber, int workingFilesNumber, ConcurrentMap<String, Frequency> wordsMap) {

		this.fileText = fileText;
		this.fileNumber = fileNumber;
		this.workingFilesNumber = workingFilesNumber;
		this.wordsMap = wordsMap;

	}

	public String call() {

		LogUtils.infoProgress("(" + Thread.currentThread().getName() + ") Processing file", fileNumber, workingFilesNumber);

		Utils.countWords(fileText, wordsMap);

		return "ok";

	}

}

