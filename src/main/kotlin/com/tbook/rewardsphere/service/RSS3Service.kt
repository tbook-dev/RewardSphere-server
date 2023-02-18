package com.tbook.rewardsphere.service

import com.tbook.rewardsphere.model.Transaction
import org.json.JSONObject
import org.springframework.stereotype.Service
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


@Service
class RSS3Service {
    private val BASE_URL = "https://api.rss3.io/v1/notes/"
    private val include_poap = "false"
    private val count_only = "false"
    private val query_status = "false"
    private val tag = ""

    fun getRss3UserProfile(address: String): List<Transaction> {

        val limit = 100
        val type = "profile"
        val url =
            URL("$BASE_URL$address?limit=$limit&tag=$tag&type=$type&include_poap=$include_poap&count_only=$count_only&query_status=$query_status")
        val connection = url.openConnection() as HttpURLConnection

        connection.requestMethod = "GET"

        val response = BufferedReader(InputStreamReader(connection.inputStream)).use {
            it.readText()
        }

        val json = JSONObject(response)
        val result = json.getJSONArray("result")
        val transactionList = mutableListOf<Transaction>()
        for (i in 0 until result.length()) {
            val data = result.getJSONObject(i)
            val network = data.getString("network")
            val address_from = data.getString("address_from")
            val address_to = data.getString("address_to")
            val tag = data.getString("tag")
            val type = data.getString("type")
            val created_at = data.getString("created_at")
            val transaction = Transaction(network, address_from, address_to, tag, type, created_at)
            transactionList.add(transaction)
        }
        return transactionList
    }
}