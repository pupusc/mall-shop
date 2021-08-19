package com.wanmi.sbc.marketing.grouponrecord.service;

import com.wanmi.sbc.marketing.api.request.grouponrecord.GrouponRecordQueryRequest;
import com.wanmi.sbc.marketing.grouponrecord.model.root.GrouponRecord;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>拼团活动参团信息表动态查询条件构建器</p>
 *
 * @author groupon
 * @date 2019-05-17 16:17:44
 */
public class GrouponRecordWhereCriteriaBuilder {
    public static Specification<GrouponRecord> build(GrouponRecordQueryRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();

            //拼团活动ID
            if (StringUtils.isNotEmpty(queryRequest.getGrouponActivityId())) {
                predicates.add(cbuild.equal(root.get("grouponActivityId"), queryRequest.getGrouponActivityId()));
            }

            //会员ID
            if (StringUtils.isNotEmpty(queryRequest.getCustomerId())) {
                predicates.add(cbuild.equal(root.get("customerId"), queryRequest.getCustomerId()));
            }

            //SPU编号
            if (StringUtils.isNotEmpty(queryRequest.getGoodsId())) {
                predicates.add(cbuild.equal(root.get("goodsId"), queryRequest.getGoodsId()));
            }

            // sku编号
            if (StringUtils.isNotEmpty(queryRequest.getGoodsInfoId())) {
                predicates.add(cbuild.equal(root.get("goodsInfoId"), queryRequest.getGoodsInfoId()));
            }


            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
