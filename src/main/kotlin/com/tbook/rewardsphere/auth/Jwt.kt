package com.tbook.rewardsphere.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import org.springframework.security.core.Authentication
import java.time.Duration
import java.time.Instant

object Jwt {
    val EXPIRATION = Duration.ofDays(1).toMillis()
    val TOKEN_NAME = "jwt_token"
    val JWT_SECRET = "28f6a59bc2d0488583e4d2c795834901"

    fun generateToken(authentication: Authentication): String {
        val addressAuth = authentication as AddressAuthentication
        val algorithm = Algorithm.HMAC256(JWT_SECRET)
        return JWT.create()
            .withExpiresAt(Instant.now().plusMillis(EXPIRATION))
            .withClaim("address", addressAuth.address)
            .sign(algorithm);
    }

    fun validateToken(token: String): Authentication {
        val algorithm = Algorithm.HMAC256(JWT_SECRET)
        try {
            val jwt = JWT.require(algorithm).build().verify(token)
            val address = jwt.getClaim("address").toString().trim('"')
            val auth = AddressAuthentication(address, "", "")
            auth.isAuthenticated = true
            return auth
        } catch (ex: JWTVerificationException) {
            ex.printStackTrace()
            val auth = AddressAuthentication("", "", "")
            auth.isAuthenticated = false
            return auth
        }
    }
}
