package com.wanmi.sbc.order.api.response.follow;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.bean.vo.*;
import com.wanmi.sbc.order.bean.vo.TradeVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class FollowListResponse {
//    /**
//     * 商品SKU信息
//     */
//    private MicroServicePage<GoodsInfoVO> goodsInfos =
//            KsBeanUtil.convertPage( new PageImpl<GoodsInfoVO>(new ArrayList<>()),GoodsInfoVO.class);
//

    /**
     * 商品SKU信息
     */
    @ApiModelProperty(value = "商品SKU信息")
    private MicroServicePage<GoodsInfoVO> goodsInfos;

    /**
     * 商品SPU信息
     */
    @ApiModelProperty(value = "商品SPU信息")
    private List<GoodsVO> goodses = new ArrayList<>();

    /**
     * 商品区间价格列表
     */
    @ApiModelProperty(value = "商品区间价格列表")
    private List<GoodsIntervalPriceVO> goodsIntervalPrices = new ArrayList<>();

    /**
     * 商品级别价格列表
     */
    @ApiModelProperty(value = "商品级别价格列表")
    private List<GoodsLevelPriceVO> goodsLevelPrices = new ArrayList<>();

    /**
     * 预约抢购信息
     */
    @ApiModelProperty(value = "预约抢购信息列表")
    private List<AppointmentSaleVO> appointmentSaleVOList;

    /**
     * 预售信息列表
     */
    @ApiModelProperty(value = "预售信息列表")
    private List<BookingSaleVO> bookingSaleVOList;
}
