package com.ofpay.rex.control;


import com.ofpay.rex.control.helper.Base64;
import com.ofpay.rex.control.helper.Rijndael;

import java.util.UUID;

/**
 * Created by chengyong on 14/12/12.
 * 为新安全控件提供解密功能
 */
public class NewSafeControlHelper {

    private static final String KEY = "1A686B1A0BDF4f4d8FD0B68C20E60976";

    /**
     * 新控件加密
     *
     * @param randomKey 随机因子
     * @param data      明文数据
     * @return 密文 or null
     */
    public static String getSecretText(String randomKey, String data) {
        try {
            Base64 base64 = new Base64();
            if (data.contains(" ")) {
                data = data.replaceAll(" ", "+");
            }
            byte[] cypherArray = data.getBytes();
            Rijndael aes256 = new Rijndael();
            aes256.makeKey(KEY.getBytes(), KEY.length() * 8);
            byte[] tmp = aes256.decryptArrayNP(randomKey.getBytes(), 0);
            byte[] realKey = new byte[32];
            System.arraycopy(base64.encode(tmp), 0, realKey, 0, 32);
            aes256.makeKey(realKey, realKey.length * 8);
            byte[] plainArray = aes256.encryptArrayNP(cypherArray, 0);
            return new Base64().encodeToString(plainArray).trim();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 用于页面前端加密
     *
     * @param randomKey 随机因子
     * @param data      明文数据
     * @return 密文 or null
     */
    public static String getCipher(String randomKey, String data) {
        try {
            Base64 base64 = new Base64();
            if (data.contains(" ")) {
                data = data.replaceAll(" ", "+");
            }
            byte[] cypherArray = data.getBytes();
            Rijndael aes256 = new Rijndael();
            aes256.makeKey(KEY.getBytes(), KEY.length() * 8);
            byte[] tmp = aes256.decryptArrayNP(randomKey.getBytes(), 0);
            byte[] realKey = new byte[32];
            System.arraycopy(base64.encode(tmp), 0, realKey, 0, 32);
            aes256.makeKey(realKey, realKey.length * 8);
            byte[] plainArray = aes256.encryptArrayNP(cypherArray, 0);
            return new Base64().encodeToString(plainArray).trim();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


    /**
     * 新控件解密
     *
     * @param randomKey 随机因子
     * @param secret    密文
     * @return 明文 or null
     */
    public static String getPlaintext(String randomKey, String secret) {
        try {
            if(randomKey == null || randomKey.length() != 32 || secret == null || secret.length() <=1 ) return null;
            Base64 base64 = new Base64();
            if (secret.contains(" ")) {
                secret = secret.replaceAll(" ", "+");
            }
            byte[] cypherArray = base64.decode(secret);
            Rijndael aes256 = new Rijndael();
            aes256.makeKey(KEY.getBytes(), KEY.length() * 8);
            byte[] tmp = aes256.decryptArrayNP(randomKey.getBytes(), 0);
            byte[] realKey = new byte[32];
            System.arraycopy(base64.encode(tmp), 0, realKey, 0, 32);
            aes256.makeKey(realKey, realKey.length * 8);
            byte[] plainArray = aes256.decryptArrayNP(cypherArray, 0);
            return new String(plainArray).trim();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * @return 32位随机字符串
     */
    public static String getRandomString() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }


}