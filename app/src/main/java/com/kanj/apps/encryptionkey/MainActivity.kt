package com.kanj.apps.encryptionkey

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun getLayoutId() = R.layout.activity_main

    override fun handleOnCreate(savedInstanceState: Bundle?, extras: Bundle?) {

        bt_save.setOnClickListener { _ -> saveSecrets() }

        showSecrets()
    }

    private fun showSecrets() {

        val savedSecret = sharedPreferences.getString(KEY_SECRET, "") ?: ""
        tv_saved_text.text = savedSecret

        if (isEncryptionSetupDone() && savedSecret.isNotEmpty()) {
            val decryptedText = decrypt(savedSecret)
            tv_secret.text = decryptedText
        } else {
            tv_secret.text = ""
        }
    }

    private fun saveSecrets() {

        val input = et_secret_input.text.toString().trim()

        if (input.isEmpty()) {
            return
        }

        val encryptedText = encrypt(input)

        sharedPreferences.edit().putString(KEY_SECRET, encryptedText).commit()

        showSecrets()
    }

    companion object {

        const val KEY_SECRET = "SECRET_DATA"
    }
}
