package com.tbook.rewardsphere.model

class Wallet(
    val address: String,
    val chain: String = "Ethereum",
    val chainId: Int = 1
) {
}