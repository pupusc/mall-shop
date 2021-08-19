package com.wanmi.sbc.marketing.bean.dto;

import com.wanmi.sbc.marketing.bean.enums.GiftType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: ZhangLingKe
 * @Description: 营销满赠多级优惠实体
 * @Date: 2018-11-20 14:02
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FullGiftLevelDTO implements Serializable {

    private static final long serialVersionUID = -5699841521417838294L;
    /**
     *  满赠多级促销Id
     */
    @ApiModelProperty(value = "满赠多级促销Id")
    private Long giftLevelId;

    /**
     *  满赠Id
     */
    @ApiModelProperty(value = "营销id")
    private Long marketingId;

    /**
     *  满金额赠
     */
    @ApiModelProperty(value = "满金额赠")
    private BigDecimal fullAmount;

    /**
     *  满数量赠
     */
    @ApiModelProperty(value = "满数量赠")
    private Long fullCount;

    /**
     *  赠品赠送的方式 0:全赠  1：赠一个
     */
    @ApiModelProperty(value = "赠品赠送的方式")
    private GiftType giftType;

    /**
     * 满赠赠品明细
     */
    @ApiModelProperty(value = "满赠赠品明细列表")
    private List<FullGiftDetailDTO> fullGiftDetailList;

}
