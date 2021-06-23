package com.wanmi.sbc.customer.util;



import jodd.util.StringUtil;
import org.apache.commons.lang3.StringUtils;

import java.security.MessageDigest;

public class SHAUtils {
    private static final String[] hexDigits = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    public SHAUtils() {
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