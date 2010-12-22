package de.ioexception.www.server.log.impl;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * A logger for console output. Flushes every entry immediately.
 * 
 * @author Benjamin Erb
 * 
 */
public class ConsoleAccessLogger extends GenericAccessLogger
{
	public ConsoleAccessLogger()
	{
		super(new PrintWriter(System.out));
	}

	@Override
	public void log(String logline)
	{
		super.log(logline);
		try
		{
			super.flush();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
