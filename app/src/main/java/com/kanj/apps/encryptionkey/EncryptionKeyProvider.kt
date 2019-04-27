package com.kanj.apps.encryptionkey

interface EncryptionKeyProvider {

    /**
     * Return a ByteArray of size 32 or more.
     *
     * @param creationRequired true if encryption keys weren't created previously
     */
    fun getEncryptionKeyAndInitVector(creationRequired: Boolean): ByteArray
}
