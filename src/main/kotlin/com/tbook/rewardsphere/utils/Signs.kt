package com.tbook.rewardsphere.utils

import org.web3j.crypto.Keys
import org.web3j.crypto.Sign
import org.web3j.utils.Numeric

object Signs {
    fun getAddressUsedToSignHashedMessage(signedHexMessage: String, originalMessage: String): String {
        var signedMessageInHex = signedHexMessage
        if (signedMessageInHex.startsWith("0x")) {
            signedMessageInHex = signedMessageInHex.substring(2)
        }

        // No need to prepend these strings with 0x because
        // Numeric.hexStringToByteArray() accepts both formats
        val r = signedMessageInHex.substring(0, 64)
        val s = signedMessageInHex.substring(64, 128)
        val v = signedMessageInHex.substring(128, 130)

        // Using Sign.signedPrefixedMessageToKey for EIP-712 compliant signatures.
        val pubkey = Sign.signedPrefixedMessageToKey(
            originalMessage.toByteArray(),
            Sign.SignatureData(
                Numeric.hexStringToByteArray(v)[0],
                Numeric.hexStringToByteArray(r),
                Numeric.hexStringToByteArray(s)
            )
        ).toString(16)
        return "0x${Keys.getAddress(pubkey)}"
    }
}
