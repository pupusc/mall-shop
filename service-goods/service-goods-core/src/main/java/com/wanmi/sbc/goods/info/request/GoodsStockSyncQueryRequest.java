package com.wanmi.sbc.goods.info.request;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.goods.info.model.root.GoodsStockSync;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
            predicates.add(cbuild.equal(root.get("deleted"), deleted));
            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }

}

