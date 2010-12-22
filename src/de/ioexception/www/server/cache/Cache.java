package de.ioexception.www.server.cache;

public interface Cache<K,V>
{
	public V put(K key, V value);
	public V get(K key);
}
