package com.wanmi.sbc.order.util;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.regex.Pattern;

/**
 * XSS工具类
 * Created by jinwei on 15/1/2016.
 */
public class XssUtils {

    /**
     * 转义 < >
     * @param value
     * @return
     */
    public static String replaceXss(String value){
        if(value != null && value.length() > 0){
            return value.replaceAll("<", "&lt;").replace(">", "&gt;");
        }else {
            return value;
        }
    }

    /**
     * 替换LIKE通配符(暂支持mysql,其他数据库需要测试后才可使用)
     * @param value
     * @return
     */
    public static String replaceLikeWildcard(String value){
        return value.replaceAll("\\\\","\\\\\\\\").replaceAll("_","\\\\_").replaceAll("%","\\\\%");
    }


    /**
     * LIKE通配符（仅支持mongoDB）
     * @param str
     * @return
     */
    public static String mongodbLike(String str) {
        return String.format("^.*%s.*$", ObjectUtils.toString(str).trim());
    }

    public static Criteria regex(String colsName, String value){
        return Criteria.where(colsName).regex(Pattern.compile(mongodbLike(value), Pattern.CASE_INSENSITIVE));
    }
}
