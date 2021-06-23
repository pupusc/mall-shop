package com.wanmi.sbc.goods.api.response.common;

import com.wanmi.sbc.customer.bean.vo.CommonLevelVO;
import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.GoodsVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsInfoForPurchaseResponse {

    /**
     * 商品列表
     */
    @ApiModelProperty(value = "商品列表")
    private List<GoodsVO> goodsList = new ArrayList<>();

    /**
     * 单品列表
     */
    @ApiModelProperty(value = "单品列表")
    private List<GoodsInfoVO> goodsInfoList = new ArrayList<>();

    /**
     * 会员等级
     */
    @ApiModelProperty(value = "会员等级")
    private HashMap<Long, CommonLevelVO> levelsMap = new HashMap<>();
}
