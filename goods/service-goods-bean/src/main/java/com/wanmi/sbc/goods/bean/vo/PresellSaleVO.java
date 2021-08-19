package com.wanmi.sbc.goods.bean.vo;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@ApiModel
@Data
public class PresellSaleVO implements Serializable {


    private static final long serialVersionUID = -7597118622814823789L;
    /**
     * 预售活动id
     */
    @ApiModelProperty(value = "预售活动id")
    private String id;
    /**
     * 预售商品活动名称
     */
    @ApiModelProperty(value = "预售活动名称")
    @NotBlank
    private String presellSaleName;

    /**
     * 店铺ID
     */
    @ApiModelProperty(value = "店铺ID")
    private Long storeId;

    /**
     * 店铺名称
     */
    @ApiModelProperty(value = "店铺名称")
    private String storeName;
    /**
     * 活动支付类型  0： 定金  ，1：全款
     */
    @ApiModelProperty(value = "活动支付类型 0： 定金  ，1：全款")
    @NotNull
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
     * 整个活动的开始日期
     */
    @ApiModelProperty(value = "活动开始时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime startTime;


    /**
     * 整个活动的结束日期
     */
    @ApiModelProperty(value = "活动结束时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime endTime;

    /**
     * 是否平台等级 （1平台（自营店铺属于平台等级） 0店铺）
     */
    @ApiModelProperty(value = "是否平台等级 （1平台（自营店铺属于平台等级） 0店铺）")
    @NotNull
    private Integer joinevelType;

    /**
     * 删除标记
     */
    @ApiModelProperty(value = "删除标记")
    private DeleteFlag delFlag;

    /**
     * 预售活动关联商品信息集合
     */
    @ApiModelProperty(value = "预售活动关联商品信息集合")
    @NotNull
    private List<PresellSaleGoodsVO> presellSaleGoodsSaveRequestList;


    /**
     * 是否勾选全选按钮  0 勾选 1 未勾选
     */
    @ApiModelProperty(value = "是否勾选全选按钮  0 勾选 1 未勾选")
    @NotNull
    private Integer selectAll;

    @ApiModelProperty(value = "操作人")
    private String createPerson;

    /**
     * 关联的客户等级   -2：指定客户 -1:全部客户 0:全部等级 other:其他等级
     */
    @ApiModelProperty(value = "关联的客户等级", dataType = "com.wanmi.sbc.marketing.bean.enums.MarketingJoinLevel")
    @NotBlank
    private String joinLevel;

    /**
     * 是否平台等级 （1平台（自营店铺属于平台等级） 0店铺）
     */
    @ApiModelProperty(value = "是否平台等级")
    private DefaultFlag joinLevelType;


    /**
     * 支付定金总数
     */
    @ApiModelProperty(value = "支付定金总数")
    private Integer handselTotalNum;

    /**
     * 支付尾款总数
     */
    @ApiModelProperty(value = "支付尾款总数")
    private Integer finalPaymentNum;

    /**
     * 支付定金总数
     */
    @ApiModelProperty(value = "支付全款总数")
    private Integer fullPaymentNum;


    /**
     * 预售活动状态 1：进行中，2：暂停中，3：未开始，4：已结束
     */
    @ApiModelProperty(value = "预售活动状态")
    private Integer presellSaleStatus;

    /**
     * 是否暂停 0 不暂停 1 暂停
     */
    @ApiModelProperty(value = "是否暂停  0 不暂停 1 暂停")
    private Integer suspended;


}
