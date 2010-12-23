package de.ioexception.www.server.cache;

public interface EntityCacheEntry
{
	byte[] getEntity();
	String getETag();
	String getContentType();
}
