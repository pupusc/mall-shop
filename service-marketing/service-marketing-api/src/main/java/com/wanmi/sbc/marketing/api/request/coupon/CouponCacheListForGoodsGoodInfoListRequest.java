package com.wanmi.sbc.marketing.api.request.coupon;

import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.marketing.bean.enums.CouponSceneType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @ApiModelProperty(value = "优惠券使用场景1商详页+领券中心+购物车2专题页")
    private List<String> couponScene = Stream.of(CouponSceneType.DETAIL_CART_CENTER.getType().toString()).collect(Collectors.toList());


}
