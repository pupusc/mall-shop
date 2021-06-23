package com.wanmi.sbc.goods.virtualcoupon.service;

import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.goods.api.request.virtualcoupon.VirtualCouponQueryRequest;
import com.wanmi.sbc.goods.util.XssUtils;
import com.wanmi.sbc.goods.virtualcoupon.model.root.VirtualCoupon;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>卡券动态查询条件构建器</p>
 * @author 梁善
 * @date 2021-01-25 10:19:19
 */
public class VirtualCouponWhereCriteriaBuilder {
    public static Specification<VirtualCoupon> build(VirtualCouponQueryRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 批量查询-电子卡券IDList
            if (CollectionUtils.isNotEmpty(queryRequest.getIdList())) {
                predicates.add(root.get("id").in(queryRequest.getIdList()));
            }

            // 批量排除-电子卡券IDList
            if (CollectionUtils.isNotEmpty(queryRequest.getNotIdList())) {
                predicates.add( cbuild.not(root.get("id").in(queryRequest.getNotIdList())));
            }
            // 电子卡券ID
            if (queryRequest.getId() != null) {
                predicates.add(cbuild.equal(root.get("id"), queryRequest.getId()));
            }



            // 店铺标识
            if (queryRequest.getStoreId() != null) {
                predicates.add(cbuild.equal(root.get("storeId"), queryRequest.getStoreId()));
            }

            // 模糊查询 - 名称
            if (StringUtils.isNotEmpty(queryRequest.getName())) {
                predicates.add(cbuild.like(root.get("name"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getName()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 0:兑换码 1:券码+密钥 2:链接
            if (queryRequest.getProvideType() != null) {
                predicates.add(cbuild.equal(root.get("provideType"), queryRequest.getProvideType()));
            }

            // 0:未发布 1:已发布
            if (queryRequest.getPublishStatus() != null) {
                predicates.add(cbuild.equal(root.get("publishStatus"), queryRequest.getPublishStatus()));
            }

            // 删除标识;0:未删除1:已删除
            if (queryRequest.getDelFlag() != null) {
                predicates.add(cbuild.equal(root.get("delFlag"), queryRequest.getDelFlag()));
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

            // 模糊查询 - 创建人
            if (StringUtils.isNotEmpty(queryRequest.getCreatePerson())) {
                predicates.add(cbuild.like(root.get("createPerson"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getCreatePerson()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 大于或等于 搜索条件:更新时间开始
            if (queryRequest.getUpdateTimeBegin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("updateTime"),
                        queryRequest.getUpdateTimeBegin()));
            }
            // 小于或等于 搜索条件:更新时间截止
            if (queryRequest.getUpdateTimeEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("updateTime"),
                        queryRequest.getUpdateTimeEnd()));
            }

            // 模糊查询 - 更新人
            if (StringUtils.isNotEmpty(queryRequest.getUpdatePerson())) {
                predicates.add(cbuild.like(root.get("updatePerson"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getUpdatePerson()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }
            List<Predicate> predicatesOr = new ArrayList<>();
            // 卡券id
            if (queryRequest.getSkuId() != null) {
                Predicate predicate = cbuild.equal(root.get("skuId"), queryRequest.getSkuId());
                Predicate or = cbuild.or(predicate);
                predicatesOr.add(or);
                Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
                Predicate[] pOr = predicatesOr.toArray(new Predicate[predicatesOr.size()]);
                Predicate p2 = cbuild.and(p);
                Predicate p3 = cbuild.and(pOr);
                return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.or(p2,p3);

            }else {
                Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
                return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
            }
        };
    }
}
