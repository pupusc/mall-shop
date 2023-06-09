package com.wanmi.sbc.goods.mini.repository.goods;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.goods.bean.wx.request.assistant.WxLiveAssistantSearchRequest;
import com.wanmi.sbc.goods.mini.model.goods.WxLiveAssistantModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Repository
public interface WxLiveAssistantRepository extends JpaRepository<WxLiveAssistantModel, Long>, JpaSpecificationExecutor<WxLiveAssistantModel> {

    default Specification<WxLiveAssistantModel> buildSearchCondition(WxLiveAssistantSearchRequest wxLiveAssistantSearchRequest){
        return (Specification<WxLiveAssistantModel>) (root, criteriaQuery, criteriaBuilder) -> {
            final List<Predicate> conditionList = new ArrayList<>();
            conditionList.add(criteriaBuilder.equal(root.get("delFlag"), DeleteFlag.NO));
            if(StringUtils.isNotEmpty(wxLiveAssistantSearchRequest.getAssistantName())){
                conditionList.add(criteriaBuilder.like(root.get("theme"), "%" + wxLiveAssistantSearchRequest.getAssistantName() + "%"));
            }
            return criteriaBuilder.and(conditionList.toArray(new Predicate[conditionList.size()]));
        };
    }

}
