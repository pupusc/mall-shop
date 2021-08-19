package com.wanmi.sbc.goods.api.provider.brand;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.brand.*;
import com.wanmi.sbc.goods.api.response.brand.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>对品牌查询接口</p>
 * Created by daiyitian on 2018-11-5-下午6:23.
 */
@FeignClient(value = "${application.goods.name}", contextId = "GoodsBrandQueryProvider")
public interface GoodsBrandQueryProvider {

    /**
     * 分页查询品牌列表
     *
     * @param request 品牌查询数据结构 {@link GoodsBrandPageRequest}
     * @return 品牌分页列表 {@link GoodsBrandPageResponse}
     */
    @PostMapping("/goods/${application.goods.version}/brand/page")
    BaseResponse<GoodsBrandPageResponse> page(@RequestBody @Valid GoodsBrandPageRequest request);

    /**
     * 条件查询品牌列表
     *
     * @param request 品牌查询数据结构 {@link GoodsBrandListRequest}
     * @return 品牌列表 {@link GoodsBrandListResponse}
     */
    @PostMapping("/goods/${application.goods.version}/brand/list")
    BaseResponse<GoodsBrandListResponse> list(@RequestBody @Valid GoodsBrandListRequest request);

    /**
     * 根据id查询品牌信息
     *
     * @param request 包含id的查询数据结构 {@link GoodsBrandByIdRequest}
     * @return 品牌信息 {@link GoodsBrandByIdResponse}
     */
    @PostMapping("/goods/${application.goods.version}/brand/get-by-id")
    BaseResponse<GoodsBrandByIdResponse> getById(@RequestBody @Valid GoodsBrandByIdRequest request);

    /**
     * 根据ids批量查询品牌列表
     *
     * @param request 包含ids的查询数据结构 {@link GoodsBrandByIdsRequest}
     * @return 品牌列表 {@link GoodsBrandByIdsResponse}
     */
    @PostMapping("/goods/${application.goods.version}/brand/list-by-ids")
    BaseResponse<GoodsBrandByIdsResponse> listByIds(@RequestBody @Valid GoodsBrandByIdsRequest request);

}
