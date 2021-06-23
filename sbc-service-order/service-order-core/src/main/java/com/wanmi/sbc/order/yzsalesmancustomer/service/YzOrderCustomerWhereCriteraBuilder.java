package com.wanmi.sbc.order.yzsalesmancustomer.service;

import com.wanmi.sbc.order.api.request.yzorder.YzOrderCustomerQueryRequest;
import com.wanmi.sbc.order.yzsalesmancustomer.model.root.YzOrderCustomer;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class YzOrderCustomerWhereCriteraBuilder {

    public static Specification<YzOrderCustomer> build(YzOrderCustomerQueryRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();

            // id
            if (queryRequest.getFlag() != null) {
                predicates.add(cbuild.equal(root.get("flag"), queryRequest.getFlag()));
            }

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
