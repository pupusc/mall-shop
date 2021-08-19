package com.wanmi.sbc.goods.cate.request;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.goods.cate.model.root.ContractCate;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunkun on 2017/10/31.
 */
@Data
public class ContractCateQueryRequest extends BaseQueryRequest {

    private static final long serialVersionUID = 8663584629048270522L;

    /**
     * 店铺id
     */
    private Long storeId;

    /**
     * 商品分类标识
     */
    private Long cateId;

    /**
     * 商品分类标识列表
     */
    private List<Long> cateIds;

    /**
     * 封装公共条件
     *
     * @return
     */
    public Specification<ContractCate> getWhereCriteria() {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cbuild.equal(root.get("storeId"), storeId));
            if (cateId != null && cateId > 0) {
                predicates.add(cbuild.equal(root.get("goodsCate"), cateId));
            }
            if (CollectionUtils.isNotEmpty(cateIds)) {
                CriteriaBuilder.In in = cbuild.in(root.get("goodsCate"));
                for (Long id : cateIds) {
                    in.value(id);
                }
                predicates.add(in);
            }
            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        }

                ;
    }
}
