package com.wanmi.ms.log.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * RequestLogBean
 * Created by aqlu on 16/7/29.
 */
@Data
public class RequestLogBean {

    private static DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    public static String systemCurrentTime() {
        return df.format(LocalDateTime.now());
    }

    private String invokeTime = systemCurrentTime(); // 时间戳，日志记录的时间,精确到毫秒，采用服务器的本地时间。ex: 2012-5-4 11:13:34:234

    /**
     * 唯一编号
     */
    private String uuId;

    /**
     * 会话标识
     */
    private String sessionId;

    /**
     * 来源IP
     */
    private String fromIp;

    /**
     * 访问耗时
     */
    private Long cost;

    /**
     * 请求参数
     */
    private Param param;

    /**
     * 响应值
     */
    private Result result;

    /**
     * 实际请求uri。譬如:/user/001
     */
    private String uri;

    /**
     * 请求Mapping, 主要针对spring mvc。譬如: /user/{id}。方便统计归类
     */
    private String mapping;

    /**
     * 接口名称(通过@RequestMapping在每个Controller的handler方法上设置), 便于阅读和定位
     */
    private String mappingName;

    /**
     * 类名与方法名, 方便开发定义问题
     */
    private String clzMethod;

    /**
     * 站点所有者
     */
    private String siteOwner;

    /**
     * 业务附加信息
     */
    private Map<String, String> additions;

    private String pinpointTraceId; // pinpoint trace id

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Param {
        /**
         * Http头
         */
        private Map<String, String> header;

        /**
         * Query String
         */
        private String queryStr;

        /**
         * request.getInputStream()
         */
        private String payload;

        /**
         * request.getParameterMap()
         */
        private String paramMap;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Result {

        /**
         * 响应http状态吗
         */
        private int status;

        /**
         * 响应内容
         */
        private String content;

        /**
         * 异常信息, 只含message
         */
        private String exceptionMsg;
    }
}
