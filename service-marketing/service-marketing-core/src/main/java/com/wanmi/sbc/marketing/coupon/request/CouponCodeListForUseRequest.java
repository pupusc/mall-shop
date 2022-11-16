package com.wanmi.sbc.marketing.coupon.request;

import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.marketing.bean.enums.CouponType;
import com.wanmi.sbc.marketing.bean.enums.FullBuyType;
import com.wanmi.sbc.marketing.bean.enums.ScopeType;
import com.wanmi.sbc.marketing.common.request.TradeItemInfo;
import com.wanmi.sbc.marketing.bean.vo.CouponCodeVO;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: gaomuwei
 * @Date: Created In 下午5:58 2018/9/27
 * @Description: 使用优惠券列表请求对象
 */
@Data
public class CouponCodeListForUseRequest {

    /**
     * 客户id
     */
    private String customerId;

    /**
     * 确认订单中的商品列表
     */
    private List<TradeItemInfo> tradeItems = new ArrayList<>();

    private Long storeId;

    private BigDecimal price;

    private Boolean hasSaveRedis;


    public String getQuerySql() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT couponCode.coupon_code_id AS `couponCodeId`, ");
        sb.append("      couponCode.coupon_code AS `couponCode`, ");
        sb.append("      couponCode.use_status AS `useStatus`, ");
        sb.append("      couponCode.use_date AS `useDate`, ");
        sb.append("      couponCode.order_code AS `orderCode`, ");
        sb.append("      couponCode.start_time AS `startTime`, ");
        sb.append("      couponCode.end_time AS `endTime`, ");
        sb.append("      activity.activity_id AS `activityId`, ");
        sb.append("      couponInfo.coupon_id AS `couponId`, ");
        sb.append("      couponInfo.coupon_name AS `couponName`, ");
        sb.append("      couponInfo.create_time AS `createTime`, ");
        sb.append("      couponInfo.scope_type AS `scopeType`, ");
        sb.append("      couponInfo.fullbuy_type AS `fullBuyType`, ");
        sb.append("      couponInfo.fullbuy_price AS `fullBuyPrice`, ");
        sb.append("      couponInfo.denomination AS `denomination`, ");
        sb.append("      couponInfo.coupon_type AS `couponType`, ");
        sb.append("      couponInfo.platform_flag AS `platformFlag`, ");
        sb.append("      couponInfo.coupon_desc AS `couponDesc`, ");
        sb.append("      couponInfo.store_id AS `storeId`");
//        sb.append("      s.store_name AS `storeName` ");
        sb.append("      FROM coupon_code AS couponCode ");
        sb.append("      LEFT JOIN coupon_info AS couponInfo ON couponCode.coupon_id = couponInfo.coupon_id ");
        sb.append("      LEFT JOIN coupon_activity AS activity ON couponCode.activity_id = activity.activity_id ");
//        sb.append("      LEFT JOIN store AS s ON s.store_id = couponInfo.store_id");
        sb.append("      WHERE couponCode.customer_id = :customerId");
        sb.append("      AND couponCode.del_flag = '0'");
        sb.append("      AND couponCode.use_status = '0'");
        sb.append("      AND couponCode.end_time > now() ORDER BY couponInfo.denomination desc");
        return sb.toString();
    }

    public static List<CouponCodeVO> converter(List<Map<String, Object>> sqlResult) {
        return sqlResult.stream().map(item ->
                CouponCodeVO.builder()
                        .couponCodeId(toStr(item, "couponCodeId"))
                        .couponCode(toStr(item, "couponCode"))
                        .useStatus(toInteger(item, "useStatus") != null ? (toInteger(item, "useStatus") == 0 ? DefaultFlag.NO : DefaultFlag.YES) : null)
                        .useDate(toDate(item, "useDate"))
                        .orderCode(toStr(item, "orderCode"))
                        .startTime(toDate(item, "startTime"))
                        .endTime(toDate(item, "endTime"))
                        .activityId(toStr(item, "activityId"))
                        .couponId(toStr(item, "couponId"))
                        .couponName(toStr(item, "couponName"))
                        .fullBuyType(toInteger(item, "fullBuyType") != null ? FullBuyType.fromValue(toInteger(item, "fullBuyType")) : null)
                        .fullBuyPrice(toBigDecimal(item, "fullBuyPrice"))
                        .denomination(toBigDecimal(item, "denomination"))
                        .platformFlag(toInteger(item, "platformFlag") != null ? (toInteger(item, "platformFlag") == 0 ? DefaultFlag.NO : DefaultFlag.YES) : null)
                        .couponType(toInteger(item, "couponType") != null ? CouponType.fromValue(toInteger(item, "couponType")) : null)
                        .createTime(toDate(item, "createTime"))
                        .scopeType(toInteger(item, "scopeType") != null ? ScopeType.fromValue(toInteger(item, "scopeType")) : null)
                        .couponDesc(toStr(item, "couponDesc"))
                        .storeId(toLong(item, "storeId"))
                        .storeName(toStr(item, "storeName"))
                        .build()
        ).collect(Collectors.toList());
    }

    private static String toStr(Map<String, Object> map, String key) {
        return map.get(key) != null ? map.get(key).toString() : null;
    }

    private static Long toLong(Map<String, Object> map, String key) {
        return map.get(key) != null ? Long.parseLong(map.get(key).toString()) : null;
    }

    private static Integer toInteger(Map<String, Object> map, String key) {
        return map.get(key) != null ? Integer.parseInt(map.get(key).toString()) : null;
    }

    private static LocalDateTime toDate(Map<String, Object> map, String key) {
        return map.get(key) != null ? DateUtil.parse(map.get(key).toString(), "yyyy-MM-dd HH:mm:ss.S") : null;
    }

    private static BigDecimal toBigDecimal(Map<String, Object> map, String key) {
        return map.get(key) != null ? new BigDecimal(map.get(key).toString()) : null;
    }
}
