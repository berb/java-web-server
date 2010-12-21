package de.ioexception.www.http;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.ioexception.www.Http;

/**
 * An enum of available HTTP versions.
 * 
 * @see also http://tools.ietf.org/html/rfc2616.html#section-3.1
 * 
 * @author Benjamin Erb
 * 
 */
public enum HttpVersion
{
	/**
	 * HTTP/1.0
	 */
	VERSION_1_0(1, 0),

	/**
	 * HTTP/1.1
	 */
	VERSION_1_1(1, 1);

	private final int major;
	private final int minor;

	private HttpVersion(int major, int minor)
	{
		this.major = major;
		this.minor = minor;
	}

	@Override
	public String toString()
	{
		return Http.HTTP + Http.PROTOCOL_DELIMITER + major + "." + minor;
	}

	/**
	 * Extracts the HTTP version from the header line.
	 * 
	 * @param headerLine HTTP request header line
	 * @return
	 * @throws IllegalArgumentException
	 */
	public static HttpVersion extractVersion(String headerLine) throws IllegalArgumentException
	{
		Matcher m = Pattern.compile(Http.HTTP + "/(\\d+)\\.(\\d+)").matcher(headerLine);
		if (m.find())
		{
			if ((Integer.parseInt(m.group(1)) == 1) && (Integer.parseInt(m.group(2)) == 1))
			{
				return VERSION_1_1;
			}
			else if ((Integer.parseInt(m.group(1)) == 1) && (Integer.parseInt(m.group(2)) == 0))
			{
				return VERSION_1_0;
			}
			else
			{
				throw new IllegalArgumentException("Unknown HTTP Version");
			}
		}
		else
		{
			throw new IllegalArgumentException("Unknown HTTP Version");
		}
	}
}
