package com.wanmi.sbc.goods.blacklist.repository;

import com.wanmi.sbc.goods.api.enums.DeleteFlagEnum;
import com.wanmi.sbc.goods.api.request.blacklist.GoodsBlackListPageProviderRequest;
import com.wanmi.sbc.goods.blacklist.model.root.GoodsBlackListDTO;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

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
    default Specification<GoodsBlackListDTO> packageWhere(GoodsBlackListPageProviderRequest goodsBlackListPageProviderRequest) {
        return new Specification<GoodsBlackListDTO>() {
            @Override
            public Predicate toPredicate(Root<GoodsBlackListDTO> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                final List<Predicate> conditionList = new ArrayList<>();

                //只是获取有效的
                conditionList.add(criteriaBuilder.equal(root.get("delFlag"), DeleteFlagEnum.NORMAL.getCode()));
                if (goodsBlackListPageProviderRequest.getId() != null) {
                    conditionList.add(criteriaBuilder.equal(root.get("id"), goodsBlackListPageProviderRequest.getId()));
                }
                return criteriaBuilder.and(conditionList.toArray(new Predicate[0]));
            }
        };
    }


}
