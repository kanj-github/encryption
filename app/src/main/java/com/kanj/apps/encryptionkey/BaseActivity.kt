package com.kanj.apps.encryptionkey

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences

    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun handleOnCreate(savedInstanceState: Bundle?, extras: Bundle?)

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())

        sharedPreferences = getSharedPreferences(EncryptionApp.PREF_NAME, Context.MODE_PRIVATE)

        handleOnCreate(savedInstanceState, intent?.extras)
    }

    protected fun isEncryptionSetupDone() = sharedPreferences.getBoolean(EncryptionApp.ENCRYPTION_SETUP, false)

    protected fun setEncryptionSetupDone() =
        sharedPreferences.edit().putBoolean(EncryptionApp.ENCRYPTION_SETUP, true).commit()

    protected fun decrypt(text: String) = EncryptionApp.getCipherUtil().decrypt(text)

    protected fun encrypt(text: String) = EncryptionApp.getCipherUtil().encrypt(text)
}
