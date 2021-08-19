package com.wanmi.sbc.account.finance.record.service;

import com.wanmi.sbc.account.api.request.finance.record.AccountDetailsPageRequest;
import com.wanmi.sbc.account.finance.record.model.entity.Reconciliation;
import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.common.util.StringUtil;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.jpa.domain.Specification;
import org.apache.commons.lang3.StringUtils;
import com.wanmi.sbc.common.util.XssUtils;
import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.wanmi.sbc.common.util.DateUtil.FMT_TIME_1;

/**
 * <p>动态查询条件构建器</p>
 * @author yangzhen
 * @date 2020-11-23 17:15:46
 */
public class ReconciliationWhereCriteriaBuilder {
    public static Specification<Reconciliation> build(AccountDetailsPageRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 店铺id
            if (queryRequest.getStoreId() != null) {
                predicates.add(cbuild.equal(root.get("storeId"), queryRequest.getStoreId()));
            }



            // 支付方式
            if (Objects.nonNull(queryRequest.getPayWay())) {
                predicates.add(cbuild.equal(root.get("payWay"), queryRequest.getPayWay().toValue()));
            }

            if(StringUtils.isNotEmpty(queryRequest.getBeginTime())){
                LocalDateTime beginTime = DateUtil.parse(queryRequest.getBeginTime(), FMT_TIME_1);
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("tradeTime"),
                        beginTime));
            }


            // 小于或等于 搜索条件:订单/退单申请时间截止
            if(StringUtils.isNotEmpty(queryRequest.getEndTime())){
                LocalDateTime endTime = DateUtil.parse(queryRequest.getEndTime(), FMT_TIME_1);
                predicates.add(cbuild.lessThan(root.get("tradeTime"),
                        endTime));
            }

            // 交易类型，0：收入 1：退款
            if (queryRequest.getType() != null) {
                predicates.add(cbuild.equal(root.get("type"), queryRequest.getType()));
            }



            // 模糊查询 - 交易流水号
            if (StringUtils.isNotEmpty(queryRequest.getTradeNo())) {
                predicates.add(cbuild.like(root.get("tradeNo"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getTradeNo()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
