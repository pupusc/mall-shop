package com.wanmi.sbc.goods.api.request.cate;

import com.wanmi.sbc.goods.bean.dto.CouponInfoForScopeNamesDTO;
import com.wanmi.sbc.goods.bean.dto.CouponMarketingScopeDTO;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * com.wanmi.sbc.goods.api.request.goodscate.GoodsCateListRequest
 * 根据条件查询商品分类列表信息请求对象
 * @author lipeng
 * @dateTime 2018/11/1 下午3:25
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsCateListCouponDetailRequest implements Serializable {

    private List<CouponInfoForScopeNamesDTO> couponInfoForScopeNamesDTOS;

    private Map<String, List<CouponMarketingScopeDTO>> couponMarketingScopeMap;

    public GoodsCateListCouponDetailRequest(List<CouponInfoForScopeNamesDTO> couponInfoForScopeNamesDTOS, Map<String, List<CouponMarketingScopeDTO>> couponMarketingScopeMap) {
        this.couponInfoForScopeNamesDTOS = couponInfoForScopeNamesDTOS;
        this.couponMarketingScopeMap = couponMarketingScopeMap;
    }

    private Map<String,List<String>> scopeIdsMap;
}
