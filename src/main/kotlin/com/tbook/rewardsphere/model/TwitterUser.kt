package com.tbook.rewardsphere.model

import java.util.Date

class TwitterUser(
    val userId: String,
    val userName: String = "",
    val name: String = "",
    val profileImageUrl: String = "",
    val Wallet: String = "",
    val Address: String = "",
    var likeCount: Int? = 0,
    var commentDate: Date? = null,
    var fragmentsShare: Int? = 0
) {
}