package com.tbook.rewardsphere.model

class Transaction(
    val network:String,
    val address_from:String,
    val address_to:String,
    val tag:String,
    val type:String,
    val created_at:String
) {
}