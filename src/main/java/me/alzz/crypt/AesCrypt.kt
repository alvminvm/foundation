package me.alzz.crypt

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

class AesCrypt {
    companion object {
        //加密
        fun encrypt(input: String, pwd: String): String {
            val cipher = Cipher.getInstance("AES")
            val keySpec = SecretKeySpec(pwd.toByteArray(), "AES")
            cipher.init(Cipher.ENCRYPT_MODE, keySpec)
            //加密、解密
            val encrypt = cipher.doFinal(input.toByteArray())
            return Base64.encode(encrypt, Base64.DEFAULT).toString(Charsets.UTF_8)
        }

        //解密
        @JvmStatic
        fun decrypt(input: String, pwd: String): String {
            val cipher = Cipher.getInstance("AES")
            val keySpec = SecretKeySpec(pwd.toByteArray(), "AES")
            cipher.init(Cipher.DECRYPT_MODE, keySpec)
            //加密、解密
            val encrypt = cipher.doFinal(Base64.decode(input, Base64.DEFAULT))
            return encrypt.toString(Charsets.UTF_8)
        }
    }
}

