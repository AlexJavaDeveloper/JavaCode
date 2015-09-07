package com.wordscounter.io;

import java.io.Console;
import java.io.PrintWriter;
import java.io.Reader;

import com.wordscounter.util.LogUtils;
import com.wordscounter.util.Utils;


public class ConsoleCL extends CommandLine {

	private final Console console;

	public ConsoleCL(Console console) {
		this.console = console;
	}

	@Override
	public CommandLine format(String format, Object... args) {
		console.format(format, args);
		return this;
	}

	@Override
	public String readLine() {
		return console.readLine();
	}

	@Override
	public int askForIntegerValue() {

		String line = console.readLine();
		while (!Utils.isNumeric(line)) {
			LogUtils.error("Format error. Please insert a valid integer number.");
			line = console.readLine();
		}

		return Integer.parseInt(line);

	}

	@Override
	public Reader reader() {
		return console.reader();
	}

	@Override
	public PrintWriter writer() {
		return console.writer();
	}

}

