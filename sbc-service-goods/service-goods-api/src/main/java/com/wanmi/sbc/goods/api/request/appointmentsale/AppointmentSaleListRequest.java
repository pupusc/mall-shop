package com.wanmi.sbc.goods.api.request.appointmentsale;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>预约抢购列表查询请求参数</p>
 *
 * @author zxd
 * @date 2020-05-21 10:32:23
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentSaleListRequest extends BaseRequest {
    private static final long serialVersionUID = 1L;


    /**
     * 批量查询-idList
     */
    @ApiModelProperty(value = "批量查询-idList")
    private List<Long> idList;

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 活动名称
     */
    @ApiModelProperty(value = "活动名称")
    private String activityName;

    /**
     * 商户id
     */
    @ApiModelProperty(value = "商户id")
    private Long storeId;

    /**
     * 预约类型 0：不预约不可购买  1：不预约可购买
     */
    @ApiModelProperty(value = "预约类型 0：不预约不可购买  1：不预约可购买")
    private Integer appointmentType;

    /**
     * 搜索条件:预约开始时间开始
     */
    @ApiModelProperty(value = "搜索条件:预约开始时间开始")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime appointmentStartTimeBegin;
    /**
     * 搜索条件:预约开始时间截止
     */
    @ApiModelProperty(value = "搜索条件:预约开始时间截止")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime appointmentStartTimeEnd;

    /**
     * 搜索条件:预约结束时间开始
     */
    @ApiModelProperty(value = "搜索条件:预约结束时间开始")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime appointmentEndTimeBegin;
    /**
     * 搜索条件:预约结束时间截止
     */
    @ApiModelProperty(value = "搜索条件:预约结束时间截止")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime appointmentEndTimeEnd;

    /**
     * 搜索条件:抢购开始时间开始
     */
    @ApiModelProperty(value = "搜索条件:抢购开始时间开始")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime snapUpStartTimeBegin;
    /**
     * 搜索条件:抢购开始时间截止
     */
    @ApiModelProperty(value = "搜索条件:抢购开始时间截止")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime snapUpStartTimeEnd;

    /**
     * 搜索条件:抢购结束时间开始
     */
    @ApiModelProperty(value = "搜索条件:抢购结束时间开始")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime snapUpEndTimeBegin;
    /**
     * 搜索条件:抢购结束时间截止
     */
    @ApiModelProperty(value = "搜索条件:抢购结束时间截止")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime snapUpEndTimeEnd;

    /**
     * 发货日期 2020-01-10
     */
    @ApiModelProperty(value = "发货日期 2020-01-10")
    private String deliverTime;

    /**
     * 参加会员  -1:全部客户 0:全部等级 other:其他等级
     */
    @ApiModelProperty(value = "参加会员  -1:全部客户 0:全部等级 other:其他等级")
    private String joinLevel;

    /**
     * 是否平台等级 （1平台（自营店铺属于平台等级） 0店铺）
     */
    @ApiModelProperty(value = "是否平台等级 （1平台（自营店铺属于平台等级） 0店铺）")
    private DefaultFlag joinLevelType;

    /**
     * 是否删除标志 0：否，1：是
     */
    @ApiModelProperty(value = "是否删除标志 0：否，1：是")
    private DeleteFlag delFlag;

    /**
     * 是否暂停 0:否 1:是
     */
    @ApiModelProperty(value = "是否暂停 0:否 1:是")
    private Integer pauseFlag;

    /**
     * 搜索条件:创建时间开始
     */
    @ApiModelProperty(value = "搜索条件:创建时间开始")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTimeBegin;
    /**
     * 搜索条件:创建时间截止
     */
    @ApiModelProperty(value = "搜索条件:创建时间截止")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTimeEnd;

    /**
     * 搜索条件:更新时间开始
     */
    @ApiModelProperty(value = "搜索条件:更新时间开始")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime updateTimeBegin;
    /**
     * 搜索条件:更新时间截止
     */
    @ApiModelProperty(value = "搜索条件:更新时间截止")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime updateTimeEnd;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createPerson;

    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人")
    private String updatePerson;

}