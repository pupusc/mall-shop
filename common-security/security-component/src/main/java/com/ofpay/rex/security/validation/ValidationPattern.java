package com.ofpay.rex.security.validation;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.owasp.validator.html.AntiSamy;
import org.owasp.validator.html.CleanResults;
import org.owasp.validator.html.Policy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * 获取配置文件
 *
 * @author of546
 */
public class ValidationPattern {
    private ValidationPattern() {
    }

    private static final Logger logger = LoggerFactory.getLogger(ValidationPattern.class);
    private static final String RESOURCE_FILE = "XSS.properties";
    public static final String POLICY_FILE_LOCATION = "antisamy-ebay-1.4.4.xml";

    private static AntiSamy as = null;
    //实例化策略文件对象

    private static Policy policy = null;

    private static Map<String, String> map = null;

    static {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream is = classLoader.getResourceAsStream(RESOURCE_FILE);
        InputStream policyIs = classLoader.getResourceAsStream(POLICY_FILE_LOCATION);

        try {
            policy = Policy.getInstance(policyIs);
            as = new AntiSamy(policy);

            Properties properties = new Properties();
            properties.load(is);

            map = new HashMap<String, String>((Map) properties);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("load XSS Filter conf file error:", e);
        }
    }

    private static final Map<String, Pattern> patternCache = new HashMap<String, Pattern>();

    public static Pattern getValidationPattern(String key) {
        String value = map.get("Validator." + key);

        // check cache
        Pattern p = patternCache.get(value);
        if (p != null) return p;

        // compile a new pattern
        if (value == null || value.equals("")) return null;
        try {
            Pattern q = Pattern.compile(value);
            patternCache.put(value, q);
            return q;
        } catch (PatternSyntaxException e) {
            logger.warn("SecurityConfiguration for " + key + " not a valid regex in XSS.properties. Returning null");
            return null;
        }
    }


    /**
     * 验证格式
     *
     * @param context
     * @param input
     * @param type
     * @param maxLength
     * @param allowNull
     * @return
     */
    public static String getValidInput(String context, String input, String type, int maxLength,
                                       boolean allowNull) throws ValidationException {
        ValidationRule vr = new ValidationRule(type);
        Pattern p = getValidationPattern(type);
        if (p != null) {
            vr.addWhitelistPattern(p);
        } else {
            throw new IllegalArgumentException("The selected type [" + type + "] was not set via the validation configuration");
        }
        vr.setMaximumLength(maxLength);
        vr.setAllowNull(allowNull);

        String ret = vr.getValid(context, input);

        return ret;

    }


    /**
     * ******
     * 过滤富文本输入
     *
     * @param value
     * @return
     */
    public static String rtfXSS(String value) {
        String result = value;
        if (StringUtils.isBlank(value))
            return result;

        try {
            CleanResults cr = as.scan(value);
            result = cr.getCleanHTML();
        } catch (Exception e) {
            logger.warn("rtfXSS fail:  " + value);
            e.printStackTrace();
        } finally {
            return result;
        }
    }

    /**
     * **********
     * 过滤xss输入
     *
     * @param value
     * @return
     */
    public static String stripXSS(String value) {
        try {
            if (StringUtils.isBlank(value)) {
                return value;
            } else {
                value = value.replaceAll("\0", "");

                //对输入数据进行html实体字符转换
                value = htmlEncode(value);
            }
        } catch (Exception e) {
            logger.warn("stripXSS fail:  " + value);
            e.printStackTrace();
        } finally {
            return value;

        }
    }


    /************
     * 过滤list
     *
     * @param list
     * @return
     */
    public static List<Object> stripJsonList(List<Object> list, String[] excludeFields, String[] rtfNames, ObjectMapper objectMapper) {
        List<Object> resultList = new ArrayList();
        for (Object item : (List) list) {
            if (item instanceof List) {
                resultList.add(stripJsonList((List<Object>) item, excludeFields, rtfNames, objectMapper));
            } else if (item instanceof Map) {
                resultList.add(stripJsonMap((Map<Object, Object>) item, excludeFields, rtfNames, objectMapper));
            } else {
                resultList.add(stripXSS(item.toString()));
            }
        }
        return resultList;
    }


    /************
     * 过滤Map
     *
     * @param jsonMap
     * @return
     */
    public static Map<Object, Object> stripJsonMap(Map<Object, Object> jsonMap, String[] excludeFields, String[] rtfNames, ObjectMapper objectMapper) {
        Map<Object, Object> resultMap = new LinkedHashMap<Object, Object>();
        for (Map.Entry<Object, Object> entry : jsonMap.entrySet()) {
            String key = String.valueOf(entry.getKey());
            Object value = entry.getValue();

            try {
                if (value == null) {
                    resultMap.put(key, value);
                } else if (ArrayUtils.contains(excludeFields, key)) {
                    resultMap.put(key, value);
                } else if (value instanceof Map) {
                    resultMap.put(key, stripJsonMap((Map<Object, Object>) value, excludeFields, rtfNames, objectMapper));
                } else if (value instanceof List) {
                    resultMap.put(key, stripJsonList((List<Object>) value, excludeFields, rtfNames, objectMapper));
                } else {
                    if (value.toString().startsWith("[") && value.toString().endsWith("]")) {
                        List<Object> bl = objectMapper.readValue(value.toString(), new TypeReference<List<Object>>() {
                        });
                        bl = stripJsonList(bl, excludeFields, rtfNames, objectMapper);
                        resultMap.put(key, objectMapper.writeValueAsString(bl));
                    } else {
                        if (ArrayUtils.contains(rtfNames, key)) {
                            resultMap.put(key, rtfXSS(value.toString()));
                        } else {
                            resultMap.put(key, stripXSS(value.toString()));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.warn("stripJsonMap fail,key=%s,value=%s ,error:%s ", key, value, e.getMessage());
                resultMap.put(key, value);
            }
        }
        return resultMap;
    }


    /**
     * **********
     * 过滤uri xss输入
     *
     * @param value
     * @return
     */
    public static String stripURIXSS(String value) {
        try {
            if (StringUtils.isBlank(value)) {
                return value;
            } else {
                //对输入数据进行html实体字符转换
                value = uriEncode(value);
            }
        } catch (Exception e) {
            logger.warn("stripXSS fail:  " + value);
            e.printStackTrace();
        } finally {
            return value;

        }
    }

    /**
     * *********
     * 过滤uri里的特殊字符 add by freeman983 2016.1.4
     *
     * @param source
     * @return
     */
    public static String uriEncode(String source) {
        String html = "";
        if (source == null) {
            return html;
        }

        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < source.length(); i++) {
            char c = source.charAt(i);
            switch (c) {
                case '(':
                    buffer.append("");
                    break;
                case ')':
                    buffer.append("");
                    break;
                case '`':
                    buffer.append("");
                    break;
                case '&':
                    buffer.append("&amp;");
                    break;
                case '"':
                    buffer.append("&quot;");
                    break;
                case '\'':
                    buffer.append("&apos;");
                    break;

                default:
                    buffer.append(c);
            }
        }
        html = buffer.toString();
        return html;
    }


    /**
     * *********
     * 过滤实体字符 add by freeman983 2015.08.28
     *
     * @param source
     * @return
     */
    public static String htmlEncode(String source) {
        String html = "";
        if (source == null) {
            return html;
        }

        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < source.length(); i++) {
            char c = source.charAt(i);
            switch (c) {
                case '<':
                    buffer.append("&lt;");
                    break;
                case '>':
                    buffer.append("&gt;");
                    break;
                case '&':
                    buffer.append("&amp;");
                    break;
                case '"':
                    buffer.append("&quot;");
                    break;
                case '\'':
                    buffer.append("&apos;");
                    break;

                case 10:
                case 13:
                    break;
                default:
                    buffer.append(c);
            }
        }
        html = buffer.toString();
        return html;
    }
}
