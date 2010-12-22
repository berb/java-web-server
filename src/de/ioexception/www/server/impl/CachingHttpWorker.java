package de.ioexception.www.server.impl;

import java.net.Socket;

import de.ioexception.www.http.HttpRequest;
import de.ioexception.www.http.HttpResponse;

public class CachingHttpWorker extends BasicHttpWorker
{

	public CachingHttpWorker(Socket socket, BasicHttpServer server)
	{
		super(socket, server);
	}
	
	@Override
	protected HttpResponse handleRequest(HttpRequest request)
	{
		// TODO: add caching support
		return super.handleRequest(request);
	}

}
