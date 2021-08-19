package com.wanmi.sbc.goods.brand.request;

import com.wanmi.sbc.goods.brand.model.root.ContractBrand;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 签约品牌查询请求结构
 * Created by sunkun on 2017/10/31.
 */
@Data
public class ContractBrandQueryRequest implements Serializable {

    private static final long serialVersionUID = 4515178783252329105L;

    /**
     * 签约品牌分类
     */
    private Long contractBrandId;


    /**
     * 店铺主键
     */
    private Long storeId;

    /**
     * 平台品牌id
     */
    private List<Long> goodsBrandIds;

    /**
     * 自定义品牌名称
     */
    private String checkBrandName;

    /**
     * 封装公共条件
     *
     * @return
     */
    public Specification<ContractBrand> getWhereCriteria() {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (storeId != null) {
                predicates.add(cbuild.equal(root.get("storeId"), storeId));
            }
            if (goodsBrandIds != null) {
                CriteriaBuilder.In in = cbuild.in(root.get("goodsBrand"));
                for (Long id : goodsBrandIds) {
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
