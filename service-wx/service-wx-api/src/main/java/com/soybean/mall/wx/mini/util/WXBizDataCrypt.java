package com.soybean.mall.wx.mini.util;

import com.alibaba.fastjson.JSON;
import com.soybean.mall.wx.mini.user.bean.response.WxBizDataResp;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/8/3 2:28 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Slf4j
public class WXBizDataCrypt {

//    private String appId;

    private String sessionKey;

    public WXBizDataCrypt(String sessionKey) {
//        this.appId = appId;
        this.sessionKey = sessionKey;
    }

    /**
     * 1.对称解密使用的算法为 WXBizDataCrypt-128-CBC，数据采用PKCS#7填充。
     * 2.对称解密的目标密文为 Base64_Decode(encryptedData)。
     * 3.对称解密秘钥 aeskey = Base64_Decode(session_key), aeskey 是16字节。
     * 4.对称解密算法初始向量 为Base64_Decode(iv)，其中iv由数据接口返回。
     * @param
     * @throws Exception
     */
    public WxBizDataResp decrypt(String encryptedData, String iv) {
        WxBizDataResp wxBizDataResp = null;
        try {
            /**
             * 小程序加密数据解密算法
             * https://developers.weixin.qq.com/miniprogram/dev/api/signature.html#wxchecksessionobject
             * 1.对称解密的目标密文为 Base64_Decode(encryptedData)。
             * 2.对称解密秘钥 aeskey = Base64_Decode(session_key), aeskey 是16字节。
             * 3.对称解密算法初始向量 为Base64_Decode(iv)，其中iv由数据接口返回。
             */
            byte[] encryptedByte = Base64.getDecoder().decode(encryptedData);
            byte[] sessionKeyByte = Base64.getDecoder().decode(this.sessionKey);
            byte[] ivByte = Base64.getDecoder().decode(iv);
            /**
             * 以下为AES-128-CBC解密算法
             */
            SecretKeySpec skeySpec = new SecretKeySpec(sessionKeyByte, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(ivByte);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivParameterSpec);
            byte[] original = cipher.doFinal(encryptedByte);
            String jsonStr = new String(original);
            log.info("WxBizDataCrypt decrypt jsonStr:{}", jsonStr);
            wxBizDataResp = JSON.parseObject(jsonStr, WxBizDataResp.class);
            log.info("WxBizDataCrypt decrypt Obj :{}" JSON.toJSONString(wxBizDataResp));
        } catch (Exception ex) {
            log.error("WXBizDataCrypt ", ex);
        }
        return wxBizDataResp;
    }
}
