package com.wanmi.sbc.message.smssignfileinfo.service;

import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.message.api.request.smssignfileinfo.SmsSignFileInfoQueryRequest;
import com.wanmi.sbc.message.smssignfileinfo.model.root.SmsSignFileInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>短信签名文件信息动态查询条件构建器</p>
 * @author lvzhenwei
 * @date 2019-12-04 14:19:35
 */
public class SmsSignFileInfoWhereCriteriaBuilder {
    public static Specification<SmsSignFileInfo> build(SmsSignFileInfoQueryRequest queryRequest) {
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

            // 短信签名id
            if (queryRequest.getSmsSignId() != null) {
                predicates.add(cbuild.equal(root.get("smsSignId"), queryRequest.getSmsSignId()));
            }

            // 模糊查询 - 文件路径
            if (StringUtils.isNotEmpty(queryRequest.getFileUrl())) {
                predicates.add(cbuild.like(root.get("fileUrl"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getFileUrl()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 文件名称
            if (StringUtils.isNotEmpty(queryRequest.getFileName())) {
                predicates.add(cbuild.like(root.get("fileName"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getFileName()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
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
