package com.wanmi.sbc.popularsearchterms;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.provider.searchterms.SearchAssociationalWordQueryProvider;
import com.wanmi.sbc.setting.api.request.searchterms.AssociationLongTailWordLikeRequest;
import com.wanmi.sbc.setting.api.response.searchterms.AssociationLongTailWordLikeResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>热门搜索词</p>
 * @author weiwenhao
 * @date 2019-04-20
 */
@RestController
@ApiModel
@Api(value = "SearchAssociationalWordQueryController",description = "搜索词模糊查询服务API")
@RequestMapping("/search_associational_word")
public class SearchAssociationalWordQueryController {

    @Autowired
    private SearchAssociationalWordQueryProvider searchAssociationalWordQueryProvider;

    /**
     * 模糊搜索词查询
     * @param request
     * @return
     */
    @ApiOperation(value = "模糊搜索词查询")
    @PostMapping("/like_associational_word")
    BaseResponse<AssociationLongTailWordLikeResponse> likeAssociationalWord(@RequestBody @Valid AssociationLongTailWordLikeRequest request){
        return searchAssociationalWordQueryProvider.likeAssociationalWord(request);
    }

}
