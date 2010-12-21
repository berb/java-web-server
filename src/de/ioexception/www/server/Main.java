package de.ioexception.www.server;

import de.ioexception.www.server.impl.BasicHttpServer;

public class Main
{
	public static void main(String[] args)
	{
		HttpServer server = new BasicHttpServer(8080);
		server.start();
	}
}
