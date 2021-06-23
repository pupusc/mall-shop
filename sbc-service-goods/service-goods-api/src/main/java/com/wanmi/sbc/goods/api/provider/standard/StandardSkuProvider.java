package com.wanmi.sbc.goods.api.provider.standard;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.standard.StandardSkuModifyRequest;
import com.wanmi.sbc.goods.api.response.standard.StandardSkuModifyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>对商品库操作接口</p>
 * Created by daiyitian on 2018-11-5-下午6:23.
 */
@FeignClient(value = "${application.goods.name}", contextId = "StandardSkuProvider")
public interface StandardSkuProvider {

    /**
     * 修改商品库信息
     *
     * @param request 商品库信息修改结构 {@link StandardSkuModifyRequest}
     * @return 商品库信息 {@link StandardSkuModifyResponse}
     */
    @PostMapping("/goods/${application.goods.version}/standard/sku/modify")
    BaseResponse<StandardSkuModifyResponse> modify(@RequestBody @Valid StandardSkuModifyRequest request);

}
