package com.wanmi.sbc.goods.index.service;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.goods.api.request.index.CmsSpecialTopicAddRequest;
import com.wanmi.sbc.goods.api.request.index.CmsSpecialTopicSearchRequest;
import com.wanmi.sbc.goods.api.request.index.CmsTitleAddRequest;
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
     * @param cmsSpecialTopicAddRequest
     */
    public void updateSpecialTopic(CmsSpecialTopicAddRequest cmsSpecialTopicAddRequest){
        Optional<IndexFeature> opt = indexFeatureRepository.findById(cmsSpecialTopicAddRequest.id);
        if(!opt.isPresent() || DeleteFlag.YES.equals(opt.get().getDelFlag())){
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, cmsSpecialTopicAddRequest.id + "不存在");
        }
        IndexFeature indexFeature = opt.get();
        if(StringUtils.isNotEmpty(cmsSpecialTopicAddRequest.name)){
            indexFeature.setName(cmsSpecialTopicAddRequest.name);
        }
        if(StringUtils.isNotEmpty(cmsSpecialTopicAddRequest.title)){
            indexFeature.setTitle(cmsSpecialTopicAddRequest.title);
        }
        if(StringUtils.isNotEmpty(cmsSpecialTopicAddRequest.subTitle)){
            indexFeature.setSubTitle(cmsSpecialTopicAddRequest.subTitle);
        }
        if(StringUtils.isNotEmpty(cmsSpecialTopicAddRequest.beginTime)){
            indexFeature.setBeginTime(LocalDateTime.parse(cmsSpecialTopicAddRequest.beginTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        if(StringUtils.isNotEmpty(cmsSpecialTopicAddRequest.endTime)){
            indexFeature.setEndTime(LocalDateTime.parse(cmsSpecialTopicAddRequest.endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        if(StringUtils.isNotEmpty(cmsSpecialTopicAddRequest.imgUrl)){
            indexFeature.setImgUrl(cmsSpecialTopicAddRequest.imgUrl);
        }
        if(StringUtils.isNotEmpty(cmsSpecialTopicAddRequest.imgHref)){
            indexFeature.setImgHref(cmsSpecialTopicAddRequest.imgHref);
        }
        if(cmsSpecialTopicAddRequest.orderNum != null){
            indexFeature.setOrderNum(cmsSpecialTopicAddRequest.orderNum);
        }
        if(cmsSpecialTopicAddRequest.publishState != null){
            indexFeature.setPublishState(cmsSpecialTopicAddRequest.publishState);
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
        return indexFeatureRepository.findAll(indexFeatureRepository.buildSearchCondition(cmsSpecialTopicSearchRequest), PageRequest.of(cmsSpecialTopicSearchRequest.pageNum,
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
    public void updateTitle(CmsTitleAddRequest cmsTitleAddRequest){
        Optional<IndexModule> opt = indexModuleRepository.findById(cmsTitleAddRequest.getId());
        if(!opt.isPresent() || DeleteFlag.YES.equals(opt.get().getDelFlag())){
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, cmsTitleAddRequest.getId() + "不存在");
        }
        IndexModule indexModule = opt.get();
        if(StringUtils.isNotEmpty(cmsTitleAddRequest.getCode())){
            indexModule.setCode(cmsTitleAddRequest.getCode());
        }
        if(StringUtils.isNotEmpty(cmsTitleAddRequest.getTitle())){
            indexModule.setTitle(cmsTitleAddRequest.getTitle());
        }
        if(StringUtils.isNotEmpty(cmsTitleAddRequest.getSubTitle())){
            indexModule.setSubTitle(cmsTitleAddRequest.getSubTitle());
        }
        if(cmsTitleAddRequest.getBookListModelId() != null){
            indexModule.setBookListModelId(cmsTitleAddRequest.getBookListModelId());
        }
        if(cmsTitleAddRequest.getPublishState() != null){
            indexModule.setPublishState(cmsTitleAddRequest.getPublishState());
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
