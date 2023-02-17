package com.tbook.rewardsphere.controller

import com.tbook.rewardsphere.model.TwitterUser
import com.tbook.rewardsphere.service.ContractService
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class HomeController(
    private val redisTemplate: RedisTemplate<String, Any>,
    val contractService: ContractService
) {

    @GetMapping()
    fun redisTest(): Any? {
        redisTemplate.opsForValue().set("test", "abc")
        return redisTemplate.opsForValue().get("test")
    }

    @GetMapping("/screenshotAddress")
    fun getUserInfo(@RequestParam("tweetUrl") tweetUrl: String): String {
        //todo : 根据twitter 的url，返回截图的地址，先mock
        val screenshotAddress = "https://pbs.twimg.com/media/ExQoprFVoAYMSCl?format=png&name=small"
        return screenshotAddress
    }

    @GetMapping("/deployContract")
    fun deployContract(@RequestParam("nft") nft: String) {
        //todo : 根据nft 参数，部署合约
    }

    @GetMapping("/contractAddress")
    fun getContractAddress(@RequestParam("transactionHash") transactionHash: String): String {
        //todo :  轮询合约部署进度，获取合约地址
        val ethUrl = "http://localhost:8545"
        val blockNumber = contractService.getContractBlockNumber(ethUrl, transactionHash)
        return blockNumber
    }


}