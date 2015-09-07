package com.wordscounter.model;

public class Frequency implements Comparable<Frequency> {

	// Attributes
	private int count;


	// Constructors
	public Frequency() {

		this.count = 1;

	}


	// Getters / Setters
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}


	// Methods
	public void incrementFrequency() {
		this.count++;
	}


	// Override Methods
	@Override
	public String toString() {

		return String.valueOf(getCount());

	}

	@Override
	public int compareTo(Frequency freq) {

		return Integer.compare(this.getCount(), freq.getCount());

	}

}

