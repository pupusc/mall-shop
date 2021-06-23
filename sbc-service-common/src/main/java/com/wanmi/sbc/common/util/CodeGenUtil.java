package com.wanmi.sbc.common.util;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by syq on 2016/12/9.
 */
public class CodeGenUtil {

    /**
     * 自定义进制(0,1没有加入,容易与o,l混淆)
     */
    private static final char[] r = new char[]
            {'6', 'b', 'g', 'a', '3', 'm', 'j', 's', '2', 'd', 'z', '7', 'p', '5', 'x', '9',
                    'c', 'i', 'k', 'u', 'f', 'r', '4', 'v', 'q', 'w', 'e', '8', 'y', 'l', 't', 'n', 'h'
            };
    /**
     * (不能与自定义进制有重复)
     */
    private static final char b = 'o';
    /**
     * 进制长度
     */
    private static final int binLen = r.length;
    /**
     * 序列最小长度
     */
    private static final int s = 10;

    /**
     * 根据ID生成多少位随机码
     *
     * @return 随机码
     */
    public static String toSerialCode(long id) {
        return toSerialCode(id, s);
    }

    public static String toSerialCode(long id, int s) {
        char[] buf = new char[32];
        int charPos = 32;
        while ((id / binLen) > 0) {
            int ind = (int) (id % binLen);
            buf[--charPos] = r[ind];
            id /= binLen;
        }
        buf[--charPos] = r[(int) (id % binLen)];
        String str = new String(buf, charPos, (32 - charPos));
        // 不够长度的自动随机补全
        if (str.length() < s) {
            StringBuilder sb = new StringBuilder();
            ThreadLocalRandom rnd = ThreadLocalRandom.current();
            for (int i = 0; i < s - str.length(); i++) {
                sb.append(r[rnd.nextInt(binLen)]);
            }
            str += sb.toString();
        }
        return str;
    }


}
