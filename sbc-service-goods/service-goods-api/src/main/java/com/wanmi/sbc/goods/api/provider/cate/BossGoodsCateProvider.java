package com.wanmi.sbc.goods.api.provider.cate;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.cate.BossGoodsCateDeleteByIdRequest;
import com.wanmi.sbc.goods.api.response.cate.BossGoodsCateDeleteByIdResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>商品分类服务操作类</p>
 * author: sunkun
 * Date: 2018-11-05
 */
@FeignClient(value = "${application.goods.name}", contextId = "BossGoodsCateProvider")
public interface BossGoodsCateProvider {

    /**
     * 根据主键删除商品分类
     * @param request 根据主键删除商品分类 {@link BossGoodsCateDeleteByIdRequest}
     * @return {@link BossGoodsCateDeleteByIdResponse}
     */
    @PostMapping("/goods/${application.goods.version}/cate/boss/delete-by-id")
    BaseResponse<BossGoodsCateDeleteByIdResponse> deleteById(@RequestBody @Valid BossGoodsCateDeleteByIdRequest request);




}
