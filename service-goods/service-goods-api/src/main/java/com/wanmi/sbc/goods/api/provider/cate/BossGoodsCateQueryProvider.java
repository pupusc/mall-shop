package com.wanmi.sbc.goods.api.provider.cate;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.cate.BossGoodsCateCheckSignChildRequest;
import com.wanmi.sbc.goods.api.request.cate.BossGoodsCateCheckSignGoodsRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>商品分类服务查询类</p>
 * author: sunkun
 * Date: 2018-11-06
 */
@FeignClient(value = "${application.goods.name}", contextId = "BossGoodsCateQueryProvider")
public interface BossGoodsCateQueryProvider {

    /**
     * 验证是否有子类(包含签约分类)
     * @param request 验证是否有子类(包含签约分类) {@link BossGoodsCateCheckSignChildRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/goods/${application.goods.version}/cate/boss/check-sign-child")
    BaseResponse<Integer> checkSignChild(@RequestBody @Valid BossGoodsCateCheckSignChildRequest request);

    /**
     * 验证签约分类下是否有商品
     * @param request 验证签约分类下是否有商品 {@link BossGoodsCateCheckSignGoodsRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/goods/${application.goods.version}/cate/boss/check-sign-goods")
    BaseResponse<Integer> checkSignGoods(@RequestBody @Valid BossGoodsCateCheckSignGoodsRequest request);
}
