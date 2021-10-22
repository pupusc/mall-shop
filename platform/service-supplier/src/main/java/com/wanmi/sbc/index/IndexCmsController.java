package com.wanmi.sbc.index;

import com.wanmi.sbc.goods.api.provider.IndexCmsProvider;
import com.wanmi.sbc.goods.api.request.index.CmsSpecialTopicAddRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public void addSpecialTopic(@RequestBody CmsSpecialTopicAddRequest cmsSpecialTopicAddRequest){
        indexCmsProvider.addSpecialTopic(cmsSpecialTopicAddRequest);
    }
}
