package com.wanmi.sbc.goods.mini.repository.goods;

import com.wanmi.sbc.goods.bean.wx.request.assistant.WxLiveAssistantSearchRequest;
import com.wanmi.sbc.goods.mini.model.goods.WxLiveAssistantModel;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface WxLiveAssistantRepository extends JpaRepository<WxLiveAssistantModel, Long>, JpaSpecificationExecutor<WxLiveAssistantModel> {

    default Specification<WxLiveAssistantModel> buildSearchCondition(WxLiveAssistantSearchRequest wxLiveAssistantSearchRequest){
        return (Specification<WxLiveAssistantModel>) (root, criteriaQuery, criteriaBuilder) -> {
            final List<Predicate> conditionList = new ArrayList<>();
            conditionList.add(criteriaBuilder.equal(root.get("delFlag"), DeleteFlag.NO));
            return criteriaBuilder.and(conditionList.toArray(new Predicate[conditionList.size()]));
        };
    }

}
