package de.ioexception.www.http.impl;

import de.ioexception.www.http.HttpResponse;
import de.ioexception.www.http.HttpStatusCode;

public class BasicHttpResponse extends BasicHttpMessage implements HttpResponse
{
	HttpStatusCode statusCode;

	@Override
	public HttpStatusCode getStatusCode()
	{
		return statusCode;
	}

	public void setStatusCode(HttpStatusCode statusCode)
	{
		this.statusCode = statusCode;
	}
	
	
}
