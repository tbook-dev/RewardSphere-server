package com.tbook.rewardsphere

class SimpleResponse(val success: Boolean, val message: String) {
    companion object {
        fun success(message: String) = SimpleResponse(true, message)
        fun failed(message: String) = SimpleResponse(false, message)
    }
}