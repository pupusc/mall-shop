package com.wanmi.sbc.goods.blacklist.repository;

import com.wanmi.sbc.goods.api.enums.DeleteFlagEnum;
import com.wanmi.sbc.goods.api.request.blacklist.GoodsBlackListCacheProviderRequest;
import com.wanmi.sbc.goods.bean.enums.PriceAdjustmentResult;
import com.wanmi.sbc.goods.blacklist.model.root.GoodsBlackListDTO;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;


//@Repository
public interface GoodsBlackListRepository extends JpaRepository<GoodsBlackListDTO, Integer>, JpaSpecificationExecutor<GoodsBlackListDTO> {

    /**
     * 拼接筛选条件
     * @return
     */
    default Specification<GoodsBlackListDTO> packageWhere(GoodsBlackListCacheProviderRequest goodsBlackListPageProviderRequest) {
        return new Specification<GoodsBlackListDTO>() {
            @Override
            public Predicate toPredicate(Root<GoodsBlackListDTO> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                final List<Predicate> conditionList = new ArrayList<>();

                if(BooleanUtils.isFalse(goodsBlackListPageProviderRequest.getSearchAll())) {
                    conditionList.add(criteriaBuilder.equal(root.get("delFlag"), DeleteFlagEnum.NORMAL.getCode()));
                }
                if (goodsBlackListPageProviderRequest.getId() != null) {
                    conditionList.add(criteriaBuilder.equal(root.get("id"), goodsBlackListPageProviderRequest.getId()));
                }
                if (!CollectionUtils.isEmpty(goodsBlackListPageProviderRequest.getBusinessCategoryColl())) {
                    conditionList.add(root.get("businessCategory").in(goodsBlackListPageProviderRequest.getBusinessCategoryColl()));
                }
                if (!CollectionUtils.isEmpty(goodsBlackListPageProviderRequest.getBusinessTypeColl())) {
                    conditionList.add(root.get("businessType").in(goodsBlackListPageProviderRequest.getBusinessTypeColl()));
                }
                if (!CollectionUtils.isEmpty(goodsBlackListPageProviderRequest.getBusinessIdColl())) {
                    conditionList.add(root.get("businessId").in(goodsBlackListPageProviderRequest.getBusinessIdColl()));
                }
                return criteriaBuilder.and(conditionList.toArray(new Predicate[0]));
            }
        };
    }

    @Query(value = "update PriceAdjustmentRecordDetail d set d.adjustResult = ?2, d.failReason = ?3 where d.priceAdjustmentNo = ?1")
    void executeFail(String adjustNo, PriceAdjustmentResult result, String failReason);

    @Query(value = "select * from t_goods_blacklist where del_flag = 0 and business_id = ?1  and business_type = ?2 and business_category = ?3", nativeQuery = true)
    List<GoodsBlackListDTO> selectBackListByBusiness(String businessId, Integer businessType, Integer businessCategory);
}
