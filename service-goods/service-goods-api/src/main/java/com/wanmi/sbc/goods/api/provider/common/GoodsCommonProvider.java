package com.wanmi.sbc.goods.api.provider.common;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.common.GoodsCommonBatchAddRequest;
import com.wanmi.sbc.goods.api.request.common.GoodsCommonModifyRequest;
import com.wanmi.sbc.goods.api.response.common.GoodsCommonBatchAddResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @author: wanggang
 * @createDate: 2018/11/2 9:52
 * @version: 1.0
 */
@FeignClient(value = "${application.goods.name}", contextId = "GoodsCommonProvider")
public interface GoodsCommonProvider {

    /**
     * 新增/编辑操作中，商品审核状态
     * @param goodsCommonModifyRequest {@link GoodsCommonModifyRequest }
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/common/modify-check-state")
    BaseResponse modifyCheckState(@RequestBody @Valid GoodsCommonModifyRequest goodsCommonModifyRequest);

    /**
     * 新增/编辑操作中，商品审核状态
     * @param request 商品以及相关的批量信息 {@link GoodsCommonBatchAddRequest }
     * @return 批量新增的商品skuId {@link GoodsCommonBatchAddResponse }
     */
    @PostMapping("/goods/${application.goods.version}/common/batch-add")
    BaseResponse<GoodsCommonBatchAddResponse> batchAdd(@RequestBody @Valid GoodsCommonBatchAddRequest request);

}
