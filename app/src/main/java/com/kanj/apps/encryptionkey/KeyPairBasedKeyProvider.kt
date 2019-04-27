package com.kanj.apps.encryptionkey

import android.content.Context
import android.os.Build
import android.security.KeyPairGeneratorSpec
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.math.BigInteger
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.Signature
import java.security.spec.AlgorithmParameterSpec
import java.util.*
import javax.security.auth.x500.X500Principal

class KeyPairBasedKeyProvider(private val context: Context) : EncryptionKeyProvider {

    /**
     * Generates a key pair if not already done. Signs a fixed text with it to be used as encryption key.
     */
    override fun getEncryptionKeyAndInitVector(creationRequired: Boolean): ByteArray {

        if (creationRequired) {
            generateKeyPair()
        }

        val ks: KeyStore = KeyStore.getInstance("AndroidKeyStore").apply {
            load(null)
        }

        val entry = ks.getEntry(KEY_ALIAS, null) as? KeyStore.PrivateKeyEntry
            ?: throw RuntimeException("Not an instance of a PrivateKeyEntry")

        val signature: ByteArray = Signature.getInstance("SHA256withRSA").run {
            initSign(entry.privateKey)
            update(TEXT_TO_SIGN.toByteArray())
            sign()
        }

        return signature.copyOfRange(0, 32)
    }

    /**
     * Generates key pair in key store.
     */
    private fun generateKeyPair() {

        val keyPairGenerator = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore")

        val start = GregorianCalendar()
        val end = GregorianCalendar()
        end.add(Calendar.YEAR, 2)

        val parameterSpec: AlgorithmParameterSpec = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            KeyPairGeneratorSpec.Builder(context)
                .setAlias(KEY_ALIAS)
                .setSubject(X500Principal("CN=${KEY_ALIAS}"))
                .setSerialNumber(BigInteger.valueOf(2019))
                .setStartDate(start.time)
                .setEndDate(end.time)
                .build()
        } else {
            KeyGenParameterSpec.Builder(KEY_ALIAS, KeyProperties.PURPOSE_SIGN)
                .setCertificateSubject(X500Principal("CN=${KEY_ALIAS}"))
                .setDigests(KeyProperties.DIGEST_SHA256)
                .setSignaturePaddings(KeyProperties.SIGNATURE_PADDING_RSA_PKCS1)
                .setCertificateSerialNumber(BigInteger.valueOf(2019))
                .build();
        }

        keyPairGenerator.apply {
            initialize(parameterSpec)
            generateKeyPair()
        }
    }

    companion object {
        const val TEXT_TO_SIGN = "10ToThePower24"
        const val KEY_ALIAS = "10ToThePower-19"
    }
}
