package com.wanmi.sbc.goods.index.service;

import com.alibaba.fastjson.JSONArray;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.goods.api.request.image.ImagePageProviderRequest;
import com.wanmi.sbc.goods.api.request.image.ImageSortProviderRequest;
import com.wanmi.sbc.goods.api.request.index.*;
import com.wanmi.sbc.goods.bean.enums.PublishState;
import com.wanmi.sbc.goods.image.model.root.ImageDTO;
import com.wanmi.sbc.goods.index.model.IndexFeature;
import com.wanmi.sbc.goods.index.model.IndexModule;
import com.wanmi.sbc.goods.index.repository.IndexFeatureRepository;
import com.wanmi.sbc.goods.index.repository.IndexModuleRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * CMS首页改版2.0ls
 */
@Service
public class IndexCmsService {

    private static final String CMS_TITLE_CACHE = "cms_title";

    @Autowired
    private IndexFeatureRepository indexFeatureRepository;
    @Autowired
    private IndexModuleRepository indexModuleRepository;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 添加特色栏目
     * @param cmsSpecialTopicAddRequest
     */
    public void addSpecialTopic(CmsSpecialTopicAddRequest cmsSpecialTopicAddRequest) {
        if (cmsSpecialTopicAddRequest.getBeginTime() == null || cmsSpecialTopicAddRequest.getEndTime() == null || cmsSpecialTopicAddRequest.getName() == null
                || cmsSpecialTopicAddRequest.getImgUrl() == null || cmsSpecialTopicAddRequest.getImgHref() == null) {
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
        long count = indexFeatureRepository.count(indexFeatureRepository.buildSearchCondition(new CmsSpecialTopicSearchRequest()));
        indexFeature.setOrderNum((int)count + 1);
        indexFeatureRepository.save(indexFeature);
    }

    /**
     * 修改特色栏目
     * @param cmsSpecialTopicUpdateRequest
     */
    public void updateSpecialTopic(CmsSpecialTopicUpdateRequest cmsSpecialTopicUpdateRequest) {
        Optional<IndexFeature> opt = indexFeatureRepository.findById(cmsSpecialTopicUpdateRequest.id);
        if (!opt.isPresent() || DeleteFlag.YES.equals(opt.get().getDelFlag())) {
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, cmsSpecialTopicUpdateRequest.id + "不存在");
        }
        IndexFeature indexFeature = opt.get();
        if (StringUtils.isNotEmpty(cmsSpecialTopicUpdateRequest.name)) {
            indexFeature.setName(cmsSpecialTopicUpdateRequest.name);
        }
        if (StringUtils.isNotEmpty(cmsSpecialTopicUpdateRequest.title)) {
            indexFeature.setTitle(cmsSpecialTopicUpdateRequest.title);
        }
        if (StringUtils.isNotEmpty(cmsSpecialTopicUpdateRequest.subTitle)) {
            indexFeature.setSubTitle(cmsSpecialTopicUpdateRequest.subTitle);
        }
        if (StringUtils.isNotEmpty(cmsSpecialTopicUpdateRequest.beginTime)) {
            indexFeature.setBeginTime(LocalDateTime.parse(cmsSpecialTopicUpdateRequest.beginTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        if (StringUtils.isNotEmpty(cmsSpecialTopicUpdateRequest.endTime)) {
            indexFeature.setEndTime(LocalDateTime.parse(cmsSpecialTopicUpdateRequest.endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        if (StringUtils.isNotEmpty(cmsSpecialTopicUpdateRequest.imgUrl)) {
            indexFeature.setImgUrl(cmsSpecialTopicUpdateRequest.imgUrl);
        }
        if (StringUtils.isNotEmpty(cmsSpecialTopicUpdateRequest.imgHref)) {
            indexFeature.setImgHref(cmsSpecialTopicUpdateRequest.imgHref);
        }
        if (cmsSpecialTopicUpdateRequest.orderNum != null) {
            indexFeature.setOrderNum(cmsSpecialTopicUpdateRequest.orderNum);
        }
        if (cmsSpecialTopicUpdateRequest.publishState != null) {
            indexFeature.setPublishState(PublishState.fromValue(cmsSpecialTopicUpdateRequest.publishState));
        }
        indexFeature.setUpdateTime(LocalDateTime.now());
        indexFeatureRepository.save(indexFeature);
    }

    /**
     * 查询特色栏目
     * @param cmsSpecialTopicSearchRequest
     * @return
     */
    public Page<IndexFeature> searchSpecialTopicPage(CmsSpecialTopicSearchRequest cmsSpecialTopicSearchRequest) {
        return indexFeatureRepository.findAll(indexFeatureRepository.buildSearchCondition(cmsSpecialTopicSearchRequest), PageRequest.of(cmsSpecialTopicSearchRequest.pageNum,
                cmsSpecialTopicSearchRequest.pageSize, Sort.by(Sort.Direction.ASC, "orderNum")));
    }

    /**
     * 查询所有特色栏目
     * @param cmsSpecialTopicSearchRequest
     * @return
     */
    public List<IndexFeature> listNoPageSpecialTopic(CmsSpecialTopicSearchRequest cmsSpecialTopicSearchRequest) {
        Sort sort = Sort.by(Sort.Direction.ASC, "orderNum");
        return indexFeatureRepository.findAll(indexFeatureRepository.buildSearchCondition(cmsSpecialTopicSearchRequest), sort);
    }

    /**
     * 查询特色栏目
     * @param imageSortProviderRequestList
     * @return
     */
    public void sortSpecialTopic(List<ImageSortProviderRequest> imageSortProviderRequestList) {
        List<Integer> imageIdSet = imageSortProviderRequestList.stream().map(ImageSortProviderRequest::getId).collect(Collectors.toList());
        CmsSpecialTopicSearchRequest cmsSpecialTopicSearchRequest = new CmsSpecialTopicSearchRequest();
        cmsSpecialTopicSearchRequest.setIdColl(imageIdSet);
        List<IndexFeature> indexFeatures = listNoPageSpecialTopic(cmsSpecialTopicSearchRequest);
        if (indexFeatures.size() != imageIdSet.size()) {
            throw new SbcRuntimeException("K-000009");
        }
        Map<Integer, IndexFeature> collect = indexFeatures.stream().collect(Collectors.toMap(IndexFeature::getId, Function.identity(), (k1, k2) -> k1));
        for (ImageSortProviderRequest imageSortProviderParam : imageSortProviderRequestList) {
            IndexFeature indexFeature = collect.get(imageSortProviderParam.getId());
            if (indexFeature == null) {
                throw new SbcRuntimeException("K-000009");
            }
            indexFeature.setOrderNum(imageSortProviderParam.getOrderNum());
            indexFeatureRepository.save(indexFeature);
        }
    }

    /**
     * 添加主副标题
     * @param cmsTitleAddRequest
     */
    public void addTitle(CmsTitleAddRequest cmsTitleAddRequest) {
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
        stringRedisTemplate.delete(CMS_TITLE_CACHE);
    }

    /**
     * 删除主副标题
     * @param id
     */
    public void deleteTitle(Integer id) {
        Optional<IndexModule> opt = indexModuleRepository.findById(id);
        if (opt.isPresent()) {
            IndexModule indexModule = opt.get();
            indexModule.setDelFlag(DeleteFlag.YES);
            indexModuleRepository.save(indexModule);
        }
    }

    /**
     * 修改主副标题
     * @param cmsTitleUpdateRequest
     */
    public void updateTitle(CmsTitleUpdateRequest cmsTitleUpdateRequest) {
        Optional<IndexModule> opt = indexModuleRepository.findById(cmsTitleUpdateRequest.getId());
        if (!opt.isPresent() || DeleteFlag.YES.equals(opt.get().getDelFlag())) {
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, cmsTitleUpdateRequest.getId() + "不存在");
        }
        IndexModule indexModule = opt.get();
        if (StringUtils.isNotEmpty(cmsTitleUpdateRequest.getCode())) {
            indexModule.setCode(cmsTitleUpdateRequest.getCode());
        }
        if (StringUtils.isNotEmpty(cmsTitleUpdateRequest.getTitle())) {
            indexModule.setTitle(cmsTitleUpdateRequest.getTitle());
        }
        if (StringUtils.isNotEmpty(cmsTitleUpdateRequest.getSubTitle())) {
            indexModule.setSubTitle(cmsTitleUpdateRequest.getSubTitle());
        }
        if (cmsTitleUpdateRequest.getBookListModelId() != null) {
            indexModule.setBookListModelId(cmsTitleUpdateRequest.getBookListModelId());
        }
        if (cmsTitleUpdateRequest.getPublishState() != null) {
            indexModule.setPublishState(PublishState.fromValue(cmsTitleUpdateRequest.getPublishState()));
        }
        indexModule.setUpdateTime(LocalDateTime.now());
        indexModuleRepository.save(indexModule);
        stringRedisTemplate.delete(CMS_TITLE_CACHE);
    }

    /**
     * 查询主副标题
     */
    public List<IndexModule> searchTitle(CmsTitleSearchRequest cmsTitleSearchRequest, Boolean useCache) {
        if(BooleanUtils.isTrue(useCache)){
            String s = stringRedisTemplate.opsForValue().get(CMS_TITLE_CACHE);
            if(StringUtils.isNotEmpty(s)){
                return JSONArray.parseArray(s, IndexModule.class);
            }else {
                List<IndexModule> list = indexModuleRepository.findAll(indexModuleRepository.buildSearchCondition(cmsTitleSearchRequest), Sort.by(Sort.Direction.ASC, "orderNum").and(Sort.by(Sort.Direction.DESC, "updateTime")));
                if(CollectionUtils.isNotEmpty(list)){
                    String s1 = JSONArray.toJSONString(list);
                    stringRedisTemplate.opsForValue().set(CMS_TITLE_CACHE, s1, 30, TimeUnit.MINUTES);
                }
            }
        }
        return indexModuleRepository.findAll(indexModuleRepository.buildSearchCondition(cmsTitleSearchRequest), Sort.by(Sort.Direction.ASC, "orderNum").and(Sort.by(Sort.Direction.DESC, "updateTime")));
    }
}
