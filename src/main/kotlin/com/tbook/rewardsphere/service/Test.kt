package com.tbook.rewardsphere.service


import com.tbook.rewardsphere.model.TwitterUser
import org.springframework.data.redis.core.RedisTemplate
import twitter4j.*
import twitter4j.conf.ConfigurationBuilder
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


fun main1() {
    val userId = "1260553941714186241"
    val bearerToken =
        "AAAAAAAAAAAAAAAAAAAAAF3%2BcAEAAAAAww5nRctaDnOpRT9iuP9sJIzNQ%2FM%3DlhJQwGNjxp3B6TUbiepZ0lZ6oEuxDUmEn2Yd5VpDNOd4LMHLn8"
    val BASE_URL = "https://api.twitter.com/2"

    val url = URL("$BASE_URL/users/$userId?user.fields=profile_image_url")
    val connection = url.openConnection() as HttpURLConnection

    connection.requestMethod = "GET"
    connection.setRequestProperty("Authorization", "Bearer $bearerToken")

    val response = BufferedReader(InputStreamReader(connection.inputStream)).use {
//
        it.readText()
    }
    val json = JSONObject(response.toString())
//    println(response.toString())
    val data = json.getJSONObject("data")
//    val user = TwitterUser(
//        data.get("id").toString(),
//        data.get("username").toString(),
//        data.get("name").toString(),
//        data.get("profile_image_url").toString()
//    )
//    println(user.name + user.userName + user.userId + user.profileImageUrl)



}