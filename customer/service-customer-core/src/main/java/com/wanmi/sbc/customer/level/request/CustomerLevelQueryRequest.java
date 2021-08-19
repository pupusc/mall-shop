package com.wanmi.sbc.customer.level.request;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.customer.level.model.root.CustomerLevel;
import com.wanmi.sbc.customer.util.XssUtils;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 客户等级查询请求参数
 * Created by CHENLI on 2017/4/13.
 */
@Data
public class CustomerLevelQueryRequest extends BaseQueryRequest {

    /**
     * 客户等级ID
     */
    private Long customerLevelId;

    /**
     * 客户等级名称
     */
    private String customerLevelName;

    /**
     * 所需成长值
     */
    private Long growthValue;

    /**
     * 等级徽章图
     */
    private String rankBadgeImg;

    /**
     * 是否是默认 0：否 1：是
     */
    private Integer isDefault;

    /**
     * 删除标记 0未删除 1已删除
     */
    private Integer delFlag;

    /**
     * 封装公共条件
     *
     * @return
     */
    public Specification<CustomerLevel> getWhereCriteria() {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (customerLevelId != null) {
                predicates.add(cbuild.equal(root.get("customerLevelId"), customerLevelId));
            }

            if (isDefault != null) {
                predicates.add(cbuild.equal(root.get("isDefalt"), isDefault));
            }

            if (Objects.nonNull(customerLevelName) && StringUtils.isNotEmpty(customerLevelName)) {
                predicates.add(cbuild.like(root.get("customerLevelName"), XssUtils.replaceLikeWildcard
                        (customerLevelName.trim())));
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
