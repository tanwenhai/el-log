package com.twh.ellog;

import java.lang.reflect.Method;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wenhai.tan
 * @date 2021/9/2
 */
public final class Metadata<T> {

  private final T data;

  private Metadata(T data) {
    this.data = data;
  }

  public T getData() {
    return data;
  }

  public static <T> String newMetadataGetKey(T data) {
    Metadata<T> metadata = new Metadata<>(data);
    String id = CachePool.newId();
    CachePool.put(id, metadata);

    return id;
  }

  public static <T> Metadata<T> getById(String id) {
    return CachePool.get(id);
  }

  private static class CachePool {

    private CachePool() {}

    private static final ConcurrentHashMap<String, Metadata<?>> CACHE = new ConcurrentHashMap<>();

    static String newId() {
      return UUID.randomUUID().toString();
    }

    static <T> void put(String key, Metadata<T> value) {
      CACHE.putIfAbsent(key, value);
    }

    @SuppressWarnings("unchecked")
    static <T> Metadata<T> get(String key) {
      return (Metadata<T>)CACHE.get(key);
    }
  }
}
