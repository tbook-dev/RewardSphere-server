package com.tbook.rewardsphere.auth

import com.tbook.rewardsphere.SimpleResponse
import com.tbook.rewardsphere.SimpleResponseEntity
import com.tbook.rewardsphere.service.NextIdService
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseCookie
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.Duration
import java.util.concurrent.ConcurrentHashMap
import javax.servlet.http.HttpServletResponse

@RestController
class LoginController(
        private val manager: AddressAuthManager,
        private val nextIdService: NextIdService
        ) {
    val nonce = ConcurrentHashMap<String, String>()

    @PostMapping("/authenticate")
    //@Operation(summary = "login using wallet", description = "login with address value and sign message")
    fun login(
            @RequestParam("address") address: String,
            @RequestParam("sign") sign: String,
            response: HttpServletResponse
    ): ResponseEntity<SimpleResponseEntity<List<String>>> {
        val r = tryAuthenticate(address, sign)
        return if (r.isSuccess) {
            val cookie = buildCookie(r.getOrNull()!!)
            val tws = nextIdService.getTwitterByAddr(address)
            if (tws.isEmpty()) {
                ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                        .body(SimpleResponseEntity.failed("no twitter binding"))
            } else {
                ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                        .body(SimpleResponseEntity.success("", tws))
            }
        } else {
            ResponseEntity.status(HttpStatus.FORBIDDEN).body(SimpleResponseEntity.failed("login failed"))
        }
    }

    private fun tryAuthenticate(address: String, sign: String): Result<AddressAuthentication> {
        val authentication = AddressAuthentication(address, sign, nonce[address].orEmpty())
        val authResult = manager.authenticate(authentication) as AddressAuthentication
        nonce.remove(address)
        return if (authResult.isAuthenticated) {
            Result.success(authResult)
        } else {
            Result.failure(Exception())
        }
    }

    @GetMapping("nonce")
    fun getNonce(@RequestParam("address") address: String): String {
        val message = "sign in to tbook reward sphere ${System.currentTimeMillis()}"
        nonce[address] = message
        return message
    }

    private fun buildCookie(authentication: AddressAuthentication): ResponseCookie {
        val token = Jwt.generateToken(authentication)
        return ResponseCookie.from(Jwt.TOKEN_NAME, token).secure(true).sameSite("none")
                .httpOnly(true).maxAge(Duration.ofDays(1)).build()
    }
}