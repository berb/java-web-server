package de.ioexception.www.server.log.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * A File-based Logger using the "Combined Log Format". It buffers log entries
 * and writes them into the logfile when a certain amount of chunks is
 * available. This decreases the overhead of i/o operations, but will cause loss
 * of entries in case of ungrateful server shutdowns.
 * 
 * @author Benjamin Erb
 * 
 */
public class BufferedFileAccessLogger extends GenericAccessLogger
{

	public BufferedFileAccessLogger(File logFile) throws IOException
	{
		super(new BufferedWriter(new FileWriter(logFile, true)));
	}

}
