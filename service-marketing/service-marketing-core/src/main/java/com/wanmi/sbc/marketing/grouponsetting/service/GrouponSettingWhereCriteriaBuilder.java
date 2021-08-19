package com.wanmi.sbc.marketing.grouponsetting.service;

import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.marketing.api.request.grouponsetting.GrouponSettingQueryRequest;
import com.wanmi.sbc.marketing.grouponsetting.model.root.GrouponSetting;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>拼团活动信息表动态查询条件构建器</p>
 * @author groupon
 * @date 2019-05-15 14:19:49
 */
public class GrouponSettingWhereCriteriaBuilder {
    public static Specification<GrouponSetting> build(GrouponSettingQueryRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 批量查询-主键List
            if (CollectionUtils.isNotEmpty(queryRequest.getIdList())) {
                predicates.add(root.get("id").in(queryRequest.getIdList()));
            }

            // 主键
            if (StringUtils.isNotEmpty(queryRequest.getId())) {
                predicates.add(cbuild.equal(root.get("id"), queryRequest.getId()));
            }

            // 拼团商品审核
            if (queryRequest.getGoodsAuditFlag() != null) {
                predicates.add(cbuild.equal(root.get("goodsAuditFlag"), queryRequest.getGoodsAuditFlag()));
            }

            // 模糊查询 - 广告
            if (StringUtils.isNotEmpty(queryRequest.getAdvert())) {
                predicates.add(cbuild.like(root.get("advert"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getAdvert()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 拼团规则
            if (StringUtils.isNotEmpty(queryRequest.getRule())) {
                predicates.add(cbuild.like(root.get("rule"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getRule()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
