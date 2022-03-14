package com.ofpay.rex.control.helper;

/**
 * Created by Miner on 2016/3/7.
 */
public class SafeControlUtil {
    public static byte[] addByteArrays(byte[] first, byte[] second)
    {
        byte[] result = new byte[first.length + second.length];

        System.arraycopy(first, 0, result, 0, first.length);
        System.arraycopy(second, 0, result, first.length, second.length);

        return result;
    }
}
