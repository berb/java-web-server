package de.ioexception.www.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.Callable;

import de.ioexception.www.http.HttpRequest;
import de.ioexception.www.http.HttpResponse;

/**
 * An abstract worker for processing incoming HTTP requests.
 * 
 * Note that {@link Callable} is used, because it is able to bubble up
 * {@link Exception}s, as opposed to {@link Runnable}.
 * 
 * @author Benjamin Erb
 * 
 */
public abstract class HttpWorker implements Callable<Void>
{
	protected final Socket socket;
	protected final HttpServer server;

	/**
	 * Creates a new worker that handles the incoming request.
	 * 
	 * @param socket
	 *            The socket this request is sent over.
	 * @param server
	 *            A reference to the core server instance.
	 */
	public HttpWorker(Socket socket, HttpServer server)
	{
		this.socket = socket;
		this.server = server;
	}

	@Override
	public Void call() throws Exception
	{
		// Parse request from InputStream
		HttpRequest request = parseRequest(socket.getInputStream());

		// Create appropriate response
		HttpResponse response = handleRequest(request);

		// Send response and close connection, if necessary
		if (keepAlive(request, response))
		{
			sendResponse(response, socket.getOutputStream());
			server.dispatchRequest(socket);
		}
		else
		{
			response.getHeaders().put("Connection", "close");
			sendResponse(response, socket.getOutputStream());
			socket.close();
		}
		//Log
		server.getAccessLogger().log(socket.getInetAddress().getHostAddress(), request, response);

		// We do not return anything here.
		return null;
	}

	/**
	 * A helper method that reads an InputStream until it reads a CRLF (\r\n\).
	 * Everything in front of the linefeed occured is returned as String.
	 * 
	 * @param inputStream
	 *            The stream to read from.
	 * @return The character sequence in front of the linefeed.
	 * @throws IOException
	 */
	protected String readLine(InputStream inputStream) throws IOException
	{
		StringBuffer result = new StringBuffer();
		boolean crRead = false;
		int n;
		while ((n = inputStream.read()) != -1)
		{
			if (n == '\r')
			{
				crRead = true;
				continue;
			}
			else if (n == '\n' && crRead)
			{
				return result.toString();
			}
			else
			{
				result.append((char) n);
			}
		}
		return result.toString();
	}

	/**
	 * Parses an incoming request. Reads the {@link InputStream} and creates an
	 * corresponding {@link HttpRequest} object, which will be returned.
	 * 
	 * @param inputStream
	 *            The stream to read from.
	 * @return
	 * @throws IOException
	 */
	abstract protected HttpRequest parseRequest(InputStream inputStream) throws IOException;

	/**
	 * Creates an appropriate {@link HttpResponse} to the given
	 * {@link HttpRequest}. Note however, that this method is not yet sending
	 * the response.
	 * 
	 * @param request
	 *            The {@link HttpRequest} that must be handled.
	 * @return
	 */
	abstract protected HttpResponse handleRequest(HttpRequest request);

	/**
	 * Sends a given {@link HttpResponse} over the given {@link OutputStream}.
	 * 
	 * @param response
	 * @param outputStream
	 * @throws IOException
	 */
	abstract protected void sendResponse(HttpResponse response, OutputStream outputStream) throws IOException;

	/**
	 * Determines whether a connection should be kept alive or not on
	 * server-side. This decision is made based upon the given (
	 * {@link HttpRequest}, {@link HttpResponse}) couple, respectively their
	 * header values.
	 * 
	 * @param request
	 * @param response
	 * @return true, if the server should keep open the connection, otherwise
	 *         false.
	 */
	abstract protected boolean keepAlive(HttpRequest request, HttpResponse response);

}
