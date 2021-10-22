package com.wanmi.sbc.goods.api.provider;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.index.CmsSpecialTopicAddRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * CMS首页
 */
@FeignClient(value = "${application.goods.name}", contextId = "IndexCmsProvider")
public interface IndexCmsProvider {

    /**
     * 商品标签
     */
    @PostMapping("/goods/${application.goods.version}/special-topic/add")
    BaseResponse addSpecialTopic(CmsSpecialTopicAddRequest cmsSpecialTopicAddRequest);
}
