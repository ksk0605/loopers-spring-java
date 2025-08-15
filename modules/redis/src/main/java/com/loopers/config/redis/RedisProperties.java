package com.loopers.config.redis;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(value = "datasource.redis")
public class RedisProperties {
    private final Integer database;
    private final RedisNodeInfo master;
    private final List<RedisNodeInfo> replicas;
}
