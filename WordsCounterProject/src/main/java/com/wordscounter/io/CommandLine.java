package com.wordscounter.io;

import java.io.PrintWriter;
import java.io.Reader;

public abstract class CommandLine {

	public abstract CommandLine format(String format, Object... args);

	public abstract String readLine();
	
	public abstract int askForIntegerValue();

	public abstract Reader reader();

	public abstract PrintWriter writer();

}

