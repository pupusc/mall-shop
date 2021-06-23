package com.wanmi.sbc.goods.api.provider.distributionmatter;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.distributionmatter.DeleteByIdListRequest;
import com.wanmi.sbc.goods.api.request.distributionmatter.DistributionGoodsMatteAddRequest;
import com.wanmi.sbc.goods.api.request.distributionmatter.DistributionGoodsMatterModifyRequest;
import com.wanmi.sbc.goods.api.request.distributionmatter.UpdateRecommendNumRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient(value = "${application.goods.name}", contextId = "DistributionGoodsMatterProvider")
public interface DistributionGoodsMatterProvider {

    /**
     * 新增商品素材
     */
    @PostMapping("/distribution-goods-matter/${application.goods.version}/add")
    BaseResponse add(@RequestBody @Valid DistributionGoodsMatteAddRequest distributionGoodsMatteAddRequest);

    /**
     * 修改商品素材
     */
    @PostMapping("/distribution-goods-matter/${application.goods.version}/modify")
    BaseResponse modify(@RequestBody @Valid DistributionGoodsMatterModifyRequest distributionGoodsMatterModifyRequest);

    /**
     * 批量删除商品素材
     */
    @PostMapping("/distribution-goods-matter/${application.goods.version}/deleteList")
    BaseResponse deleteList(@RequestBody @Valid DeleteByIdListRequest deleteByIdListRequest);




    @PostMapping("/distribution-goods-matter/${application.goods.version}/updataRecomendNumById")
    BaseResponse updataRecomendNumById(@RequestBody @Valid  UpdateRecommendNumRequest updateRecommendNumRequest);


    @PostMapping("/distribution-goods-matter/${application.goods.version}/updataQrcode")
    BaseResponse<DistributionGoodsMatterModifyRequest> updataQrcode(@RequestBody @Valid  DistributionGoodsMatterModifyRequest distributionGoodsMatterModifyReques);
}
