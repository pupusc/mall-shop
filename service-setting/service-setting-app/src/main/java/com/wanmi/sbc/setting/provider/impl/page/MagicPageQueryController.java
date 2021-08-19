package com.wanmi.sbc.setting.provider.impl.page;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.provider.page.MagicPageQueryProvider;
import com.wanmi.sbc.setting.api.request.page.MagicPageMainQueryRequest;
import com.wanmi.sbc.setting.api.response.page.MagicPageMainQueryResponse;
import com.wanmi.sbc.setting.page.model.root.MagicPage;
import com.wanmi.sbc.setting.page.service.MagicPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>查询缓存在数据库中的页面dom</p>
 *
 * @author lq
 */
@RestController
@Validated
public class MagicPageQueryController implements MagicPageQueryProvider {

    @Autowired
    private MagicPageService magicPageService;

    @Override
    public BaseResponse<MagicPageMainQueryResponse> getMain(MagicPageMainQueryRequest magicPageMainQueryRequest) {
        MagicPage magicPage = magicPageService.get();
        return BaseResponse.success(MagicPageMainQueryResponse.builder()
                .htmlString(magicPage.getHtmlString())
                .createTime(magicPage.getCreateTime())
                .id(magicPage.getId())
                .build());

    }
}

