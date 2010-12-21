package de.ioexception.www.http;

import java.util.Map;

/**
 * An interface for basic HTTP message operations.
 * 
 * @author Benjamin Erb
 * 
 */
public interface HttpMessage
{
	/**
	 * Returns the used protocol version of this message.
	 * 
	 * @return
	 */
	HttpVersion getHttpVersion();

	/**
	 * Returns a list of key-value header fields.
	 * 
	 * @return
	 */
	Map<String, String> getHeaders();

	/**
	 * Returns the entity as a byte array or null, if no entity is available.
	 * 
	 * @return
	 */
	byte[] getEntity();

}
