package com.wanmi.sbc.message.smstemplate.service;

import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.message.api.request.smstemplate.SmsTemplateQueryRequest;
import com.wanmi.sbc.message.smstemplate.model.root.SmsTemplate;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>短信模板动态查询条件构建器</p>
 * @author lvzhenwei
 * @date 2019-12-03 15:43:29
 */
public class SmsTemplateWhereCriteriaBuilder {
    public static Specification<SmsTemplate> build(SmsTemplateQueryRequest queryRequest) {
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

            // 业务类型
            if (StringUtils.isNotEmpty(queryRequest.getBusinessType())) {
                predicates.add(cbuild.equal(root.get("businessType"), queryRequest.getBusinessType()));
            }

            // 模糊查询 - 模板名称
            if (StringUtils.isNotEmpty(queryRequest.getTemplateName())) {
                predicates.add(cbuild.like(root.get("templateName"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getTemplateName()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 精准查询 - 模板名称
            if (StringUtils.isNotEmpty(queryRequest.getEqualTemplateName())) {
                predicates.add(cbuild.equal(root.get("templateName"), XssUtils.replaceLikeWildcard(queryRequest.getEqualTemplateName())));
            }

            // 模糊查询 - 模板内容
            if (StringUtils.isNotEmpty(queryRequest.getTemplateContent())) {
                predicates.add(cbuild.like(root.get("templateContent"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getTemplateContent()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 短信模板申请说明
            if (StringUtils.isNotEmpty(queryRequest.getRemark())) {
                predicates.add(cbuild.like(root.get("remark"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getRemark()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 短信类型。其中： 0：验证码。 1：短信通知。 2：推广短信。 短信类型,0：验证码,1：短信通知,2：推广短信,3：国际/港澳台消息。
            if (queryRequest.getTemplateType() != null) {
                predicates.add(cbuild.equal(root.get("templateType"), queryRequest.getTemplateType()));
            }

            // 模板状态，0：待审核，1：审核通过，2：审核未通过
            if (queryRequest.getReviewStatus() != null) {
                predicates.add(cbuild.equal(root.get("reviewStatus"), queryRequest.getReviewStatus()));
            }

            // 短信签名id
            if (Objects.nonNull(queryRequest.getSignId())) {
                predicates.add(cbuild.equal(root.get("signId"), queryRequest.getSignId()));
            }

            // 模糊查询 - 模板code,模板审核通过返回的模板code，发送短信时使用
            if (StringUtils.isNotEmpty(queryRequest.getTemplateCode())) {
                predicates.add(cbuild.like(root.get("templateCode"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getTemplateCode()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
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

            // 删除标识位，0：未删除，1：已删除
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
