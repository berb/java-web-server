package de.ioexception.www.http;

/**
 * An interface for HTTP responses.
 * 
 * @see also http://tools.ietf.org/html/rfc2616.html#section-6
 * 
 * @author Benjamin Erb
 *
 */
public interface HttpResponse extends HttpMessage
{
	/**
	 * Returns the HTTP Status Code of this response.
	 * 
	 * @see also http://tools.ietf.org/html/rfc2616.html#section-6.1.1
	 * 
	 * @return
	 */
	HttpStatusCode getStatusCode();
}
