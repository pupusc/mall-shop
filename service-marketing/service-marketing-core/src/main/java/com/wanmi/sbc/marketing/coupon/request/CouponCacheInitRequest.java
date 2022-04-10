package com.wanmi.sbc.marketing.coupon.request;

import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.common.util.UUIDUtil;
import com.wanmi.sbc.marketing.bean.enums.*;
import com.wanmi.sbc.marketing.coupon.model.entity.cache.CouponActivityCache;
import com.wanmi.sbc.marketing.coupon.model.entity.cache.CouponCache;
import com.wanmi.sbc.marketing.coupon.model.entity.cache.CouponInfoCache;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 正在进行的优惠券活动查询
 */
@Data
@Builder
public class CouponCacheInitRequest {

    /**
     * 优惠券活动Id集合
     */
    private List<String> couponActivityIds;

    /**
     * 查询该时间段内开始的活动
     */
    private LocalDateTime queryStartTime;

    /**
     * 查询该时间段内开始的活动
     */
    private LocalDateTime queryEndTime;

    /**
     * request构建查询对象
     *
     * @return
     */
    public String getQuerySql() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT coupon_activity_config.activity_config_id AS `activityConfigId`, ");
        sb.append("      coupon_activity_config.total_count AS `totalCount`, ");
        sb.append("      coupon_activity_config.has_left AS `hasLeft`, ");
        sb.append("      coupon_info.coupon_id AS `couponInfo.couponId`, ");
        sb.append("      coupon_info.coupon_name AS `couponInfo.couponName`, ");
        sb.append("      coupon_info.range_day_type AS `couponInfo.rangeDayType`, ");
        sb.append("      coupon_info.start_time AS `couponInfo.startTime`, ");
        sb.append("      coupon_info.end_time AS `couponInfo.endTime`, ");
        sb.append("      coupon_info.effective_days AS `couponInfo.effectiveDays`, ");
        sb.append("      coupon_info.fullbuy_price AS `couponInfo.fullBuyPrice`, ");
        sb.append("      coupon_info.fullbuy_type AS `couponInfo.fullBuyType`, ");
        sb.append("      coupon_info.denomination AS `couponInfo.denomination`, ");
        sb.append("      coupon_info.store_id AS `couponInfo.storeId`, ");
        sb.append("      coupon_info.platform_flag AS `couponInfo.platformFlag`, ");
        sb.append("      coupon_info.scope_type AS `couponInfo.scopeType`, ");
        sb.append("      coupon_info.coupon_desc AS `couponInfo.couponDesc`, ");
        sb.append("      coupon_info.coupon_type AS `couponInfo.couponType`, ");
        sb.append("      coupon_info.create_time AS `couponInfo.createTime`, ");
        sb.append("      coupon_activity.activity_id AS `couponActivity.activityId`, ");
        sb.append("      coupon_activity.activity_name AS `couponActivity.activityName`, ");
        sb.append("      coupon_activity.start_time AS `couponActivity.startTime`, ");
        sb.append("      coupon_activity.end_time AS `couponActivity.endTime`, ");
        sb.append("      coupon_activity.activity_type AS `couponActivity.activityType`, ");
        sb.append("      coupon_activity.receive_type AS `couponActivity.receiveType`, ");
        sb.append("      coupon_activity.pause_flag AS `couponActivity.pauseFlag`, ");
        sb.append("      coupon_activity.join_level AS `couponActivity.joinLevel`, ");
        sb.append("      coupon_activity.receive_count AS `couponActivity.receiveCount`, ");
        sb.append("      coupon_activity.terminals AS `couponActivity.terminals`, ");
        sb.append("      coupon_activity.store_id AS `couponActivity.storeId`, ");
        sb.append("      coupon_activity.platform_flag AS `couponActivity.platformFlag`, ");
        sb.append("      coupon_activity.create_time AS `couponActivity.createTime` ,");
        sb.append("      coupon_activity.activity_scene AS `couponActivity.activityScene` ");
        sb.append("     FROM coupon_activity_config ");
        sb.append("      LEFT JOIN coupon_info ON coupon_info.coupon_id = coupon_activity_config.coupon_id ");
        sb.append("      LEFT JOIN coupon_activity ON coupon_activity.activity_id = coupon_activity_config.activity_id");
        sb.append("     WHERE 1=1  AND coupon_info.del_flag = 0 AND coupon_activity.del_flag = 0");
        sb.append("     AND coupon_activity.activity_type = 0");
        sb.append("     AND coupon_activity.start_time < now() < coupon_activity.end_time");
        if (CollectionUtils.isNotEmpty(couponActivityIds)) {
            sb.append("     AND coupon_activity_config.activity_id in (:couponActivityIds) ");
        }
        if (this.queryStartTime != null) {
            sb.append("     AND :queryStartTime <= coupon_activity.start_time");
        }
        if (this.queryEndTime != null) {
            sb.append("     AND :queryEndTime >= coupon_activity.start_time");
        }
        return sb.toString();
    }

    /**
     * 原生sql查询出二维数组，该方法帮助转换为List<bean>
     * @param sqlResult
     * @return
     */
    public static List<CouponCache> converter(List<Map<String, Object>> sqlResult) {
        return sqlResult.stream().map(item ->
                CouponCache.builder()
                        .id(UUIDUtil.getUUID())
                        .totalCount(Long.parseLong(item.get("totalCount").toString()))
                        .activityConfigId(item.get("activityConfigId").toString())
                        .hasLeft(toInteger(item, "hasLeft") != null ? DefaultFlag.fromValue(toInteger(item, "hasLeft")) : DefaultFlag.YES)
                        .couponActivityId(toStr(item, "couponActivity.activityId"))
                        .couponInfoId(toStr(item, "couponInfo.couponId"))
                        .couponInfo(CouponInfoCache.builder()
                                .couponId(toStr(item, "couponInfo.couponId"))
                                .couponName(toStr(item, "couponInfo.couponName"))
                                .rangeDayType(toInteger(item, "couponInfo.rangeDayType") != null ? RangeDayType.fromValue(toInteger(item, "couponInfo.rangeDayType")) : null)
                                .startTime(toDate(item, "couponInfo.startTime"))
                                .endTime(toDate(item, "couponInfo.endTime"))
                                .effectiveDays(toInteger(item, "couponInfo.effectiveDays"))
                                .fullBuyPrice(toDouble(item, "couponInfo.fullBuyPrice"))
                                .fullBuyType(toInteger(item, "couponInfo.fullBuyType") != null ? FullBuyType.fromValue(toInteger(item, "couponInfo.fullBuyType")) : null)
                                .denomination(toDouble(item, "couponInfo.denomination"))
                                .storeId(toLong(item, "couponInfo.storeId"))
                                .platformFlag(toInteger(item, "couponInfo.platformFlag") != null ? (toInteger(item, "couponInfo.platformFlag") == 0 ? DefaultFlag.NO : DefaultFlag.YES) : null)
                                .scopeType(toInteger(item, "couponInfo.scopeType") != null ? ScopeType.fromValue(toInteger(item, "couponInfo.scopeType")) : null)
                                .couponDesc(toStr(item, "couponInfo.couponDesc"))
                                .couponType(toInteger(item, "couponInfo.couponType") != null ? CouponType.fromValue(toInteger(item, "couponInfo.couponType")) : null)
                                .couponTypeInteger(toInteger(item, "couponInfo.couponType") != null ? CouponType.fromValue(toInteger(item, "couponInfo.couponType")).toValue() : null)
                                .createTime(toDate(item, "couponInfo.createTime"))
                                .build())
                        .couponActivity(CouponActivityCache.builder()
                                .activityId(toStr(item, "couponActivity.activityId"))
                                .activityName(toStr(item, "couponActivity.activityName"))
                                .startTime(toDate(item, "couponActivity.startTime"))
                                .endTime(toDate(item, "couponActivity.endTime"))
                                .couponActivityType(toInteger(item, "couponActivity.activityType") != null ? CouponActivityType.fromValue(toInteger(item, "couponActivity.activityType")) : null)
                                .receiveType(toInteger(item, "couponActivity.receiveType") != null ? DefaultFlag.fromValue(toInteger(item, "couponActivity.receiveType")) : null)
                                .pauseFlag(toInteger(item, "couponActivity.pauseFlag") != null ? (toInteger(item, "couponActivity.pauseFlag") == 0 ? DefaultFlag.NO : DefaultFlag.YES) : null)
                                .joinLevel(toStr(item, "couponActivity.joinLevel") != null ? toStr(item, "couponActivity.joinLevel").split(",") : null)
                                .receiveCount(toInteger(item, "couponActivity.receiveCount"))
                                .terminals(toStr(item, "couponActivity.terminals"))
                                .storeId(toLong(item, "couponActivity.storeId"))
                                .platformFlag(toInteger(item, "couponActivity.platformFlag") != null ? (toInteger(item, "couponActivity.platformFlag") == 0 ? DefaultFlag.NO : DefaultFlag.YES) : null)
                                .createTime(toDate(item, "couponActivity.createTime"))
                                .activityScene(toStr(item, "couponActivity.activityScene") != null ? toStr(item, "couponActivity.activityScene").split(",") : null)
                                .build()
                        )
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

    private static Double toDouble(Map<String, Object> map, String key) {
        return map.get(key) != null ? Double.parseDouble(map.get(key).toString()) : null;
    }
}
