package com.tbook.rewardsphere.auth

import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.http.HttpStatus
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.HttpStatusEntryPoint
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter

@Configuration
@EnableMethodSecurity
class SecurityConfig {
    @Bean
    fun filterChain(http: HttpSecurity, jwtTokenFilter: JwtAuthenticationFilter): SecurityFilterChain {
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                .exceptionHandling()
                .authenticationEntryPoint(HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                .and()

                .formLogin().and()

                .csrf().disable()

                .authorizeRequests()
                .anyRequest().permitAll()
                .and()

                .httpBasic(Customizer.withDefaults())
                .addFilter(jwtTokenFilter)

        return http.build()
    }

    @Bean
    fun passWordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun corsFilter(): FilterRegistrationBean<*>? {
        val source = UrlBasedCorsConfigurationSource()
        val config = CorsConfiguration()
        config.allowCredentials = true
        config.allowedOrigins = arrayListOf(
                "https://tbook.vip",
                "https://app.tbook.vip",
                "https://api.tbook.vip",
                "https://tbook.com",
                "https://www.tbook.com",
                "https://app.tbook.com",
                "https://api.tbook.com",
                "https://grant.tbook.com"
        )
        config.allowedHeaders = arrayListOf("*", "X-Requested-With", "Content-Type", "Authorization")
        config.allowedMethods = arrayListOf("GET", "POST", "HEAD", "PUT", "DELETE", "PATCH", "OPTIONS")
        source.registerCorsConfiguration("/**", config)
        val bean = FilterRegistrationBean(CorsFilter(source))
        bean.order = Ordered.HIGHEST_PRECEDENCE
        return bean
    }

}
