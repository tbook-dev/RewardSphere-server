package com.tbook.rewardsphere.controller

import com.tbook.rewardsphere.auth.AddressAuthentication
import com.tbook.rewardsphere.model.Attribute
import com.tbook.rewardsphere.model.NFTMetaData
import com.tbook.rewardsphere.service.ContractService
import com.tbook.rewardsphere.service.NextIdService
import com.tbook.rewardsphere.service.S3Service
import com.tbook.rewardsphere.service.TwitterInfoService
import mu.KotlinLogging
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
import java.security.Principal

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
    private val logger = KotlinLogging.logger{}

    private val twitterToken =
            "AAAAAAAAAAAAAAAAAAAAAF3%2BcAEAAAAAww5nRctaDnOpRT9iuP9sJIzNQ%2FM%3DlhJQwGNjxp3B6TUbiepZ0lZ6oEuxDUmEn2Yd5VpDNOd4LMHLn8"

    @PostMapping("/screenshotAddress")
    fun getUserInfo(@RequestParam("tweetUrl") tweetUrl: String): Any {
        val id = URI.create(tweetUrl).path.split("/").last()
        val request = RequestEntity.post("https://tweetpik.com/api/images")
                .header("Content-Type", "application/json")
                .header("Authorization", twipicKey)
                .body(mapOf("tweetId" to id))
        val response = restTemplate.exchange<String>(request)
        println(response.body)
        return mapOf("image" to "https://ik.imagekit.io/tweetpik/356884789962211917/${id}.png",
                "twitId" to id)
    }

    @PostMapping("/addMapping")
    fun deployContract(@RequestParam("twitId") twitId: String,
                       @RequestParam("address", required = false, defaultValue = "") address: String,
                       principal: Principal): Any {
        try {
            val pic = "https://ik.imagekit.io/tweetpik/356884789962211917/${twitId}.png"
            //val content = twitterService.getTweet(twitId, twitterToken)
            val idAttr = Attribute("twit_id", twitId)
            val meta = NFTMetaData("RewardSphere", "RewardSphere",
                    "RewardSphere NFT", pic, listOf(idAttr))
            val nftId = contractService.addMapping(twitId)
            s3Service.putMeta(nftId.toString(), meta)
            //redisTemplate.opsForValue().set(twitId, nftId)
            val authentication = principal as AddressAuthentication?
            if (authentication != null) {
                contractService.airDropTo(authentication.address, nftId, 1)
            } else if (address.isNotEmpty() && address != "undefined") {
                contractService.airDropTo(address, nftId, 1)
            }
            return mapOf("opensea" to "https://testnets.opensea.io/assets/mumbai/0xACdE17C1A595Ae2Cf12605a157Ae8Ad5Ddf8953F/${nftId}",
                    "contract" to "https://mumbai.polygonscan.com/address/0xACdE17C1A595Ae2Cf12605a157Ae8Ad5Ddf8953F",
                    "nftId" to nftId
            )
        } catch (ex: Exception) {
            logger.error("add mapping error", ex)
            throw ex
        }
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