package com.wanmi.sbc.common.util;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import java.io.*;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * \* Author: zgl
 * \* Date: 2020-3-25
 * \* Time: 14:20
 * \* Description:
 * \
 */
public class RSUtils {
    //非对称密钥算法
    public static final String KEY_ALGORITHM="RSA";


    /**
     * 密钥长度，DH算法的默认密钥长度是1024
     * 密钥长度必须是64的倍数，在512到65536位之间
     * */
    private static final int KEY_SIZE=512;

    private static String PUBLIC_KEY = null;
    private static String PRIVATE_KEY = null;

    /**
     * 初始化密钥对
     * @return Map 甲方密钥的Map
     * */
    public static void initKey() throws Exception{
        //实例化密钥生成器
        KeyPairGenerator keyPairGenerator=KeyPairGenerator.getInstance(KEY_ALGORITHM);
        //初始化密钥生成器
        keyPairGenerator.initialize(KEY_SIZE);
        //生成密钥对
        KeyPair keyPair=keyPairGenerator.generateKeyPair();
        //甲方公钥
        PUBLIC_KEY = Base64.encodeBase64String(keyPair.getPublic().getEncoded());
        //甲方私钥
       PRIVATE_KEY = Base64.encodeBase64String( keyPair.getPrivate().getEncoded());
        System.out.println("公钥："+PUBLIC_KEY);
        System.out.println("私钥："+PRIVATE_KEY);

    }



    /**
     * 私钥加密
     * @param data 待加密数据
     * @param key 密钥
     * @return byte[] 加密数据
     * */
    public static byte[] encryptByPrivateKey(byte[] data,String key) throws Exception{

        PrivateKey privateKey=getPrivateKey(key);
        //数据加密
        Cipher cipher=Cipher.getInstance(privateKey.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }


    /**
     * 公钥加密
     * @param data 待加密数据
     * @param key 密钥
     * @return byte[] 加密数据
     * */
    public static byte[] encryptByPublicKey(byte[] data,String key) throws Exception{


        PublicKey pubKey=getPublicKey(key);

        //数据加密
        Cipher cipher=Cipher.getInstance(pubKey.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        return cipher.doFinal(data);
    }
    /**
     * 私钥解密
     * @param data 待解密数据
     * @param key 密钥
     * @return byte[] 解密数据
     * */
    public static byte[] decryptByPrivateKey(byte[] data,String key) throws Exception{

        PrivateKey privateKey=getPrivateKey(key);
        //数据解密
        Cipher cipher=Cipher.getInstance(privateKey.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }
    /**
     * 公钥解密
     * @param data 待解密数据
     * @param key 密钥
     * @return byte[] 解密数据
     * */
    public static byte[] decryptByPublicKey(byte[] data,String key) throws Exception{


        PublicKey pubKey=getPublicKey(key);
        //数据解密
        Cipher cipher=Cipher.getInstance(pubKey.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, pubKey);
        cipher.update(data);
        return cipher.doFinal();
    }
    /**
     * 取得私钥
     * @param key 密钥
     * @return byte[] 私钥
     * */
    public static PrivateKey getPrivateKey(String key) throws Exception {
        //取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec=new PKCS8EncodedKeySpec(Base64.decodeBase64(key));
        KeyFactory keyFactory=KeyFactory.getInstance(KEY_ALGORITHM);
        //生成私钥
        return keyFactory.generatePrivate(pkcs8KeySpec);
    }
    /**
     * 取得公钥
     * @param key 密钥
     * @return byte[] 公钥
     * */
    public static PublicKey getPublicKey(String key) throws Exception{
        //实例化密钥工厂
        KeyFactory keyFactory=KeyFactory.getInstance(KEY_ALGORITHM);
        //初始化公钥
        //密钥材料转换
        X509EncodedKeySpec x509KeySpec=new X509EncodedKeySpec(Base64.decodeBase64(key));
        //产生公钥
        return keyFactory.generatePublic(x509KeySpec);
    }


}
