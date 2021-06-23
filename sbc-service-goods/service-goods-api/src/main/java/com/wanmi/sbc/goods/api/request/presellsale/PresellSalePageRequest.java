package com.wanmi.sbc.goods.api.request.presellsale;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.goods.bean.enums.PresellSaleStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ApiModel
public class PresellSalePageRequest extends BaseQueryRequest implements Serializable {


    private static final long serialVersionUID = -5622985783829166647L;

    /**
     * 预售商品活动名称
     */
    @ApiModelProperty(value = "预售商品活动名称")
    private String presellSaleName;

    /**
     * 店铺ID
     */
    @ApiModelProperty(value = "店铺ID")
    private Long storeId;


    /**
     * 活动支付类型  0： 定金  ，1：全款
     */
    @ApiModelProperty(value = "活动支付类型  0： 定金  ，1：全款")
    private Integer presellType;


    /**
     * 预售定金开始时间
     */
    @ApiModelProperty(value = "预售定金开始时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime handselStartTime;


    /**
     * 预售定金结束时间
     */
    @ApiModelProperty(value = "预售定金结束时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime handselEndTime;



    /**
     * 尾款支付开始时间
     */
    @ApiModelProperty(value = "尾款支付开始时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime finalPaymentStartTime;

    /**
     * 尾款支付结束时间
     */
    @ApiModelProperty(value = "尾款支付结束时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime finalPaymentEndTime;

    /**
     * 预售开始时间
     */
    @ApiModelProperty(value = "预售开始时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime presellStartTime;


    /**
     * 预售结束时间
     */
    @ApiModelProperty(value = "预售结束时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime presellEndTime;


    /**
     * 发货日期
     */
    @ApiModelProperty(value = "发货日期")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime deliverTime;


    /**
     * 发货开始时间
     */
    @ApiModelProperty(value = "发货开始时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime deliverTimeStart;

    /**
     * 发货结束时间
     */
    @ApiModelProperty(value = "发货时间结束")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime deliverTimeEnd;

    /**
     * 删除标记
     */
    @ApiModelProperty(value = "删除标记", dataType = "com.wanmi.sbc.common.enums.DeleteFlag")
    private Integer delFlag;


    /**
     * 查询类型，0：全部，1：进行中，2：暂停中，3：未开始，4：已结束
     */
    @ApiModelProperty(value = "查询类型，0：全部，1：进行中，2：暂停中，3：未开始，4：已结束")
    private PresellSaleStatus queryTab;

    /**
     * 是否平台等级 （1平台（自营店铺属于平台等级） 0店铺）
     */
    @ApiModelProperty(value = "是否平台等级 （1平台（自营店铺属于平台等级） 0店铺）")
    private DefaultFlag joinLevelType;

    /**
     * 店铺名称
     */
    @ApiModelProperty(value = "店铺名称")
    private String storeName;
}
