package com.wanmi.sbc.store.response;

import com.wanmi.sbc.customer.bean.vo.StoreCustomerFollowVO;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.GoodsIntervalPriceVO;
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

/**
 * 关注店铺列表视图响应
 * Created by daiyitian on 2017/3/24.
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoreCustomerFollowResponse {

    /**
     * 分页信息
     */
    @ApiModelProperty(value = "分页信息")
    private Page<StoreCustomerFollowVO> storePage = new PageImpl<>(new ArrayList<>());

    /**
     * 商品SPU信息
     */
    @ApiModelProperty(value = "商品SPU信息")
    private List<GoodsInfoVO> goodsInfoList = new ArrayList<>();

    /**
     * 商品区间价格列表
     */
    @ApiModelProperty(value = "商品区间价格列表")
    private List<GoodsIntervalPriceVO> goodsIntervalPrices = new ArrayList<>();

}
