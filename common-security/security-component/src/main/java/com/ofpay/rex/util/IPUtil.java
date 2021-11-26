package com.ofpay.rex.util;

import java.util.StringTokenizer;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: Administrator
 * Date: 15-3-2
 * Time: 下午1:10
 */
public class IPUtil {


    /**
     * 获取客户端ip
     */
    public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP");

        if (!checkIP(ip)) {
            ip = request.getHeader("REMOTE-HOST");
        }

        if (!checkIP(ip)) {
            ip = request.getHeader("x-forwarded-for");
        }
        if (!checkIP(ip)) {
            ip = request.getHeader("Proxy-Client-IP");//只在 Apache（Weblogic Plug-In Enable）+WebLogic 搭配下出现
        }
        if (!checkIP(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");//只在 Apache（Weblogic Plug-In Enable）+WebLogic 搭配下出现
        }
        if (!checkIP(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (!checkIP(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (!checkIP(ip)) {
            ip = request.getRemoteAddr();
        }

        // 多级反向代理
        if (null != ip && !"".equals(ip.trim())) {
            StringTokenizer st = new StringTokenizer(ip, ",");
            if (st.countTokens() > 1) {
                return st.nextToken();
            }
        }

        return ip;
    }

    /**
     * 获取客户端xip
     */
    public static String getXip(HttpServletRequest request) {
        //clientip,proxy1ip,proxy2ip...
        String xip = request.getHeader("x-forwarded-for");
        if (StringUtils.isBlank(xip)) {
            xip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isBlank(xip)) {
            xip = "";
        }

        return xip;
    }


    /**
     * 获取客户端ip
     * 返回ip数组,[ip,xip]
     */
    public static String[] getIps(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP");

        if (!checkIP(ip)) {
            ip = request.getHeader("REMOTE-HOST");
        }

        if (!checkIP(ip)) {
            ip = request.getHeader("x-forwarded-for");
        }
        if (!checkIP(ip)) {
            ip = request.getHeader("Proxy-Client-IP");//只在 Apache（Weblogic Plug-In Enable）+WebLogic 搭配下出现
        }
        if (!checkIP(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");//只在 Apache（Weblogic Plug-In Enable）+WebLogic 搭配下出现
        }
        if (!checkIP(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (!checkIP(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (!checkIP(ip)) {
            ip = request.getRemoteAddr();
        }

        // 多级反向代理
        if (null != ip && !"".equals(ip.trim())) {
            StringTokenizer st = new StringTokenizer(ip, ",");
            if (st.countTokens() > 1) {
                ip = st.nextToken();
            }
        }

        //clientip,proxy1ip,proxy2ip...
        String xip = request.getHeader("x-forwarded-for");
        if (StringUtils.isBlank(xip)) {
            xip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isBlank(xip)) {
            xip = "";
        }

        return new String[]{ip, xip};
    }

    private static boolean checkIP(String ip) {
        if (StringUtils.isNotBlank(ip) && ip.split("\\.").length == 4) {
            return true;
        }
        return false;
    }
}
