package de.ioexception.www.server.impl;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.ioexception.www.server.HttpServer;

/**
 * A simple HTTP server implementation.
 * 
 * @author Benjamin Erb
 * 
 */
public class BasicHttpServer implements HttpServer
{
	public static final String SERVER_NAME = "MyServer";
	public static final String SERVER_VERSION = "0.1";
	public static final int DEFAULT_PORT = 8080;
	public static final String SERVER_SIGNATURE = SERVER_NAME + "/" + SERVER_VERSION;

	private volatile boolean running = false;

	private final ExecutorService workerPool;
	private final ExecutorService dispatcherService;
	private final ServerSocket serverSocket;

	
	/**
	 *  Creates a new HTTP server.
	 */
	public BasicHttpServer()
	{
		this(BasicHttpServer.DEFAULT_PORT);
	}
	
	/**
	 * Creates a new HTTP server bound to the given port.
	 * 
	 * @param port
	 *            listening port
	 * @throws IOException
	 */
	public BasicHttpServer(int port)
	{
		try
		{
			serverSocket = new ServerSocket(port);
			workerPool = Executors.newFixedThreadPool(16);
			dispatcherService = Executors.newSingleThreadExecutor();
		}
		catch (IOException e)
		{
			throw new RuntimeException("Error while starting server", e);
		}

	}

	@Override
	public void dispatchRequest(Socket socket)
	{
		workerPool.submit(new BasicHttpWorker(socket, this));
	}

	@Override
	public void start()
	{
		running = true;
		// Initiate the main server loop accepting incoming connections.
		dispatcherService.submit(new Runnable()
		{
			@Override
			public void run()
			{
				while (running)
				{
					try
					{
						Socket socket = serverSocket.accept();
						dispatchRequest(socket);
					}
					catch (SocketException e)
					{
						// ignore due to close signaling
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
			}
		});
		System.err.println("Webserver started on port " + serverSocket.getLocalPort() + "...");

	}

	@Override
	public void stop()
	{
		try
		{
			running = false;
			dispatcherService.shutdown();
			workerPool.shutdown();
			serverSocket.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			System.err.println("Webserver stopped.");
		}
	}

	@Override
	public String getServerSignature()
	{
		return BasicHttpServer.SERVER_SIGNATURE;
	}

}
