package com.wordscounter.model;

import java.util.List;
import java.util.Map.Entry;

public class ExecutionData {

	// Attributes
	private int id;
	private ExecutionTypeEnum executionType;
	private int workingFilesNumber;
	private int workingFilesSize;
	private long executionTime;
	private List<Entry<String, Frequency>> mostUsedWords;
	private List<Entry<String, Frequency>> lessUsedWords;


	// Constructors
	public ExecutionData(int id,
			ExecutionTypeEnum executionType,
			int numWorkingFiles,
			int fileSize,
			long executionTime,
			List<Entry<String, Frequency>> mostUsedWords,
			List<Entry<String, Frequency>> lessUsedWords) {

		this.id = id;
		this.executionType = executionType;
		this.workingFilesNumber = numWorkingFiles;
		this.workingFilesSize = fileSize;
		this.executionTime = executionTime;
		this.mostUsedWords = mostUsedWords;
		this.lessUsedWords = lessUsedWords;

	}


	// Getters / Setters
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public ExecutionTypeEnum getExecutionType() {
		return executionType;
	}
	public void setExecutionType(ExecutionTypeEnum executionType) {
		this.executionType = executionType;
	}

	public int getWorkingFilesNumber() {
		return workingFilesNumber;
	}
	public void setWorkingFilesNumber(int workingFilesNumber) {
		this.workingFilesNumber = workingFilesNumber;
	}

	public int getWorkingFilesSize() {
		return workingFilesSize;
	}
	public void setWorkingFilesSize(int workingFilesSize) {
		this.workingFilesSize = workingFilesSize;
	}

	public long getExecutionTime() {
		return executionTime;
	}
	public void setExecutionTime(long executionTime) {
		this.executionTime = executionTime;
	}

	public List<Entry<String, Frequency>> getMostUsedWords() {
		return mostUsedWords;
	}
	public void setMostUsedWords(List<Entry<String, Frequency>> mostUsedWords) {
		this.mostUsedWords = mostUsedWords;
	}

	public List<Entry<String, Frequency>> getLessUsedWords() {
		return lessUsedWords;
	}
	public void setLessUsedWords(List<Entry<String, Frequency>> lessUsedWords) {
		this.lessUsedWords = lessUsedWords;
	}

}

