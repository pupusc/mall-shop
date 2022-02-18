package com.wanmi.sbc.common.util;

import jodd.util.StringUtil;
import org.apache.commons.lang3.StringUtils;

import java.security.MessageDigest;

public class ShaUtil {

    private static final String[] hexDigits = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    public ShaUtil() {
    }

    public static String encryptSHA1(String data, String salt) throws Exception {
        if (StringUtils.isEmpty(data)) {
            return "";
        } else {
            MessageDigest sha = MessageDigest.getInstance("SHA");
            if (StringUtil.isNotEmpty(salt)) {
                sha.update(salt.getBytes());
            }

            byte[] bytes = sha.digest(data.getBytes());
            return byteArrayToHexString(bytes);
        }
    }

    public static String encryptSHA1(String str) {

        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f' };
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes("UTF-8"));
            byte[] md = mdTemp.digest();
            int j = md.length;
            char[] buf = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf);
        } catch (Exception e) {
            return null;
        }
    }

    private static String byteToHexString(byte b) {
        int ret = b;
        if (b < 0) {
            ret = b + 256;
        }

        int m = ret / 16;
        int n = ret % 16;
        return hexDigits[m] + hexDigits[n];
    }

    private static String byteArrayToHexString(byte[] bytes) {
        StringBuffer sb = new StringBuffer();

        for(int i = 0; i < bytes.length; ++i) {
            sb.append(byteToHexString(bytes[i]));
        }

        return sb.toString();
    }
}