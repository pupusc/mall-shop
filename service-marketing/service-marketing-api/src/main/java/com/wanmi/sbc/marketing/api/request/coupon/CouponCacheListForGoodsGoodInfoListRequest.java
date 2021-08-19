package com.wanmi.sbc.marketing.api.request.coupon;

import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponCacheListForGoodsGoodInfoListRequest implements Serializable{

    @ApiModelProperty(value = "商品信息列表")
    private List<GoodsInfoVO> goodsInfoList;

    /**
     * 会员id
     */
    @ApiModelProperty(value = "客户信息")
    private CustomerVO customer;

}
