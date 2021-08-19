package com.wanmi.sbc.setting.sensitivewords.service;

import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.setting.api.request.SensitiveWordsQueryRequest;
import com.wanmi.sbc.setting.sensitivewords.model.root.SensitiveWords;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>动态查询条件构建器</p>
 * @author wjj
 * @date 2019-02-22 16:09:48
 */
public class SensitiveWordsWhereCriteriaBuilder {
    public static Specification<SensitiveWords> build(SensitiveWordsQueryRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 批量查询-敏感词id 主键List
            if (CollectionUtils.isNotEmpty(queryRequest.getSensitiveIdList())) {
                predicates.add(root.get("sensitiveId").in(queryRequest.getSensitiveIdList()));
            }

            // 敏感词id 主键
            if (queryRequest.getSensitiveId() != null) {
                predicates.add(cbuild.equal(root.get("sensitiveId"), queryRequest.getSensitiveId()));
            }

            //敏感词名称查询
            if (StringUtils.isNotEmpty(queryRequest.getSensitiveWords())) {
                predicates.add(cbuild.equal(root.get("sensitiveWords"), queryRequest.getSensitiveWords()));
            }

            // 模糊查询 - 敏感词内容
            if (StringUtils.isNotEmpty(queryRequest.getLikeSensitiveWords())) {
                predicates.add(cbuild.like(root.get("sensitiveWords"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getLikeSensitiveWords()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 是否删除
            if (queryRequest.getDelFlag() != null) {
                predicates.add(cbuild.equal(root.get("delFlag"), queryRequest.getDelFlag()));
            }

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
