package com.ofpay.rex.control.helper;

import com.ofpay.rex.util.BouncyCastleProviderSingleton;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Security;
import java.security.spec.RSAPrivateKeySpec;


/**
 * 为安全控件替代方案解密使用
 */
public class RSAUtil {

    public static final String KEY_ALGORITHM = "RSA";
    private static final String PRIVATEEXPONENT = "10523781683513122571101210400181086521304619241816570771104330336919211169678312784821391590517384032191422670679085943616176637181048831940946278620177793";
    private static final String MODULE = "11812106393197340491227115141493566722272017524720967967012014202857975687667545096243675617321102597267196884759608111042453640352649613804023653142429767";

    /**
     * 私钥解密
     *
     * @return
     * @throws Exception
     */
    public static String decryptByPrivateKey(byte[] data)
            throws Exception {
        BigInteger bigIntModulus = new BigInteger(MODULE);
        BigInteger bigIntPrivateExponent = new BigInteger(PRIVATEEXPONENT);
        RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(bigIntModulus, bigIntPrivateExponent);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM, BouncyCastleProviderSingleton.getInstance());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);


        return new String(cipher.doFinal(data));
    }

}
