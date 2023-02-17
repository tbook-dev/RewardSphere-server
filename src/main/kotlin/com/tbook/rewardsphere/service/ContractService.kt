package com.tbook.rewardsphere.service

import org.springframework.stereotype.Service
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt

@Service
class ContractService {
    val ethUrl = "http://localhost:8545"


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