package com.wordscounter.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentMap;

import com.wordscounter.model.Frequency;


public class Utils {

	public static String formatTime(long time) {

		NumberFormat formatter = new DecimalFormat("#0.000");

		return formatter.format(time / 1000d);

	}

	public static List<Entry<String, Frequency>> getFirstElements(List<Entry<String, Frequency>> list, int topWordsNumber) {

		List<Entry<String, Frequency>> topList = new LinkedList<Entry<String, Frequency>>();

		ListIterator<Entry<String, Frequency>> it = list.listIterator();
		int i = 0;

		while (it.hasNext() && i < topWordsNumber) {

			topList.add(it.next());
			i++;
		}

		return topList;

	}

	public static List<Entry<String, Frequency>> getLastElements(List<Entry<String, Frequency>> list, int topWordsNumber) {

		List<Entry<String, Frequency>> topList = new LinkedList<Entry<String, Frequency>>();

		for (int i = 1; i <= topWordsNumber; i++) {

			topList.add(list.get(list.size()-i));

		}

		return topList;

	}

	public static void countWords(String fileText, ConcurrentMap<String, Frequency> wordsMap) {

		String[] fileWords = fileText.split("\\s+");

		for (String word : fileWords) {

			Frequency freq = wordsMap.get(word);
			if (freq == null) {
				wordsMap.put(word, new Frequency());
			} else {
				freq.incrementFrequency();
			}

		}

	}

	public static boolean isNumeric(String str) {

		if (str == null) {
			return false;
		} else {
			return str.matches("\\d+");
		}

	}

}

