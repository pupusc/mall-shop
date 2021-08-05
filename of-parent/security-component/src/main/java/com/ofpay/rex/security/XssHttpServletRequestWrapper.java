package com.ofpay.rex.security;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.ofpay.rex.security.validation.ValidationException;
import com.ofpay.rex.security.validation.ValidationPattern;
import com.ofpay.rex.util.HTMLEscapeUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 限制和过滤输入
 * <pre>
 * 警告：下面方法可直接获取(未经过html编码)
 * request.getHeader
 * request.getParameter   -- 未做任何过滤
 *
 *
 * 1.取消getParameter方法过滤，主要目的是spring mvc默认使用getParameterValues
 * 所以要求编码是必须使用参数话form，例如：
 * SubjectController.java:
 * public @ResponseBody Map<String, Object> create(@Valid SubjectForm subjectForm){
 *     ...
 * };
 *
 * 2.内容大于2000个字符大文本字段不能在form提供set方法，统一为put方法
 * 且校验工作移至控制层方法，例如：
 * SubjectForm.java:
 * public void putContent (String content) {
 *     this.content = content;
 * }
 *
 * SubjectController.java:
 * public @ResponseBody Map<String, Object> create(@Valid SubjectForm subjectForm,... , HttpServletRequest request) {
 *     String content = request.getParameter("content");
 *     if (content == null || !Range.between(1, 1000000).contains(content.length()) {
 *         return fail...
 *     }
 *     content = cleanTags(content);   // clean not whiteList(htmlTag)
 *     content = cleanStyle(content);  // rich editor
 *     subjectForm.putContent(content);
 * }
 *
 * 3.内容小于2000个字符小文本字段维持form提供set方法，校验工作保留，例如：
 * SubjectForm.java:
 * #NotEmptry
 * #Length(max = 100, message="xxx")
 * private String title;
 *
 * SubjectController.java:
 * ...
 * subjectForm.setTitle(request.getParameter("title"));
 *
 *
 * 常见场景：
 * 密码、跳转之前网址
 *
 * 其他要求：
 * 对所有用户输入做长度，类型，白名单控制。
 *
 * </pre>
 *
 * @author of546
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private static final Logger logger = LoggerFactory.getLogger(XssHttpServletRequestWrapper.class);

    private ObjectMapper objectMapper = null;


    private byte[] rawData;

    private int paramNameSize = 200;

    private int paramValueSize = 2000;

    private boolean stripPath = false;

    private boolean stripJsonStream = false;

    private String streamCharset = "UTF-8";

    private Map<String, String> cacheMap;

    private String[] excludeFields; //

    private String[] rtfNames;

    public XssHttpServletRequestWrapper(HttpServletRequest servletRequest, String[] excludeFields, String[] rtfNames, boolean stripPath, boolean stripJsonStream, String streamCharset) {
        super(servletRequest);
        this.excludeFields = excludeFields;
        this.rtfNames = rtfNames;
        this.stripPath = stripPath;
        this.stripJsonStream = stripJsonStream;

        if (this.stripPath) {
            cacheMap = new HashMap<String, String>();
        }

        this.stripJsonStream = stripJsonStream;
        this.streamCharset = streamCharset;


        if (this.stripJsonStream) {
            objectMapper = new ObjectMapper();
            objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            objectMapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
        }
    }

    public XssHttpServletRequestWrapper(HttpServletRequest servletRequest, String[] excludeFields, String[] rtfNames, Integer pns, Integer pvs, boolean stripPath, boolean stripJsonStream, String streamCharset) {
        super(servletRequest);

        this.excludeFields = excludeFields;
        this.rtfNames = rtfNames;
        if (null != pns) {
            this.paramNameSize = pns.intValue();
        }

        if (null != pvs) {
            this.paramValueSize = pvs.intValue();
        }

        this.stripPath = stripPath;
        if (this.stripPath) {
            cacheMap = new HashMap<String, String>();
        }

        this.stripJsonStream = stripJsonStream;
        this.streamCharset = streamCharset;


        if (this.stripJsonStream) {
            objectMapper = new ObjectMapper();
            objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            objectMapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
        }
    }

    public String getContextPath() {
        String path = super.getContextPath();
        // Return empty String for the ROOT context
        if (path == null || "".equals(path.trim())) return "";

        String clean = "";
        try {
            clean = ValidationPattern.getValidInput("HTTP context path: " + path, path, "HTTPContextPath", 300, false);
        } catch (ValidationException e) {
            logger.warn("Skipping bad ContextPath", e);
        }
        return clean;
    }

    public Cookie[] getCookies() {
        Cookie[] cookies = super.getCookies();
        if (cookies == null) return new Cookie[0];

        List<Cookie> newCookies = new ArrayList<Cookie>();
        for (Cookie c : cookies) {
            // build a new clean cookie
            try {
                // get data from original cookie
                String name = ValidationPattern.getValidInput("Cookie name: " + c.getName(), c.getName(), "HTTPCookieName", 300, true);
                String value = ValidationPattern.getValidInput("Cookie value: " + c.getValue(), c.getValue(), "HTTPCookieValue", 1000, true);
                int maxAge = c.getMaxAge();
                String domain = c.getDomain();
                String path = c.getPath();

                Cookie n = new Cookie(name, value);
                n.setMaxAge(maxAge);

                if (domain != null) {
                    n.setDomain(ValidationPattern.getValidInput("Cookie domain: " + domain, domain, "HTTPHeaderValue", 200, false));
                }
                if (path != null) {
                    n.setPath(ValidationPattern.getValidInput("Cookie path: " + path, path, "HTTPHeaderValue", 300, false));
                }
                newCookies.add(n);
            } catch (ValidationException e) {
                logger.warn("Skipping bad cookie: {}={}", c.getName(), c.getValue(), e);
            }
        }
        return newCookies.toArray(new Cookie[newCookies.size()]);
    }

    public String getHeader(String name) {
        String value = super.getHeader(name);
        String clean = "";
        try {
            clean = ValidationPattern.getValidInput("HTTP header value: " + value, value, "HTTPHeaderValue", 2000, true);
        } catch (ValidationException e) {
            logger.warn("Skipping bad Header", e);
        }
        return clean;
    }

    public Enumeration getHeaderNames() {
        Vector<String> v = new Vector<String>();
        Enumeration en = super.getHeaderNames();
        while (en.hasMoreElements()) {
            try {
                String name = (String) en.nextElement();
                String clean = ValidationPattern.getValidInput("HTTP header name: " + name, name, "HTTPHeaderName", 150, true);
                v.add(clean);
            } catch (ValidationException e) {
                logger.warn("Skipping bad HeaderNames.i", e);
            }
        }
        return v.elements();
    }

    public Enumeration getHeaders(String name) {
        Vector<String> v = new Vector<String>();
        Enumeration en = super.getHeaders(name);
        while (en.hasMoreElements()) {
            try {
                String value = (String) en.nextElement();
                String clean = ValidationPattern.getValidInput("HTTP header value (" + name + "): " + value, value, "HTTPHeaderValue", 2000, true);
                v.add(HTMLEscapeUtil.escape(clean));
            } catch (ValidationException e) {
                logger.warn("Skipping bad Headers.i", e);
            }
        }
        return v.elements();
    }

    public String getParameter(String name) {
        String orig = super.getParameter(name);
        String clean = null;
        try {
            if (ArrayUtils.contains(excludeFields, name)) {
                clean = orig;
            } else if (ArrayUtils.contains(rtfNames, name)) {
                clean = ValidationPattern.rtfXSS(orig);
            } else {
                clean = ValidationPattern.getValidInput("HTTP parameter name: " + name, orig, "HTTPParameterValue", paramValueSize, true);
                clean = ValidationPattern.stripXSS(clean);
            }
        } catch (ValidationException e) {
            logger.warn("Skipping bad parameter", e);
        }
        return clean;
    }

    public Map getParameterMap() {
        Map<String, String[]> map = super.getParameterMap();
        Map<String, String[]> cleanMap = new HashMap<String, String[]>();
        for (Object o : map.entrySet()) {
            try {
                Map.Entry e = (Map.Entry) o;
                String name = (String) e.getKey();
                String[] value = (String[]) e.getValue();

                if (ArrayUtils.contains(excludeFields, name)) {
                    cleanMap.put(name, value);
                } else if (ArrayUtils.contains(rtfNames, name)) {
                    name = ValidationPattern.getValidInput("HTTP parameter name: " + name, name, "HTTPParameterName", paramNameSize, true);
                    String[] cleanValues = new String[value.length];
                    for (int j = 0; j < value.length; j++) {
                        cleanValues[j] = ValidationPattern.rtfXSS(value[j]);
                    }
                    cleanMap.put(name, cleanValues);
                } else {
                    name = ValidationPattern.getValidInput("HTTP parameter name: " + name, name, "HTTPParameterName", paramNameSize, true);
                    String[] cleanValues = new String[value.length];
                    for (int j = 0; j < value.length; j++) {
                        String cleanValue = ValidationPattern.getValidInput("HTTP parameter value: " + value[j], value[j], "HTTPParameterValue", paramValueSize, true);
                        cleanValues[j] = ValidationPattern.stripXSS(cleanValue);
                    }
                    cleanMap.put(name, cleanValues);
                }
            } catch (ValidationException e) {
                logger.warn("Skipping bad ParameterMap.i", e);
            }
        }
        return cleanMap;
    }

    public Enumeration getParameterNames() {
        Vector<String> v = new Vector<String>();
        Enumeration en = super.getParameterNames();
        while (en.hasMoreElements()) {
            try {
                String name = (String) en.nextElement();
                if (ArrayUtils.contains(excludeFields, name)) {
                    v.add(name);
                } else {
                    String clean = ValidationPattern.getValidInput("HTTP parameter name: " + name, name, "HTTPParameterName", paramNameSize, true);
                    v.add(clean);
                }
            } catch (ValidationException e) {
                logger.warn("Skipping bad ParameterNames.i", e);
            }
        }
        return v.elements();
    }

    public String[] getParameterValues(String name) {
        if (ArrayUtils.contains(excludeFields, name)) {
            return super.getParameterValues(name);
        }

        String[] values = super.getParameterValues(name);
        List<String> newValues;

        if (values == null)
            return null;

        boolean isRtf = false;
        if (ArrayUtils.contains(rtfNames, name)) {
            isRtf = true;
        }

        newValues = new ArrayList<String>();
        for (String value : values) {
            try {
                String cleanValue = value;
                //富文本参数值不检验长度，交给业务系统检验
                if (!isRtf) {
                    cleanValue = ValidationPattern.getValidInput("HTTP parameter value: " + value, value, "HTTPParameterValue", paramValueSize, true);
                }
                newValues.add(isRtf ? ValidationPattern.rtfXSS(cleanValue) : ValidationPattern.stripXSS(cleanValue));
            } catch (ValidationException e) {
                logger.warn("Skipping bad ParameterValues.i", e);
            }
        }
        return newValues.toArray(new String[newValues.size()]);
    }

    public String getPathInfo() {
        String path = super.getPathInfo();
        if (path == null) return null;
        String clean = "";
        try {
            clean = ValidationPattern.getValidInput("HTTP path: " + path, path, "HTTPPath", 150, true);
        } catch (ValidationException e) {
            logger.warn("Skipping bad PathInfo", e);
        }
        return clean;
    }

    public String getQueryString() {
        String query = super.getQueryString();
        String clean = "";
        try {
            clean = ValidationPattern.getValidInput("HTTP query string: " + query, query, "HTTPQueryString", 2000, true);
        } catch (ValidationException e) {
            logger.warn("Skipping bad QueryString", e);
        }
        return clean;
    }

    public String getRequestedSessionId() {
        String id = super.getRequestedSessionId();
        String clean = "";
        try {
            clean = ValidationPattern.getValidInput("Requested cookie: " + id, id, "HTTPJSESSIONID", 128, true);
        } catch (ValidationException e) {
            logger.warn("Skipping bad RequestedSessionId", e);
        }
        return clean;
    }

    @Override
    public String getRequestURI() {
        String uri = super.getRequestURI();

        String clean = "";
        try {
            clean = ValidationPattern.getValidInput("HTTP URI: " + uri, uri, "HTTPURI", 2000, false);
        } catch (ValidationException e) {
            logger.warn("Skipping bad RequestURI", e);
        }

        return clean;
    }

    public StringBuffer getRequestURL() {
        String url = super.getRequestURL().toString();
        String clean = "";
        try {
            clean = ValidationPattern.getValidInput("HTTP URL: " + url, url, "HTTPURL", 2000, false);
        } catch (ValidationException e) {
            logger.warn("Skipping bad RequestURL", e);
        }
        return new StringBuffer(clean);
    }

    public String getScheme() {
        String scheme = super.getScheme();
        String clean = "";
        try {
            clean = ValidationPattern.getValidInput("HTTP scheme: " + scheme, scheme, "HTTPScheme", 10, false);
        } catch (ValidationException e) {
            logger.warn("Skipping bad Scheme", e);
        }
        return clean;
    }

    public String getServerName() {
        String name = super.getServerName();
        String clean = "";
        try {
            clean = ValidationPattern.getValidInput("HTTP server name: " + name, name, "HTTPServerName", 100, false);
        } catch (ValidationException e) {
            logger.warn("Skipping bad ServerName", e);
        }
        return clean;
    }

    public int getServerPort() {
        int port = super.getServerPort();
        if (port < 0 || port > 0xFFFF) {
            logger.warn("HTTP server port out of range: " + port);
            port = 0;
        }
        return port;
    }

    public String getServletPath() {
        String path = super.getServletPath();
        String clean = "";
        try {
            clean = ValidationPattern.getValidInput("HTTP servlet path: " + path, path, "HTTPServletPath", 200, false);
            if (stripPath) {
                if (cacheMap.containsKey(clean)) {
                    clean = cacheMap.get(clean);
                } else {
                    clean = ValidationPattern.stripURIXSS(clean);
                }
            }
        } catch (ValidationException e) {
            logger.warn("Skipping bad ServletPath", e);
        }
        return clean;
    }


    /*****************
     * 对json类型请求进行过滤
     *
     * @return
     * @throws IOException
     */
    @Override
    public ServletInputStream getInputStream() throws IOException {

        if (!stripJsonStream || (getRequest().getContentType() != null && getRequest().getContentType().contains("multipart/form-data"))) {
            return super.getInputStream();
        }
        ServletInputStream stream = null;
        if (rawData == null) {
            stream = super.getInputStream();
            try {
                String bodyData = IOUtils.toString(stream, streamCharset);
                if (StringUtils.isNotBlank(bodyData)) {
                    if (bodyData.startsWith("[")) {
                        List<Object> beanList = objectMapper.readValue(bodyData, new TypeReference<List<Object>>() {
                        });
                        beanList = ValidationPattern.stripJsonList(beanList, excludeFields,rtfNames,objectMapper);
                        bodyData = objectMapper.writeValueAsString(beanList);
                    } else {
                        Map<Object, Object> jsonMap = objectMapper.readValue(bodyData, new TypeReference<Map<Object, Object>>() {
                        });
                        jsonMap = ValidationPattern.stripJsonMap(jsonMap, excludeFields,rtfNames,objectMapper);
                        bodyData = objectMapper.writeValueAsString(jsonMap);
                    }
                }
                rawData = bodyData.getBytes(streamCharset);
            } catch (Exception e) {
                e.printStackTrace();
                logger.warn("Parse json request fail  err:" + e.getMessage());
                return super.getInputStream();
            }
        }

        final ByteArrayInputStream bais = new ByteArrayInputStream(rawData);

        stream = new ServletInputStream() {
            @Override
            public int read() throws IOException {
                return bais.read();
            }
        };

        return stream;

    }


    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }


}

