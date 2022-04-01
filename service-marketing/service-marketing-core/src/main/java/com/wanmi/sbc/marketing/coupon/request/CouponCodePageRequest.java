package com.wanmi.sbc.marketing.coupon.request;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.marketing.bean.enums.CouponActivityType;
import com.wanmi.sbc.marketing.bean.enums.CouponType;
import com.wanmi.sbc.marketing.bean.enums.FullBuyType;
import com.wanmi.sbc.marketing.bean.enums.ScopeType;
import com.wanmi.sbc.marketing.bean.vo.CouponCodeVO;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author CHENLI
 */
@Data
public class CouponCodePageRequest extends BaseQueryRequest {
    /**
     *  领取人id,同时表示领取状态
     */
    private String customerId;

    /**
     *  使用状态,0(未使用)，1(使用)，2(已过期)
     */
    private int useStatus;

    /**
     * 优惠券类型 0通用券 1店铺券 2运费券
     */
    private CouponType couponType;

    /**
     * 活动类型
     */
    private List<CouponActivityType> couponActivityTypes;

    private Long storeId;

    private List<String> couponIds;

    /**
     * request构建查询我的优惠券对象
     * @return
     */
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
        sb.append("      activity.activity_title AS `activityTitle`, ");
        sb.append("      activity.activity_desc AS `activityDesc`, ");
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
        sb.append("      couponInfo.store_id AS `storeId` ");
//        sb.append("      s.store_name AS `storeName` ");

        return sb.toString();
    }

    /**
     * 分页查询时查询我的优惠券总数
     * @return
     */
    public String getQueryTotalCountSql(){
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT count(1) AS `totalCount` ");

        return sb.toString();
    }

    /**
     * 查询条件
     * @return
     */
    public String getQueryConditionSql() {
        StringBuilder sb = new StringBuilder();
        sb.append("      FROM coupon_code AS couponCode ");
        sb.append("      LEFT JOIN coupon_info AS couponInfo ON couponCode.coupon_id = couponInfo.coupon_id ");
        sb.append("      LEFT JOIN coupon_activity AS activity ON couponCode.activity_id = activity.activity_id ");
//        sb.append("      LEFT JOIN store AS s ON s.store_id = couponInfo.store_id");
        sb.append("      WHERE 1=1 AND couponCode.del_flag = 0");
        if (Objects.nonNull(customerId)){
            sb.append("  AND couponCode.customer_id = :customerId");
        }

        if(CollectionUtils.isNotEmpty(couponActivityTypes)){
            sb.append("  AND activity.activity_type in (:couponActivityTypes) ");
        }

        if (Objects.nonNull(storeId)){
            sb.append("  AND couponInfo.store_id = :storeId");
            sb.append("  AND activity.store_id = :activityStoreId");
        }

        if(CollectionUtils.isNotEmpty(couponIds)){
            sb.append(" AND couponCode.coupon_id in :couponIds");
        }

        //使用状态
        if (Objects.nonNull(useStatus)){
            switch (useStatus){
                case 0 :
                    //未使用
                    sb.append(" AND couponCode.use_status = 0 ");
                    sb.append(" AND couponCode.end_time > now() ");
                    break;
                case 1 :
                    //已使用
                    sb.append(" AND couponCode.use_status = 1 ");
                    break;
                case 2 :
                    //已过期
                    sb.append(" AND couponCode.use_status = 0 ");
                    sb.append(" AND couponCode.end_time < now() ");
                    break;
                default:
                    break;
            }
        }
        //优惠券类型
        if (Objects.nonNull(couponType)) {
            sb.append(" AND couponInfo.coupon_type = :couponType");
        }
        //排序
        sb.append("  ORDER BY couponInfo.coupon_type ASC");
        if (Objects.nonNull(useStatus)){
            switch (useStatus){
                case 0 :
                    sb.append(" , couponCode.acquire_time DESC ");
                    break;
                case 1 :
                    sb.append(" , couponCode.use_date DESC ");
                    break;
                case 2 :
                    sb.append(" , couponCode.end_time DESC ");
                    break;
                default:
                    break;
            }
        }
        sb.append(" , couponInfo.create_time DESC");
        return sb.toString();
    }

    /**
     * request构建查询未使用优惠券总数
     * @return
     */
    public String getQueryUnUseCountSql() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT count(1) AS `unUseCount` ");
        sb.append("      FROM coupon_code AS couponCode ");
        sb.append("      LEFT JOIN coupon_info AS couponInfo ON couponCode.coupon_id = couponInfo.coupon_id ");
        sb.append("      LEFT JOIN coupon_activity AS activity ON couponCode.activity_id = activity.activity_id ");
//        sb.append("      LEFT JOIN store AS s ON s.store_id = couponInfo.store_id");
        sb.append("      WHERE 1=1 AND couponCode.del_flag = 0");
        if (Objects.nonNull(customerId)){
            sb.append("  AND couponCode.customer_id = :customerId");
        }
        if (Objects.nonNull(storeId)){
            sb.append("  AND couponInfo.store_id = :storeId");
            sb.append("  AND activity.store_id = :activityStoreId");
        }
        //未使用
        sb.append(" AND couponCode.use_status = 0 ");
        sb.append(" AND couponCode.end_time > now() ");

        //优惠券类型
        if (Objects.nonNull(couponType)) {
            sb.append(" AND couponInfo.coupon_type = :couponType");
        }

        if(CollectionUtils.isNotEmpty(couponIds)){
            sb.append(" AND couponCode.coupon_id in :couponIds");
        }

        return sb.toString();
    }

    /**
     * request构建查询已使用优惠券总数
     * @return
     */
    public String getQueryUsedCountSql() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT count(1) AS `usedCount` ");
        sb.append("      FROM coupon_code AS couponCode ");
        sb.append("      LEFT JOIN coupon_info AS couponInfo ON couponCode.coupon_id = couponInfo.coupon_id ");
        sb.append("      LEFT JOIN coupon_activity AS activity ON couponCode.activity_id = activity.activity_id ");
//        sb.append("      LEFT JOIN store AS s ON s.store_id = couponInfo.store_id");
        sb.append("      WHERE 1=1 AND couponCode.del_flag = 0");
        if (Objects.nonNull(customerId)){
            sb.append("  AND couponCode.customer_id = :customerId");
        }
        if (Objects.nonNull(storeId)){
            sb.append("  AND couponInfo.store_id = :storeId");
            sb.append("  AND activity.store_id = :activityStoreId");
        }
        //已使用
        sb.append(" AND couponCode.use_status = 1 ");

        //优惠券类型
        if (Objects.nonNull(couponType)) {
            sb.append(" AND couponInfo.coupon_type = :couponType");
        }

        if(CollectionUtils.isNotEmpty(couponIds)){
            sb.append(" AND couponCode.coupon_id in :couponIds");
        }

        return sb.toString();
    }

    /**
     * request构建查询已过期优惠券总数
     * @return
     */
    public String getQueryOverDueCountSql() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT count(1) AS `overDueCount` ");
        sb.append("      FROM coupon_code AS couponCode ");
        sb.append("      LEFT JOIN coupon_info AS couponInfo ON couponCode.coupon_id = couponInfo.coupon_id ");
        sb.append("      LEFT JOIN coupon_activity AS activity ON couponCode.activity_id = activity.activity_id ");
//        sb.append("      LEFT JOIN store AS s ON s.store_id = couponInfo.store_id");
        sb.append("      WHERE 1=1 AND couponCode.del_flag = 0");
        if (Objects.nonNull(customerId)){
            sb.append("  AND couponCode.customer_id = :customerId");
        }
        if (Objects.nonNull(storeId)){
            sb.append("  AND couponInfo.store_id = :storeId");
            sb.append("  AND activity.store_id = :activityStoreId");
        }
        //已过期
        sb.append(" AND couponCode.use_status = 0 ");
        sb.append(" AND couponCode.end_time < now() ");

        //优惠券类型
        if (Objects.nonNull(couponType)) {
            sb.append(" AND couponInfo.coupon_type = :couponType");
        }

        if(CollectionUtils.isNotEmpty(couponIds)){
            sb.append(" AND couponCode.coupon_id in :couponIds");
        }

        return sb.toString();
    }

    /**
     * 查询对象转换
     * @param sqlResult
     * @return
     */
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
                .activityTitle(toStr(item, "activityTitle"))
                .activityDesc(toStr(item, "activityDesc"))
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
