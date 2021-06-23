package com.wanmi.sbc.order.api.response.purchase;

import com.wanmi.sbc.goods.bean.vo.GoodsMarketingVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p></p>
 * author: sunkun
 * Date: 2018-12-03
 */
@Data
@ApiModel
public class PurchaseQueryGoodsMarketingListResponse implements Serializable {

    private static final long serialVersionUID = -2691719200903830667L;

    @ApiModelProperty(value = "商品营销关系列表")
    private List<GoodsMarketingVO> goodsMarketingList;
}
