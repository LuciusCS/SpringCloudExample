package com.example.auth.util;

import com.alibaba.nacos.shaded.com.google.gson.Gson;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.StandardCharsets;

public class GsonRedisSerializer<T> implements RedisSerializer<T> {

    private final Gson gson = new Gson();

    @Override
    public byte[] serialize(T t) throws SerializationException {
        return gson.toJson(t).getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        return gson.fromJson(new String(bytes, StandardCharsets.UTF_8), (Class<T>) Object.class);
    }
}
