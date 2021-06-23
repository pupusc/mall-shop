package com.wanmi.sbc.searchterms.popularsearchterms;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.provider.popularsearchterms.PopularSearchTermsQueryProvider;
import com.wanmi.sbc.setting.api.response.popularsearchterms.PopularSearchTermsListResponse;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * <p>热门搜索词</p>
 * @author weiwenhao
 * @date 2019-04-18
 */
@RestController
@ApiModel
@Api(value = "PopularSearchTermsQueryController",description = "热门搜索词查询服务API")
@RequestMapping("/popular_search_terms")
public class PopularSearchTermsQueryController {

    @Autowired
    private PopularSearchTermsQueryProvider popularSearchTermsQueryProvider;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;


    @ApiOperation(value = "热门搜索词列表查询")
    @RequestMapping(value ="/list",method = RequestMethod.POST)
    public BaseResponse<PopularSearchTermsListResponse> listPopularSearchTerms() {
        operateLogMQUtil.convertAndSend("搜索词","热门搜索词列表查询","热门搜索词列表查询");
        return popularSearchTermsQueryProvider.listPopularSearchTerms();
    }
}
