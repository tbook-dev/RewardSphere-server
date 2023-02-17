package com.tbook.rewardsphere.model

import java.math.BigDecimal

class NFT(
    val id: String,
    val name: String,
    val description: String,
    val imageUrl: String,
    val price: BigDecimal,
    val owner: String
) {
}