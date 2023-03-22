package com.wanmi.sbc.setting.provider.impl.defaultsearchterms;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.provider.defaultsearchterms.DefaultSearchTermProvider;
import com.wanmi.sbc.setting.api.response.defaultsearchterms.SearchTermBo;
import com.wanmi.sbc.setting.defaultsearchterms.service.DefaultSearchTermsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/22/10:35
 * @Description:
 */

@RestController
public class DefaultSearchTermsController implements DefaultSearchTermProvider {
    @Autowired
    DefaultSearchTermsService defaultSearchTermsService;

    @Override
    public BaseResponse<List<SearchTermBo>> getSearchTermTree(SearchTermBo bo) {
        BaseResponse<List<SearchTermBo>> listDefaultSearchTerms = defaultSearchTermsService.listDefaultSearchTermsByPage(bo);
        return listDefaultSearchTerms;
    }

    @Override
    public BaseResponse<Long> deleteSearchTerm(SearchTermBo bo) {
        BaseResponse<Long> delete = defaultSearchTermsService.delete(bo);
        return delete;
    }

    @Override
    public BaseResponse<Long> updateSearchTerm(SearchTermBo bo) {
        BaseResponse<Long> update = defaultSearchTermsService.update(bo);
        return update;
    }

    @Override
    public BaseResponse<Integer> addSearchTerm(SearchTermBo bo) {
        BaseResponse<Integer> add = defaultSearchTermsService.add(bo);
        return add;
    }

    @Override
    public BaseResponse<Boolean> existName(SearchTermBo bo) {
        boolean isExist = defaultSearchTermsService.existName(bo);
        return BaseResponse.success(isExist);
    }

    @Override
    public BaseResponse<Boolean> existId(SearchTermBo bo) {
        return BaseResponse.success(defaultSearchTermsService.existId(bo));
    }
}
