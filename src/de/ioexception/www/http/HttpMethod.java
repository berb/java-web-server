package de.ioexception.www.http;

/**
 * An enum of available HTTP methods.
 * 
 * @see also http://tools.ietf.org/html/rfc2616.html#section-5.1
 * 
 * @author Benjamin Erb
 * 
 */
public enum HttpMethod
{
	HEAD,
	GET,
	POST,
	PUT,
	DELETE;

	@Override
	public String toString()
	{
		return this.name();
	}

	/**
	 * Extracts the HTTP method from the header line.
	 * 
	 * @param headerLine HTTP request header line
	 * @return the HTTP method 
	 * @throws IllegalArgumentException
	 */
	public static HttpMethod extractMethod(String headerLine) throws IllegalArgumentException
	{
		String method = headerLine.split(" ")[0];
		if (method != null)
		{
			return HttpMethod.valueOf(method);
		}
		else
		{
			throw new IllegalArgumentException();
		}
	}
}
