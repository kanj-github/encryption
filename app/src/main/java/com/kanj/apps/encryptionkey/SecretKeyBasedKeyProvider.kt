package com.kanj.apps.encryptionkey

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Log
import androidx.annotation.RequiresApi
import java.math.BigInteger
import java.security.KeyStore
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.security.auth.x500.X500Principal

class SecretKeyBasedKeyProvider : EncryptionKeyProvider {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun getEncryptionKeyAndInitVector(creationRequired: Boolean): ByteArray {

        if (creationRequired) {
            return generateSecretKey()
        }

        return readSecretKeyFromStore()
    }

    /**
     * Generates a secret key in key store
     *
     * @return encoded secret key
     */
    @RequiresApi(Build.VERSION_CODES.M)
    private fun generateSecretKey(): ByteArray {

        val keyGenerator = KeyGenerator.getInstance("AES", "AndroidKeyStore")

        val parameterSpec = KeyGenParameterSpec.Builder(
            KEY_ALIAS,
            KeyProperties.PURPOSE_SIGN or KeyProperties.PURPOSE_VERIFY
        )
            .setCertificateSubject(X500Principal("CN=$KEY_ALIAS"))
            .setDigests(KeyProperties.DIGEST_SHA256)
            .setCertificateSerialNumber(BigInteger.valueOf(2019))
            .build();

        val secretKey = keyGenerator.run {
            init(parameterSpec)
            generateKey()
        }.also {
            Log.v("Kanj", "secret algo ${it.algorithm}, format ${it.format}, bytes ${it.encoded}")
        }

        return secretKey.encoded
    }

    private fun readSecretKeyFromStore(): ByteArray {

        val secretKey = KeyStore.getInstance("AndroidKeyStore").run {
            load(null)
            getEntry(KEY_ALIAS, KeyStore.PasswordProtection(CHAR_ARRAY))
        } as SecretKey

        return secretKey.encoded
    }

    companion object {
        val CHAR_ARRAY = arrayOf<Char>().toCharArray()
        const val KEY_ALIAS = "10ToThePower10"
    }
}
