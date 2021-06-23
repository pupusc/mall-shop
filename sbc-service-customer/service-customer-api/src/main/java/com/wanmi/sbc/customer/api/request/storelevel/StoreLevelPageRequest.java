package com.wanmi.sbc.customer.api.request.storelevel;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>商户客户等级表分页查询请求参数</p>
 *
 * @author yang
 * @date 2019-02-27 19:51:30
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreLevelPageRequest extends BaseQueryRequest {
    private static final long serialVersionUID = 1L;

    /**
     * 批量查询-idList
     */
    private List<String> storeLevelIdList;

    /**
     * id
     */
    private Long storeLevelId;

    /**
     * 店铺编号
     */
    private Long storeId;

    /**
     * 等级名称
     */
    private String levelName;

    /**
     * 折扣率
     */
    private BigDecimal discountRate;

    /**
     * 客户升级所需累积支付金额
     */
    private BigDecimal amountConditions;

    /**
     * 客户升级所需累积支付订单笔数
     */
    private Integer orderConditions;

    /**
     * 删除标记 0:未删除 1:已删除
     */
    private Integer delFlag;

    /**
     * 搜索条件:创建时间开始
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTimeBegin;
    /**
     * 搜索条件:创建时间截止
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTimeEnd;

    /**
     * 创建人
     */
    private String createPerson;

    /**
     * 搜索条件:更新时间开始
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime updateTimeBegin;
    /**
     * 搜索条件:更新时间截止
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime updateTimeEnd;

    /**
     * 更新人
     */
    private String updatePerson;

    /**
     * 搜索条件:删除时间开始
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime deleteTimeBegin;
    /**
     * 搜索条件:删除时间截止
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime deleteTimeEnd;

    /**
     * 删除人
     */
    private String deletePerson;

}