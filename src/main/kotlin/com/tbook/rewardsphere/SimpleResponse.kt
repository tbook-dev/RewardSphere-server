package com.tbook.rewardsphere

class SimpleResponse(val success: Boolean, val message: String) {
    companion object {
        fun success(message: String) = SimpleResponse(true, message)
        fun failed(message: String) = SimpleResponse(false, message)
    }
}

class SimpleResponseEntity<T>(val success: Boolean, val message: String, val entity: T?) {
    companion object {
        fun <T>success(message: String, entity: T) = SimpleResponseEntity(true, message, entity)
        fun <T>failed(message: String, entity: T? = null) = SimpleResponseEntity<T>(false, message, entity)
    }
}