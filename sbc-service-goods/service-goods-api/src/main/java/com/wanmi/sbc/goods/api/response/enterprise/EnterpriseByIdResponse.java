package com.wanmi.sbc.goods.api.response.enterprise;

import com.wanmi.sbc.customer.bean.vo.StoreLevelVO;
import com.wanmi.sbc.goods.bean.vo.GoodsCustomerPriceVO;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.GoodsIntervalPriceVO;
import com.wanmi.sbc.goods.bean.vo.GoodsLevelPriceVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 企业购商品 设价详情页
 */
@ApiModel
@Data
public class EnterpriseByIdResponse {

    @ApiModelProperty("商品信息")
    private GoodsInfoVO goodsInfo;

    @ApiModelProperty("用户等级列表")
    private List<StoreLevelVO> storeLevels;

    @ApiModelProperty("商品客户等级价格")
    private List<GoodsLevelPriceVO> goodsLevelPrices;

    @ApiModelProperty("商品订货区间价格实体")
    private List<GoodsIntervalPriceVO> goodsIntervalPrices;

    @ApiModelProperty("商品客户价格")
    private List<GoodsCustomerPriceVO> goodsCustomerPrices;
}
