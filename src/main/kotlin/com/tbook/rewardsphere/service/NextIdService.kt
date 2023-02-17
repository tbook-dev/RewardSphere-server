package com.tbook.rewardsphere.service

import com.tbook.rewardsphere.model.ProofResponse
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForEntity

@Service
class NextIdService(private val restTemplate: RestTemplate) {
    private val twitterToken = "AAAAAAAAAAAAAAAAAAAAAF3%2BcAEAAAAAww5nRctaDnOpRT9iuP9sJIzNQ%2FM%3DlhJQwGNjxp3B6TUbiepZ0lZ6oEuxDUmEn2Yd5VpDNOd4LMHLn8"
    private val twitterEndpointURL = "https://api.twitter.com/2/tweets"
    private val twitterParams = mapOf(
        "tweet.fields" to "lang,author_id",
        "user.fields" to "created_at",
    )

    private val nextIdEndpointURL = "https://proof-service.next.id/v1"

    fun getTwit(id: String) {
        restTemplate.getForEntity(twitterEndpointURL, String::class.java)
//        "User-Agent" to "v2TweetLookupJS",
//        "authorization" to "Bearer ${this.twitterToken}"
    }

    fun getWalletByTwitter(twitterHandle: String): List<String> {
        val response = restTemplate.getForEntity<ProofResponse>("${this.nextIdEndpointURL}/proof?platform=twitter&identity=${twitterHandle}&exact=true")
        return response.body!!.ids.flatMap {
            it.proofs.filter { p -> p.platform.equals("ethereum", true) }
        }.map { it.identity }
    }

    fun getTwitterByAddr(addr: String): List<String> {
        val response = restTemplate.getForEntity<ProofResponse>("${this.nextIdEndpointURL}/proof?platform=ethereum&identity=${addr}&exact=true")
        return response.body!!.ids.flatMap {
            it.proofs.filter { p -> p.platform.equals("twitter", true) }
        }.map { it.identity }
    }
}