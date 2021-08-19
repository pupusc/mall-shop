package com.wanmi.sbc.setting.provider.impl.popularsearchterms;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.provider.popularsearchterms.PopularSearchTermsQueryProvider;
import com.wanmi.sbc.setting.api.response.popularsearchterms.PopularSearchTermsListResponse;
import com.wanmi.sbc.setting.popularsearchterms.service.PopularSearchTermsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>热门搜索词</p>
 *
 * @author weiwenhao
 * @date 2019-04-18
 */
@RestController
public class PopularSearchTermsQueryController implements PopularSearchTermsQueryProvider {


    @Autowired
    PopularSearchTermsService popularSearchTermsService;

    @Override
    public BaseResponse<PopularSearchTermsListResponse> listPopularSearchTerms() {
        return popularSearchTermsService.listPopularSearchTerms();
    }
}
