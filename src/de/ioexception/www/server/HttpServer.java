package de.ioexception.www.server;

import java.net.Socket;

import de.ioexception.www.server.log.AccessLogger;

/**
 * A basic HTTP server interface.
 * 
 * @author Benjamin Erb
 *
 */
public interface HttpServer
{
	/**
	 * Starts the configured webserver.
	 */
	public void start();

	/**
	 * Shuts down the webserver.
	 */
	public void stop();

	/**
	 * Dispatches an incoming socket connection to an appropriate handler.
	 * 
	 * @param socket
	 */
	public void dispatchRequest(Socket socket);

	/**
	 * Returns the signature of the webserver.
	 * 
	 * @return
	 */
	public String getServerSignature();
	
	/**
	 * Returns the signature of the webserver.
	 * 
	 * @return
	 */
	public AccessLogger getAccessLogger();

}
