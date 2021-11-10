package com.wanmi.sbc.setting.topicconfig.service;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.setting.api.request.topicconfig.HeadImageConfigAddRequest;
import com.wanmi.sbc.setting.api.request.topicconfig.TopicConfigAddRequest;
import com.wanmi.sbc.setting.api.request.topicconfig.TopicQueryRequest;
import com.wanmi.sbc.setting.api.request.topicconfig.TopicStoreyAddRequest;
import com.wanmi.sbc.setting.bean.dto.TopicHeadImageDTO;
import com.wanmi.sbc.setting.bean.dto.TopicStoreyDTO;
import com.wanmi.sbc.setting.bean.dto.TopicStoreyGoodsDTO;
import com.wanmi.sbc.setting.bean.vo.TopicActivityVO;
import com.wanmi.sbc.setting.bean.vo.TopicConfigVO;
import com.wanmi.sbc.setting.topicconfig.model.root.TopicHeadImage;
import com.wanmi.sbc.setting.topicconfig.model.root.Topic;
import com.wanmi.sbc.setting.topicconfig.model.root.TopicStorey;
import com.wanmi.sbc.setting.topicconfig.model.root.TopicStoreyGoods;
import com.wanmi.sbc.setting.topicconfig.repository.TopicHeadImageRepository;
import com.wanmi.sbc.setting.topicconfig.repository.TopicRepository;
import com.wanmi.sbc.setting.topicconfig.repository.TopicStoreyGoodsRepository;
import com.wanmi.sbc.setting.topicconfig.repository.TopicStoreyRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
    private TopicStoreyGoodsRepository goodsRepository;

    public void addTopic(TopicConfigAddRequest request) {
        Topic topic = KsBeanUtil.convert(request, Topic.class);
        topic.setCreateTime(LocalDateTime.now());
        topic.setUpdateTime(LocalDateTime.now());
        topic.setDeleted(DeleteFlag.NO.toValue());
        topic.setStatus(1);
        topicSettingRepository.save(topic);
    }

    public MicroServicePage<TopicConfigVO> listTopic(TopicQueryRequest request) {
        Page<Topic> page = topicSettingRepository.findAll(getTopicWhereCriteria(request),request.getPageRequest());
        if(page == null){
            return new MicroServicePage<>();
        }
        return KsBeanUtil.convertPage(page,TopicConfigVO.class);
    }


    @Transactional
    public void addHeadImage(HeadImageConfigAddRequest request){
        //删除原头图
        topicHeadImageRepository.deleteByTopicId(request.getTopicId());
        List<TopicHeadImage> list = KsBeanUtil.convertList(request.getHeadImage(), TopicHeadImage.class);
        topicHeadImageRepository.saveAll(list);
    }

    @Transactional
    public void addStorey(TopicStoreyAddRequest request){
        //删除原楼层
        storeyRepository.deleteByTopicId(request.getTopicId());
       List<TopicStorey> list= KsBeanUtil.convertList(request.getStoreyList(),TopicStorey.class);
        storeyRepository.saveAll(list);
    }
    
    public TopicActivityVO detail(Integer id){
        Topic topic = topicSettingRepository.getOne(id);
        if(topic == null){
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        TopicActivityVO topicVO = new TopicActivityVO();
        List<TopicHeadImage> images = topicHeadImageRepository.getByTopicId(id);
        topicVO.setHeadImageList(KsBeanUtil.convertList(images, TopicHeadImageDTO.class));
        List<TopicStorey> storeys = storeyRepository.getByTopicId(id);
        if(CollectionUtils.isEmpty(storeys)){
            return topicVO;
        }
        topicVO.setStoreyList(KsBeanUtil.convertList(storeys, TopicStoreyDTO.class));
        List<TopicStoreyGoods> goods = goodsRepository.getByTopicId(id);
        if(CollectionUtils.isEmpty(goods)){
            return topicVO;
        }
        topicVO.getStoreyList().forEach(p->{
            List<TopicStoreyGoods> items = goods.stream().filter(g->g.getStoreyId().equals(p.getId())).collect(Collectors.toList());;
            if(CollectionUtils.isNotEmpty(items)){
                p.setGoods(KsBeanUtil.convertList(items, TopicStoreyGoodsDTO.class));
            }
        });
        return topicVO;
    }







    public Specification<Topic> getTopicWhereCriteria(TopicQueryRequest request) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (request.getId() != null) {
                predicates.add(cbuild.equal(root.get("id"), request.getId()));
            }
            if (StringUtils.isNotEmpty(request.getName())) {
                predicates.add(cbuild.like(root.get("topicName"), StringUtil.SQL_LIKE_CHAR.concat(XssUtils.replaceLikeWildcard(request.getName().trim())).concat(StringUtil.SQL_LIKE_CHAR)));
            }
            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
