package com.tbook.rewardsphere.service

import com.amazonaws.HttpMethod
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.tbook.rewardsphere.model.NFTMetaData
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ContentDisposition
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*


@Service
class S3Service(
        private val s3: AmazonS3,
        private val restTemplate: RestTemplate,
        @Value("\${r2.publicUrl}") private val publicUrl: String,
) {
    val mapper = ObjectMapper()
    fun getPreSignedUrl(): String {
        val name = UUID.randomUUID().toString().replace("-", "")
        return getSignUrl(name)
    }

    fun putMeta(nftId: String, meta: NFTMetaData): String {
        val name = "${nftId}.json"

        val objectMeta = ObjectMetadata()
        objectMeta.contentType = MediaType.APPLICATION_JSON_VALUE
        val request = PutObjectRequest("btm", name,
                ByteArrayInputStream(mapper.valueToTree<JsonNode>(meta).toString().toByteArray()),
                objectMeta)
        val response = s3.putObject(request)
        println(response)
        return "${publicUrl}/${name}"
    }

    fun putStringAsObject(name: String, content: String): String {
        val url = getSignUrl(name)
        restTemplate.put(url, content.toByteArray())
        return "${publicUrl}/name"
    }

    private fun getSignUrl(name: String): String {
        val expire = Date.from(LocalDateTime.now().plusHours(1).atZone(ZoneId.systemDefault()).toInstant())
        return s3.generatePresignedUrl("", name, expire, HttpMethod.PUT).toString()
    }
}