package com.wanmi.sbc.customer.storesharerecord.service;

import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.customer.api.request.storesharerecord.StoreShareRecordQueryRequest;
import com.wanmi.sbc.customer.storesharerecord.model.root.StoreShareRecord;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>商城分享动态查询条件构建器</p>
 * @author zhangwenchang
 * @date 2020-03-06 13:48:42
 */
public class StoreShareRecordWhereCriteriaBuilder {
    public static Specification<StoreShareRecord> build(StoreShareRecordQueryRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 批量查询-shareIdList
            if (CollectionUtils.isNotEmpty(queryRequest.getShareIdList())) {
                predicates.add(root.get("shareId").in(queryRequest.getShareIdList()));
            }

            // shareId
            if (queryRequest.getShareId() != null) {
                predicates.add(cbuild.equal(root.get("shareId"), queryRequest.getShareId()));
            }

            // 模糊查询 - 会员Id
            if (StringUtils.isNotEmpty(queryRequest.getCustomerId())) {
                predicates.add(cbuild.like(root.get("customerId"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getCustomerId()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 店铺ID
            if (queryRequest.getStoreId() != null) {
                predicates.add(cbuild.equal(root.get("storeId"), queryRequest.getStoreId()));
            }

            // 公司信息ID
            if (queryRequest.getCompanyInfoId() != null) {
                predicates.add(cbuild.equal(root.get("companyInfoId"), queryRequest.getCompanyInfoId()));
            }

            // 0分享首页，1分享店铺首页
            if (queryRequest.getIndexType() != null) {
                predicates.add(cbuild.equal(root.get("indexType"), queryRequest.getIndexType()));
            }

            // 终端：1 H5，2pc，3APP，4小程序
            if (queryRequest.getTerminalSource() != null) {
                predicates.add(cbuild.equal(root.get("terminalSource"), queryRequest.getTerminalSource()));
            }

            // 大于或等于 搜索条件:创建时间开始
            if (queryRequest.getCreateTimeBegin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("createTime"),
                        queryRequest.getCreateTimeBegin()));
            }
            // 小于或等于 搜索条件:创建时间截止
            if (queryRequest.getCreateTimeEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("createTime"),
                        queryRequest.getCreateTimeEnd()));
            }

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
