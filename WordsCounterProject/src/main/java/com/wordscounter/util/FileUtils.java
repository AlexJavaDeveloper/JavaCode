package com.wordscounter.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Scanner;


public class FileUtils {

	public static String read(String path, String encoding) throws IOException {

		StringBuilder text = new StringBuilder();
		String lineSeparator = System.getProperty("line.separator");
		Scanner scanner = new Scanner(new FileInputStream(path), encoding);

		try {

			while (scanner.hasNextLine()){
				text.append(scanner.nextLine() + lineSeparator);
			}

		} finally {
			scanner.close();
		}

		return text.toString();

	}

	public static void write(String path, String encoding, String text) throws IOException {

		Writer out = new OutputStreamWriter(new FileOutputStream(path), encoding);

		try {
			out.write(text);
		} finally {
			out.close();
		}

	}

}

