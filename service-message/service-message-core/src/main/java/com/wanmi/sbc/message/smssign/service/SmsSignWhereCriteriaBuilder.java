package com.wanmi.sbc.message.smssign.service;

import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.message.api.request.smssign.SmsSignQueryRequest;
import com.wanmi.sbc.message.smssign.model.root.SmsSign;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>短信签名动态查询条件构建器</p>
 * @author lvzhenwei
 * @date 2019-12-03 15:49:24
 */
public class SmsSignWhereCriteriaBuilder {
    public static Specification<SmsSign> build(SmsSignQueryRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 批量查询-主键List
            if (CollectionUtils.isNotEmpty(queryRequest.getIdList())) {
                predicates.add(root.get("id").in(queryRequest.getIdList()));
            }

            // 主键
            if (queryRequest.getId() != null) {
                predicates.add(cbuild.equal(root.get("id"), queryRequest.getId()));
            }

            // 模糊查询 - 签名名称
            if (StringUtils.isNotEmpty(queryRequest.getSmsSignName())) {
                predicates.add(cbuild.like(root.get("smsSignName"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getSmsSignName()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 签名来源,0：企事业单位的全称或简称,1：工信部备案网站的全称或简称,2：APP应用的全称或简称,3：公众号或小程序的全称或简称,4：电商平台店铺名的全称或简称,5：商标名的全称或简称
            if (queryRequest.getSignSource() != null) {
                predicates.add(cbuild.equal(root.get("signSource"), queryRequest.getSignSource()));
            }

            // 模糊查询 - 短信签名申请说明
            if (StringUtils.isNotEmpty(queryRequest.getRemark())) {
                predicates.add(cbuild.like(root.get("remark"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getRemark()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 是否涉及第三方利益：0：否，1：是
            if (queryRequest.getInvolveThirdInterest() != null) {
                predicates.add(cbuild.equal(root.get("involveThirdInterest"), queryRequest.getInvolveThirdInterest()));
            }

            // 审核状态：0:待审核，1:审核通过，2:审核未通过
            if (queryRequest.getReviewStatus() != null) {
                predicates.add(cbuild.equal(root.get("reviewStatus"), queryRequest.getReviewStatus()));
            }

            // 模糊查询 - 审核原因
            if (StringUtils.isNotEmpty(queryRequest.getReviewReason())) {
                predicates.add(cbuild.like(root.get("reviewReason"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getReviewReason()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 短信配置id
            if (queryRequest.getSmsSettingId() != null) {
                predicates.add(cbuild.equal(root.get("smsSettingId"), queryRequest.getSmsSettingId()));
            }

            // 删除标识，0：未删除，1：已删除
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

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
