package com.tbook.rewardsphere.controller

import com.tbook.rewardsphere.enums.IncentiveRules
import com.tbook.rewardsphere.service.TwitterInfoService
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
class IncentiveController(
    private val redisTemplate: RedisTemplate<String, Any>,
    val twitterInfoService: TwitterInfoService
) {

    val bearerToken =
        "AAAAAAAAAAAAAAAAAAAAAF3%2BcAEAAAAAww5nRctaDnOpRT9iuP9sJIzNQ%2FM%3DlhJQwGNjxp3B6TUbiepZ0lZ6oEuxDUmEn2Yd5VpDNOd4LMHLn8"

    @GetMapping("/incentiveRules")
    fun getIncentiveRules(): List<Map<String, Any>> {
        return IncentiveRules.values().map {
            mapOf("code" to it.code, "desc" to it.desc)
        }
    }


    @GetMapping("/getIncentiveIds")
    fun getIncentiveRules(@RequestParam("twitterId") twitterId: String): List<String> {
        return twitterInfoService.getCommentsUserList(twitterId, bearerToken)
    }


}