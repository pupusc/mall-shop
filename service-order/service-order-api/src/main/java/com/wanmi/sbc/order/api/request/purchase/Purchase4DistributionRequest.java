package com.wanmi.sbc.order.api.request.purchase;

import com.wanmi.sbc.common.base.DistributeChannel;
import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.GoodsIntervalPriceVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 购物车处理社交分销业务
 */
@Data
@ApiModel
public class Purchase4DistributionRequest implements Serializable {


    private static final long serialVersionUID = 8907694333808194891L;

    /**
     * 商品SKU信息
     */
    @ApiModelProperty(value = "商品SKU信息")
    private List<GoodsInfoVO> goodsInfos;

    /**
     * 商品区间价格列表
     */
    @ApiModelProperty(value = "商品区间价格列表")
    private List<GoodsIntervalPriceVO> goodsIntervalPrices;


    /**
     * 分销渠道信息
     */
    @ApiModelProperty(value = "分销渠道信息")
    private DistributeChannel distributeChannel;

    /**
     * 会员信息
     */
    @ApiModelProperty(value = "会员信息")
    private CustomerVO customer;
}
