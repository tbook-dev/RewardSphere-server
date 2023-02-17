package com.tbook.rewardsphere.controller

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HomeController(private val redisTemplate: RedisTemplate<String, Any>) {

    @GetMapping()
    fun redisTest(): Any? {
        redisTemplate.opsForValue().set("test", "abc")
        return redisTemplate.opsForValue().get("test")
    }
}