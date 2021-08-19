package com.wanmi.sbc.linkedmall.signature;

import com.wanmi.sbc.linkedmall.api.request.signature.SignatureVerifyRequest;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

@Service
public class SignatureService {

    @Value("${linkedmall.publicKey}")
    private String publicKey;
    @Value("${linkedmall.signatureAlgorithm}")
    private String signatureAlgorithm;
    @Value("${linkedmall.keyAlgorithm}")
    private String keyAlgorithm;
    public boolean verify(SignatureVerifyRequest request){
        return verifyOfficial(request.getOriginalString(), request.getSignature(), publicKey);
    }
    /**
     * 拼装linkedmall签名时用的字符串
     * @param object
     * @return
     */
    private String originalString(Object object){
        Class<?> aClass = object.getClass();
        Map<String, Object> data = new HashMap<>();
        Field[] declaredFields = aClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            String name = declaredField.getName();
            Object value=null;
            declaredField.setAccessible(true);
//            以下不参与验签
            if (name!="signature"&&name!="signatureMethod"&&name!="serialVersionUID"){
                try {
                    value = declaredField.get(object);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            if (value!=null){
                data.put(name,value);
            }
        }
        String dataToBeSigned = "";
        List arrayList = new ArrayList(data.entrySet());
        Collections.sort(arrayList, new Comparator()
        {
            public int compare(Object arg1, Object arg2)
            {
                Map.Entry obj1 = (Map.Entry) arg1;
                Map.Entry obj2 = (Map.Entry) arg2;
                return (obj1.getKey()).toString().compareTo(obj2.getKey().toString());
            }
        });
        for (Iterator iter = arrayList.iterator(); iter.hasNext();)
        {
            Map.Entry entry = (Map.Entry)iter.next();
            dataToBeSigned = dataToBeSigned + (dataToBeSigned.equals("") ? "" : "&")
                    + entry.getKey() + "=" + entry.getValue();
        }
        return dataToBeSigned;
    }
    /**
     * 官方验签方法
     *
     * @param sign      签名结果
     * @param publicKey 公钥
     * @return 验签结果
     */
    private boolean verifyOfficial(String text, String sign, String publicKey) {
        try {
            Signature signature = Signature.getInstance(signatureAlgorithm);
            PublicKey key = KeyFactory.getInstance(keyAlgorithm).generatePublic(new X509EncodedKeySpec(Base64.decodeBase64(publicKey)));
            signature.initVerify(key);
            signature.update(text.getBytes());
            return signature.verify(Base64.decodeBase64(sign));
        } catch (Exception e) {
            System.out.println("验签失败");
        }
        return false;
    }
}
