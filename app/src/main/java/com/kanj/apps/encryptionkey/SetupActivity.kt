package com.kanj.apps.encryptionkey

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log

class SetupActivity : BaseActivity() {

    override fun getLayoutId() = R.layout.activity_setup

    override fun handleOnCreate(savedInstanceState: Bundle?, extras: Bundle?) {

        if (isEncryptionSetupDone()) {
            initializeAndProceed(getEncryptionKey(false))
            return
        }

        val encryptionKey = getEncryptionKey(true)
        encryptStuff(encryptionKey)
        setEncryptionSetupDone()
        initializeAndProceed(encryptionKey)
    }

    private fun initializeAndProceed(key: String) {

        initializeEncryptedStuff(key)

        Handler(mainLooper).postDelayed(
            {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            },
            SPLASH_DURATION
        )

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                val keyProvider: EncryptionKeyProvider = SecretKeyBasedKeyProvider()
                keyProvider.getEncryptionKeyAndInitVector(true).also {
                    Log.v("Kanj", "Secret key gave $it")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getEncryptionKey(creationRequired: Boolean): String {

        val keyProvider: EncryptionKeyProvider = KeyPairBasedKeyProvider(this)
        return keyProvider.getEncryptionKeyAndInitVector(creationRequired)
    }

    /**
     * Will need to encrypt stuff if it isn't already done
     * (when app is updated from an older version which doesn't use encryption)
     */
    private fun encryptStuff(key: String) {

    }

    /**
     * Put the encryption key so that whoever needs to decrypt can get it
     */
    private fun initializeEncryptedStuff(key: String) {
        EncryptionApp.initCipher(key)
    }

    companion object {
        const val SPLASH_DURATION = 2000L
    }
}
