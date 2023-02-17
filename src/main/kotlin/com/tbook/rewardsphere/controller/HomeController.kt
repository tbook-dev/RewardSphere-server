package com.tbook.rewardsphere.controller

import com.tbook.rewardsphere.model.NFTMetaData
import com.tbook.rewardsphere.service.ContractService
import com.tbook.rewardsphere.service.NextIdService
import com.tbook.rewardsphere.service.S3Service
import com.tbook.rewardsphere.service.TwitterInfoService
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.http.RequestEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange
import java.net.URI

@RestController
class HomeController(
        private val redisTemplate: RedisTemplate<String, Any>,
        val contractService: ContractService,
        private val restTemplate: RestTemplate,
        @Value("\${twipic_key}") private val twipicKey: String,
        private val twitterService: TwitterInfoService,
        private val s3Service: S3Service,
        private val nextIdService: NextIdService
        ) {
    private val twitterToken =
            "AAAAAAAAAAAAAAAAAAAAAF3%2BcAEAAAAAww5nRctaDnOpRT9iuP9sJIzNQ%2FM%3DlhJQwGNjxp3B6TUbiepZ0lZ6oEuxDUmEn2Yd5VpDNOd4LMHLn8"

    @PostMapping("/screenshotAddress")
    fun getUserInfo(@RequestParam("tweetUrl") tweetUrl: String): Any {
        val id = URI.create(tweetUrl).path.split("/").last()
        val request = RequestEntity.post("https://tweetpik.com/api/images")
                .header("Content-Type", "application/json")
                .header("Authorization", twipicKey).body("{\"tweetId\":\"${id}\"}")
        restTemplate.exchange<String>(request)
        return mapOf("image" to "https://ik.imagekit.io/tweetpik/356884789962211917/${id}",
                "twitId" to id)
    }

    @GetMapping("/deployContract")
    fun deployContract(@RequestParam("twitId") twitId: String) {
        val pic = "https://ik.imagekit.io/tweetpik/356884789962211917/${twitId}"
        val content = twitterService.getTweet(twitId, twitterToken)
        val meta = NFTMetaData("RewardSphere", "RewardSphere", content, pic)
        s3Service.putMeta(twitId, meta)
    }

    @GetMapping("/contractAddress")
    fun getContractAddress(@RequestParam("transactionHash") transactionHash: String): String {
        //todo :  轮询合约部署进度，获取合约地址
        val ethUrl = "http://localhost:8545"
        val blockNumber = contractService.getContractBlockNumber(ethUrl, transactionHash)
        return blockNumber
    }

    @GetMapping("getTwitterHandle")
    fun getByAddress(@RequestParam("address") address: String): List<String> {
        return nextIdService.getTwitterByAddr(address)
    }
}