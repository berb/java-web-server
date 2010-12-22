package de.ioexception.www.server.log.impl;

import java.io.Flushable;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.LinkedBlockingQueue;

import de.ioexception.www.http.HttpRequest;
import de.ioexception.www.http.HttpResponse;
import de.ioexception.www.server.log.AccessLogger;

/**
 * An abstract logger class with provides basic functionalities for logging
 * access, using the "Combined Log Format".
 * 
 * @author Benjamin Erb
 */
public abstract class GenericAccessLogger implements AccessLogger, Flushable
{
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH);
	
	private volatile boolean running = true;
	private final LinkedBlockingQueue<String> logQueue = new LinkedBlockingQueue<String>();
	private final Writer writer;

	/**
	 * The writer instance to be used.
	 * 
	 * @param writer
	 */
	public GenericAccessLogger(Writer writer)
	{
		this.writer = writer;
	}

	@Override
	public void log(String logline)
	{
		try
		{
			logQueue.put(logline);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public Void call() throws Exception
	{
		try
		{
			while (running || logQueue.size() > 0)
			{
				writer.write(logQueue.take());
			}
			writer.flush();
			return null;
		}
		finally
		{
			writer.close();
		}
	}

	public void shutdown()
	{
		try
		{
			writer.flush();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		running = false;
	}
	

	@Override
	public void log(String clientHost, HttpRequest request, HttpResponse response)
	{
		StringBuilder s = new StringBuilder();
		s.append(clientHost + " ");
		s.append("- ");
		s.append("- ");
		s.append("[" + dateFormat.format(new Date()) + "] ");
		s.append("\"" + request.getHttpMethod().toString() + " " + request.getRequestUri() + " " + request.getHttpVersion().toString() + "\" ");
		s.append(response.getStatusCode().getCode() + " ");
		s.append((response.getEntity() != null ? response.getEntity().length : 0) + " ");
		s.append("\"" + (request.getHeaders().containsKey("Referer") ? request.getHeaders().get("Referer") : "") + "\" ");
		s.append("\"" + (request.getHeaders().containsKey("User-Agent") ? request.getHeaders().get("User-Agent") : "") + "\"");
		s.append("\n");
		log(s.toString());
	}
	
	@Override
	public void flush() throws IOException
	{
		writer.flush();
	}

}
