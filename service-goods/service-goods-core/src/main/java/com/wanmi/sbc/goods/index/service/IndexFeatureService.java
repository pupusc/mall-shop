package com.wanmi.sbc.goods.index.service;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.goods.api.request.index.CmsSpecialTopicAddRequest;
import com.wanmi.sbc.goods.api.request.index.CmsSpecialTopicSearchRequest;
import com.wanmi.sbc.goods.bean.enums.PublishState;
import com.wanmi.sbc.goods.index.model.IndexFeature;
import com.wanmi.sbc.goods.index.repository.IndexFeatureRepository;
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
 * 特色栏目
 */
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
        indexFeature.setPublishState(PublishState.ENABLE);
        indexFeature.setOrderNum(cmsSpecialTopicAddRequest.getOrderNum());
        indexFeatureRepository.save(indexFeature);
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
        indexFeature.setUpdateTime(LocalDateTime.now());
        indexFeatureRepository.save(indexFeature);
    }

    public Page<IndexFeature> searchSpecialTopicPage(CmsSpecialTopicSearchRequest cmsSpecialTopicSearchRequest){
        return indexFeatureRepository.findAll(buildSearchCondition(cmsSpecialTopicSearchRequest), PageRequest.of(cmsSpecialTopicSearchRequest.pageNum,
                cmsSpecialTopicSearchRequest.pageSize, Sort.by(Sort.Direction.ASC, "orderNum")));
    }

    private Specification<IndexFeature> buildSearchCondition(CmsSpecialTopicSearchRequest cmsSpecialTopicSearchRequest){
        return (Specification<IndexFeature>) (root, criteriaQuery, criteriaBuilder) -> {
            final List<Predicate> conditionList = new ArrayList<>();
            conditionList.add(criteriaBuilder.equal(root.get("delFlag"), DeleteFlag.NO));
            if (cmsSpecialTopicSearchRequest.id != null) {
                conditionList.add(criteriaBuilder.equal(root.get("id"), cmsSpecialTopicSearchRequest.id));
            }
            if (StringUtils.isNotEmpty(cmsSpecialTopicSearchRequest.name)) {
                conditionList.add(criteriaBuilder.like(root.get("name"), cmsSpecialTopicSearchRequest.name + "%"));
            }
            if (cmsSpecialTopicSearchRequest.publishState != null) {
                conditionList.add(criteriaBuilder.equal(root.get("publishState"), cmsSpecialTopicSearchRequest.publishState));
            }
            if(cmsSpecialTopicSearchRequest.state != null){
                LocalDateTime now = LocalDateTime.now();
                if(cmsSpecialTopicSearchRequest.state == 0){
                    //未开始
                    conditionList.add(criteriaBuilder.greaterThan(root.get("beginTime"), now));
                }else if(cmsSpecialTopicSearchRequest.state == 1){
                    //进行中
                    conditionList.add(criteriaBuilder.greaterThanOrEqualTo(root.get("endTime"), now));
                    conditionList.add(criteriaBuilder.lessThanOrEqualTo(root.get("beginTime"), now));
                }else if(cmsSpecialTopicSearchRequest.state == 2){
                    //已结束
                    conditionList.add(criteriaBuilder.lessThan(root.get("endTime"), now));
                }
            }
            return criteriaBuilder.and(conditionList.toArray(new Predicate[conditionList.size()]));
        };
    }

}
