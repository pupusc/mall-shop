package com.wanmi.sbc.goods.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.AppointmentStatus;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>预约抢购VO</p>
 *
 * @author zxd
 * @date 2020-05-21 10:32:23
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentSaleVO implements Serializable {
    private static final long serialVersionUID = 1L;

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

    @ApiModelProperty(value = "店铺名称")
    private String storeName;

    /**
     * 预约类型 0：不预约不可购买  1：不预约可购买
     */
    @ApiModelProperty(value = "预约类型 0：不预约不可购买  1：不预约可购买")
    private Integer appointmentType;

    /**
     * 预约开始时间
     */
    @ApiModelProperty(value = "预约开始时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime appointmentStartTime;

    /**
     * 预约结束时间
     */
    @ApiModelProperty(value = "预约结束时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime appointmentEndTime;

    /**
     * 抢购开始时间
     */
    @ApiModelProperty(value = "抢购开始时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime snapUpStartTime;

    /**
     * 抢购结束时间
     */
    @ApiModelProperty(value = "抢购结束时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime snapUpEndTime;

    /**
     * 发货日期 2020-01-10
     */
    @ApiModelProperty(value = "发货日期 2020-01-10")
    private String deliverTime;

    /**
     * 参加会员 -3:企业会员 -2:付费会员 -1:全部客户 0:全部等级 other:其他等级
     */
    @ApiModelProperty(value = "参加会员  -3:企业会员 -2:付费会员 -1:全部客户 0:全部等级 other:其他等级")
    private String joinLevel;

    /**
     * 是否平台等级 （1平台（自营店铺属于平台等级） 0店铺）
     */
    @ApiModelProperty(value = "是否平台等级 （1平台（自营店铺属于平台等级） 0店铺）")
    private DefaultFlag joinLevelType;

    /**
     * 是否暂停 0:否 1:是
     */
    @ApiModelProperty(value = "是否暂停 0:否 1:是")
    private Integer pauseFlag;

    /**
     * 预约人数
     */
    @ApiModelProperty(value = "预约人数")
    private Integer appointmentCount;

    /**
     * 购买人数
     */
    @ApiModelProperty(value = "购买人数")
    private Integer buyerCount;

    /**
     * 状态 0:全部 1:进行中，2 已暂停 3 未开始 4. 已结束
     */
    @ApiModelProperty(value = "状态 0:全部 1:进行中，2 已暂停 3 未开始 4. 已结束")
    private AppointmentStatus status;

    /**
     * 预约商品活动
     */
    @ApiModelProperty(value = "预约活动商品信息列表")
    private List<AppointmentSaleGoodsVO> appointmentSaleGoods;

    /**
     * 预约商品活动
     */
    @ApiModelProperty(value = "预约活动商品信息")
    private AppointmentSaleGoodsVO appointmentSaleGood;

    /**
     * 预约价
     */
    @ApiModelProperty(value = "预约价")
    private BigDecimal price;

    /**
     * 库存
     */
    @ApiModelProperty(value = "库存")
    private Long stock;

    /**
     * 等级名称
     */
    @ApiModelProperty(value = "等级名称")
    private String levelName;

    public void buildStatus() {
        if (this.appointmentStartTime.isAfter(LocalDateTime.now())) {
            this.status = AppointmentStatus.NO_START;
        }
        if (this.appointmentStartTime.isBefore(LocalDateTime.now()) && this.snapUpEndTime.isAfter(LocalDateTime.now())) {
            this.status = AppointmentStatus.RUNNING;
        }
        if (this.pauseFlag == 1) {
            this.status = AppointmentStatus.SUSPENDED;
        }
        if (this.snapUpEndTime.isBefore(LocalDateTime.now())) {
            this.status = AppointmentStatus.END;
        }
    }

}