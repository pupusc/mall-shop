package com.wanmi.ms.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.Ordered;

/**
 * Request日志配置
 * Created by aqlu on 15/11/20.
 */
@ConfigurationProperties(prefix = "request.log")
@Data
public class RequestLogProperties {
    private static final String DEFAULT_PASSWORD_FIELD_REGEX =
            "((\\w+)Password|((\\w+)_)?password|password)((_|[A-Z])(\\w+)?)?|((\\w+)Pwd|((\\w+)_)?pwd|pwd)((_|[A-Z])(\\w+)?)?";

    private static final String[] DEFAULT_URL_MAPPINGS = { "/*" };

    private static final String DEFAULT_EXCLUDE_URL_MAPPINGS = "/_jiankong.jsp";

    /**
     * 是否启用Request日志
     */
    private boolean enable = false;

    /**
     * 是否记录请求参数；默认：true;
     */
    private boolean needParam = true;

    /**
     * 是否记录响应结果；默认：false;
     */
    private boolean needResult = false;

    /**
     * 最大返回结果长度，超过此长度记录时会被截取
     */
    private int maxResultLength = 512;

    /**
     * 最大请求参数体长度，超过此长度记录时会被截取
     */
    private int maxBodyLength = 512;

    /**
     * 过滤器路径, 多个可以采用英文半角逗号(",")分隔。单个Pattern的语法遵循Filter的规则。
     */
    private String[] urlPatterns = DEFAULT_URL_MAPPINGS;

    /**
     * 过滤器加载顺序，数字越小优先级越高，可以是负数；默认为第二优先级, 给EncodingFilter预留一个位置
     */
    private int order = Ordered.HIGHEST_PRECEDENCE + 1;

    /**
     * 不希望记录日志的URL Pattern, 如果有多个可以采用英文半角逗号(",")分隔; 单个Pattern语法支持AntPathMatcher匹配。
     */
    private String excludePatterns = DEFAULT_EXCLUDE_URL_MAPPINGS;

    /**
     * 不希望记录日志的Method, 如果有多个可以采用英文半角逗号(",")分隔;
     * Request Method: GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE
     */
    private String excludeMethods = "";

    /**
     * 记录方式; 0: 将日志记录在msg字段; 1: 将日志采用Marker记录; 默认值:0;
     */
    private int logType = 1;

    /**
     * 是否针对请求参数开启模糊化Password字段; 包括body与 parameterMap.
     */
    private boolean needBlurPasswordForParam = true;

    /**
     * 密码字段的匹配正则; 默认正则能够匹配字段: password、pwd、xxxPassword、xxxPwd、xxx_password、xxx_pwd、xxx_password_xxx、xxx_pwd_xxx (xxx可以为任意字符)
     */
    private String passwordFieldRegex = DEFAULT_PASSWORD_FIELD_REGEX;

    /**
     * 不希望记录日志的Header, 如果有多个可以采用英文半角逗号(",")分隔; 默认排除鉴权字段。
     */
    private String excludeHeaders = "Authorization";

    /**
     * 希望记录日志的Header, 如果有多个可以采用英文半角逗号(",")分隔; 配置此字段时，excludeHeaders将不生效
     */
    private String includeHeaders = "";

    /**
     * 从request获取siteOwner的Key
     */
    private String siteOwnerKey = "siteOwner";

    /**
     * 从request获取requestId的Key
     */
    private String requestIdKey = "reqId";

    /**
     * 从request获取sessionId的Key
     */
    private String sessionIdKey = "sessionId";
}
