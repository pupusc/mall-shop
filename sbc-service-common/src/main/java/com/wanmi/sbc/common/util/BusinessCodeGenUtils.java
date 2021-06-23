package com.wanmi.sbc.common.util;

import com.wanmi.sbc.common.constant.PaidCardConstant;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 业务号生产工具类，生成各种业务号码
 */
public class BusinessCodeGenUtils {

    /**
     * 付费会员购买记录流水号:F+日期8位+时分秒6位+4位随机
     * @return
     */
    public static String genPaidCardBuyRecordCode(){
        StringBuilder code = new StringBuilder();
        code.append(PaidCardConstant.PAID_CARD_BUY_RECORD_PAY_CODE_PRE);
        String dateTimePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        code.append(dateTimePart);
        String randomNumeric = RandomStringUtils.randomNumeric(4);
        code.append(randomNumeric);
        return code.toString();
    }

    /**
     * 付费会员购买记录流水号:F+日期8位+时分秒6位+4位随机
     * @return
     */
    public static String genPaidCardCode(){
        String randomNumeric = RandomStringUtils.randomAlphanumeric(12);
        return randomNumeric.toUpperCase();
    }

    public static void main(String[] args) {
        System.out.println(genPaidCardCode());
    }

}
