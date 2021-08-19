package com.wanmi.ms.log;

import brave.ScopedSpan;
import brave.Span;
import brave.Tracing;
import brave.propagation.TraceContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.wanmi.ms.log.domain.RequestLogBean;
import com.wanmi.ms.util.HttpServletRequestCopier;
import com.wanmi.ms.util.HttpServletResponseCopier;
import com.wanmi.ms.util.UUIDGen;
import com.wanmi.ms.util.Utils;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.marker.Markers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;
import java.util.regex.Pattern;

/**
 * http请求日志记录
 * Created by aqlu on 15/10/13.
 */
@Slf4j
public class RequestLogFilter extends GenericFilterBean {
    public static final String DEFAULT_PASSWORD_FIELD_REGEX = "((\\w+)Password|((\\w+)_)?password|password)((_|[A-Z])(\\w+)?)?|((\\w+)Pwd|((\\w+)_)?pwd|pwd)((_|[A-Z])(\\w+)?)?";

    private static final Logger requestLog = LoggerFactory.getLogger("requestLog");

    private static final String SUB_SUFFIX = "...(source length is %s)";

    private static final String DEFAULT_SITE_OWNER_KEY = "siteOwner";

    private static final String DEFAULT_REQUEST_ID_KEY = "reqId";

    private static final String DEFAULT_SESSION_ID_KEY = "sessionId";

    private static final String SEPARATOR = ",";

    private static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 是否需要记录参数, 包括queryString、parameterMap、headers
     */
    private boolean needParam = true;

    /**
     * 是否需要记录响应结果
     */
    private boolean needResult = false;

    private int maxResultLength = 512;

    private int maxBodyLength = 512;

    private Pattern passwordPattern = Pattern.compile(DEFAULT_PASSWORD_FIELD_REGEX);

    /**
     * 不希望记录日志的URL Pattern, 如果有多个可以采用英文半角逗号(",")分隔; 单个Pattern语法支持AntPathMatcher匹配。
     */
    private String[] excludePatterns;

    /**
     * 不需要记录日志的请求method。譬如通常可以忽略的method: OPTIONS
     */
    private String[] excludeMethods;

    private AntPathMatcher pathMatcher = new AntPathMatcher();

    /**
     * 记录方式; 0: 将日志记录在msg字段; 1: 将日志采用Marker记录;
     */
    private int logType = 0;

    /**
     * 是否针对请求参数开启模糊化Password字段; 包括body与 parameterMap
     * 
     * <pre>
     *     注意: 开启后此项有一定的性能消耗, 具体的需要根参数体的字段数、嵌套产层次相关。
     * 测试使用如下格式1json体, 100000次耗时单核CPU耗时1500~1800ms, 大概耗时 1/55 ms/次;
     * 测试使用如下格式2json体, 100000次耗时单核CPU耗时3500~3800ms, 大概耗时 1/25 ms/次;
     * 格式1json:
     * {
     *   "password": {"l1_1": ["l1_1_1","l1_1_2"],"l1_2": {"l1_2_1": 121}},
     *   "l2": {
     *     "pwd": null,
     *     "l2_2": true,
     *     "l2_3": {"userPwd": "123456"}
     *   },
     *   "l3": ["sss",["bbbb",{"ssss": 123}]]
     * }
     * 格式2json:
     * {"password": {"l1_1": ["l1_1_1","l1_1_2"],"l1_2": {"l1_2_1": 121}},"l2": {"pwd": null,"l2_2": true,"l2_3": {"userPwd": "123456","xx": {"password": {"l1_1": ["l1_1_1","l1_1_2"],"l1_2": {"l1_2_1": 121}},"l2": {"pwd": null,"l2_2": true,"l2_3": {"userPwd": "123456"}},"l3": ["aaa",{"user_pwd": "aaaaaaa","bbb": {"password": {"l1_1": ["l1_1_1","l1_1_2"],"l1_2": {"l1_2_1": 121}},"l2": {"pwd": null,"l2_2": true,"l2_3": {"userPwd": "123456","2xxx": {"password": {"l1_1": ["l1_1_1","l1_1_2"],"l1_2": {"l1_2_1": 121}},"l2": {"pwd": null,"l2_2": true,"l2_3": {"userPwd": "123456"}},"l3": ["aaa",{"user_pwd": "aaaaaaa"}]}}},"l3": ["aaa",{"user_pwd": "aaaaaaa"}]}}]}}},"l3": ["aaa",{"user_pwd": "aaaaaaa"}]}
     * </pre>
     */
    private boolean needBlurPasswordForParam = true;

    /**
     * 不希望记录日志的Header, 如果有多个可以采用英文半角逗号(",")分隔;
     */
    private String[] excludeHeaders;

    /**
     * 希望记录日志的Header, 如果有多个可以采用英文半角逗号(",")分隔; 配置此字段时，excludeHeaders将不生效
     */
    private String[] includeHeaders;

    /**
     * 获取siteId的key
     */
    private String siteOwnerKey;

    /**
     * 获取request id的key
     */
    private String requestIdKey;

    /**
     * 获取session id的key
     */
    private String sessionIdKey;

    /**
     * 初始参数设置
     */
    @Override
    protected void initFilterBean() throws ServletException {
        super.initFilterBean();
        String needResultStr = this.getFilterConfig().getInitParameter("NEED_RESULT");
        needResult = Boolean.parseBoolean(needResultStr);

        String needParamStr = this.getFilterConfig().getInitParameter("NEED_PARAM");
        needParam = Boolean.parseBoolean(needParamStr);

        String maxResultLengthStr = this.getFilterConfig().getInitParameter("MAX_RESULT_LENGTH");
        maxResultLength = Integer.parseInt(maxResultLengthStr);

        String maxBodyLengthStr = this.getFilterConfig().getInitParameter("MAX_BODY_LENGTH");
        maxBodyLength = Integer.parseInt(maxBodyLengthStr);

        String logTypeStr = this.getFilterConfig().getInitParameter("LOG_TYPE");
        logType = Integer.parseInt(logTypeStr);

        String initExcludePatterns = this.getFilterConfig().getInitParameter("EXCLUDE_PATTERNS");
        if (Utils.hasText(initExcludePatterns)) {
            excludePatterns = initExcludePatterns.split(SEPARATOR);
        }

        String initExcludeMethods = this.getFilterConfig().getInitParameter("EXCLUDE_METHODS");
        if (Utils.hasText(initExcludeMethods)) {
            excludeMethods = initExcludeMethods.split(SEPARATOR);
        }

        String initExcludeHeaders = this.getFilterConfig().getInitParameter("EXCLUDE_HEADERS");
        if (Utils.hasText(initExcludeHeaders)) {
            excludeHeaders = initExcludeHeaders.split(SEPARATOR);
        }

        String initIncludeHeaders = this.getFilterConfig().getInitParameter("INCLUDE_HEADERS");
        if (Utils.hasText(initIncludeHeaders)) {
            includeHeaders = initIncludeHeaders.split(SEPARATOR);
        }

        String passwordFieldRegex = this.getFilterConfig().getInitParameter("PASSWORD_FIELD_REGEX");
        if (Utils.hasText(passwordFieldRegex)) {
            this.passwordPattern = Pattern.compile(passwordFieldRegex);
        }

        String needBlurPasswordForParamStr = this.getFilterConfig().getInitParameter("NEED_BLUR_PASSWORD_FOR_PARAM");
        needBlurPasswordForParam = Boolean.parseBoolean(needBlurPasswordForParamStr);

        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);

        String siteOwnerKeyParam = this.getFilterConfig().getInitParameter("SITE_OWNER_KEY");
        siteOwnerKey = Utils.hasText(siteOwnerKeyParam) ? siteOwnerKeyParam : DEFAULT_SITE_OWNER_KEY;

        String requestIdKeyParam = this.getFilterConfig().getInitParameter("REQUEST_ID_KEY");
        requestIdKey = Utils.hasText(requestIdKeyParam) ? requestIdKeyParam : DEFAULT_REQUEST_ID_KEY;

        String sessionIdKeyParam = this.getFilterConfig().getInitParameter("SESSION_ID_KEY");
        sessionIdKey = Utils.hasText(sessionIdKeyParam) ? sessionIdKeyParam : DEFAULT_SESSION_ID_KEY;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;

            String requestUri = httpRequest.getRequestURI();
            String method = httpRequest.getMethod();

            if (isExcludePath(requestUri) || isExcludeMethod(method)) {
                // 如果是排除的URI或排除的Method则不记录日志, 直接放行
                chain.doFilter(request, response);
            } else {
                RequestLogBean logBean = new RequestLogBean();

                // 非排除的uri则记录日志
                long startTimeMills = System.currentTimeMillis();

                if (response.getCharacterEncoding() == null) {
                    response.setCharacterEncoding("UTF-8"); // Or whatever default. UTF-8 is good for World Domination.
                }

                String uuid = Utils.hasText(request.getParameter(requestIdKey)) ? request.getParameter(requestIdKey)
                        : httpRequest.getHeader(requestIdKey); // reqId
                uuid = Utils.hasText(uuid) ? uuid : UUIDGen.systemUuid();

                String sessionId = Utils.hasText(request.getParameter(sessionIdKey))
                        ? request.getParameter(sessionIdKey)
                        : httpRequest.getHeader(sessionIdKey); // sessionId
                sessionId = Utils.hasText(sessionId) ? sessionId : httpRequest.getSession().getId(); // sessionId

                // Dubbo log 摆渡
                IntfLogThreadFerry.putUUID(uuid);
                IntfLogThreadFerry.putSessionId(sessionId);

                HttpServletResponseCopier responseCopier = null;
                HttpServletRequestCopier requestCopier = null;
                try {
                    responseCopier = new HttpServletResponseCopier((HttpServletResponse) response);
                    requestCopier = new HttpServletRequestCopier((HttpServletRequest) request);
                    chain.doFilter(requestCopier, responseCopier);
                } finally {
                    try {
                        // 在方法执行完成后再收集日志信息, 缩短logMap生命周期, 以提高性能
                        // 在日志中先打印"costTime", 方便查看
                        logBean.setUuId(uuid); // uuid
                        logBean.setCost(System.currentTimeMillis() - startTimeMills); // 耗时
                        logBean.setUri(String.format("%s %s", method, requestUri)); // uri
                        logBean.setFromIp(Utils.getRealRemoteIp(httpRequest)); // fromIp
                        logBean.setSessionId(sessionId); // sessionId
                        logBean.setMapping(MDC.get("reqLog_mapping")); // see RequestLogAspect
                        logBean.setMappingName(MDC.get("reqLog_mappingName")); // see RequestLogAspect
                        logBean.setClzMethod(MDC.get("reqLog_clzMethod")); // see RequestLogAspect
                        String exMsg = MDC.get("reqLog_exMsg"); // see RequestLogAspect
                        MDC.remove("reqLog_mapping"); // 清除mapping
                        MDC.remove("reqLog_clzMethod"); // 清除clzMethod
                        MDC.remove("reqLog_exMsg"); // 清除exMsg
                        MDC.remove("reqLog_mappingName"); // 清除mappingName

                        Object siteOwner = httpRequest.getAttribute(this.siteOwnerKey);
                        if (siteOwner != null) {// 设置站点所有者, 需要业务通过request.setAttribute设置
                            logBean.setSiteOwner(siteOwner.toString());
                        }

                        // 封装参数
                        if (needParam && requestCopier != null) {
                            logBean.setParam(getParam(requestCopier));
                        }

                        // 封装结果集
                        RequestLogBean.Result result = new RequestLogBean.Result();
                        result.setStatus(((HttpServletResponse) response).getStatus());
                        if (needResult && responseCopier != null) {
                            String responseContent = getOutputParamJsonStr(responseCopier);
                            result.setContent(truncString(responseContent, maxResultLength));
                        }
                        result.setExceptionMsg(exMsg);
                        logBean.setResult(result);

                        // 获取pinpoint的trace Id，需启用pinpoint的相应的http插件，譬如：httpclient、okhttp、tomcat、resin等等
                        String pinpointTraceId = httpRequest.getHeader("Pinpoint-TraceID");
                        if (pinpointTraceId == null) { // 如未获取到则从MDC中获取
                            pinpointTraceId = MDC.get("PtxId"); // 获取pinpoint的traceId，需启用pinpoint的logback插件
                        }
                        logBean.setPinpointTraceId(pinpointTraceId);

                        // 将MDC的其他信息全部附加到Addition
                        logBean.setAdditions(MDC.getCopyOfContextMap());
                        MDC.clear(); // 清空MDC

                        //增加sleuth的信息
                        TraceContext traceContext = (TraceContext) request.getAttribute("brave.propagation.TraceContext");
                        if(!Objects.isNull(traceContext)) {
                            MDC.put("X-B3-SpanId", traceContext.spanIdString());
                            MDC.put("X-B3-TraceId", traceContext.traceIdString());
                        }

                        // 根据异常消息判断日志级别
                        recordLog(logBean, Utils.hasText(exMsg));

                    } catch (Exception ex) {
                        // do nothing, ignore
                        log.info("记录request log处理异常", ex);
                    }

                    // 清空 Dubbo log 摆渡
                    IntfLogThreadFerry.clean();
                }
            }
        }
    }

    /**
     * 记录日志
     */
    private void recordLog(RequestLogBean requestLogBean, boolean hasError) throws JsonProcessingException {
        if (hasError) {
            switch (logType) {
            case 0:
                requestLog.error(objectMapper.writeValueAsString(requestLogBean));
                break;
            case 1:
                requestLog.error(Markers.appendFields(requestLogBean), "");
                break;
            default:
                requestLog.error(objectMapper.writeValueAsString(requestLogBean));
            }
        } else {
            switch (logType) {
            case 0:
                requestLog.info(objectMapper.writeValueAsString(requestLogBean));
                break;
            case 1:
                requestLog.info(Markers.appendFields(requestLogBean), "");
                break;
            default:
                requestLog.info(objectMapper.writeValueAsString(requestLogBean));
            }
        }
    }

    /**
     * 获取参数
     */
    private RequestLogBean.Param getParam(HttpServletRequestCopier requestCopier) {
        // get paramMap
        String paramMap = null;
        try {
            paramMap = objectMapper.writeValueAsString(requestCopier.getParameterMap());
        } catch (JsonProcessingException e) {
            // do nothing, ignore
            log.info("记录request log日志JSON处理异常", e);
        }
        paramMap = needBlurPasswordForParam ? blurPasswordField(paramMap) : paramMap;

        // get queryString
        String queryStr = requestCopier.getQueryString();
        try {
            queryStr = queryStr != null ? URLDecoder.decode(queryStr, requestCopier.getCharacterEncoding()) : null;
        } catch (UnsupportedEncodingException e) {
            log.debug("URL decode [{}] by [{}] failed.", queryStr, requestCopier.getCharacterEncoding(), e);
        }

        // get header
        Enumeration<String> headers = requestCopier.getHeaderNames();
        Map<String, String> headersMap = new HashMap<>();
        while (headers.hasMoreElements()) {
            String header = headers.nextElement();

            if (includeHeaders != null && includeHeaders.length > 0) { // 如果配置了includeHeaders
                if (isIncludeHeaders(header)) { // 只记录包含的header
                    headersMap.put(header, requestCopier.getHeader(header));
                }
            } else { // 如果没有配置includeHeaders，则按排除方式处理
                if (!isExcludeHeaders(header)) { // 判断header是否需要记录
                    headersMap.put(header, requestCopier.getHeader(header));
                }
            }
        }

        // get paylaod
        String payload = getRequestBody(requestCopier);
        payload = needBlurPasswordForParam ? blurPasswordField(payload) : payload; // 模糊化password
        payload = truncString(payload, maxBodyLength); // 超长截取

        return new RequestLogBean.Param(headersMap, queryStr, payload, paramMap);

    }

    private String getOutputParamJsonStr(HttpServletResponseCopier responseCopier) {
        String contentType = responseCopier.getContentType();

        // 只记录文本
        try {
            responseCopier.flushBuffer();
            byte[] copy = responseCopier.getCopy();

            if (contentType == null || contentType.contains("text") || contentType.contains("json")) {
              //  return new String(copy, responseCopier.getCharacterEncoding());
                return new String(copy, "UTF-8");
            } else {
                return String.format("无法获取内容。contentType:%s, contentLength:%s bytes", contentType, copy.length);
            }
        } catch (Exception e) {
            log.warn("http接口日志返回值封装失败", e);
        }
        return null;
    }

    private String truncString(String str, int maxLength) {
        if (str == null) {
            return null;
        }

        if (str.length() > maxLength) {
            String subSuffix = String.format(SUB_SUFFIX, str.length());
            return str.substring(0, maxLength - subSuffix.length()) + subSuffix;
        }

        return str;
    }

    @Override
    public void destroy() {

    }

    private String getRequestBody(HttpServletRequestCopier requestCopier) {
        try {
            if (requestCopier.getContentType() != null
                    && requestCopier.getContentType().contains("multipart/form-data")) {
                // 过滤掉文件上传
                return "multipart/form-data, contentLength is: " + requestCopier.getContentLength();
            }

            ServletInputStream servletInputStream = requestCopier.getInputStream();
            byte[] bytes;
            if (servletInputStream.isFinished()) { // 判断request流是否读取过
                bytes = requestCopier.getCopy();
            } else {
                bytes = convertInputStreamToBytes(servletInputStream);
            }

            bytes = bytes == null ? new byte[0] : bytes;

            String charset = requestCopier.getCharacterEncoding();
            if (charset != null) {
                return new String(bytes, charset);
            } else {
                return new String(bytes);
            }
        } catch (Exception ex) {
            log.warn("http接口日志参数体封装失败", ex);
        }

        return "";
    }

    /**
     * 判断是否为排除的uri
     * @param requestUri 需判定的uri
     */
    private boolean isExcludePath(String requestUri) {
        if (excludePatterns == null || excludePatterns.length == 0) {
            return false;
        }

        for (String pattern : this.excludePatterns) {
            if (pathMatcher.match(pattern.trim(), requestUri)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 判断是否为排除的method
     * @param method request method(GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE)
     */
    private boolean isExcludeMethod(String method) {
        if (excludeMethods == null) {
            return false;
        }
        for (String excludeMethod : excludeMethods) {
            if (excludeMethod.equalsIgnoreCase(method)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 判断是否为排除的header
     */
    private boolean isExcludeHeaders(String header) {
        if (excludeHeaders == null) {
            return false;
        }
        for (String excludeHeader : excludeHeaders) {
            if (excludeHeader.trim().equalsIgnoreCase(header)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 判断是否为包含的header
     */
    private boolean isIncludeHeaders(String header) {
        if (includeHeaders == null) {
            return true;
        }
        for (String includeHeader : includeHeaders) {
            if (includeHeader.trim().equalsIgnoreCase(header)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 密码字段处理, 字符串加水印
     */
    private String blurPasswordField(String json) {
        if (!Utils.hasText(json)) {
            return json;
        }

        try {
            Map<Object, Object> jsonMap = objectMapper.readValue(json, new TypeReference<Map<Object, Object>>() {
            });
            return objectMapper.writeValueAsString(blurPasswordField(jsonMap));
        } catch (IOException e) {
            // ignore exception
            return json;
        }
    }

    /**
     * 密码字段处理, 字符串加水印
     */
    @SuppressWarnings("unchecked")
    public Map<Object, Object> blurPasswordField(Map<Object, Object> jsonMap) {
        Map<Object, Object> resultMap = new LinkedHashMap<>();

        for (Map.Entry<Object, Object> entry : jsonMap.entrySet()) {
            String key = String.valueOf(entry.getKey());

            Object value = entry.getValue();

            if (passwordPattern.matcher(key).matches()) {
                String originPwd = value == null ? null : String.valueOf(value);
                resultMap.put(entry.getKey(), blurString(originPwd));
            } else {
                if (value instanceof Map) {
                    Map<Object, Object> tmpResultMap = blurPasswordField((Map<Object, Object>) value);
                    resultMap.put(entry.getKey(), tmpResultMap);
                } else if (value instanceof List) {
                    List<Object> tmpList = new ArrayList<>();
                    for (Object item : (List) value) {
                        if (item instanceof Map) {
                            Map<Object, Object> tmpResultMap = blurPasswordField((Map<Object, Object>) item);
                            tmpList.add(tmpResultMap);
                        } else {
                            tmpList.add(item);
                        }
                    }
                    resultMap.put(entry.getKey(), tmpList);
                } else {
                    resultMap.put(entry.getKey(), value);
                }
            }
        }

        return resultMap;
    }

    /**
     * 字符串加水印处理
     * 
     * <pre>
     *     长度在9位以下的字符串,处理成首字母加*号的方式(*号个数等于字符串长度-1);
     * 长度在9位及以上的字符串, 将最后8位替换成*号。
     * 譬如:
     * 123处理后1**
     * 123456789处理后1********
     * 1234567890处理后12********
     * </pre>
     * 
     * @param source 原始字符串
     */
    private static String blurString(String source) {
        if (!Utils.hasText(source)) {
            return source;
        }

        if (source.length() < 9) {
            return source.substring(0, 1) + repeat('*', source.length() - 1);
        } else {
            return source.substring(0, source.length() - 8) + repeat('*', 8);
        }
    }

    @SuppressWarnings("SameParameterValue")
    private static String repeat(char ch, int repeat) {
        char[] buf = new char[repeat];
        for (int i = repeat - 1; i >= 0; i--) {
            buf[i] = ch;
        }
        return new String(buf);
    }

    private byte[] convertInputStreamToBytes(InputStream input) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int n;

        try {
            while (-1 != (n = input.read(buffer))) {
                output.write(buffer, 0, n);
            }
        } catch (IOException e) {
            if (log.isDebugEnabled()) {
                log.info("convertInputStreamToBytes failed.", e);
            }
        }
        return output.toByteArray();
    }
}