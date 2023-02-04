package com.wanmi.sbc.popularsearchterms;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.provider.defaultsearchterms.DefaultSearchTermsQueryProvider;
import com.wanmi.sbc.setting.api.response.defaultsearchterms.DefaultSearchTermsListResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description 默认搜索词
 * @Author zh
 * @Date  2023/2/4 11:26
 */
@RestController
@ApiModel
@Api(value = "DefaultSearchTermsQueryController", description = "默认词查询服务API")
@RequestMapping("/default_search_terms/v2")
public class DefaultSearchTermsQueryController {

    @Autowired
    private DefaultSearchTermsQueryProvider defaultSearchTermsQueryProvider;


    @ApiOperation(value = "默认词列表查询")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public BaseResponse<List<DefaultSearchTermsListResponse>> listDefaultSearchTerms() {
        return defaultSearchTermsQueryProvider.listDefaultSearchTerms(0);
    }

}
