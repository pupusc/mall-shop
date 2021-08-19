package com.wanmi.sbc.goods.api.provider.freight;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.freight.*;
import com.wanmi.sbc.goods.api.response.freight.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>对店铺运费模板查询接口</p>
 * Created by daiyitian on 2018-11-1-下午6:23.
 */
@FeignClient(value = "${application.goods.name}", contextId = "FreightTemplateStoreQueryProvider")
public interface FreightTemplateStoreQueryProvider {

    /**
     * 根据id查询店铺运费模板
     *
     * @param request 包含id的查询请求结构 {@link FreightTemplateStoreByIdRequest}
     * @return 店铺运费模板 {@link FreightTemplateStoreByIdResponse}
     */
    @PostMapping("/goods/${application.goods.version}/freight/store/get-by-id")
    BaseResponse<FreightTemplateStoreByIdResponse> getById(@RequestBody @Valid
                                                                   FreightTemplateStoreByIdRequest request);

    /**
     * 查询店铺运费模板分页列表
     *
     * @param request 分页查询请求结构 {@link FreightTemplateStorePageRequest}
     * @return 店铺运费模板分页列表 {@link FreightTemplateStorePageResponse}
     */
    @PostMapping("/goods/${application.goods.version}/freight/store/page")
    BaseResponse<FreightTemplateStorePageResponse> page(@RequestBody @Valid
                                                                FreightTemplateStorePageRequest request);

    /**
     * 根据店铺id和删除状态查询店铺运费模板列表
     *
     * @param request 包含店铺id和删除状态的查询请求结构 {@link FreightTemplateStoreListByStoreIdAndDeleteFlagRequest}
     * @return 店铺运费模板列表 {@link FreightTemplateStoreListByStoreIdAndDeleteFlagResponse}
     */
    @PostMapping("/goods/${application.goods.version}/freight/store/list-by-store-id-and-delete-flag")
    BaseResponse<FreightTemplateStoreListByStoreIdAndDeleteFlagResponse> listByStoreIdAndDeleteFlag(@RequestBody @Valid
                                                                                                            FreightTemplateStoreListByStoreIdAndDeleteFlagRequest request);

    /**
     * 根据运费模板id和店铺id查询区域id
     *
     * @param request 包含运费模板id和店铺id查询请求结构 {@link FreightTemplateStoreAreaIdByIdAndStoreIdRequest}
     * @return 多个区域id数据 {@link FreightTemplateStoreAreaIdByIdAndStoreIdResponse}
     */
    @PostMapping("/goods/${application.goods.version}/freight/store/query-area-id-by-id-and-store-id")
    BaseResponse<FreightTemplateStoreAreaIdByIdAndStoreIdResponse> queryAreaIdsByIdAndStoreId(@RequestBody @Valid
                                                                                                      FreightTemplateStoreAreaIdByIdAndStoreIdRequest
                                                                                                      request);

}
