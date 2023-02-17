package com.tbook.rewardsphere.auth

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority

class AddressAuthentication(
    val address: String,
    val sign: String,
    val nonce: String,
    authorities: List<GrantedAuthority> = emptyList()): AbstractAuthenticationToken(authorities) {
    override fun getCredentials(): List<String> {
        return listOf()
    }

    override fun getPrincipal(): String {
        return address
    }
}
