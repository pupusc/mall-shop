package com.wanmi.sbc.goods.mini.repository.goods;

import com.wanmi.sbc.goods.bean.wx.request.assistant.WxLiveAssistantSearchRequest;
import com.wanmi.sbc.goods.mini.model.goods.WxLiveAssistantGoodsModel;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Repository
public interface WxLiveAssistantGoodsRepository extends JpaRepository<WxLiveAssistantGoodsModel, Long>, JpaSpecificationExecutor<WxLiveAssistantGoodsModel> {

    default Specification<WxLiveAssistantGoodsModel> buildSearchCondition(WxLiveAssistantSearchRequest wxLiveAssistantSearchRequest){
        return (Specification<WxLiveAssistantGoodsModel>) (root, criteriaQuery, criteriaBuilder) -> {
            final List<Predicate> conditionList = new ArrayList<>();
            if(wxLiveAssistantSearchRequest.getLiveAssistantId() != null){
                conditionList.add(criteriaBuilder.equal(root.get("assistId"), wxLiveAssistantSearchRequest.getLiveAssistantId()));
            }
            return criteriaBuilder.and(conditionList.toArray(new Predicate[conditionList.size()]));
        };
    }
}
