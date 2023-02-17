package com.tbook.rewardsphere.auth

import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.stereotype.Component
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Component
class JwtAuthenticationFilter(authManager: AddressAuthManager) : BasicAuthenticationFilter(authManager) {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain
    ) {
        val tokenCookie = request.cookies?.find { it.name.equals(Jwt.TOKEN_NAME) }
        if (tokenCookie == null) {
            chain.doFilter(request, response)
            return
        }

        val auth = Jwt.validateToken(tokenCookie.value) as AddressAuthentication
        if (!auth.isAuthenticated) {
            chain.doFilter(request, response)
            return
        }

        SecurityContextHolder.getContext().authentication = auth
        chain.doFilter(request, response)
    }
}
