package com.wanmi.sbc.index;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.goods.api.provider.IndexCmsProvider;
import com.wanmi.sbc.goods.api.request.index.CmsSpecialTopicAddRequest;
import com.wanmi.sbc.goods.api.request.index.CmsSpecialTopicSearchRequest;
import com.wanmi.sbc.goods.api.request.index.CmsTitleAddRequest;
import com.wanmi.sbc.goods.api.response.index.IndexFeatureDto;
import com.wanmi.sbc.goods.api.response.index.IndexModuleDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * CMS首页
 */
@RestController
@RequestMapping("/cms")
public class IndexCmsController {

    @Autowired
    private IndexCmsProvider indexCmsProvider;

    /**
     * 添加特色栏目
     */
    @PostMapping("/special-topic/add")
    public BaseResponse addSpecialTopic(@RequestBody CmsSpecialTopicAddRequest cmsSpecialTopicAddRequest){
        indexCmsProvider.addSpecialTopic(cmsSpecialTopicAddRequest);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 修改特色栏目
     */
    @PostMapping("/special-topic/update")
    public BaseResponse updateSpecialTopic(@RequestBody CmsSpecialTopicAddRequest cmsSpecialTopicAddRequest){
        indexCmsProvider.updateSpecialTopic(cmsSpecialTopicAddRequest);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 查询特色栏目
     */
    @PostMapping("/special-topic/search")
    public BaseResponse<MicroServicePage<IndexFeatureDto>> searchSpecialTopic(@RequestBody CmsSpecialTopicSearchRequest cmsSpecialTopicSearchRequest){
        return indexCmsProvider.searchSpecialTopic(cmsSpecialTopicSearchRequest);
    }

    /**
     * 添加主副标题
     */
    @PostMapping("/title/add")
    public BaseResponse addTitle(CmsTitleAddRequest cmsTitleAddRequest){
        indexCmsProvider.addTitle(cmsTitleAddRequest);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 删除主副标题
     */
    @PostMapping("/title/delete")
    public BaseResponse deleteTitle(Integer id){
        indexCmsProvider.deleteTitle(id);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 更新主副标题
     */
    @PostMapping("/title/update")
    public BaseResponse updateTitle(CmsTitleAddRequest cmsTitleAddRequest){
        indexCmsProvider.updateTitle(cmsTitleAddRequest);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 查询主副标题
     */
    @PostMapping("/title/search")
    public BaseResponse<List<IndexModuleDto>> searchTitle(){
        return indexCmsProvider.searchTitle();
    }

}
