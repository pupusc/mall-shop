package com.wanmi.sbc.goods.api.provider.brand;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.brand.*;
import com.wanmi.sbc.goods.api.response.brand.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>对品牌操作接口</p>
 * Created by daiyitian on 2018-11-5-下午6:23.
 */
@FeignClient(value = "${application.goods.name}", contextId = "GoodsBrandProvider")
public interface GoodsBrandProvider {

    /**
     * 新增品牌
     *
     * @param request 品牌新增结构 {@link GoodsBrandAddRequest}
     * @return 新增品牌信息 {@link GoodsBrandAddResponse}
     */
    @PostMapping("/goods/${application.goods.version}/brand/add")
    BaseResponse<GoodsBrandAddResponse> add(@RequestBody @Valid GoodsBrandAddRequest request);

    /**
     * 修改品牌
     *
     * @param request 品牌修改结构 {@link GoodsBrandModifyRequest}
     * @return 修改品牌信息 {@link GoodsBrandModifyResponse}
     */
    @PostMapping("/goods/${application.goods.version}/brand/modify")
    BaseResponse<GoodsBrandModifyResponse> modify(@RequestBody @Valid GoodsBrandModifyRequest request);

    /**
     * 根据id删除品牌信息
     *
     * @param request 包含id的删除数据结构 {@link GoodsBrandDeleteByIdRequest}
     * @return 品牌信息 {@link GoodsBrandDeleteResponse}
     */
    @PostMapping("/goods/${application.goods.version}/brand/delete-by-id")
    BaseResponse<GoodsBrandDeleteResponse> delete(@RequestBody @Valid GoodsBrandDeleteByIdRequest request);

}
