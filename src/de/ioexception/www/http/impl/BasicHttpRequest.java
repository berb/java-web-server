package de.ioexception.www.http.impl;

import de.ioexception.www.http.HttpMethod;
import de.ioexception.www.http.HttpRequest;

public class BasicHttpRequest extends BasicHttpMessage implements HttpRequest
{
	HttpMethod method;
	String requestUri;

	@Override
	public HttpMethod getHttpMethod()
	{
		return method;
	}

	@Override
	public String getRequestUri()
	{
		return requestUri;
	}

	public HttpMethod getMethod()
	{
		return method;
	}

	public void setMethod(HttpMethod method)
	{
		this.method = method;
	}

	public void setRequestUri(String requestUri)
	{
		this.requestUri = requestUri;
	}
	
	
	
	

}
