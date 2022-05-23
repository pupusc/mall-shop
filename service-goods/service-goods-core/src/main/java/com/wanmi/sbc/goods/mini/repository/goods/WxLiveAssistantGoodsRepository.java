package com.wanmi.sbc.goods.mini.repository.goods;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.goods.bean.wx.request.assistant.WxLiveAssistantSearchRequest;
import com.wanmi.sbc.goods.mini.model.goods.WxLiveAssistantGoodsModel;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Repository
public interface WxLiveAssistantGoodsRepository extends JpaRepository<WxLiveAssistantGoodsModel, Long>, JpaSpecificationExecutor<WxLiveAssistantGoodsModel> {

    /**
     * 查找商品是否存在于还未开播的直播计划中
     */
    @Query(value = "select lg.* from t_wx_live_assistant as l join t_wx_live_assistant_goods as lg on lg.assist_id=l.id where lg.goods_id in ?1 and l.del_flag=0 and lg.del_flag=0 and now() <= l.end_time", nativeQuery = true)
    List<WxLiveAssistantGoodsModel> findTimeConflictGoods(List<String> goodsIds);


    default Specification<WxLiveAssistantGoodsModel> buildSearchCondition(WxLiveAssistantSearchRequest wxLiveAssistantSearchRequest){
        return (Specification<WxLiveAssistantGoodsModel>) (root, criteriaQuery, criteriaBuilder) -> {
            final List<Predicate> conditionList = new ArrayList<>();
            if(wxLiveAssistantSearchRequest.getLiveAssistantId() != null){
                conditionList.add(criteriaBuilder.equal(root.get("assistId"), wxLiveAssistantSearchRequest.getLiveAssistantId()));
            }
            if(wxLiveAssistantSearchRequest.getGoodsIdIn() != null){
                conditionList.add(root.get("goodsId").in(wxLiveAssistantSearchRequest.getGoodsIdIn()));
            }
            conditionList.add(criteriaBuilder.equal(root.get("delFlag"), DeleteFlag.NO));
            return criteriaBuilder.and(conditionList.toArray(new Predicate[conditionList.size()]));
        };
    }
}
