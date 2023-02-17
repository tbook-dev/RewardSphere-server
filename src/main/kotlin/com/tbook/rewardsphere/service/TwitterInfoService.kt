package com.tbook.rewardsphere.service

import okhttp3.OkHttpClient
import okhttp3.Request
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import com.google.gson.GsonBuilder
import com.tbook.rewardsphere.model.TwitterUser
import org.json.JSONObject


@Service
class TwitterInfoService(private val restTemplate: RestTemplate) {
    private val twitterToken =
        "AAAAAAAAAAAAAAAAAAAAAF3%2BcAEAAAAAww5nRctaDnOpRT9iuP9sJIzNQ%2FM%3DlhJQwGNjxp3B6TUbiepZ0lZ6oEuxDUmEn2Yd5VpDNOd4LMHLn8"
    val BASE_URL = "https://api.twitter.com/2"


    //获取指定 ID 的推文
    fun getTweet(tweetId: String, bearerToken: String): String {
        val url = URL("$BASE_URL/tweets/$tweetId")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.setRequestProperty("Authorization", "Bearer $bearerToken")

        val response = StringBuffer()
        BufferedReader(InputStreamReader(connection.inputStream)).use {
            var inputLine: String?
            while (it.readLine().also { inputLine = it } != null) {
                response.append(inputLine)
            }
        }

        return response.toString()
    }

    //获取转推信息
    fun getRetweets(tweetId: String, bearerToken: String): List<String> {
        val url = URL("$BASE_URL/tweets/$tweetId/retweeted_by")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.setRequestProperty("Authorization", "Bearer $bearerToken")

        val response = StringBuffer()
        BufferedReader(InputStreamReader(connection.inputStream)).use {
            var inputLine: String?
            while (it.readLine().also { inputLine = it } != null) {
                response.append(inputLine)
            }
        }

        val retweets = mutableListOf<String>()
        val json = JSONObject(response.toString())
        val dataArray = json.getJSONArray("data")
        println(dataArray.toString())

        for (i in 0 until dataArray.length()) {
            val data = dataArray.getJSONObject(i)
            retweets.add(data.getString("id"))
        }

        return retweets
    }

    //获取评论的用户信息
    fun getCommentsUserList(tweetId: String, bearerToken: String): List<String> {
        val tweetId = "1600122565090889728" //
        val bearerToken =
            "AAAAAAAAAAAAAAAAAAAAAF3%2BcAEAAAAAww5nRctaDnOpRT9iuP9sJIzNQ%2FM%3DlhJQwGNjxp3B6TUbiepZ0lZ6oEuxDUmEn2Yd5VpDNOd4LMHLn8" // 替换成你的 Bearer Token
//        val url =
//            URL("$BASE_URL/tweets/$tweetId?expansions=referenced_tweets.id.author_id&tweet.fields=conversation_id,created_at,in_reply_to_user_id,referenced_tweets,text,public_metrics")
//    val url = URL("https://api.twitter.com/2/tweets/search/recent?query=${tweetId}&tweet.fields=public_metrics")
//    val url = URL("https://api.twitter.com/2/tweets/$tweetId")
        val url =
            URL("https://api.twitter.com/2/tweets/search/recent?query=conversation_id:$tweetId&tweet.fields=public_metrics&expansions=author_id")
        val connection = url.openConnection() as HttpURLConnection

        connection.requestMethod = "GET"
        connection.setRequestProperty("Authorization", "Bearer $bearerToken")

        val response = BufferedReader(InputStreamReader(connection.inputStream)).use {
//
            it.readText()
        }
        val json = JSONObject(response.toString())
//        println(response.toString())
        val includesInfo = json.getJSONObject("includes")
        val userArray = includesInfo.getJSONArray("users")
        val userIdList = mutableListOf<String>()
        for (i in 0 until userArray.length()) {
            val userId = userArray.getJSONObject(i).getString("id")
            userIdList.add(userId)
        }
        return userIdList

    }

    fun getLikeCnt(tweetIds: List<String>, bearerToken: String) {
        // 构建请求
        val request = Request.Builder()
            .url("https://api.twitter.com/2/tweets?ids=${tweetIds.joinToString(",")}&tweet.fields=public_metrics")
            .addHeader("Authorization", "Bearer $bearerToken")
            .build()

        val client = OkHttpClient()
        val gson = GsonBuilder().setPrettyPrinting().create()

        // 发送请求并处理响应
        val response = client.newCall(request).execute()
        if (response.isSuccessful) {
            val responseData = response.body?.string()
            println(responseData.toString())
            val parsedData = gson.fromJson(responseData, Map::class.java)

//        val tweets = parsedData["data"] as List<Map<String, Any>>
//
//        for (tweet in tweets) {
//            println("推文 ID: ${tweet["id"]}")
//            val publicMetrics = tweet["public_metrics"] as Map<String, Int>
////                println("评论数: ${publicMetrics["reply_count"]}")
//            println("点赞数: ${publicMetrics["like_count"]}")
//        }
        } else {
            println("Error occurred while fetching tweets: ${response.code} ${response.message}")
        }
    }

    //根据userId 获取用户信息
    fun getTwitterUserService(userId: String, bearerToken: String): TwitterUser {
        val url = URL("$BASE_URL/users/$userId?user.fields=profile_image_url")
        val connection = url.openConnection() as HttpURLConnection

        connection.requestMethod = "GET"
        connection.setRequestProperty("Authorization", "Bearer $bearerToken")

        val response = BufferedReader(InputStreamReader(connection.inputStream)).use {
            it.readText()
        }
        val json = JSONObject(response)
        val data = json.getJSONObject("data")
        val user = TwitterUser(
            data.get("id").toString(),
            data.get("username").toString(),
            data.get("name").toString(),
            data.get("profile_image_url").toString(),
            //TODO：main wallet & wallet list
            "mainWallet",
            "address"
        )
        return user
    }

}