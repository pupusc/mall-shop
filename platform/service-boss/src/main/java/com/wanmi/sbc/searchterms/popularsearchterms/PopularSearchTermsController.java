package com.wanmi.sbc.searchterms.popularsearchterms;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.provider.popularsearchterms.PopularSearchTermsProvider;
import com.wanmi.sbc.setting.api.request.popularsearchterms.PopularSearchTermsDeleteRequest;
import com.wanmi.sbc.setting.api.request.popularsearchterms.PopularSearchTermsModifyRequest;
import com.wanmi.sbc.setting.api.request.popularsearchterms.PopularSearchTermsRequest;
import com.wanmi.sbc.setting.api.request.popularsearchterms.PopularSearchTermsSortRequest;
import com.wanmi.sbc.setting.api.response.popularsearchterms.PopularSearchTermsDeleteResponse;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;


/**
 * @menu 搜索功能
 * @tag feature_d_v0
 * @status done
 */
@RestController
@ApiModel
@Api(tags = "PopularSearchTermsController",description = "热门搜索词增删改服务API")
@RequestMapping("/popular_search_terms")
public class PopularSearchTermsController {

    @Autowired
    private PopularSearchTermsProvider popularSearchTermsQueryProvider;


    @Autowired
    private CommonUtil commonUtil;


    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    /**
     * @description 小程序搜索词查询
     * @menu  新增搜索词
     * @tag feature_d_v0.09
     * @status done
     */
    @ApiOperation(value = "新增热门搜索词")
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public BaseResponse add(@RequestBody @Valid PopularSearchTermsRequest request) {

        request.setCreatePerson(commonUtil.getOperatorId());

        popularSearchTermsQueryProvider.add(request);

        operateLogMQUtil.convertAndSend("搜索词","新增热门搜索测","新增热门搜索词："+request.getPopularSearchKeyword());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * @description 小程序搜索词查询
     * @menu  搜索功能
     * @tag feature_d_v0.09
     * @status done
     */
    @ApiOperation(value = "修改热门搜索词")
    @RequestMapping(value = "/modify",method = RequestMethod.POST)
    public BaseResponse<PopularSearchTermsDeleteResponse> modify(@RequestBody @Valid PopularSearchTermsModifyRequest request) {
        request.setUpdatePerson(commonUtil.getOperatorId());
        operateLogMQUtil.convertAndSend("搜索词","修改热门搜索词","修改热门搜索词："+request.getPopularSearchKeyword());
        return   popularSearchTermsQueryProvider.modify(request);
    }


    /**
     * 删除热门搜索词
     * @param request
     * @return
     */
    @ApiOperation(value = "删除热门搜索词")
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public BaseResponse<PopularSearchTermsDeleteResponse> deleteById( @RequestBody @Valid PopularSearchTermsDeleteRequest request) {
        operateLogMQUtil.convertAndSend("搜索词","删除热门搜索词","删除热门搜索词："+request.getId());
        return popularSearchTermsQueryProvider.deleteById(request);
    }

    /**
     * 热门搜索词排序
     * @param request
     * @return
     */
    @ApiOperation(value = "热门搜索词排序")
    @RequestMapping(value = "/sort_popular_search_terms",method = RequestMethod.POST)
    public BaseResponse sortPopularSearchTerms(@RequestBody @Valid List<PopularSearchTermsSortRequest> request) {
        operateLogMQUtil.convertAndSend("搜索词","热门搜索词排序","热门搜索词排序：");
        return popularSearchTermsQueryProvider.sortPopularSearchTerms(request);
    }

}
