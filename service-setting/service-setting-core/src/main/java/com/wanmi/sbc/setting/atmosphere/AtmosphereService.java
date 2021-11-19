package com.wanmi.sbc.setting.atmosphere;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.api.request.AtmosphereQueryRequest;
import com.wanmi.sbc.setting.api.request.topicconfig.TopicStoreyContentQueryRequest;
import com.wanmi.sbc.setting.atmosphere.model.root.Atmosphere;
import com.wanmi.sbc.setting.atmosphere.repository.AtmosphereRepository;
import com.wanmi.sbc.setting.bean.dto.AtmosphereDTO;
import com.wanmi.sbc.setting.bean.vo.TopicConfigVO;
import com.wanmi.sbc.setting.topicconfig.model.root.Topic;
import com.wanmi.sbc.setting.topicconfig.model.root.TopicStoreyContent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class AtmosphereService {

    @Autowired
    private AtmosphereRepository atmosphereRepository;

    public void add(List<AtmosphereDTO> list){
        List<Atmosphere> atmospheres = KsBeanUtil.convertList(list,Atmosphere.class);
        atmospheres.forEach(a->{
            a.setCreateTime(LocalDateTime.now());
            a.setUpdateTime(LocalDateTime.now());
            a.setDeleted(0);
        });
        atmosphereRepository.saveAll(atmospheres);
    }

    public MicroServicePage<AtmosphereDTO> page(AtmosphereQueryRequest request){
        BaseQueryRequest pageQuery = new BaseQueryRequest();
        pageQuery.setPageNum(request.getPageNum());
        pageQuery.setPageSize(request.getPageSize());
        Page<Atmosphere> page = atmosphereRepository.findAll(getAtmosphereWhereCriteria(request), pageQuery.getPageRequest());
        if(page == null || CollectionUtils.isEmpty(page.getContent())){
            return new MicroServicePage<>();
        }
        MicroServicePage<AtmosphereDTO> list = KsBeanUtil.convertPage(page, AtmosphereDTO.class);
        list.getContent().forEach(p->{
            if(StringUtils.isNotEmpty(p.getElementDesc())){
                Map<String,String> map = (Map<String, String>) JSON.parse(p.getElementDesc());
                p.setElementOne(map.get("elementOne"));
                p.setElementTwo(map.get("elementTwo"));
                p.setElementThree(map.get("elementThree"));
                p.setElementFour(map.get("elementFour"));
            }
        });
        return list;

    }

    public void delete(Integer id){
        atmosphereRepository.disableById(id);
    }

    public Specification<Atmosphere> getAtmosphereWhereCriteria(AtmosphereQueryRequest request) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(request.getSkuNo())) {
                predicates.add(root.get("skuNo").in(request.getSkuNo()));
            }
            if (CollectionUtils.isNotEmpty(request.getSkuId())) {
                predicates.add(root.get("skuId").in(request.getSkuId()));
            }
            if (request.getStartTime() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("startTime"),request.getStartTime()));
            }
            if (request.getEndTime() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("endTime"),request.getEndTime()));
            }
            predicates.add(cbuild.equal(root.get("deleted"), 0));
            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }

}
