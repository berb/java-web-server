package de.ioexception.www.server.impl;

import java.net.Socket;
import java.util.HashMap;

import de.ioexception.www.Http;
import de.ioexception.www.http.HttpRequest;
import de.ioexception.www.http.HttpResponse;
import de.ioexception.www.http.HttpStatusCode;
import de.ioexception.www.http.impl.BasicHttpResponse;
import de.ioexception.www.server.cache.Cache;
import de.ioexception.www.server.cache.EntityCacheEntry;
import de.ioexception.www.server.cache.impl.EntityCacheEntryImpl;

public class CachingHttpWorker extends BasicHttpWorker
{
	private final Cache<String, EntityCacheEntry> cache;

	public CachingHttpWorker(Socket socket, BasicHttpServer server, Cache<String, EntityCacheEntry> cache)
	{
		super(socket, server);
		this.cache = cache;
	}

	@Override
	protected HttpResponse handleRequest(HttpRequest request)
	{
		EntityCacheEntry cacheEntry = cache.get(request.getRequestUri());
		
		if(null == cacheEntry)
		{
			HttpResponse response = super.handleRequest(request); 
			if(response.getStatusCode().equals(HttpStatusCode.OK) && response.getEntity() != null && response.getEntity().length > 0)
			{
				EntityCacheEntry entry = new EntityCacheEntryImpl(response.getEntity(), response.getHeaders().get(Http.ETAG), response.getHeaders().get(Http.CONTENT_TYPE));
				cache.put(request.getRequestUri(), entry);
			}
			return response;
		}
		else
		{
			//TODO check for conditional request
			BasicHttpResponse response = new BasicHttpResponse();
			response.setHeaders(new HashMap<String, String>());
			response.getHeaders().put(Http.SERVER, server.getServerSignature());
			response.setVersion(request.getHttpVersion());
			response.getHeaders().put(Http.CONTENT_LENGTH, ""+cacheEntry.getEntity().length);
			if(null != cacheEntry.getETag())
			{
				response.getHeaders().put(Http.ETAG, cacheEntry.getETag());	
			}
			if(null != cacheEntry.getContentType())
			{
				response.getHeaders().put(Http.CONTENT_TYPE, cacheEntry.getContentType());	
			}
			response.setEntity(cacheEntry.getEntity());
			response.setStatusCode(HttpStatusCode.OK);
			return response;
		}
	}

}
