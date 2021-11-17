package com.wanmi.sbc.setting.atmosphere;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
        if(page == null){
            return new MicroServicePage<>();
        }
        return KsBeanUtil.convertPage(page, AtmosphereDTO.class);

    }

    public Specification<Atmosphere> getAtmosphereWhereCriteria(AtmosphereQueryRequest request) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(request.getSkuNo())) {
                predicates.add(root.get("skuNo").in(request.getSkuNo()));
            }
            predicates.add(cbuild.equal(root.get("deleted"), 0));
            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }

}
