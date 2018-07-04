package com.chehubang.duolejie.utils;


import android.util.Base64;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


public class RsaTool {

    /**
     * 使用公钥对明文进行加密，返回BASE64编码的字符串
     *
     * @param publicKey
     * @param plainText
     * @return
     */
    public static String encrypt(String publicKey, String plainText) {
        log.d(plainText);
        try {
            byte[] dataBytes = plainText.getBytes("UTF-8");
            byte[] keyBytes = publicKey.getBytes("UTF-8"); //Base64.decode(publicKey.getBytes(),Base64.DEFAULT);
            Key k = new SecretKeySpec(keyBytes, "AES");
            Cipher ciper = Cipher.getInstance("AES/ECB/PKCS5Padding");
            ciper.init(Cipher.ENCRYPT_MODE, k);
            byte[] s = ciper.doFinal(dataBytes);
            return new String(Base64.encode(s, Base64.DEFAULT));//

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
}
