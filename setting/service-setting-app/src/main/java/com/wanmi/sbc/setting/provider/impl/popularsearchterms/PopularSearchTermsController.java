package com.wanmi.sbc.setting.provider.impl.popularsearchterms;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.api.provider.popularsearchterms.PopularSearchTermsProvider;
import com.wanmi.sbc.setting.api.request.popularsearchterms.PopularSearchTermsDeleteRequest;
import com.wanmi.sbc.setting.api.request.popularsearchterms.PopularSearchTermsModifyRequest;
import com.wanmi.sbc.setting.api.request.popularsearchterms.PopularSearchTermsRequest;
import com.wanmi.sbc.setting.api.request.popularsearchterms.PopularSearchTermsSortRequest;
import com.wanmi.sbc.setting.api.response.popularsearchterms.PopularSearchTermsDeleteResponse;
import com.wanmi.sbc.setting.api.response.popularsearchterms.PopularSearchTermsResponse;
import com.wanmi.sbc.setting.popularsearchterms.model.PopularSearchTerms;
import com.wanmi.sbc.setting.popularsearchterms.service.PopularSearchTermsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>热门搜索词</p>
 *
 * @author weiwenhao
 * @date 2019-04-17
 */
@RestController
public class PopularSearchTermsController implements PopularSearchTermsProvider {


    @Autowired
    PopularSearchTermsService popularSearchTermsService;


    /**
     * 新增热门搜索词
     *
     * @param request
     * @return
     */
    @Override
    public BaseResponse<PopularSearchTermsResponse> add(@Valid PopularSearchTermsRequest request) {
        PopularSearchTerms popularSearchTerms = new PopularSearchTerms();
        KsBeanUtil.copyPropertiesThird(request, popularSearchTerms);
        return BaseResponse.success(new PopularSearchTermsResponse(popularSearchTermsService.add(popularSearchTerms)));
    }


    /**
     * 修改热门搜索词
     * @param request
     * @return
     */
    @Override
    public BaseResponse<PopularSearchTermsDeleteResponse> modify(@Valid PopularSearchTermsModifyRequest request) {
        PopularSearchTerms popularSearchTerms = new PopularSearchTerms();
        KsBeanUtil.copyPropertiesThird(request, popularSearchTerms);
        return popularSearchTermsService.modify(popularSearchTerms);
    }


    /**
     * 删除热门搜索词
     * @param request
     * @return
     */
    @Override
    public BaseResponse<PopularSearchTermsDeleteResponse> deleteById(@Valid PopularSearchTermsDeleteRequest request) {
        PopularSearchTerms popularSearchTerms = new PopularSearchTerms();
        KsBeanUtil.copyPropertiesThird(request, popularSearchTerms);
        return popularSearchTermsService.deleteById(popularSearchTerms);
    }

    /**
     * 热门搜索词排序
     * @param request
     * @return
     */
    @Override
    public BaseResponse sortPopularSearchTerms(@Valid List<PopularSearchTermsSortRequest> request) {
       List<PopularSearchTerms> popularSearchTermsList= request.stream().map(popularSearchTermsSortRequest -> {
            PopularSearchTerms popularSearchTerms=new PopularSearchTerms();
            popularSearchTerms.setSortNumber(popularSearchTermsSortRequest.getSortNumber());
            popularSearchTerms.setId(popularSearchTermsSortRequest.getId());
            return popularSearchTerms;
        }).collect(Collectors.toList());
        return popularSearchTermsService.sortPopularSearchTerms(popularSearchTermsList);
    }

}
