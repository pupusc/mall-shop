package com.wanmi.sbc.customer.storecustomer.request;

import com.wanmi.sbc.customer.bean.enums.CustomerType;
import com.wanmi.sbc.customer.storecustomer.root.StoreCustomerRela;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by hht on 2017/11/17.
 */
@Data
public class StoreCustomerRequest {

    /**
     * 客户Id
     */
    private String customerId;

    /**
     * 商家Id
     */
    private Long companyInfoId;

    /**
     * 商铺Id
     */
    private Long storeId;

    /**
     * 店铺等级标识
     */
    private Long storeLevelId;

    /**
     * 批量商铺Id
     */
    private List<Long> storeIds;

    /**
     * 客户类型
     */
    private CustomerType customerType;


    public Specification<StoreCustomerRela> getWhereCriteria() {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (Objects.nonNull(customerId) && StringUtils.isNotEmpty(customerId.trim())) {
                predicates.add(cbuild.equal(root.get("customerId"), customerId));
            }
            if (Objects.nonNull(companyInfoId)) {
                predicates.add(cbuild.equal(root.get("companyInfoId"), companyInfoId));
            }
            if (Objects.nonNull(storeId)) {
                predicates.add(cbuild.equal(root.get("storeId"), storeId));
            }
            if (Objects.nonNull(customerType)) {
                predicates.add(cbuild.equal(root.get("customerType"), customerType));
            }
            if (Objects.nonNull(storeLevelId)) {
                predicates.add(cbuild.equal(root.get("storeLevelId"), storeLevelId));
            }
            if (CollectionUtils.isNotEmpty(storeIds)) {
                predicates.add(root.get("storeId").in(storeIds));
            }

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);

            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
