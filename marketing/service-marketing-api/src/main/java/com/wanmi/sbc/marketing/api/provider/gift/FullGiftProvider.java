package com.wanmi.sbc.marketing.api.provider.gift;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.marketing.api.request.gift.FullGiftAddRequest;
import com.wanmi.sbc.marketing.api.request.gift.FullGiftModifyRequest;
import com.wanmi.sbc.marketing.api.response.gift.FullGiftAddResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @Author: ZhangLingKe
 * @Description: 满赠优惠服务修改操作接口
 * @Date: 2018-11-15 15:56
 */
@FeignClient(value = "${application.marketing.name}", contextId = "FullGiftProvider")
public interface FullGiftProvider {


    /**
     * 新增满赠数据
     * @param addRequest 新增参数 {@link FullGiftAddRequest}
     * @return
     */
    @PostMapping("/marketing/${application.marketing.version}/full-gift/add")
    BaseResponse<FullGiftAddResponse> add(@RequestBody @Valid FullGiftAddRequest addRequest);

    /**
     * 修改满赠数据
     * @param modifyRequest 修改参数 {@link FullGiftModifyRequest}
     * @return
     */
    @PostMapping("/marketing/${application.marketing.version}/full-gift/modify")
    BaseResponse modify(@RequestBody @Valid FullGiftModifyRequest modifyRequest);

}
