package com.wanmi.sbc.marketing.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p>订单相关营销信息请求Bean</p>
 * Created by of628-wenzhi on 2018-02-27-下午4:30.
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TradeMarketingDTO implements Serializable {

    private static final long serialVersionUID = -4198768506845115947L;

    /**
     * 营销活动Id
     */
    @ApiModelProperty(value = "营销活动Id")
    private Long marketingId;
    /**
     * 店铺id
     */
    @ApiModelProperty(value = "营销活动Id")
    private Long storeId;

    /**
     * 营销等级id
     */
    @ApiModelProperty(value = "营销等级id")
    private Long marketingLevelId;

    /**
     * 该营销活动关联的订单商品id集合
     */
    @ApiModelProperty(value = "该营销活动关联的订单商品id集合")
    private List<String> skuIds;

    /**
     * 如果是满赠，则填入用户选择的赠品id集合
     */
    @ApiModelProperty(value = "如果是满赠，则填入用户选择的赠品id集合")
    private List<String> giftSkuIds;
    /**
     * 如果是加价购，则填入用户选择的换购商品id集合
     */
    @ApiModelProperty(value = "如果是加价购，则填入用户选择的换购商品id集合")
    private List<String> markupSkuIds;
}
