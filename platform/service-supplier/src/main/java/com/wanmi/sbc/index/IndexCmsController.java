package com.wanmi.sbc.index;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.goods.api.provider.IndexCmsProvider;
import com.wanmi.sbc.goods.api.request.index.*;
import com.wanmi.sbc.goods.api.response.index.IndexFeatureVo;
import com.wanmi.sbc.goods.api.response.index.IndexModuleVo;
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
     * @description 添加特色栏目
     * @param cmsSpecialTopicAddRequest
     * @menu 后台CMS2.0
     * @status done
     */
    @PostMapping("/special-topic/add")
    public BaseResponse addSpecialTopic(@RequestBody CmsSpecialTopicAddRequest cmsSpecialTopicAddRequest){
        indexCmsProvider.addSpecialTopic(cmsSpecialTopicAddRequest);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * @description 修改特色栏目
     * @param cmsSpecialTopicUpdateRequest
     * @menu 后台CMS2.0
     * @status done
     */
    @PostMapping("/special-topic/update")
    public BaseResponse updateSpecialTopic(@RequestBody CmsSpecialTopicUpdateRequest cmsSpecialTopicUpdateRequest){
        indexCmsProvider.updateSpecialTopic(cmsSpecialTopicUpdateRequest);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * @description 查询特色栏目
     * @param cmsSpecialTopicSearchRequest
     * @menu 后台CMS2.0
     * @status done
     */
    @PostMapping("/special-topic/search")
    public BaseResponse<MicroServicePage<IndexFeatureVo>> searchSpecialTopic(@RequestBody CmsSpecialTopicSearchRequest cmsSpecialTopicSearchRequest){
        return indexCmsProvider.searchSpecialTopic(cmsSpecialTopicSearchRequest);
    }

    /**
     * 添加主副标题
     */
    @PostMapping("/title/add")
    public BaseResponse addTitle(@RequestBody CmsTitleAddRequest cmsTitleAddRequest){
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
     * @description 更新主副标题
     * @param cmsTitleUpdateRequest
     * @menu 后台CMS2.0
     * @status done
     */
    @PostMapping("/title/update")
    public BaseResponse updateTitle(@RequestBody CmsTitleUpdateRequest cmsTitleUpdateRequest){
        indexCmsProvider.updateTitle(cmsTitleUpdateRequest);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * @description 查询主副标题
     * @param cmsTitleSearchRequest
     * @menu 后台CMS2.0
     * @status done
     */
    @PostMapping("/title/search")
    public BaseResponse<List<IndexModuleVo>> searchTitle(@RequestBody CmsTitleSearchRequest cmsTitleSearchRequest){
        return indexCmsProvider.searchTitle(cmsTitleSearchRequest);
    }

}
