package com.wanmi.sbc.goods.index.service;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.goods.api.request.index.CmsSpecialTopicAddRequest;
import com.wanmi.sbc.goods.bean.enums.PublishState;
import com.wanmi.sbc.goods.index.model.IndexFeature;
import com.wanmi.sbc.goods.index.repository.IndexFeatureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class IndexFeatureService {

    @Autowired
    private IndexFeatureRepository indexFeatureRepository;

    /**
     * 添加特色栏目
     * @param cmsSpecialTopicAddRequest
     */
    public void addSpecialTopic(CmsSpecialTopicAddRequest cmsSpecialTopicAddRequest){
        if(cmsSpecialTopicAddRequest.getBeginTime() == null || cmsSpecialTopicAddRequest.getEndTime() == null || cmsSpecialTopicAddRequest.getName() == null
            || cmsSpecialTopicAddRequest.getImgUrl() == null || cmsSpecialTopicAddRequest.getImgHref() == null){
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "缺少参数");
        }
        IndexFeature indexFeature = new IndexFeature();
        indexFeature.setName(cmsSpecialTopicAddRequest.getName());
        indexFeature.setTitle(cmsSpecialTopicAddRequest.getTitle());
        indexFeature.setSubTitle(cmsSpecialTopicAddRequest.getSubTitle());
        indexFeature.setBeginTime(LocalDateTime.parse(cmsSpecialTopicAddRequest.getBeginTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        indexFeature.setEndTime(LocalDateTime.parse(cmsSpecialTopicAddRequest.getEndTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        indexFeature.setDelFlag(DeleteFlag.NO);
        indexFeature.setImgUrl(cmsSpecialTopicAddRequest.getImgUrl());
        indexFeature.setImgHref(cmsSpecialTopicAddRequest.getImgHref());
        LocalDateTime now = LocalDateTime.now();
        indexFeature.setCreateTime(now);
        indexFeature.setUpdateTime(now);
        indexFeature.setVersion(1);
        indexFeature.setPublishState(PublishState.NOT_ENABLE);
    }
}
