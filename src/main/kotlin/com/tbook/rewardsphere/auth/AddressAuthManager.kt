package com.tbook.rewardsphere.auth

import com.tbook.rewardsphere.utils.Signs
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component

@Component
class AddressAuthManager: AuthenticationManager {
    override fun authenticate(authentication: Authentication): Authentication {
        val addAuth = authentication as AddressAuthentication
        val recoveredAddress = Signs.getAddressUsedToSignHashedMessage(addAuth.sign, addAuth.nonce)
        addAuth.isAuthenticated = recoveredAddress == addAuth.address
        return addAuth
    }
}
