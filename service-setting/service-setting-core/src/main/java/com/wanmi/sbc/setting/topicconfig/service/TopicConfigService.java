package com.wanmi.sbc.setting.topicconfig.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.SortType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.*;
import com.wanmi.sbc.setting.api.request.topicconfig.*;
import com.wanmi.sbc.setting.api.response.TopicStoreyContentResponse;
import com.wanmi.sbc.setting.bean.dto.TopicHeadImageDTO;
import com.wanmi.sbc.setting.bean.dto.TopicStoreyCouponDTO;
import com.wanmi.sbc.setting.bean.dto.TopicStoreyDTO;
import com.wanmi.sbc.setting.bean.dto.TopicStoreyContentDTO;
import com.wanmi.sbc.setting.bean.enums.TopicStoreyType;
import com.wanmi.sbc.setting.bean.vo.TopicActivityVO;
import com.wanmi.sbc.setting.bean.vo.TopicConfigVO;
import com.wanmi.sbc.setting.topicconfig.model.root.TopicHeadImage;
import com.wanmi.sbc.setting.topicconfig.model.root.Topic;
import com.wanmi.sbc.setting.topicconfig.model.root.TopicStorey;
import com.wanmi.sbc.setting.topicconfig.model.root.TopicStoreyContent;
import com.wanmi.sbc.setting.topicconfig.repository.TopicHeadImageRepository;
import com.wanmi.sbc.setting.topicconfig.repository.TopicRepository;
import com.wanmi.sbc.setting.topicconfig.repository.TopicStoreyContentRepository;
import com.wanmi.sbc.setting.topicconfig.repository.TopicStoreyRepository;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springfox.documentation.spring.web.json.Json;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TopicConfigService {
    @Autowired
    private TopicRepository topicSettingRepository;

    @Autowired
    private TopicHeadImageRepository topicHeadImageRepository;

    @Autowired
    private TopicStoreyRepository storeyRepository;

    @Autowired
    private TopicStoreyContentRepository contentRepository;

    public void addTopic(TopicConfigAddRequest request) {
        Topic topic = KsBeanUtil.convert(request, Topic.class);
        topic.setTopicKey(UUIDUtil.getUUID());
        topic.setTrackKey(topic.getTopicKey());
        topic.setCreateTime(LocalDateTime.now());
        topic.setUpdateTime(LocalDateTime.now());
        topic.setDeleted(DeleteFlag.NO.toValue());
        topic.setStatus(0);
        topicSettingRepository.save(topic);
    }

    public void modifyTopic(TopicConfigModifyRequest request) {
        Topic oldTopic = topicSettingRepository.getOne(request.getId());
        if(oldTopic == null){
            addTopic(request);
        }
        Topic topic = KsBeanUtil.convert(oldTopic, Topic.class);
        topic.setTopicName(request.getTopicName());
        topic.setTopicColor(request.getTopicColor());
        topic.setNavigationColor(request.getNavigationColor());
        topic.setUpdateTime(LocalDateTime.now());
        topicSettingRepository.save(topic);
    }

    public void enableTopic(EnableTopicRequest request) {
        topicSettingRepository.enable(request.getId(),request.getOptType());
    }

    public MicroServicePage<TopicConfigVO> listTopic(TopicQueryRequest request) {
        BaseQueryRequest pageQuery = new BaseQueryRequest();
        pageQuery.setPageNum(request.getPageNum());
        pageQuery.setPageSize(request.getPageSize());
        pageQuery.setSortColumn("createTime");
        pageQuery.setSortRole("desc");
        Page<Topic> page = topicSettingRepository.findAll(getTopicWhereCriteria(request), pageQuery.getPageRequest());
        if(page == null){
            return new MicroServicePage<>();
        }
        return KsBeanUtil.convertPage(page,TopicConfigVO.class);
    }


    public void addHeadImage(HeadImageConfigAddRequest request){
        TopicHeadImage headImage = KsBeanUtil.convert(request, TopicHeadImage.class);
        headImage.setCreateTime(LocalDateTime.now());
        headImage.setUpdateTime(LocalDateTime.now());
        headImage.setDeleted(DeleteFlag.NO.toValue());
        topicHeadImageRepository.save(headImage);
    }

    public void modifyHeadImage(TopicHeadImageModifyRequest request){
        TopicHeadImage headImage = KsBeanUtil.convert(request, TopicHeadImage.class);
        headImage.setCreateTime(LocalDateTime.now());
        headImage.setUpdateTime(LocalDateTime.now());
        headImage.setDeleted(DeleteFlag.NO.toValue());
        topicHeadImageRepository.save(headImage);
    }

    public List<TopicHeadImageDTO> listHeadImage(TopicHeadImageQueryRequest request){
        List<TopicHeadImage> list = topicHeadImageRepository.getByTopicId(request.getTopicId());
        if(CollectionUtils.isEmpty(list)){
            return Collections.EMPTY_LIST;
        }
        return KsBeanUtil.convertList(list,TopicHeadImageDTO.class);
    }

    public void deleteHeadImage(Integer id){
        topicHeadImageRepository.delById(id);
    }

    public void addStorey(TopicStoreyAddRequest request){
        TopicStorey storey = KsBeanUtil.convert(request, TopicStorey.class);
        storey.setCreateTime(LocalDateTime.now());
        storey.setUpdateTime(LocalDateTime.now());
        storey.setDeleted(DeleteFlag.NO.toValue());
        storey.setStatus(1);
        storeyRepository.save(storey);
    }

    public void modifyStorey(TopicStoreyModifyRequest request){
        TopicStorey oldStorey = storeyRepository.getOne(request.getId());
        if(oldStorey == null){
            addStorey(request);
        }
        TopicStorey storey = KsBeanUtil.convert(request, TopicStorey.class);
        storey.setCreateTime(oldStorey.getCreateTime());
        storey.setUpdateTime(LocalDateTime.now());
        storey.setDeleted(DeleteFlag.NO.toValue());
        storey.setStatus(1);
        storeyRepository.save(storey);
    }

    public List<TopicStoreyDTO> listStorey(TopicHeadImageQueryRequest request){
        List<TopicStorey> list = storeyRepository.getByTopicId(request.getTopicId());
        if(CollectionUtils.isEmpty(list)){
            return Collections.EMPTY_LIST;
        }
        return KsBeanUtil.convertList(list, TopicStoreyDTO.class);
    }
    
    public TopicActivityVO detail(String id){
        List<Topic> list = topicSettingRepository.getByKey(id);
        if(CollectionUtils.isEmpty(list)){
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        Topic topic = list.get(0);
        if(Objects.equals(topic.getStatus(),0)){
            return null;
        }
        TopicActivityVO topicVO = new TopicActivityVO();
        topicVO.setTopicColor(topic.getTopicColor());
        topicVO.setNavigationColor(topic.getNavigationColor());
        topicVO.setTopicName(topic.getTopicName());
        List<TopicHeadImage> images = topicHeadImageRepository.getByTopicId(topic.getId());
        topicVO.setHeadImageList(KsBeanUtil.convertList(images, TopicHeadImageDTO.class));
        List<TopicStorey> storeys = storeyRepository.getAvailByTopicId(topic.getId());
        if(CollectionUtils.isEmpty(storeys)){
            return topicVO;
        }
        List<TopicStorey> sortStoreys = storeys.stream().sorted(Comparator.comparing(TopicStorey::getSorting)).collect(Collectors.toList());
        topicVO.setStoreyList(KsBeanUtil.convertList(sortStoreys, TopicStoreyDTO.class));
        List<TopicStoreyContent> contents = contentRepository.getByTopicId(topic.getId());
        if(CollectionUtils.isEmpty(contents)){
            return topicVO;
        }
        List<TopicStoreyContentDTO>  contentDTOS = KsBeanUtil.convertList(contents, TopicStoreyContentDTO.class);
        topicVO.getStoreyList().forEach(p -> {
            List<TopicStoreyContentDTO> items = contentDTOS.stream().filter(g -> g.getStoreyId().equals(p.getId())).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(items)) {
                return;
            }
            List<TopicStoreyContentDTO> itemList = new ArrayList<>(items.size());
            if (Arrays.asList(TopicStoreyType.HETERSCROLLIMAGE.getId(),TopicStoreyType.COUPON.getId()).contains(p.getStoreyType())) {
                //轮播类型或优惠券根据时间过滤
                itemList.addAll(items.stream().filter(i->i.getType().equals(2) && (i.getStartTime() == null ||  i.getEndTime() == null)).collect(Collectors.toList()));
                itemList.addAll(items.stream().filter(i->i.getType().equals(2) && i.getStartTime() != null && i.getEndTime() != null && i.getStartTime().compareTo(LocalDateTime.now()) <= 0 && i.getEndTime().compareTo(LocalDateTime.now()) >=0).collect(Collectors.toList()));
            }else{
                itemList = items;
            }
            if(Objects.equals(TopicStoreyType.COUPON.getId(),p.getStoreyType())){
                itemList.forEach(item->{
                    TopicStoreyCouponDTO couponDTO = JSONObject.parseObject(item.getAttributeInfo(),TopicStoreyCouponDTO.class);
                    item.setActivityId(couponDTO.getActivityId());
                    item.setCouponId(couponDTO.getCouponId());
                    item.setReceiveImageUrl(couponDTO.getReceiveImageUrl());
                    item.setUseImageUrl(couponDTO.getUseImageUrl());
                    item.setDoneImageUrl(couponDTO.getDoneImageUrl());
                });
            }
            //排序
            List<TopicStoreyContentDTO> sortContents = itemList.stream().sorted(Comparator.comparing(TopicStoreyContentDTO::getType).thenComparing(TopicStoreyContentDTO::getSorting)).collect(Collectors.toList());
            p.setContents(sortContents);

        });


        return topicVO;
    }



    @Transactional
    public void enableStorey(EnableTopicStoreyRequest request){
        storeyRepository.enable(request.getStoreyId(),request.getOptType());
    }

    @Transactional
    public void addStoreyContents(TopicStoreyContentAddRequest request){
        if(request == null){
            throw  new  SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        //删除原数据
        contentRepository.deleteBySid(request.getStoreyId());
        List<TopicStoreyContent> contents = new ArrayList<>();
        //设置属性
        if(request.getStoreyType().equals(TopicStoreyType.COUPON.getId())){
            request.getLinkContents().forEach(c->{
                TopicStoreyCouponDTO couponDTO = (TopicStoreyCouponDTO)c;
                c.setAttributeInfo(JSON.toJSONString(couponDTO));
            });
        }
        if(CollectionUtils.isNotEmpty(request.getGoodsContents())){
            contents.addAll(KsBeanUtil.convertList(request.getGoodsContents(),TopicStoreyContent.class));
        }
        if(CollectionUtils.isNotEmpty(request.getLinkContents())){
            contents.addAll(KsBeanUtil.convertList(request.getLinkContents(),TopicStoreyContent.class));
        }
        contents.forEach(c->{
            c.setCreateTime(LocalDateTime.now());
            c.setUpdateTime(LocalDateTime.now());
            c.setDeleted(DeleteFlag.NO.toValue());
            c.setStoreyId(request.getStoreyId());
            c.setTopicId(request.getTopicId());
        });
        contentRepository.saveAll(contents);
    }

    public TopicStoreyContentResponse listTopicStoreyContent(TopicStoreyContentQueryRequest request){
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.ASC,"type"));
        orders.add(new Sort.Order(Sort.Direction.ASC,"sorting"));
         TopicStorey topicStorey = storeyRepository.getOne(request.getStoreyId());
         if(topicStorey == null){
             return null;
         }
         TopicStoreyContentResponse response = new TopicStoreyContentResponse();
         response.setStoreyId(request.getStoreyId());
         response.setStoreyType(topicStorey.getStoreyType());
         List<TopicStoreyContent> list = contentRepository.findAll(getTopicStoreyContentWhereCriteria(request),Sort.by(orders));
         if(CollectionUtils.isEmpty(list)){
            return response;
         }
         List<TopicStoreyContent> goodsContents = list.stream().filter(p->p.getType() == 1).collect(Collectors.toList());
         if(CollectionUtils.isNotEmpty(goodsContents)){
             response.setGoodsContents(KsBeanUtil.convertList(goodsContents,TopicStoreyContentDTO.class));
         }
        List<TopicStoreyContent> linkContents = list.stream().filter(p->p.getType() == 2).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(linkContents)){
            response.setLinkContents(KsBeanUtil.convertList(linkContents,TopicStoreyContentDTO.class));
        }
        return response;
    }


    public Specification<Topic> getTopicWhereCriteria(TopicQueryRequest request) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (request.getTopicKey() != null) {
                predicates.add(cbuild.equal(root.get("topicKey"), request.getTopicKey()));
            }
            if (StringUtils.isNotEmpty(request.getTopicName())) {
                predicates.add(cbuild.like(root.get("topicName"), StringUtil.SQL_LIKE_CHAR.concat(XssUtils.replaceLikeWildcard(request.getTopicName().trim())).concat(StringUtil.SQL_LIKE_CHAR)));
            }
            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }

    public Specification<TopicStoreyContent> getTopicStoreyContentWhereCriteria(TopicStoreyContentQueryRequest request) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (request.getStoreyId() != null) {
                predicates.add(cbuild.equal(root.get("storeyId"), request.getStoreyId()));
            }
            if (request.getTopicId() != null) {
                predicates.add(cbuild.equal(root.get("topicId"), request.getTopicId()));
            }
            predicates.add(cbuild.equal(root.get("deleted"), 0));
            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }


}
