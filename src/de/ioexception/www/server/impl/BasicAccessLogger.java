package de.ioexception.www.server.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.LinkedBlockingQueue;

import de.ioexception.www.http.HttpRequest;
import de.ioexception.www.http.HttpResponse;
import de.ioexception.www.server.AccessLogger;

public class BasicAccessLogger implements AccessLogger
{
	private final BufferedWriter log;
	private volatile boolean running = true;
	private final LinkedBlockingQueue<String> logQueue = new LinkedBlockingQueue<String>();
	
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH);
	
	/*
	 * [10/Oct/2000:13:55:36 -0700] (%t)
    The time that the request was received. The format is:

    [day/month/year:hour:minute:second zone]
    day = 2*digit
    month = 3*letter
    year = 4*digit
    hour = 2*digit
    minute = 2*digit
    second = 2*digit
    zone = (`+' | `-') 4*digit 
	 */
	
	public BasicAccessLogger(File logFile) throws IOException
	{
		log = new BufferedWriter(new FileWriter(logFile, true));
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
			while(running || logQueue.size() > 0)
			{
				log.write(logQueue.take());
				log.flush();
			}
			log.flush();
			return null;
		}
		finally
		{
			System.err.println("closing..");
			log.close();
		}
	}
	
	public void shutdown()
	{
		running = false;
	}


	@Override
	public void log(String clientHost, HttpRequest request, HttpResponse response)
	{
		StringBuilder s = new StringBuilder();
		s.append(clientHost+" ");
		s.append("- ");
		s.append("- ");
		s.append("["+dateFormat.format(new Date())+"] ");
		s.append("\""+request.getHttpMethod().toString()+" "+request.getRequestUri()+" "+request.getHttpVersion().toString()+"\" ");
		s.append(response.getStatusCode().getCode()+" ");
		s.append((response.getEntity() != null ? response.getEntity().length : 0)+" ");
		s.append("\""+(request.getHeaders().containsKey("Referer") ? request.getHeaders().get("Referer") : "")+"\" " );
		s.append("\""+(request.getHeaders().containsKey("User-Agent") ? request.getHeaders().get("User-Agent") : "")+"\"");
		s.append("\r\n");
		log(s.toString());
	}

}
