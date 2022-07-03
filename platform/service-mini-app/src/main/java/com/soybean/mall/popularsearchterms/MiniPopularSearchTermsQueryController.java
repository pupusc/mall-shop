package com.soybean.mall.popularsearchterms;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.provider.popularsearchterms.PopularSearchTermsQueryProvider;
import com.wanmi.sbc.setting.api.response.popularsearchterms.PopularSearchTermsListResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * @menu 搜索功能
 * @tag feature_d_v0
 * @status done
 */
@RestController
@ApiModel
@Api(value = "PopularSearchTermsQueryController", description = "热门搜索词查询服务API")
@RequestMapping("/popular_search_terms")
public class MiniPopularSearchTermsQueryController {

    @Autowired
    private PopularSearchTermsQueryProvider popularSearchTermsQueryProvider;

    /**
     * @description 小程序端-热捜词
     * @menu  搜索功能
     * @tag feature_d_v0.09
     * @status done
     */
    @ApiOperation(value = "小程序端-热捜词")
    @RequestMapping(value = "/mini/list", method = RequestMethod.POST)
    public BaseResponse<PopularSearchTermsListResponse> listMiniPopularSearchTerms() {
        return popularSearchTermsQueryProvider.listPopularSearchTerms(1);
    }

}
