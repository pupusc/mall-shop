package com.ofpay.rex.control;

import com.ofpay.rex.control.helper.RSAUtil;
import org.apache.commons.lang.StringUtils;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by chengyong on 14/12/12.
 */
public class HardControlHelper {


    /**
     * *******
     * logger
     */
    private static Logger logger = LoggerFactory.getLogger(HardControlHelper.class);


    /**
     * ************
     * 按照约定解密硬件信息
     *
     * @param source
     * @return
     */
    public static HardVO decodeHardInfo(String source) {
        HardVO ret = null;
        try {

            if (StringUtils.isNotBlank(source) && source.length() == 256) {
                String s1 = source.substring(0, 128);
                String s2 = source.substring(128);

                String v1 = RSAUtil.decryptByPrivateKey(Hex.decode(s1));
                String v2 = RSAUtil.decryptByPrivateKey(Hex.decode(s2));

                String[] a1 = v1.split(",");
                String[] a2 = v2.split(",");

                ret = new HardVO();

                if (a1.length == 3) {
                    ret.setBiosId(a1[0]);
                    ret.setCpuId(a1[1]);
                    ret.setDiskId(a1[2]);
                }

                if (a2.length == 3) {
                    ret.setMainboard(a2[0]);
                    ret.setMac(a2[1]);
                }
            }
        } catch (Exception e) {
            logger.error("decode {%s} fail : {%s}", source, e.getMessage());
            e.printStackTrace();
        }
        return ret;
    }
}