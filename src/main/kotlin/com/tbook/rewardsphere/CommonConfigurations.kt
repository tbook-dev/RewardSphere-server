package com.tbook.rewardsphere

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.web.client.RestTemplate


@Configuration
class CommonConfigurations {
    @Bean
    fun s3client(
        @Value("\${r2.endpoint}") r2ServiceEndpoint: String,
        @Value("\${r2.accountId}") accountIdValue: String,
        @Value("\${r2.accessKey}") accessKeyValue: String,
        @Value("\${r2.secretKey}") secretKeyValue: String
    ): AmazonS3? {
        val accountR2Url = String.format(r2ServiceEndpoint, accountIdValue)
        val credentials: AWSCredentials = BasicAWSCredentials(
            accessKeyValue,
            secretKeyValue
        )
        val endpointConfiguration = EndpointConfiguration(accountR2Url, null)
        return AmazonS3ClientBuilder
            .standard()
            .withEndpointConfiguration(endpointConfiguration)
            .withPathStyleAccessEnabled(true)
            .withCredentials(AWSStaticCredentialsProvider(credentials))
            .build()
    }

    @Bean
    fun restTemplate(): RestTemplate {
        return RestTemplate()
    }

    @Bean
    fun jedisConnectionFactory(): JedisConnectionFactory {
        return JedisConnectionFactory()
    }

    @Bean
    fun redisTemplate(factory: JedisConnectionFactory): RedisTemplate<String, Any> {
        val template = RedisTemplate<String, Any>()
        template.setConnectionFactory(factory)
        return template
    }
}
