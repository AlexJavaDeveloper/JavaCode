package com.wordscounter.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;

import com.wordscounter.util.LogUtils;
import com.wordscounter.util.Utils;


public class StreamsCL extends CommandLine {

	private final BufferedReader reader;
	private final PrintWriter writer;

	public StreamsCL(BufferedReader reader, PrintWriter writer) {
		this.reader = reader;
		this.writer = writer;
	}

	@Override
	public CommandLine format(String format, Object... args) {
		writer.printf(format, args);
		return this;
	}

	@Override
	public String readLine() {

		String line = null;
		try {
			line = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return line;

	}

	@Override
	public int askForIntegerValue() {

		String line = null;
		try {
			line = reader.readLine();
			while (!Utils.isNumeric(line)) {
				LogUtils.error("Format error. Please insert a valid integer number.");
				line = reader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return Integer.parseInt(line);

	}

	@Override
	public Reader reader() {
		return reader;
	}

	@Override
	public PrintWriter writer() {
		return writer;
	}

}

