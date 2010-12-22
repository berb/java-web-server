package de.ioexception.www.server.log;

import java.util.concurrent.Callable;

import de.ioexception.www.http.HttpRequest;
import de.ioexception.www.http.HttpResponse;

public interface AccessLogger extends Callable<Void>
{
	public void log(String clientHost, HttpRequest request, HttpResponse response);
	public void log(String s);
}
