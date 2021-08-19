package com.wanmi.sbc.common.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * 订单ID生成
 * Created by Administrator on 2017/4/18.
 */
@Service
public class GeneratorService {

    /**
     * 父订单号前缀
     */
    public static final String _PREFIX_PARENT_TRADE_ID = "PO";

    /**
     * 订单号前缀
     */
    public static final String _PREFIX_TRADE_ID = "O";

    /**
     * 供应商订单号前缀
     */
    public static final String _PREFIX_PROVIDER_TRADE_ID = "P";

    /**
     * 供应商第三方子订单号前缀
     */
    public static final String _PREFIX_PROVIDER_THIRD_TRADE_ID = "PS";

    /**
     * 商家订单号前缀
     */
    public static final String _PREFIX_STORE_TRADE_ID = "S";

    /**
     * 尾款订单号前缀
     */
    public static final String _PREFIX_TRADE_TAIL_ID = "OT";

    public static final String _PREFIX_GOODS_ADJUST_RECORD_ID = "AP";

    /**
     * 有赞订单前缀
     */
    public static final String _PREFIX_YOUZAN_TRADE_ID = "E";


    private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DateUtil.FMT_TIME_7, Locale.UK);

    private static DateTimeFormatter dateTimeFormatter3 = DateTimeFormatter.ofPattern(DateUtil.FMT_TIME_3, Locale.UK);


    /**
     * 生成tid
     * O+ "yyyyMMddHHmmss" + random(3)
     */
    public String generateTid() {
        return _PREFIX_TRADE_ID + LocalDateTime.now().format(dateTimeFormatter) + RandomStringUtils.randomNumeric(4);
    }

    /**
     * 生成供应商tid
     * P+ "yyyyMMddHHmmss" + random(3)
     */
    public String generateProviderTid() {
        return _PREFIX_PROVIDER_TRADE_ID + LocalDateTime.now().format(dateTimeFormatter) + RandomStringUtils.randomNumeric(4);
    }

    /**
     * 生成供应商子订单tid
     * P+ "yyyyMMddHHmmss" + random(3)
     */
    public String generateProviderThirdTid() {
        return _PREFIX_PROVIDER_THIRD_TRADE_ID + LocalDateTime.now().format(dateTimeFormatter) + RandomStringUtils.randomNumeric(4);
    }

    /**
     * 生成供应商tid
     * P+ "yyyyMMddHHmmss" + random(3)
     */
    public String generateStoreTid() {
        return _PREFIX_STORE_TRADE_ID + LocalDateTime.now().format(dateTimeFormatter) + RandomStringUtils.randomNumeric(4);
    }

    /**
     * 生成尾款tid(临时)
     * OT+ "yyyyMMddHHmmss" + random(3)
     */
    public String generateTailTid() {
        return _PREFIX_TRADE_TAIL_ID + LocalDateTime.now().format(dateTimeFormatter) + RandomStringUtils.randomNumeric(4);
    }

    /**
     * 生成父订单号 po+id （用于组织批量订单合并支付，目前仅在支付与退款中使用）
     * O+ "yyyyMMddHHmmss" + random(3)
     */
    public String generatePoId() {
        return _PREFIX_PARENT_TRADE_ID + LocalDateTime.now().format(dateTimeFormatter) + RandomStringUtils.randomNumeric(4);
    }

    /**
     * 生成oid
     * OD+ "yyyyMMddHHmmss" + random(3)
     *
     * @return
     */
    public String generateOid() {
        return "OD" + LocalDateTime.now().format(dateTimeFormatter) + RandomStringUtils.randomNumeric(4);
    }

    /**
     * 生成支付单号
     *
     * @return
     */
    public String generatePid() {
        return "PD" + LocalDateTime.now().format(dateTimeFormatter) + RandomStringUtils.randomNumeric(4);
    }

    /**
     * 生成支付单流水号
     *
     * @return
     */
    public String generateSid() {
        return "P" + LocalDateTime.now().format(dateTimeFormatter) + RandomStringUtils.randomNumeric(4);
    }


    /**
     * 生成退款单号
     *
     * @return
     */
    public String generateRid() {
        return "RD" + LocalDateTime.now().format(dateTimeFormatter) + RandomStringUtils.randomNumeric(4);
    }

    /**
     * 退款单流水单号
     *
     * @return
     */
    public String generateRF() {
        return "RF" + LocalDateTime.now().format(dateTimeFormatter) + RandomStringUtils.randomNumeric(4);
    }

    /**
     * 生成拼团团号
     */
    public String generateGrouponNo() {
        return "G" + LocalDateTime.now().format(dateTimeFormatter) + RandomStringUtils.randomNumeric(4);
    }


    /**
     * 生成id 自定义前缀
     *
     * @param prefix
     * @return
     */
    public String generate(String prefix) {
        return prefix + LocalDateTime.now().format(dateTimeFormatter) + RandomStringUtils.randomNumeric(4);
    }

    /**
     * 生成财务对账明细id
     *
     * @return
     */
    public String generateRNid() {
        return "RN" + LocalDateTime.now().format(dateTimeFormatter) + RandomStringUtils.randomNumeric(4);
    }

    /**
     * 生成调价记录单号
     *
     * @return
     */
    public String generateAPId() {
        return _PREFIX_GOODS_ADJUST_RECORD_ID + LocalDateTime.now().format(dateTimeFormatter3) + RandomStringUtils.randomNumeric(4);
    }

}
