package com.wordscounter.util;

public class LogUtils {


	public static void info(String message) {
		System.out.println(message);
	}

	public static void emptyLine() {
		info("");
	}

	public static void infoStart(String task) {
		emptyLine();
		info("//////////// " + task + " START " + "////////////");
		emptyLine();
	}

	public static void infoEnd(String task, long startTime, long endTime) {
		emptyLine();
		info(task + " execution time: " + Utils.formatTime(endTime - startTime) + " seconds");
		emptyLine();
		info("//////////// " + task + "  END  ////////////");
		emptyLine();
	}

	public static void infoProgress(String message, int step, int total) {
		System.out.print(message + " " + String.valueOf(step) + "/" + String.valueOf(total) + "\r");
	}

	public static void error(String error) {
		info("[ERROR] " + error);
	}

}

