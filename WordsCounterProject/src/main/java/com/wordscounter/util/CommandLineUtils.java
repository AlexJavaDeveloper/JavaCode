package com.wordscounter.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import com.wordscounter.io.CommandLine;
import com.wordscounter.io.ConsoleCL;
import com.wordscounter.io.StreamsCL;


public class CommandLineUtils {

	public static CommandLine getDefaultCommandLineIO() {

		CommandLine io;

		if (System.console() == null) {
			io = new StreamsCL(new BufferedReader(new InputStreamReader(System.in)), new PrintWriter(System.out, true));
		} else {
			io = new ConsoleCL(System.console());
		}

		return io;

	}

}

