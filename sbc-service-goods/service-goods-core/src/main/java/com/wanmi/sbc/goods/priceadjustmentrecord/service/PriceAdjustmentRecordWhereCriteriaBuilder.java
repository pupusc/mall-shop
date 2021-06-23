package com.wanmi.sbc.goods.priceadjustmentrecord.service;

import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.goods.api.request.priceadjustmentrecord.PriceAdjustmentRecordQueryRequest;
import com.wanmi.sbc.goods.priceadjustmentrecord.model.root.PriceAdjustmentRecord;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.jpa.domain.Specification;
import org.apache.commons.lang3.StringUtils;
import com.wanmi.sbc.common.util.XssUtils;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>调价记录表动态查询条件构建器</p>
 * @author chenli
 * @date 2020-12-09 19:57:21
 */
public class PriceAdjustmentRecordWhereCriteriaBuilder {
    public static Specification<PriceAdjustmentRecord> build(PriceAdjustmentRecordQueryRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 批量查询-调价单号List
            if (CollectionUtils.isNotEmpty(queryRequest.getIdList())) {
                predicates.add(root.get("id").in(queryRequest.getIdList()));
            }

            // 模糊查询 - 调价单号
            if (StringUtils.isNotEmpty(queryRequest.getId())) {
                predicates.add(cbuild.like(root.get("id"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getId()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 调价类型：0 市场价、 1 等级价、2 阶梯价 
            if (queryRequest.getPriceAdjustmentType() != null) {
                predicates.add(cbuild.equal(root.get("priceAdjustmentType"), queryRequest.getPriceAdjustmentType()));
            }

            // 店铺id
            if (queryRequest.getStoreId() != null) {
                predicates.add(cbuild.equal(root.get("storeId"), queryRequest.getStoreId()));
            }

            // 调价商品数
            if (queryRequest.getGoodsNum() != null) {
                predicates.add(cbuild.equal(root.get("goodsNum"), queryRequest.getGoodsNum()));
            }

            // 大于或等于 搜索条件:生效时间开始
            if (queryRequest.getEffectiveTimeBegin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("effectiveTime"),
                        queryRequest.getEffectiveTimeBegin()));
            }
            // 小于或等于 搜索条件:生效时间截止
            if (queryRequest.getEffectiveTimeEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("effectiveTime"),
                        queryRequest.getEffectiveTimeEnd()));
            }

            // 模糊查询 - 制单人名称
            if (StringUtils.isNotEmpty(queryRequest.getCreatorName())) {
                predicates.add(cbuild.like(root.get("creatorName"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getCreatorName()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 制单人账户
            if (StringUtils.isNotEmpty(queryRequest.getCreatorAccount())) {
                predicates.add(cbuild.like(root.get("creatorAccount"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getCreatorAccount()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 创建人
            if (StringUtils.isNotEmpty(queryRequest.getCreatePerson())) {
                predicates.add(cbuild.like(root.get("createPerson"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getCreatePerson()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
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

            // 是否确认：0 未确认、1 已确认
            if (queryRequest.getConfirmFlag() != null) {
                predicates.add(cbuild.equal(root.get("confirmFlag"), queryRequest.getConfirmFlag()));
            }

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
