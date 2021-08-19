package com.wanmi.sbc.customer.address.request;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.customer.address.model.root.CustomerDeliveryAddress;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 会员收货地址查询条件
 * Created by CHENLI on 2017/4/20.
 */
@Data
public class CustomerDeliveryAddressQueryRequest extends BaseQueryRequest {
    /**
     * 收货地址ID
     */
    private String deliveryAddressId;

    /**
     * 客户ID
     */
    private String customerId;

    /**
     * 是否是默认地址 0：否 1：是
     */
    private Integer isDefaltAddress;

    /**
     * 删除标志 0未删除 1已删除
     */
    private Integer delFlag;

    /**
     * 封装公共条件
     *
     * @return
     */
    public Specification<CustomerDeliveryAddress> getWhereCriteria() {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (Objects.nonNull(customerId) && StringUtils.isNotEmpty(customerId.trim())) {
                predicates.add(cbuild.equal(root.get("customerId"), customerId));
            }
            if (Objects.nonNull(deliveryAddressId) && StringUtils.isNotEmpty(deliveryAddressId.trim())) {
                predicates.add(cbuild.equal(root.get("deliveryAddressId"), deliveryAddressId));
            }
            if (isDefaltAddress != null) {
                predicates.add(cbuild.equal(root.get("isDefaltAddress"), isDefaltAddress));
            }
            //删除标记
            if (delFlag != null) {
                predicates.add(cbuild.equal(root.get("delFlag"), delFlag));
            }
            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
