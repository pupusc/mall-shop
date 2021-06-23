package com.wanmi.sbc.goods.api.provider.freight;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.freight.FreightTemplateStoreDeleteByIdAndStoreIdRequest;
import com.wanmi.sbc.goods.api.request.freight.FreightTemplateStoreSaveRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>对店铺运费模板操作接口</p>
 * Created by daiyitian on 2018-11-1-下午6:23.
 */
@FeignClient(value = "${application.goods.name}", contextId = "FreightTemplateStoreProvider")
public interface FreightTemplateStoreProvider {

    /**
     * 新增或更新店铺运费模板
     *
     * @param request 店铺运费模板保存请求结构 {@link FreightTemplateStoreSaveRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/goods/${application.goods.version}/freight/store/save")
    BaseResponse save(@RequestBody @Valid FreightTemplateStoreSaveRequest request);

    /**
     * 根据运费模板id和店铺id删除店铺运费模板
     *
     * @param request 包含运费模板id和店铺id的删除请求结构 {@link FreightTemplateStoreDeleteByIdAndStoreIdRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/goods/${application.goods.version}/freight/store/delete-by-id-and-store-id")
    BaseResponse deleteByIdAndStoreId(@RequestBody @Valid FreightTemplateStoreDeleteByIdAndStoreIdRequest request);

}
