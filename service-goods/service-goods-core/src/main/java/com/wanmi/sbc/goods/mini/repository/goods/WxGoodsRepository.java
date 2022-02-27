package com.wanmi.sbc.goods.mini.repository.goods;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.goods.bean.wx.request.WxGoodsSearchRequest;
import com.wanmi.sbc.goods.mini.enums.goods.WxGoodsStatus;
import com.wanmi.sbc.goods.mini.model.goods.WxGoodsModel;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Repository
public interface WxGoodsRepository extends JpaRepository<WxGoodsModel, Long>, JpaSpecificationExecutor<WxGoodsModel> {

    @Query(value = "select count(*) from t_wx_goods where goods_id=? and del_flag=0", nativeQuery = true)
    Integer getOnShelfCount(String goodsId);

    WxGoodsModel findByGoodsIdAndDelFlag(String goodsId, DeleteFlag Flag);

    default Specification<WxGoodsModel> buildSearchCondition(WxGoodsSearchRequest wxGoodsSearchRequest){
        return (Specification<WxGoodsModel>) (root, criteriaQuery, criteriaBuilder) -> {
            final List<Predicate> conditionList = new ArrayList<>();
            conditionList.add(criteriaBuilder.equal(root.get("delFlag"), DeleteFlag.NO));
            if (CollectionUtils.isNotEmpty(wxGoodsSearchRequest.getGoodsIds())) {
                conditionList.add(root.get("goodsId").in(wxGoodsSearchRequest.getGoodsIds()));
            }
            if (wxGoodsSearchRequest.getAuditStatus() != null) {
                conditionList.add(criteriaBuilder.equal(root.get("auditStatus").as(Integer.class), wxGoodsSearchRequest.getAuditStatus()));
            }
            if (wxGoodsSearchRequest.getSaleStatus() != null) {
                if(wxGoodsSearchRequest.getSaleStatus() == 0){
                    //不可售
                    conditionList.add(criteriaBuilder.isNull(root.get("platformProductId")));
                }else if(wxGoodsSearchRequest.getSaleStatus() == 1){
                    //可售
                    conditionList.add(criteriaBuilder.isNotNull(root.get("platformProductId")));
                }
            }
            return criteriaBuilder.and(conditionList.toArray(new Predicate[conditionList.size()]));
        };
    }

}
