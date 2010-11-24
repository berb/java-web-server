package de.ioexception.www.server;

import java.net.Socket;

import de.ioexception.www.server.router.RequestRouter;

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
	
	public RequestRouter getRequestRouter();
	
	public void setRequestRouter(RequestRouter requestRouter);

}
