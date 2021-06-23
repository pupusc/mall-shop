package com.wanmi.sbc.third.share;

import com.wanmi.sbc.third.share.response.TicketResponse;

import java.security.MessageDigest;
import java.util.Formatter;
import java.util.UUID;

/**
 * 微信公众平台sign
 */
public class WeChatSign {
    public static void main(String[] args) {
        String jsapi_ticket = "kgt8ON7yVITDhtdwci0qefnTevXlu05myFDO8CY_vok9S-oaSZrHQfmhSMI8KFMTFN9G6yaCNlcRxaDqoMS7gQ";
        // 注意 URL 一定要动态获取，不能 hardcode
        String url = "http://pc.s2btest.kstore.shop/goods-detail/8ad0328d643ef12f0164402814d90023";
        TicketResponse response = sign(jsapi_ticket, url);
        System.out.println(response.toString());
    }


    public static TicketResponse sign(String ticket, String url) {
        TicketResponse response = new TicketResponse();
        String nonceStr = createNonceStr();
        String timestamp = createTimestamp();
        String string1;
        String signature = "";

        //注意这里参数名必须全部小写，且必须有序
        string1 = "jsapi_ticket=" + ticket +
                "&noncestr=" + nonceStr +
                "&timestamp=" + timestamp +
                "&url=" + url;
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(string1.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        } catch (Exception e) {
            throw new RuntimeException("微信签名异常："+string1,e);
        }

        response.setSignature(signature);
        response.setTimestamp(timestamp);
        response.setUrl(url);
        response.setNonceStr(nonceStr);
        return response;
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    private static String createNonceStr() {
        return UUID.randomUUID().toString();
    }

    private static String createTimestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }

}


