package ru.isshepelev.auto.infrastructure.service;

public interface RedisService {


    void save(String key, Object value, long timeout);

    String get(String key);

    void delete(String key);
}
