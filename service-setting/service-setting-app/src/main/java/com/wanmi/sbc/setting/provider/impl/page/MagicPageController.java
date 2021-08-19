package com.wanmi.sbc.setting.provider.impl.page;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.provider.page.MagicPageProvider;
import com.wanmi.sbc.setting.api.request.page.MagicPageMainSaveRequest;
import com.wanmi.sbc.setting.page.service.MagicPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>缓存魔方首页dom到数据库</p>
 *
 * @author lq
 */
@RestController
@Validated
public class MagicPageController implements MagicPageProvider {

    @Autowired
    private MagicPageService magicPageService;

    @Override
    public BaseResponse saveMain(@Valid MagicPageMainSaveRequest magicPageMainSaveRequest) {
        magicPageService.add(magicPageMainSaveRequest.getHtmlString(), magicPageMainSaveRequest.getOperatePerson());
        return BaseResponse.SUCCESSFUL();
    }
}

