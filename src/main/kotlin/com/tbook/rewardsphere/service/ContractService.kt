package com.tbook.rewardsphere.service

import com.fasterxml.jackson.databind.JsonNode
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.RequestEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt
import java.net.URI

@Service
class ContractService(
        private val restTemplate: RestTemplate,
        @Value("\${contract_caller_url}") private val callerUrl: String,
        @Value("\${contract_caller_secret}") private val callerSecret: String
) {
    val ethUrl = "http://localhost:8545"
    private val logger = KotlinLogging.logger{}

    fun addMapping(twitId: String): Long {
        println("caller url: $callerUrl")
        val request = RequestEntity.post(URI.create("${callerUrl}/addMapping"))
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, callerSecret)
                .body("""{"twitId": "$twitId"}""")
        val result = restTemplate.exchange<JsonNode>(request).body!!
        return result["nextId"].asLong()
    }

    fun airDropTo(address: String, nftId: Long, count: Int) {
        logger.info { "airDropTo: $address, $nftId, $count" }
        val request = RequestEntity.post(URI.create("${callerUrl}/airDrop"))
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, callerSecret)
                .body(mapOf(
                        "address" to address,
                        "count" to count,
                        "nftId" to nftId
                ))
        val result = restTemplate.exchange<JsonNode>(request).body!!
        logger.info("airdrop result {}", result)
    }

    fun getContractBlockNumber(ethUrl: String, transactionHash: String): String {
        // 连接到以太坊网络
        val web3 = Web3j.build(HttpService(ethUrl))

        // 合约事务哈希
//        val transactionHash = "0x123456789abcdef"

        // 获取区块链上指定合约地址的合约部署块号
        val receipt: EthGetTransactionReceipt = web3.ethGetTransactionReceipt(transactionHash).send()

        // 如果返回结果不为 null，表示合约已经被部署
        val blockNumber =
                if (receipt.transactionReceipt.isPresent) receipt.transactionReceipt.get().blockNumber.toString() else "null"

        // 打印部署块号
        println("合约部署块号: $blockNumber")
        return blockNumber
    }
}