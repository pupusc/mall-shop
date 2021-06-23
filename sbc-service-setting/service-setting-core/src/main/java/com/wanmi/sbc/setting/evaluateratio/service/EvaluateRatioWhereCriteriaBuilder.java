package com.wanmi.sbc.setting.evaluateratio.service;

import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.setting.api.request.evaluateratio.EvaluateRatioQueryRequest;
import com.wanmi.sbc.setting.evaluateratio.model.root.EvaluateRatio;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.apache.commons.lang3.StringUtils;
import com.wanmi.sbc.common.util.XssUtils;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>商品评价系数设置动态查询条件构建器</p>
 * @author liutao
 * @date 2019-02-27 15:53:40
 */
public class EvaluateRatioWhereCriteriaBuilder {
    public static Specification<EvaluateRatio> build(EvaluateRatioQueryRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 批量查询-系数idList
            if (CollectionUtils.isNotEmpty(queryRequest.getRatioIdList())) {
                predicates.add(root.get("ratioId").in(queryRequest.getRatioIdList()));
            }

            // 系数id
            if (StringUtils.isNotEmpty(queryRequest.getRatioId())) {
                predicates.add(cbuild.equal(root.get("ratioId"), queryRequest.getRatioId()));
            }

            // 商品评论系数
            if (queryRequest.getGoodsRatio() != null) {
                predicates.add(cbuild.equal(root.get("goodsRatio"), queryRequest.getGoodsRatio()));
            }

            // 服务评论系数
            if (queryRequest.getServerRatio() != null) {
                predicates.add(cbuild.equal(root.get("serverRatio"), queryRequest.getServerRatio()));
            }

            // 物流评分系数
            if (queryRequest.getLogisticsRatio() != null) {
                predicates.add(cbuild.equal(root.get("logisticsRatio"), queryRequest.getLogisticsRatio()));
            }

            // 是否删除标志 0：否，1：是
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

            // 大于或等于 搜索条件:修改时间开始
            if (queryRequest.getUpdateTimeBegin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("updateTime"),
                        queryRequest.getUpdateTimeBegin()));
            }
            // 小于或等于 搜索条件:修改时间截止
            if (queryRequest.getUpdateTimeEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("updateTime"),
                        queryRequest.getUpdateTimeEnd()));
            }

            // 模糊查询 - 修改人
            if (StringUtils.isNotEmpty(queryRequest.getUpdatePerson())) {
                predicates.add(cbuild.like(root.get("updatePerson"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getUpdatePerson()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 大于或等于 搜索条件:删除时间开始
            if (queryRequest.getDelTimeBegin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("delTime"),
                        queryRequest.getDelTimeBegin()));
            }
            // 小于或等于 搜索条件:删除时间截止
            if (queryRequest.getDelTimeEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("delTime"),
                        queryRequest.getDelTimeEnd()));
            }

            // 模糊查询 - 删除人
            if (StringUtils.isNotEmpty(queryRequest.getDelPerson())) {
                predicates.add(cbuild.like(root.get("delPerson"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getDelPerson()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
