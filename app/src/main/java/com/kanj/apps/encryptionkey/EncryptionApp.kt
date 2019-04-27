package com.kanj.apps.encryptionkey

import android.app.Application

class EncryptionApp : Application() {

    companion object {

        const val PREF_NAME = "garbage"
        const val ENCRYPTION_SETUP = "ENCRYPTION_SETUP"

        private var cipherUtil: AesCipherUtil? = null

        fun initCipher(keyAndInitVector: ByteArray) {

            if (keyAndInitVector.size < 32) {
                throw IllegalArgumentException("Provide ByteArray with at least 32 bytes")
            }

            cipherUtil = AesCipherUtil(keyAndInitVector.copyOfRange(0, 16), keyAndInitVector.copyOfRange(16, 32))
        }

        fun getCipherUtil(): AesCipherUtil = cipherUtil ?: throw RuntimeException("Cipher not initialized")
    }
}
