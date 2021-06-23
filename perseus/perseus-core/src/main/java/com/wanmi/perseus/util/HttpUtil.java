package com.wanmi.perseus.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Http相关帮助类
 *
 * @author lihe 2013-7-4 下午5:30:05
 * @see
 */
@Slf4j
public final class HttpUtil {

    private static final String UNKNOWN = "unknown";

    public static final String LOCAL_ADDRESS = "127.0.0.1";

    private HttpUtil(){}

    /**
     * 获取当前HTTP请求对象
     *
     * @return
     */
    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    /**
     * 获取当前HTTP请求对象
     *
     * @return
     */
    public static HttpServletResponse getResponse() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }

    /**
     * 获取当前Scheme（含端口号）
     * 如http://127.0.0.1:80
     *
     * @return
     */
    public static String getBasePath() {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return StringUtils.EMPTY;
        }
        return MessageFormat.format("{0}://{1}:{2}", request.getScheme(), request.getServerName(), String.valueOf(request.getServerPort()));
    }

    /**
     * 获取当前项目名
     *
     * @return
     */
    public static String getProjectName() {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return StringUtils.EMPTY;
        }
        return request.getContextPath();
    }

    /**
     * 获取当前项目路径
     *
     * @return
     */
    public static String getProjectRealPath() {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return StringUtils.EMPTY;
        }
        return request.getSession().getServletContext().getRealPath("/");
    }

    /**
     * 获取客户端ip
     *
     * @return
     */
    public static String getIpAddr() {
        HttpServletRequest request = getRequest();

        String ip = request.getHeader("X-Real-IP");
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("x-forwarded-for");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("http_client_ip");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        // 如果是多级代理，那么取第一个ip为客户ip
        if (ip != null && ip.indexOf(',') != -1) {
            ip = ip.substring(ip.lastIndexOf(',') + 1, ip.length()).trim();
        }
        return ip;
    }


    /**
     * @param ip           目标ip,一般在局域网内
     * @param sourceString 命令处理的结果字符串
     * @param macSeparator mac分隔符号
     * @return mac地址，用上面的分隔符号表示
     */
    public static String filterMacAddress(final String ip, final String sourceString, final String macSeparator) {
        String result = "";
        String regExp = "((([0-9,A-F,a-f]{1,2}" + macSeparator + "){1,5})[0-9,A-F,a-f]{1,2})";
        Pattern pattern = Pattern.compile(regExp);
        Matcher matcher = pattern.matcher(sourceString);
        while (matcher.find()) {
            result = matcher.group(1);
            if (sourceString.indexOf(ip) <= sourceString.lastIndexOf(matcher.group(1))) {
                break; //如果有多个IP,只匹配本IP对应的Mac.
            }
        }
        return result;
    }

}
