package com.wanmi.sbc.goods.api.provider;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.goods.api.request.index.CmsSpecialTopicAddRequest;
import com.wanmi.sbc.goods.api.request.index.CmsSpecialTopicSearchRequest;
import com.wanmi.sbc.goods.api.request.index.CmsTitleAddRequest;
import com.wanmi.sbc.goods.api.response.index.IndexFeatureDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * CMS首页
 */
@FeignClient(value = "${application.goods.name}", contextId = "IndexCmsProvider")
public interface IndexCmsProvider {

    /**
     * 添加特色栏目
     */
    @PostMapping("/goods/${application.goods.version}/special-topic/add")
    BaseResponse addSpecialTopic(CmsSpecialTopicAddRequest cmsSpecialTopicAddRequest);

    /**
     * 修改特色栏目
     */
    @PostMapping("/goods/${application.goods.version}/special-topic/update")
    BaseResponse updateSpecialTopic(CmsSpecialTopicAddRequest cmsSpecialTopicAddRequest);

    /**
     * 修改特色栏目
     */
    @PostMapping("/goods/${application.goods.version}/special-topic/search")
    BaseResponse<MicroServicePage<IndexFeatureDto>> searchSpecialTopic(CmsSpecialTopicSearchRequest cmsSpecialTopicSearchRequest);

    /**
     * 添加主副标题
     */
    @PostMapping("/goods/${application.goods.version}/title/add")
    BaseResponse addTitle(CmsTitleAddRequest cmsTitleAddRequest);

    /**
     * 删除主副标题
     */
    @PostMapping("/goods/${application.goods.version}/title/delete")
    BaseResponse deleteTitle(Integer id);

    /**
     * 添加主副标题
     */
    @PostMapping("/goods/${application.goods.version}/title/update")
    BaseResponse updateTitle(CmsTitleAddRequest cmsTitleAddRequest);

    /**
     * 查询主副标题
     */
    @PostMapping("/goods/${application.goods.version}/title/search")
    BaseResponse searchTitle();
}
