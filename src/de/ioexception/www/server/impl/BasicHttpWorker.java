package de.ioexception.www.server.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import de.ioexception.www.Http;
import de.ioexception.www.http.HttpMethod;
import de.ioexception.www.http.HttpRequest;
import de.ioexception.www.http.HttpResponse;
import de.ioexception.www.http.HttpStatusCode;
import de.ioexception.www.http.HttpVersion;
import de.ioexception.www.http.impl.BasicHttpRequest;
import de.ioexception.www.http.impl.BasicHttpResponse;
import de.ioexception.www.server.HttpWorker;

/**
 * @author Benjamin Erb
 *
 */
public class BasicHttpWorker extends HttpWorker
{
	public BasicHttpWorker(Socket socket, BasicHttpServer server)
	{
		super(socket, server);
	}

	@Override
	protected HttpRequest parseRequest(InputStream inputStream) throws IOException
	{
		String firstLine = readLine(inputStream);

		BasicHttpRequest request = new BasicHttpRequest();

		request.setVersion(HttpVersion.extractVersion(firstLine));
		request.setRequestUri(firstLine.split(" ", 3)[1]);
		request.setMethod(HttpMethod.extractMethod(firstLine));

		Map<String, String> headers = new HashMap<String, String>();

		String nextLine = "";
		while (!(nextLine = readLine(inputStream)).equals(""))
		{
			String values[] = nextLine.split(":", 2);
			headers.put(values[0], values[1].trim());
		}
		request.setHeaders(headers);

		if (headers.containsKey(Http.CONTENT_LENGTH))
		{
			int size = Integer.parseInt(headers.get(Http.CONTENT_LENGTH));
			byte[] data = new byte[size];
			int n;
			for (int i = 0; i < size && (n = inputStream.read()) != -1; i++)
			{
				data[i] = (byte) n;
			}
			request.setEntity(data);
		}
		else
		{
			request.setEntity(null);
		}

		return request;
	}

	@Override
	protected HttpResponse handleRequest(HttpRequest request)
	{

		
		BasicHttpResponse response = new BasicHttpResponse();
		response.setHeaders(new HashMap<String, String>());
		response.getHeaders().put(Http.SERVER, server.getServerSignature());
		response.setVersion(request.getHttpVersion());

		String requestUri = request.getRequestUri();
		if (requestUri.equals("/"))
		{
			requestUri = "/index.html";
		}
		File f = new File("webroot/" + requestUri);

		File rootDir = new File("webroot/");
		try
		{
			if (!f.getCanonicalPath().startsWith(rootDir.getCanonicalPath()))
			{
				response.setStatusCode(HttpStatusCode.FORBIDDEN);
				return response;
			}
		}
		catch (IOException e1)
		{
			response.setStatusCode(HttpStatusCode.INTERNAL_SERVER_ERROR);
			return response;
		}

		if (f.exists())
		{
			response.setStatusCode(HttpStatusCode.OK);
			InputStream inputStream;
			try
			{
				inputStream = new FileInputStream(f);
				byte fileContent[] = new byte[(int) f.length()];
				inputStream.read(fileContent);
				inputStream.close();
				response.setEntity(fileContent);

				// guess and set the content type
				response.getHeaders().put(Http.CONTENT_TYPE, URLConnection.guessContentTypeFromName(f.getAbsolutePath()));
			}
			catch (FileNotFoundException e)
			{
				response.setStatusCode(HttpStatusCode.NOT_FOUND);
			}
			catch (IOException e)
			{
				response.setStatusCode(HttpStatusCode.INTERNAL_SERVER_ERROR);
			}

		}
		else
		{
			response.setStatusCode(HttpStatusCode.NOT_FOUND);
		}

		return response;
	}

	@Override
	protected void sendResponse(HttpResponse response, OutputStream outputStream) throws IOException
	{
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));

		writer.write(response.getHttpVersion().toString());
		writer.write(' ');
		writer.write("" + response.getStatusCode().getCode());
		writer.write(' ');
		writer.write(response.getStatusCode().getReasonPhrase());
		writer.write(Http.CRLF);

		if (response.getEntity() != null && response.getEntity().length > 0)
		{
			response.getHeaders().put(Http.CONTENT_LENGTH, "" + response.getEntity().length);
		}
		else
		{
			response.getHeaders().put(Http.CONTENT_LENGTH, "" + 0);
		}

		if (response.getHeaders() != null)
		{
			for (String key : response.getHeaders().keySet())
			{
				writer.write(key + ": " + response.getHeaders().get(key) + Http.CRLF);
			}
		}
		writer.write(Http.CRLF);
		writer.flush();

		if (response.getEntity() != null && response.getEntity().length > 0)
		{
			outputStream.write(response.getEntity());
		}
		outputStream.flush();

	}

	@Override
	protected boolean keepAlive(HttpRequest request, HttpResponse response)
	{
		// http://www.w3.org/Protocols/rfc2616/rfc2616-sec8.html
		if (response.getHeaders().containsKey(Http.CONNECTION) && response.getHeaders().get(Http.CONNECTION).equalsIgnoreCase("close"))
		{
			return false;
		}
		if (request.getHttpVersion().equals(HttpVersion.VERSION_1_1))
		{
			if (request.getHeaders().containsKey(Http.CONNECTION) && request.getHeaders().get(Http.CONNECTION).equalsIgnoreCase("close"))
			{
				return false;
			}
			else
			{
				return true;
			}
		}
		return false;
	}

}
