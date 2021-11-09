package com.wanmi.sbc.marketing.points.request;


import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.goods.bean.enums.GoodsAdAuditStatus;
import com.wanmi.sbc.marketing.points.model.root.PointsExchangeActivity;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class PointsExchangeActivityQueryRequest extends BaseQueryRequest {


    private static final long serialVersionUID = -2029439724591178457L;

    private String activityName;

    private List<String> skuList;

    private String goodsName;

    private Integer status;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    public Specification<PointsExchangeActivity> getWhereCriteria() {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (status != null) {
                predicates.add(cbuild.equal(root.get("status"), status));
            }
            //模糊查询
            if (StringUtils.isNotEmpty(activityName)) {
                predicates.add(cbuild.like(root.get("activityName"), StringUtil.SQL_LIKE_CHAR.concat(XssUtils.replaceLikeWildcard(activityName.trim())).concat(StringUtil.SQL_LIKE_CHAR)));
            }
            if (startTime != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("startTime"), startTime));
            }
            if (endTime != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("endTime"), endTime));
            }
            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
