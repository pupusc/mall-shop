package com.wanmi.sbc.goods.api.provider.standard;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.standard.*;
import com.wanmi.sbc.goods.api.response.standard.StandardGoodsDeleteProviderGoodsIdsResponse;
import com.wanmi.sbc.goods.api.response.standard.StandardGoodsListUsedGoodsIdResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>商品库操作接口</p>
 * author: sunkun
 * Date: 2018-11-07
 */
@FeignClient(value = "${application.goods.name}", contextId = "StandardGoodsProvider")
public interface StandardGoodsProvider {

    /**
     * 商品库新增
     * @param request 商品库新增 {@link StandardGoodsAddRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/goods/${application.goods.version}/standard/add")
    BaseResponse<String> add(@RequestBody @Valid StandardGoodsAddRequest request);

    /**
     * 商品库更新
     * @param request 商品库更新 {@link StandardGoodsModifyRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/goods/${application.goods.version}/standard/modify")
    BaseResponse modify(@RequestBody @Valid StandardGoodsModifyRequest request);


    /**
     * 商品库删除
     * @param request 商品库删除 {@link StandardGoodsDeleteByGoodsIdsRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/goods/${application.goods.version}/standard/delete-by-goods-ids")
    BaseResponse delete(@RequestBody @Valid StandardGoodsDeleteByGoodsIdsRequest request);
    /**
     * 商品库删除
     * @param request 供货商品库删除 {@link StandardGoodsDeleteByGoodsIdsRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/goods/${application.goods.version}/standard/delete-provider-by-goods-ids")
    BaseResponse deleteProvider(@RequestBody @Valid StandardGoodsDeleteProviderByGoodsIdsRequest deleteByGoodsIdsRequest);


    /**
     * 商品库删除
     * @param request 供货商品库删除添加删除原因 {@link StandardGoodsDeleteByGoodsIdsRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/goods/${application.goods.version}/standard/delete-provider-by-goods-ids-add-reason")
    BaseResponse<StandardGoodsDeleteProviderGoodsIdsResponse> deleteProviderAddReason(@RequestBody @Valid StandardGoodsDeleteProviderByGoodsIdsRequest deleteByGoodsIdsRequest);


}
