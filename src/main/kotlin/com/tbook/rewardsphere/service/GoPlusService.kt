package com.tbook.rewardsphere.service

import com.tbook.rewardsphere.model.GoPlusMaliciousResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForEntity

@Service
class GoPlusService(
        @Value("\${goplus.appKey}") private val appKey: String,
        @Value("\${goplus.secret}") private val appSecret: String,
        private val restTemplate: RestTemplate
) {
    fun isMaliciousAddress(address: String, chain: Int = 137): Boolean {
        val response = restTemplate.getForEntity<GoPlusMaliciousResponse>("https://api.gopluslabs.io/api/v1/address_security/${address}?chain_id=${chain}").body
                ?: return false
        val result = response.result
        return result.blacklistDoubt == "1" || result.stealingAttack == "1"
    }
}