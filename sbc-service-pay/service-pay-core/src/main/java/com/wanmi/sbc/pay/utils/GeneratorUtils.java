package com.wanmi.sbc.pay.utils;

import com.wanmi.sbc.common.util.DateUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Created by sunkun on 2017/8/4.
 */
@Service
public class GeneratorUtils {

    private static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(DateUtil.FMT_TIME_3, Locale.UK);

    /**
     * 生成支付单号
     *
     * @return
     */
    public static String generatePT() {
        return "PT" + LocalDateTime.now().format(dateFormat) + RandomStringUtils.randomNumeric(4);
    }


    /**
     * 生成id 自定义前缀
     *
     * @param prefix
     * @return
     */
    public static String generate(String prefix) {
        return prefix + LocalDateTime.now().format(dateFormat) + RandomStringUtils.randomNumeric(4);
    }

}
