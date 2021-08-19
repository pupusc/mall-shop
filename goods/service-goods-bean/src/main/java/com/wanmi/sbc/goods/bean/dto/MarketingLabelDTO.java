package com.wanmi.sbc.goods.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * MarketingLabelDTO
 *
 * @author lipeng
 * @dateTime 2018/11/8 下午4:38
 */
@ApiModel
@Data
public class MarketingLabelDTO implements Serializable {

    private static final long serialVersionUID = 4283126665477864263L;

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
}
