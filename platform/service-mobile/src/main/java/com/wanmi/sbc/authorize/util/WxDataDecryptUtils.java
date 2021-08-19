package com.wanmi.sbc.authorize.util;

import com.alibaba.fastjson.JSONObject;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.AlgorithmParameters;
import java.security.Key;
import java.security.Security;


/**
 * TODO
 * <p>Filename: com.qianmi.gcs.wx.purchase.infrastructure.support.utils.WxPhoneUtils.java</p>
 * <p>Date: 2018-09-19 18:32.</p>
 *
 * @author <a href="mailto:qiyekun@qianmi.com">of600-QYK</a>
 * @since v0.0.1
 */
@Slf4j
@UtilityClass
public class WxDataDecryptUtils {

    // 算法名
    private static final String KEY_NAME         = "AES";
    // 加解密算法/模式/填充方式
    // ECB模式只用密钥即可对数据进行加密解密，CBC模式需要添加一个iv
    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS7Padding";

    /**
     * 微信 数据解密<br/>
     * 对称解密使用的算法为 AES-128-CBC，数据采用PKCS#7填充<br/>
     * 对称解密的目标密文:encrypted=Base64_Decode(encryptData)<br/>
     * 对称解密秘钥:key = Base64_Decode(session_key),aeskey是16字节<br/>
     * 对称解密算法初始向量:iv = Base64_Decode(iv),同样是16字节<br/>
     *
     * @param encrypted   目标密文
     * @param session_key 会话ID
     * @param iv          加密算法的初始向量
     */
    private String wxDecrypt(String encrypted, String session_key, String iv) {
        String json = null;
        byte[] encrypted64 = Base64.decodeBase64(encrypted);
        byte[] key64 = Base64.decodeBase64(session_key);
        byte[] iv64 = Base64.decodeBase64(iv);
        try {
            init();
            json = new String(decrypt(encrypted64, key64, generateIV(iv64)));
        } catch (Exception e) {
            log.error("微信 数据解密 {}", e);
        }
        return json;
    }

    /**
     * 初始化密钥
     */
    private void init() throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        KeyGenerator.getInstance(KEY_NAME).init(128);
    }

    /**
     * 生成iv
     */
    private AlgorithmParameters generateIV(byte[] iv) throws Exception {
        // iv 为一个 16 字节的数组，这里采用和 iOS 端一样的构造方法，数据全为0
        // Arrays.fill(iv, (byte) 0x00);
        AlgorithmParameters params = AlgorithmParameters.getInstance(KEY_NAME);
        params.init(new IvParameterSpec(iv));
        return params;
    }

    /**
     * 生成解密
     */
    private byte[] decrypt(byte[] encryptedData, byte[] keyBytes, AlgorithmParameters iv) throws Exception {
        Key key = new SecretKeySpec(keyBytes, KEY_NAME);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        // 设置为解密模式
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        return cipher.doFinal(encryptedData);
    }

    /**
     * 获取微信用户手机号
     * @param iv
     * @param sessionKey
     * @param encryptedData
     * @return
     */
    public String getPhoneNumber(String iv, String sessionKey, String encryptedData) {
        JSONObject jsonObject = JSONObject.parseObject(wxDecrypt(encryptedData, sessionKey, iv));
        return jsonObject.get("phoneNumber") != null ? (String) jsonObject.get("phoneNumber") : null;
    }

    /**
     * 获取微信用户基本信息
     * @param iv
     * @param sessionKey
     * @param encryptedData
     * @return
     */
    public JSONObject getUserBaseInfo(String iv, String sessionKey, String encryptedData) {
        JSONObject jsonObject = JSONObject.parseObject(wxDecrypt(encryptedData, sessionKey, iv));
        return jsonObject;
    }

}
