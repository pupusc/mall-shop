package com.wanmi.sbc.goods.goodslabel.service;

import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.goods.api.request.goodslabel.GoodsLabelQueryRequest;
import com.wanmi.sbc.goods.goodslabel.model.root.GoodsLabel;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>商品标签动态查询条件构建器</p>
 * @author dyt
 * @date 2020-09-29 13:57:19
 */
public class GoodsLabelWhereCriteriaBuilder {
    public static Specification<GoodsLabel> build(GoodsLabelQueryRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 批量查询-标签idList
            if (CollectionUtils.isNotEmpty(queryRequest.getGoodsLabelIdList())) {
                predicates.add(root.get("goodsLabelId").in(queryRequest.getGoodsLabelIdList()));
            }

            // 标签id
            if (queryRequest.getGoodsLabelId() != null) {
                predicates.add(cbuild.equal(root.get("goodsLabelId"), queryRequest.getGoodsLabelId()));
            }

            // 模糊查询 - 标签名称
            if (StringUtils.isNotEmpty(queryRequest.getLabelName())) {
                predicates.add(cbuild.like(root.get("labelName"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getLabelName()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 前端是否展示 0: 关闭 1:开启
            if (queryRequest.getLabelVisible() != null) {
                predicates.add(cbuild.equal(root.get("labelVisible"), queryRequest.getLabelVisible()));
            }

            // 排序
            if (queryRequest.getLabelSort() != null) {
                predicates.add(cbuild.equal(root.get("labelSort"), queryRequest.getLabelSort()));
            }

            // 店铺id
            if (queryRequest.getStoreId() != null) {
                predicates.add(cbuild.equal(root.get("storeId"), queryRequest.getStoreId()));
            }

            // 删除标识 0:未删除1:已删除
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

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
