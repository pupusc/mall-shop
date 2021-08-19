package com.wanmi.sbc.customer.api.response.store;

import com.wanmi.sbc.customer.bean.vo.StoreCustomerFollowVO;
import com.wanmi.sbc.customer.bean.vo.StoreEvaluateSumVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 店铺基本信息
 * (安全考虑只保留必要信息,隐藏前端会员无需知道的信息)
 * Created by bail on 2017/11/29.
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class StoreBaseResponse extends StoreCustomerFollowVO{
    private static final long serialVersionUID = -961765692993345430L;

    /**
     * 店铺评价信息
     */
    private StoreEvaluateSumVO storeEvaluateSumVO;

    /**
     * 关注总数
     */
    private Long followSum;

    /**
     * 商品总数
     */
    private Long goodsSum;
}
