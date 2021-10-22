package com.wanmi.sbc.goods.provider.impl;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.provider.IndexCmsProvider;
import com.wanmi.sbc.goods.api.request.index.CmsSpecialTopicAddRequest;
import com.wanmi.sbc.goods.index.service.IndexFeatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * CMS首页
 */
@RestController
public class IndexCmsController implements IndexCmsProvider {

    @Autowired
    private IndexFeatureService indexFeatureService;

    /**
     * 添加特色栏目
     * @param cmsSpecialTopicAddRequest
     * @return
     */
    @Override
    public BaseResponse addSpecialTopic(CmsSpecialTopicAddRequest cmsSpecialTopicAddRequest) {
        indexFeatureService
        return null;
    }
}
