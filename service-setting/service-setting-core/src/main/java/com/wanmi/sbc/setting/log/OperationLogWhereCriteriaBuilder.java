package com.wanmi.sbc.setting.log;

import com.google.common.collect.Lists;
import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.setting.api.request.OperationLogListRequest;
import com.wanmi.sbc.setting.util.XssUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.List;

import static java.util.Objects.nonNull;

public class OperationLogWhereCriteriaBuilder {
    private OperationLogWhereCriteriaBuilder() {
    }

    public static Specification<OperationLog> buildQueryOpLogByCriteria(OperationLogListRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = Lists.newArrayList();

            if (nonNull(queryRequest.getStoreId())) {
                predicates.add(cbuild.equal(root.get("storeId"), queryRequest.getStoreId()));
            }
            if (nonNull(queryRequest.getCompanyInfoId())) {
                predicates.add(cbuild.equal(root.get("companyInfoId"), queryRequest.getCompanyInfoId()));
            }
            if (StringUtils.isNotBlank(queryRequest.getOpModule())) {
                predicates.add(cbuild.equal(root.get("opModule"), queryRequest.getOpModule()));
            }

            if (StringUtils.isNotBlank(queryRequest.getBeginTime())) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("opTime"),
                        DateUtil.parse(queryRequest.getBeginTime(), DateUtil.FMT_TIME_1)));
            }
            if (StringUtils.isNotBlank(queryRequest.getEndTime())) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("opTime"),
                        DateUtil.parse(queryRequest.getEndTime(), DateUtil.FMT_TIME_1)));
            }

            if (StringUtils.isNotBlank(queryRequest.getOpAccount())) {
                predicates.add(cbuild.like(root.get("opAccount"), buildLike(queryRequest.getOpAccount())));
            }
            if (StringUtils.isNotBlank(queryRequest.getOpName())) {
                predicates.add(cbuild.like(root.get("opName"), buildLike(queryRequest.getOpName())));
            }
            if (StringUtils.isNotBlank(queryRequest.getOpCode())) {
                predicates.add(cbuild.like(root.get("opCode"), buildLike(queryRequest.getOpCode())));
            }
            if (StringUtils.isNotBlank(queryRequest.getOpContext())) {
                predicates.add(cbuild.like(root.get("opContext"), buildLike(queryRequest.getOpContext())));
            }

            cquery.orderBy(cbuild.desc(root.get("opTime")));
            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }

    private static String buildLike(String field) {
        return StringUtil.SQL_LIKE_CHAR + XssUtils.replaceLikeWildcard(field.trim()) + StringUtil.SQL_LIKE_CHAR;
    }
}
