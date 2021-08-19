package com.wanmi.ms.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.Ordered;

/**
 * Xss Filter配置
 * Created by aqlu on 15/11/20.
 */
@ConfigurationProperties(prefix = "xss-filter")
@Data
public class XssFilterProperties {
    private static final String[] DEFAULT_URL_MAPPINGS = { "/*" };

    private boolean enable = true;

    /**
     * 对路径参数过滤,如/member/A36289/info,A36289就是路径参数
     */
    private boolean stripPath = true;

    /**
     * 需要忽略的请求参数名，多个参数用“,”分隔
     */
    private String excludeFieldsName;

    /**
     * 富文本参数过滤，多个参数用“,”分隔，富文本参数值大小不做限制
     */
    private String RTFName;

    /**
     *  请求参数名大小限制，默认150个字符长度
     */
    private int paramNameSize = 150;

    /**
     * 请求参数值大小限制，默认2000个字符
     */
    private int paramValueSize = 2000;

    /**
     * 过滤器路径
     */
    private String[] urlPatterns = DEFAULT_URL_MAPPINGS;

    /**
     * 过滤器加载顺序，数字越小优先级越高，可以是负数；
     */
    private int order = Ordered.HIGHEST_PRECEDENCE;

    /**
     * 是否需要对二进制方式提交的流进行过滤，默认为false，设为true时有效
     */
    private boolean stripJsonStream = false;

    /**
     * 解析二进制流的编码格式，默认为UTF-8
     */
    private String streamCharset = "UTF-8";
}
