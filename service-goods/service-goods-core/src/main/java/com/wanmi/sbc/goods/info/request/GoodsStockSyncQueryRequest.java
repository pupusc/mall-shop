package com.wanmi.sbc.goods.info.request;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.goods.common.model.root.RiskVerify;
import com.wanmi.sbc.goods.info.model.root.GoodsStockSync;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;




/**
 * 商品库存同步查询请求
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsStockSyncQueryRequest extends BaseQueryRequest {

     private Integer status;

     private Integer deleted = 0;

     private List<String> goodsNos;

    /**
     * 封装公共条件
     *
     * @return
     */
    public Specification<GoodsStockSync> getWhereCriteria() {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            //批量商品编号
            if (status != null) {
                predicates.add(cbuild.equal(root.get("status"), status));
            }
            if(CollectionUtils.isNotEmpty(goodsNos)){
                predicates.add(root.get("goodsNo").in(goodsNos));
            }
            predicates.add(cbuild.equal(root.get("deleted"), deleted));
            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }

    public Specification<RiskVerify> getVerifyWhereCriteria() {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            //批量商品编号
            if (status != null) {
                predicates.add(cbuild.equal(root.get("status"), status));
            }
            if(CollectionUtils.isNotEmpty(goodsNos)){
                predicates.add(root.get("goodsNo").in(goodsNos));
            }
            predicates.add(cbuild.equal(root.get("deleted"), deleted));
            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }


}

