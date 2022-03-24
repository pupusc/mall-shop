package com.wanmi.sbc.marketing.coupon.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.marketing.bean.vo.CouponCodeVO;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;

import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.util.DateUtil;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@Data
public class CouponCodeWillExpireRequest implements Serializable {
    private static final long serialVersionUID = 8784667574170687982L;
    /**
     * 临期日期
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime willExpireDate;

    private List<String> customerId;

    public String getQuerySql() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT couponCode.coupon_code_id AS `couponCodeId`, ");
        sb.append("      couponCode.coupon_code AS `couponCode`, ");
        sb.append("      couponCode.customer_id AS `CustomerId`, ");
        sb.append("      couponInfo.coupon_name AS `couponName`, ");
        sb.append("      couponInfo.coupon_id AS `couponId`, ");
        sb.append("      couponCode.end_time AS `endTime`");
        sb.append("      FROM coupon_code AS couponCode ");
        sb.append("      LEFT JOIN coupon_info AS couponInfo ON couponCode.coupon_id = couponInfo.coupon_id ");
        sb.append("      WHERE 1=1 AND couponCode.del_flag = 0");
        sb.append(" AND couponCode.use_status = 0 ");
        sb.append(" AND couponCode.end_time > now() ");
        if (Objects.nonNull(willExpireDate)){
            sb.append("  AND couponCode.end_time < :willExpireDate");
        }
        if(CollectionUtils.isNotEmpty(customerId)){
            sb.append(" AND couponCode.customer_id in (:customerId) ");
        }
        return sb.toString();
    }


    public static List<CouponCodeVO> converter(List<Map<String, Object>> sqlResult) {
        return sqlResult.stream().map(item ->
                CouponCodeVO.builder()
                        .couponCodeId(toStr(item, "couponCodeId"))
                        .couponCode(toStr(item, "couponCode"))
                        .endTime(toDate(item, "endTime"))
                        .couponId(toStr(item, "couponId"))
                        .couponName(toStr(item, "couponName"))
                        .customerId(toStr(item,"customerId"))
                        .build()
        ).collect(Collectors.toList());
    }

    private static String toStr(Map<String, Object> map, String key) {
        return map.get(key) != null ? map.get(key).toString() : null;
    }

    private static LocalDateTime toDate(Map<String, Object> map, String key) {
        return map.get(key) != null ? DateUtil.parse(map.get(key).toString(), "yyyy-MM-dd HH:mm:ss.S") : null;
    }
}
