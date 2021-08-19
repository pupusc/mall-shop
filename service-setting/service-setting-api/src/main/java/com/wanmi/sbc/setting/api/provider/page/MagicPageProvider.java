package com.wanmi.sbc.setting.api.provider.page;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.page.MagicPageMainQueryRequest;
import com.wanmi.sbc.setting.api.request.page.MagicPageMainSaveRequest;
import com.wanmi.sbc.setting.api.response.page.MagicPageMainQueryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>缓存魔方首页dom到数据库中</p>
 *
 * @author lq
 */
@FeignClient(value = "${application.setting.name}", contextId = "MagicPageProvider")
public interface MagicPageProvider {

    /**
     * 查询缓存在数据库中的首页dom
     *
     * @param magicPageMainSaveRequest 查询对象 {@link MagicPageMainSaveRequest}
     * @return  {@link BaseResponse}
     * @author lq
     */
    @PostMapping("/setting/${application.setting.version}/magic-page/save-main")
    BaseResponse saveMain(@RequestBody @Valid MagicPageMainSaveRequest magicPageMainSaveRequest);

}

