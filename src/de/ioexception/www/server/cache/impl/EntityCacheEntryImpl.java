package de.ioexception.www.server.cache.impl;

import de.ioexception.www.server.cache.EntityCacheEntry;

public class EntityCacheEntryImpl implements EntityCacheEntry
{
	private final byte[] entity;
	private final String eTag;
	private final String contentType;

	public EntityCacheEntryImpl(byte[] entity, String eTag, String contentType)
	{
		super();
		this.entity = entity;
		this.eTag = eTag;
		this.contentType = contentType;
	}

	@Override
	public byte[] getEntity()
	{
		return entity;
	}

	@Override
	public String getETag()
	{
		return eTag;
	}

	@Override
	public String getContentType()
	{
		return contentType;
	}

}
