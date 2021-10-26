package com.wanmi.sbc.goods.index.service;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.goods.api.request.index.*;
import com.wanmi.sbc.goods.bean.enums.PublishState;
import com.wanmi.sbc.goods.index.model.IndexFeature;
import com.wanmi.sbc.goods.index.model.IndexModule;
import com.wanmi.sbc.goods.index.repository.IndexFeatureRepository;
import com.wanmi.sbc.goods.index.repository.IndexModuleRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * CMS首页改版2.0
 */
@Service
public class IndexCmsService {

    @Autowired
    private IndexFeatureRepository indexFeatureRepository;
    @Autowired
    private IndexModuleRepository indexModuleRepository;

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
        indexFeature.setPublishState(PublishState.ENABLE);
        indexFeature.setOrderNum(cmsSpecialTopicAddRequest.getOrderNum());
        indexFeatureRepository.save(indexFeature);
    }

    /**
     * 添加主副标题
     * @param cmsTitleAddRequest
     */
    public void addTitle(CmsTitleAddRequest cmsTitleAddRequest){
        IndexModule indexModule = new IndexModule();
        LocalDateTime now = LocalDateTime.now();
        indexModule.setCode(cmsTitleAddRequest.getCode());
        indexModule.setBookListModelId(cmsTitleAddRequest.getBookListModelId());
        indexModule.setOrderNum(cmsTitleAddRequest.getOrderNum());
        indexModule.setTitle(cmsTitleAddRequest.getTitle());
        indexModule.setSubTitle(cmsTitleAddRequest.getSubTitle());
        indexModule.setCreateTime(now);
        indexModule.setUpdateTime(now);
        indexModule.setVersion(1);
        indexModule.setPublishState(PublishState.ENABLE);
        indexModule.setDelFlag(DeleteFlag.NO);
        indexModuleRepository.save(indexModule);
    }

    /**
     * 修改特色栏目
     * @param cmsSpecialTopicUpdateRequest
     */
    public void updateSpecialTopic(CmsSpecialTopicUpdateRequest cmsSpecialTopicUpdateRequest){
        Optional<IndexFeature> opt = indexFeatureRepository.findById(cmsSpecialTopicUpdateRequest.id);
        if(!opt.isPresent() || DeleteFlag.YES.equals(opt.get().getDelFlag())){
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, cmsSpecialTopicUpdateRequest.id + "不存在");
        }
        IndexFeature indexFeature = opt.get();
        if(StringUtils.isNotEmpty(cmsSpecialTopicUpdateRequest.name)){
            indexFeature.setName(cmsSpecialTopicUpdateRequest.name);
        }
        if(StringUtils.isNotEmpty(cmsSpecialTopicUpdateRequest.title)){
            indexFeature.setTitle(cmsSpecialTopicUpdateRequest.title);
        }
        if(StringUtils.isNotEmpty(cmsSpecialTopicUpdateRequest.subTitle)){
            indexFeature.setSubTitle(cmsSpecialTopicUpdateRequest.subTitle);
        }
        if(StringUtils.isNotEmpty(cmsSpecialTopicUpdateRequest.beginTime)){
            indexFeature.setBeginTime(LocalDateTime.parse(cmsSpecialTopicUpdateRequest.beginTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        if(StringUtils.isNotEmpty(cmsSpecialTopicUpdateRequest.endTime)){
            indexFeature.setEndTime(LocalDateTime.parse(cmsSpecialTopicUpdateRequest.endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        if(StringUtils.isNotEmpty(cmsSpecialTopicUpdateRequest.imgUrl)){
            indexFeature.setImgUrl(cmsSpecialTopicUpdateRequest.imgUrl);
        }
        if(StringUtils.isNotEmpty(cmsSpecialTopicUpdateRequest.imgHref)){
            indexFeature.setImgHref(cmsSpecialTopicUpdateRequest.imgHref);
        }
        if(cmsSpecialTopicUpdateRequest.orderNum != null){
            indexFeature.setOrderNum(cmsSpecialTopicUpdateRequest.orderNum);
        }
        if(cmsSpecialTopicUpdateRequest.publishState != null){
            indexFeature.setPublishState(cmsSpecialTopicUpdateRequest.publishState);
//            if(cmsSpecialTopicAddRequest.publishState == 0){
//                indexFeature.setPublishState(PublishState.NOT_ENABLE);
//            }else {
//                indexFeature.setPublishState(PublishState.ENABLE);
//            }
        }
        indexFeature.setUpdateTime(LocalDateTime.now());
        indexFeatureRepository.save(indexFeature);
    }

    /**
     * 查询特色栏目
     * @param cmsSpecialTopicSearchRequest
     * @return
     */
    public Page<IndexFeature> searchSpecialTopicPage(CmsSpecialTopicSearchRequest cmsSpecialTopicSearchRequest){
        return indexFeatureRepository.findAll(indexFeatureRepository.buildSearchCondition(cmsSpecialTopicSearchRequest), PageRequest.of(cmsSpecialTopicSearchRequest.pageNum - 1,
                cmsSpecialTopicSearchRequest.pageSize, Sort.by(Sort.Direction.ASC, "orderNum")));
    }

    /**
     * 删除主副标题
     * @param id
     */
    public void deleteTitle(Integer id){
        Optional<IndexModule> opt = indexModuleRepository.findById(id);
        if(opt.isPresent()){
            IndexModule indexModule = opt.get();
            indexModule.setDelFlag(DeleteFlag.YES);
            indexModuleRepository.save(indexModule);
        }
    }

    /**
     * 修改主副标题
     * @param cmsTitleAddRequest
     */
    public void updateTitle(CmsTitleUpdateRequest cmsTitleUpdateRequest){
        Optional<IndexModule> opt = indexModuleRepository.findById(cmsTitleUpdateRequest.getId());
        if(!opt.isPresent() || DeleteFlag.YES.equals(opt.get().getDelFlag())){
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, cmsTitleUpdateRequest.getId() + "不存在");
        }
        IndexModule indexModule = opt.get();
        if(StringUtils.isNotEmpty(cmsTitleUpdateRequest.getCode())){
            indexModule.setCode(cmsTitleUpdateRequest.getCode());
        }
        if(StringUtils.isNotEmpty(cmsTitleUpdateRequest.getTitle())){
            indexModule.setTitle(cmsTitleUpdateRequest.getTitle());
        }
        if(StringUtils.isNotEmpty(cmsTitleUpdateRequest.getSubTitle())){
            indexModule.setSubTitle(cmsTitleUpdateRequest.getSubTitle());
        }
        if(cmsTitleUpdateRequest.getBookListModelId() != null){
            indexModule.setBookListModelId(cmsTitleUpdateRequest.getBookListModelId());
        }
        if(cmsTitleUpdateRequest.getPublishState() != null){
            indexModule.setPublishState(cmsTitleUpdateRequest.getPublishState());
        }
        indexModule.setUpdateTime(LocalDateTime.now());
        indexModuleRepository.save(indexModule);
    }

    /**
     * 查询主副标题
     * @return
     */
    public List<IndexModule> searchTitle(){
        return indexModuleRepository.findAll(indexModuleRepository.buildSearchCondition());
    }

}
