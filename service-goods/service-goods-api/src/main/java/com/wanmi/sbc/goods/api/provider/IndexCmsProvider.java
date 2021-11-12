package com.wanmi.sbc.goods.api.provider;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.goods.api.request.image.ImageSortProviderRequest;
import com.wanmi.sbc.goods.api.request.index.*;
import com.wanmi.sbc.goods.api.response.index.IndexFeatureVo;
import com.wanmi.sbc.goods.api.response.index.IndexModuleVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * CMS首页
 */
@FeignClient(value = "${application.goods.name}", contextId = "IndexCmsProvider")
public interface IndexCmsProvider {

    /**
     * 添加特色栏目
     */
    @PostMapping("/goods/${application.goods.version}/special-topic/add")
    BaseResponse addSpecialTopic(@RequestBody CmsSpecialTopicAddRequest cmsSpecialTopicAddRequest);

    /**
     * 修改特色栏目
     */
    @PostMapping("/goods/${application.goods.version}/special-topic/update")
    BaseResponse updateSpecialTopic(@RequestBody CmsSpecialTopicUpdateRequest cmsSpecialTopicUpdateRequest);

    /**
     * 修改特色栏目
     */
    @PostMapping("/goods/${application.goods.version}/special-topic/search")
    BaseResponse<MicroServicePage<IndexFeatureVo>> searchSpecialTopic(@RequestBody CmsSpecialTopicSearchRequest cmsSpecialTopicSearchRequest);

    /**
     * 不分页查询特色栏目
     * @param cmsSpecialTopicSearchRequest
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/special-topic/list-no-page")
    BaseResponse<List<IndexFeatureVo>> listNoPageSpecialTopic(@RequestBody CmsSpecialTopicSearchRequest cmsSpecialTopicSearchRequest);

    /**
     * 排序特色栏目
     * @param imageSortProviderRequestList
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/special-topic/sort")
    BaseResponse sortSpecialTopic(@RequestBody List<ImageSortProviderRequest> imageSortProviderRequestList);
    /**
     * 添加主副标题
     */
    @PostMapping("/goods/${application.goods.version}/title/add")
    BaseResponse addTitle(@RequestBody CmsTitleAddRequest cmsTitleAddRequest);

    /**
     * 删除主副标题
     */
    @PostMapping("/goods/${application.goods.version}/title/delete/{id}")
    BaseResponse deleteTitle(@PathVariable("id") Integer id);

    /**
     * 添加主副标题
     */
    @PostMapping("/goods/${application.goods.version}/title/update")
    BaseResponse updateTitle(@RequestBody CmsTitleUpdateRequest cmsTitleUpdateRequest);

    /**
     * 查询主副标题，前端请求
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/title/front/search")
    BaseResponse<List<IndexModuleVo>> searchTitle();

    /**
     * 查询主副标题，后台请求
     */
    @PostMapping("/goods/${application.goods.version}/title/back/search")
    BaseResponse<List<IndexModuleVo>> searchTitle(@RequestBody CmsTitleSearchRequest cmsTitleSearchRequest);
}
