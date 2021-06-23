package com.wanmi.sbc.setting.api.provider.page;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.page.MagicPageMainQueryRequest;
import com.wanmi.sbc.setting.api.response.page.MagicPageMainQueryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>查询缓存在数据库中的页面dom</p>
 *
 * @author lq
 */
@FeignClient(value = "${application.setting.name}", contextId = "MagicPageQueryProvider")
public interface MagicPageQueryProvider {

    /**
     * 查询缓存在数据库中的首页dom
     *
     * @param magicPageMainQueryRequest 查询对象 {@link MagicPageMainQueryRequest}
     * @return 建站首页dom字符串 {@link MagicPageMainQueryResponse}
     * @author lq
     */
    @PostMapping("/setting/${application.setting.version}/magic-page/main")
    BaseResponse<MagicPageMainQueryResponse> getMain(@RequestBody @Valid MagicPageMainQueryRequest magicPageMainQueryRequest);

}

