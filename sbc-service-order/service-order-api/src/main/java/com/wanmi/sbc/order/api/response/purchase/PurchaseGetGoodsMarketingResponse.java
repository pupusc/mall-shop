package com.wanmi.sbc.order.api.response.purchase;

import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.marketing.bean.vo.MarketingViewVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * <p></p>
 * author: sunkun
 * Date: 2018-12-06
 */
@Data
@ApiModel
public class PurchaseGetGoodsMarketingResponse implements Serializable {

    private static final long serialVersionUID = 2323226296834614295L;

    @ApiModelProperty(value = "单品营销信息map", notes = "key为单品id，value为营销列表")
    private Map<String, List<MarketingViewVO>> map;

    @ApiModelProperty(value = "单品信息列表")
    private List<GoodsInfoVO> goodsInfos;
}
