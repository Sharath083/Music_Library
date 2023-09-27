package com.example.redis

import com.google.gson.Gson
import redis.clients.jedis.Jedis

class RedisHelper(private val redisClient: Jedis) {

    fun set(key: String, value: Any) {
        val jsonString = Gson().toJson(value)
        redisClient.set(key, jsonString.trim())
    }
    fun getString(key: String): String? {
        return Gson().toJson(redisClient.get(key))
    }
    fun delete(key:String){
        redisClient.del(key)
    }

}
