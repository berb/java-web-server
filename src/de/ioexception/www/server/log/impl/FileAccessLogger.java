package de.ioexception.www.server.log.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * A File-based Logger using the "Combined Log Format". It directly writes each
 * log entry into file and thus leads to poor i/o efficiency under heavy server
 * load.
 * 
 * @author Benjamin Erb
 * 
 */
public class FileAccessLogger extends GenericAccessLogger
{

	public FileAccessLogger(File logFile) throws IOException
	{
		super(new FileWriter(logFile, true));
	}

}
