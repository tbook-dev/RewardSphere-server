package com.tbook.rewardsphere.controller

import com.tbook.rewardsphere.model.NFT
import com.tbook.rewardsphere.model.TwitterUser
import com.tbook.rewardsphere.service.TwitterInfoService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.data.redis.core.RedisTemplate
import com.fasterxml.jackson.databind.ObjectMapper


@RestController
class UserProfileController(
    val twitterInfoService: TwitterInfoService,
    private val redisTemplate: RedisTemplate<String, Any>
) {

    val bearerToken =
        "AAAAAAAAAAAAAAAAAAAAAF3%2BcAEAAAAAww5nRctaDnOpRT9iuP9sJIzNQ%2FM%3DlhJQwGNjxp3B6TUbiepZ0lZ6oEuxDUmEn2Yd5VpDNOd4LMHLn8"

    @GetMapping("/userInfo")
    fun getUserInfo(@RequestParam("userId") userId: String): TwitterUser {
        return twitterInfoService.getTwitterUserService(userId, bearerToken)
    }


    @GetMapping("redisTest")
    fun redisTest(): Any? {
        // 设置redis : 地址-twitterId
        redisTemplate.opsForValue().set("0x0206EB62A15656F983B0601B5111875D40B9C3b7", "1478980168194555908")
        val address = "0x0206EB62A15656F983B0601B5111875D40B9C3b7"
        return redisTemplate.opsForValue().get(address)

    }

}