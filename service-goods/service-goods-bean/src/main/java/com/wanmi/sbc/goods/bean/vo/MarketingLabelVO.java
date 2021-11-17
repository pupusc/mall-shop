package com.wanmi.sbc.goods.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 营销标签
 * Created by dyt on 2018/2/28.
 */
@ApiModel
@Data
public class MarketingLabelVO  implements Serializable {

    private static final long serialVersionUID = -3098691550938179678L;

    /**
     * 营销编号
     */
    @ApiModelProperty(value = "营销编号")
    private Long marketingId;

    /**
     * 促销类型 0：满减 1:满折 2:满赠
     * 与Marketing.marketingType保持一致
     */
    @ApiModelProperty(value = "促销类型", dataType = "com.wanmi.sbc.goods.bean.enums.MarketingType")
    private Integer marketingType;

    /**
     * 促销描述
     */
    @ApiModelProperty(value = "促销描述")
    private String marketingDesc;

    /**
     * 活动状态
     */
    @ApiModelProperty(value = "活动状态", dataType = "com.wanmi.sbc.marketing.bean.enums.MarketingStatus")
    private Integer marketingStatus;

    /**
     * 进度比例
     */
    @ApiModelProperty(value = "进度比例，以%为单位")
    private BigDecimal progressRatio;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime endTime;

    /**
     * 积分活动定价
     */
    private Double price;

}
