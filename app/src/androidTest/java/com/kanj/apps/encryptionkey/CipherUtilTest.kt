package com.kanj.apps.encryptionkey

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CipherUtilTest {

    @Test
    fun basicEncryptDecryptTest() {

        val cipherUtil = AesCipherUtil(KEY.toByteArray(), INIT_VECTOR.toByteArray())

        val encrypted = cipherUtil.encrypt(TEXT)
        assertEquals(TEXT, cipherUtil.decrypt(encrypted))
    }

    companion object {
        private const val KEY = "ZMTdRwAomIwMaXwY"
        private const val INIT_VECTOR = "A*b&c^d%E(f#g@h!"
        private const val TEXT = "igbjkhgi kjgjhg 76476 %#$^%$ YTRGVJH"
    }
}
