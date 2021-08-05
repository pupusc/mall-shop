package com.ofpay.rex.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 过滤器配置
 * 排除特定url(＊注意＊，排除的需要单独建立安全校验)
 * <p/>
 * <pre>
 * &lt;filter&gt;
 *     &lt;filter-name&gt;xssFilter&lt;/filter-name&gt;
 *     &lt;filter-class&gt;com.qianmi.security.filter.XssFilter&lt;/filter-class&gt;
 * &lt;/filter&gt;
 * </pre>
 *
 * @author of546
 */
public class XssFilter extends OncePerRequestFilter {

    private Integer paramNameSize;

    private Integer paramValueSize;

    private String excludeFieldsName;

    private String RTFName;

    private boolean stripPath = false;

    private boolean stripJsonStream = false;

    private String streamCharset = "UTF-8";


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String[] efns = {};
        String[] rtfs = {};

        if (StringUtils.isNotBlank(excludeFieldsName)) {
            efns = StringUtils.split(excludeFieldsName, ',');
        }

        if (StringUtils.isNotBlank(RTFName)) {
            rtfs = StringUtils.split(RTFName, ',');
        }

        filterChain.doFilter(new XssHttpServletRequestWrapper(request, efns, rtfs, paramNameSize, paramValueSize, stripPath, stripJsonStream,streamCharset), response);
    }

    @Override
    public void destroy() {
        this.excludeFieldsName = null;
        this.RTFName = null;
    }

    public void setRTFName(String RTFName) {
        this.RTFName = RTFName;
    }

    public void setParamValueSize(Integer paramValueSize) {
        this.paramValueSize = paramValueSize;
    }

    public void setParamNameSize(Integer paramNameSize) {
        this.paramNameSize = paramNameSize;
    }

    public void setExcludeFieldsName(String excludeFieldsName) {
        this.excludeFieldsName = excludeFieldsName;
    }

    public void setStripPath(boolean stripPath) {
        this.stripPath = stripPath;
    }

    public void setStripJsonStream(boolean stripJsonStream) {
        this.stripJsonStream = stripJsonStream;
    }

    public void setStreamCharset(String streamCharset) {
        this.streamCharset = streamCharset;
    }



}
