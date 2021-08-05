package com.ofpay.rex.security.rate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.ofpay.rex.util.IPUtil;

/**
 * userkey生成器帮助类
 * User: Administrator
 * Date: 15-3-5
 * Time: 上午9:50
 */
public class UserKeyGenerate {


    /**
     * sessionid
     *
     * @param session
     * @return
     */
    public static String getSessionIdKey(HttpSession session) {
        return session.getId();
    }

    /**
     * ip
     *
     * @param request
     * @return
     */
    public static String getIPKey(HttpServletRequest request) {
        return IPUtil.getIp(request);
    }
}
