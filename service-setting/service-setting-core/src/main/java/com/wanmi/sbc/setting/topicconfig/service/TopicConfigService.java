package com.wanmi.sbc.setting.topicconfig.service;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.setting.api.request.topicconfig.HeadImageConfigAddRequest;
import com.wanmi.sbc.setting.api.request.topicconfig.TopicConfigAddRequest;
import com.wanmi.sbc.setting.api.request.topicconfig.TopicQueryRequest;
import com.wanmi.sbc.setting.bean.vo.TopicConfigVO;
import com.wanmi.sbc.setting.topicconfig.model.root.HeadImageSetting;
import com.wanmi.sbc.setting.topicconfig.model.root.TopicSetting;
import com.wanmi.sbc.setting.topicconfig.repository.HeadImageSettingRepository;
import com.wanmi.sbc.setting.topicconfig.repository.TopicSettingRepository;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.description.annotation.AnnotationList;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class TopicConfigService {
    @Autowired
    private TopicSettingRepository topicSettingRepository;

    @Autowired
    private HeadImageSettingRepository headImageSettingRepository;

    public void addTopic(TopicConfigAddRequest request) {
        TopicSetting topic = KsBeanUtil.convert(request, TopicSetting.class);
        topic.setCreateTime(LocalDateTime.now());
        topic.setUpdateTime(LocalDateTime.now());
        topic.setDeleted(DeleteFlag.NO.toValue());
        topic.setStatus(1);
        topicSettingRepository.save(topic);
    }

    public List<TopicConfigVO> listTopic(TopicQueryRequest request) {
        List<TopicSetting> list = topicSettingRepository.findAll(getTopicWhereCriteria(request));
        if(CollectionUtils.isEmpty(list)){
            return Collections.EMPTY_LIST;
        }
        return KsBeanUtil.convert(list,TopicConfigVO.class);
    }

    public void addHeadImage(HeadImageConfigAddRequest request){
        List<HeadImageSetting> list = KsBeanUtil.convertList(request.getHeadImage(),HeadImageSetting.class);
        headImageSettingRepository.saveAll(list);
    }


    public



    public Specification<TopicSetting> getTopicWhereCriteria(TopicQueryRequest request) {
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
