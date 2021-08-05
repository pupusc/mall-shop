package com.ofpay.rex.control;

import com.ofpay.rex.util.BouncyCastleProviderSingleton;
import org.apache.commons.lang.StringUtils;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.pkcs.RSAPrivateKeyStructure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.RSAPrivateKeySpec;
import javax.crypto.Cipher;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by chengyong on 14/12/12.
 */
public class SafeControlHelper {

    private static ThreadLocal<Cipher> ciphers = new ThreadLocal<Cipher>();

    private static final String ALGORITHM = "RSA";

    private static final String CHARCODE = "ISO8859-1";

    /**
     * *
     * 私钥路径key
     */
    private static final String PRIVATE_KEY = "SP_KEY";

    /**
     * 字符编码
     */
    private static final String ENCODING = "ISO8859-1";


    /**
     * *******
     * logger
     */
    private static Logger logger = LoggerFactory.getLogger(SafeControlHelper.class);


    /**
     * 私钥
     */
    private static RSAPrivateKey privateKey;


    static {
        String privateKeyPath = System.getProperty(PRIVATE_KEY);// 读取默认系统变量

        logger.info("****************  pKey path {}  *****************", privateKeyPath);

        if (null == privateKeyPath || "".equals(privateKeyPath)) {
            logger.error("pKey path is null");
        } else {


            try {

                logger.info("**************** begin read pKey file  *****************");

                InputStream is = new FileInputStream(privateKeyPath);
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String readLine = null;
                StringBuilder sb = new StringBuilder();
                while ((readLine = br.readLine()) != null) {
                    if (readLine.charAt(0) == '-') {
                        continue;
                    } else {
                        sb.append(readLine);
                    }
                }

                logger.info("**************** end read pKey file  *****************");


                String buffer = Base64.decode(sb.toString(), CHARCODE);
                RSAPrivateKeyStructure asn1PrivKey = new RSAPrivateKeyStructure(
                        (ASN1Sequence) ASN1Sequence.fromByteArray(buffer
                                .getBytes(CHARCODE)));
                RSAPrivateKeySpec rsaPrivKeySpec = new RSAPrivateKeySpec(
                        asn1PrivKey.getModulus(), asn1PrivKey.getPrivateExponent());
                KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);

                privateKey = (RSAPrivateKey) keyFactory
                        .generatePrivate(rsaPrivKeySpec);


            } catch (FileNotFoundException fe) {
                logger.error("pKey path \"{}\" not exist！！！", privateKeyPath);
                fe.printStackTrace();
            } catch (Exception e) {
                logger.error("pKey load error: {}", e.getMessage());
                e.printStackTrace();
            }
        }
    }


    /**
     * *************
     * 判断客户端是否使用64位浏览器
     *
     * @param req
     * @return
     */
    public static boolean isX64Browser(HttpServletRequest req) {
        String userAgent = req.getHeader("User-Agent");
        if (StringUtils.isNotBlank(userAgent) && userAgent.indexOf("x64") != -1) {
            return true;
        }
        return false;
    }


    /**
     * ************
     * 密文解码
     *
     * @param souce
     * @return
     */
    public static SafeControlVO decodeSafeInput(String souce) {
        SafeControlVO safeControlVO = null;
        try {
            String enString = Base64.decode(souce, ENCODING);// Base64解码

            Cipher cipher = ciphers.get();
            if (cipher == null) {
                cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding",
                        BouncyCastleProviderSingleton.getInstance());
                cipher.init(Cipher.DECRYPT_MODE, privateKey);
                ciphers.set(cipher);
            }

            byte[] pureString = cipher.doFinal(enString.getBytes(ENCODING));


            String[] strArray = new String(pureString, ENCODING).trim().split(",");

            if (strArray.length >= 3) {
                safeControlVO = new SafeControlVO();
                safeControlVO.setPassword(java.net.URLDecoder.decode(strArray[0],
                        ENCODING));
                safeControlVO.setCupid(strArray[1]);
                safeControlVO.setMac(strArray[2]);
            }
        } catch (UnsupportedEncodingException e) {
            logger.error("decode fail : {}", e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            logger.error("decode fail : {}", e.getMessage());
            e.printStackTrace();
        }
        return safeControlVO;
    }
}