package com.wanmi.sbc.customer.paidcard.service;

import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.customer.api.request.paidcard.PaidCardQueryRequest;
import com.wanmi.sbc.customer.paidcard.model.root.PaidCard;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.apache.commons.lang3.StringUtils;
import com.wanmi.sbc.common.util.XssUtils;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>付费会员动态查询条件构建器</p>
 * @author xuhai
 * @date 2021-01-29 14:03:56
 */
public class PaidCardWhereCriteriaBuilder {
    public static Specification<PaidCard> build(PaidCardQueryRequest queryRequest) {
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

            // 模糊查询 - 名称
            if (StringUtils.isNotEmpty(queryRequest.getName())) {
                predicates.add(cbuild.like(root.get("name"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getName()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 背景信息。背景颜色传十六进制类似 #ccc；背景图片传图片地址
            if (StringUtils.isNotEmpty(queryRequest.getBackground())) {
                predicates.add(cbuild.like(root.get("background"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getBackground()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 付费会员图标
            if (StringUtils.isNotEmpty(queryRequest.getIcon())) {
                predicates.add(cbuild.like(root.get("icon"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getIcon()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 折扣率
            if (queryRequest.getDiscountRate() != null) {
                predicates.add(cbuild.equal(root.get("discountRate"), queryRequest.getDiscountRate()));
            }

            // 模糊查询 - 规则说明
            if (StringUtils.isNotEmpty(queryRequest.getRule())) {
                predicates.add(cbuild.like(root.get("rule"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getRule()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 付费会员用户协议
            if (StringUtils.isNotEmpty(queryRequest.getAgreement())) {
                predicates.add(cbuild.like(root.get("agreement"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getAgreement()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 删除标识 1：删除；0：未删除
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

            // 启动禁用标识 1：启用；2：禁用
            if (queryRequest.getEnable() != null) {
                predicates.add(cbuild.equal(root.get("enable"), queryRequest.getEnable()));
            }

            // 模糊查询 - 创建人ID
            if (StringUtils.isNotEmpty(queryRequest.getCreatePerson())) {
                predicates.add(cbuild.like(root.get("createPerson"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getCreatePerson()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 修改人ID
            if (StringUtils.isNotEmpty(queryRequest.getUpdatePerson())) {
                predicates.add(cbuild.like(root.get("updatePerson"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getUpdatePerson()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 背景类型0背景色；1背景图片
            if (queryRequest.getBgType() != null) {
                predicates.add(cbuild.equal(root.get("bgType"), queryRequest.getBgType()));
            }

            // 模糊查询 - 前景色
            if (StringUtils.isNotEmpty(queryRequest.getTextColor())) {
                predicates.add(cbuild.like(root.get("textColor"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getTextColor()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            //获取方式
            if(Objects.nonNull(queryRequest.getAccessType())) {
                predicates.add(cbuild.equal(root.get("accessType"), queryRequest.getAccessType()));
            }

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
