package com.example.redis

import redis.clients.jedis.Jedis

object RedisClient {
    val jedis:Jedis=Jedis("localhost",6379)
}