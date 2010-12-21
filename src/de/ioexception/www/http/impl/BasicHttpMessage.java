package de.ioexception.www.http.impl;

import java.util.Map;

import de.ioexception.www.http.HttpMessage;
import de.ioexception.www.http.HttpVersion;

public class BasicHttpMessage implements HttpMessage
{
	private HttpVersion version;
	private byte[] entity = null;
	private Map<String, String> headers;
	
	
	@Override
	public HttpVersion getHttpVersion()
	{
		return version;
	}

	@Override
	public Map<String, String> getHeaders()
	{
		return headers;
	}

	@Override
	public byte[] getEntity()
	{
		return entity;
	}

	public HttpVersion getVersion()
	{
		return version;
	}

	public void setVersion(HttpVersion version)
	{
		this.version = version;
	}

	public void setEntity(byte[] entity)
	{
		this.entity = entity;
	}

	public void setHeaders(Map<String, String> headers)
	{
		this.headers = headers;
	}

	
	
}
