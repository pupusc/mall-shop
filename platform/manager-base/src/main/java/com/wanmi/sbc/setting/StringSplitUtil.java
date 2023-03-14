package com.wanmi.sbc.setting;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Liang Jun
 * @date 2022-05-17 18:25:00
 */
public final class StringSplitUtil {
    private static final String DEFAULT_SPLIT_SYMBOL = ";";

    private StringSplitUtil() {}

    public static List<String> split(String string) {
        return split(string, DEFAULT_SPLIT_SYMBOL);
    }

    public static String join(List<String> list) {
        return join(list, DEFAULT_SPLIT_SYMBOL);
    }

    public static List<String> split(String string, String symbol) {
        if (StringUtils.isBlank(string)) {
            return new ArrayList<>();
        }
        return Arrays.asList(string.split(symbol));
    }

    public static String join(List<String> list, String symbol) {
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return StringUtils.join(list, symbol);
    }
}
