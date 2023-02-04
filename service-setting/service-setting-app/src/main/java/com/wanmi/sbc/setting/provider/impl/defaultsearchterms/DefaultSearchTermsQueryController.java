package com.wanmi.sbc.setting.provider.impl.defaultsearchterms;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.provider.defaultsearchterms.DefaultSearchTermsQueryProvider;
import com.wanmi.sbc.setting.api.provider.popularsearchterms.PopularSearchTermsQueryProvider;
import com.wanmi.sbc.setting.api.response.defaultsearchterms.DefaultSearchTermsListResponse;
import com.wanmi.sbc.setting.api.response.popularsearchterms.PopularSearchTermsListResponse;
import com.wanmi.sbc.setting.defaultsearchterms.service.DefaultSearchTermsService;
import com.wanmi.sbc.setting.popularsearchterms.service.PopularSearchTermsService;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description: 默认搜索词
 * @Author zh
 * @Date 2023/2/4 11:14
 */
@RestController
public class DefaultSearchTermsQueryController implements DefaultSearchTermsQueryProvider {


    @Autowired
    DefaultSearchTermsService defaultSearchTermsService;

    @Override
    public BaseResponse<List<DefaultSearchTermsListResponse>> listDefaultSearchTerms(Integer defaultChannel) {
        if (defaultChannel == null) {
            defaultChannel = 0;
        }
        return defaultSearchTermsService.listDefaultSearchTerms(defaultChannel);
    }
}
