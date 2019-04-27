package com.kanj.apps.encryptionkey

import android.util.Base64
import java.security.NoSuchAlgorithmException
import javax.crypto.Cipher
import javax.crypto.NoSuchPaddingException
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class AesCipherUtil(private val key: ByteArray, private val initVector: ByteArray) {

    init {
        if (key.size != 16 || initVector.size != 16) {
            throw IllegalArgumentException("Key and initialization vector should be 16 bytes long")
        }
    }

    fun encrypt(input: String): String {
        val aesCipher = initAesCipher(Cipher.ENCRYPT_MODE)
        return Base64.encodeToString(aesCipher.doFinal(input.toByteArray()), Base64.DEFAULT)
    }

    fun decrypt(input: String): String {
        val aesCipher = initAesCipher(Cipher.DECRYPT_MODE)
        return String(aesCipher.doFinal(Base64.decode(input, Base64.DEFAULT)))
    }

    @Throws(NoSuchAlgorithmException::class, NoSuchPaddingException::class)
    private fun initAesCipher(opMode: Int): Cipher {

        val cipher = Cipher.getInstance(CIPHER_MODE)
        val secretKeySpec = SecretKeySpec(key, ALGORITHM)
        cipher.init(opMode, secretKeySpec, IvParameterSpec(initVector))
        return cipher
    }

    companion object {

        private const val CIPHER_MODE = "AES/CBC/PKCS5Padding"
        private const val ALGORITHM = "AES"
    }
}
