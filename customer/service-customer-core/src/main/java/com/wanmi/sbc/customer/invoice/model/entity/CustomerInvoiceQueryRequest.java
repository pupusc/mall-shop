package com.wanmi.sbc.customer.invoice.model.entity;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.customer.bean.enums.CheckState;
import com.wanmi.sbc.customer.invoice.model.root.CustomerInvoice;
import com.wanmi.sbc.customer.util.XssUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.Enumerated;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 客户增票资质查询
 * Created by CHENLI on 2017/4/26.
 */
@Data
public class CustomerInvoiceQueryRequest extends BaseQueryRequest {

    /**
     * 会员名称
     */
    private String customerName;

    /**
     * 客户ID
     */
    private List<String> customerIds;

    /**
     * 单位全称
     */
    private String companyName;

    /**
     * 增票资质审核状态  0:待审核 1:已审核通过 2:审核未通过
     */
    @Enumerated
    private CheckState checkState;

    /**
     * 负责业务员
     */
    private String employeeId;

    /**
     * 负责业务员ID集合
     */
    @ApiModelProperty(value = "负责业务员ID集合")
    private List<String> employeeIds;

    /**
     * 封装公共条件
     *
     * @return
     */
    public Specification<CustomerInvoice> getWhereCriteria() {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (CollectionUtils.isNotEmpty(customerIds)) {
                predicates.add(root.get("customerId").in(customerIds));
            }

            if (Objects.nonNull(companyName) && StringUtils.isNotEmpty(companyName.trim())) {
                predicates.add(cbuild.like(root.get("companyName"), buildLike(companyName)));
            }
            if (checkState != null) {
                predicates.add(cbuild.equal(root.get("checkState"), checkState));
            }

            predicates.add(cbuild.equal(root.get("delFlag"), DeleteFlag.NO));
            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }

    private static String buildLike(String field) {
        StringBuilder stringBuilder = new StringBuilder();
        return stringBuilder.append("%").append(XssUtils.replaceLikeWildcard(field)).append("%").toString();
    }
}
