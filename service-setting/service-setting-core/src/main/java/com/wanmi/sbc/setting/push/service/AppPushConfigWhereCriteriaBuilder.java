package com.wanmi.sbc.setting.push.service;

import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.setting.api.request.push.AppPushConfigQueryRequest;
import com.wanmi.sbc.setting.push.model.root.AppPushConfig;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>消息推送动态查询条件构建器</p>
 * @author chenyufei
 * @date 2019-05-10 14:39:59
 */
public class AppPushConfigWhereCriteriaBuilder {

    public static Specification<AppPushConfig> build(AppPushConfigQueryRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 批量查询-消息推送配置编号List
            if (CollectionUtils.isNotEmpty(queryRequest.getAppPushIdList())) {
                predicates.add(root.get("appPushId").in(queryRequest.getAppPushIdList()));
            }

            // 消息推送配置编号
            if (queryRequest.getAppPushId() != null) {
                predicates.add(cbuild.equal(root.get("appPushId"), queryRequest.getAppPushId()));
            }

            // 模糊查询 - 消息推送配置名称
            if (StringUtils.isNotEmpty(queryRequest.getAppPushName())) {
                predicates.add(cbuild.like(root.get("appPushName"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getAppPushName()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 消息推送提供商  0:友盟
            if (StringUtils.isNotEmpty(queryRequest.getAppPushManufacturer())) {
                predicates.add(cbuild.like(root.get("appPushManufacturer"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getAppPushManufacturer()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - Android App Key
            if (StringUtils.isNotEmpty(queryRequest.getAndroidAppKey())) {
                predicates.add(cbuild.like(root.get("androidAppKey"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getAndroidAppKey()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - Android Umeng Message Secret
            if (StringUtils.isNotEmpty(queryRequest.getAndroidUmengMessageSecret())) {
                predicates.add(cbuild.like(root.get("androidUmengMessageSecret"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getAndroidUmengMessageSecret()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - Android App Master Secret
            if (StringUtils.isNotEmpty(queryRequest.getAndroidAppMasterSecret())) {
                predicates.add(cbuild.like(root.get("androidAppMasterSecret"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getAndroidAppMasterSecret()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - IOS App Key
            if (StringUtils.isNotEmpty(queryRequest.getIosAppKey())) {
                predicates.add(cbuild.like(root.get("iosAppKey"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getIosAppKey()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - IOS App Master Secret
            if (StringUtils.isNotEmpty(queryRequest.getIosAppMasterSecret())) {
                predicates.add(cbuild.like(root.get("iosAppMasterSecret"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getIosAppMasterSecret()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 状态,0:未启用1:已启用
            if (queryRequest.getStatus() != null) {
                predicates.add(cbuild.equal(root.get("status"), queryRequest.getStatus()));
            }

            // 大于或等于 搜索条件:创建日期开始
            if (queryRequest.getCreateTimeBegin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("createTime"),
                        queryRequest.getCreateTimeBegin()));
            }
            // 小于或等于 搜索条件:创建日期截止
            if (queryRequest.getCreateTimeEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("createTime"),
                        queryRequest.getCreateTimeEnd()));
            }

            // 大于或等于 搜索条件:更新日期开始
            if (queryRequest.getUpdateTimeBegin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("updateTime"),
                        queryRequest.getUpdateTimeBegin()));
            }
            // 小于或等于 搜索条件:更新日期截止
            if (queryRequest.getUpdateTimeEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("updateTime"),
                        queryRequest.getUpdateTimeEnd()));
            }

            // 大于或等于 搜索条件:删除日期开始
            if (queryRequest.getDelTimeBegin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("delTime"),
                        queryRequest.getDelTimeBegin()));
            }
            // 小于或等于 搜索条件:删除日期截止
            if (queryRequest.getDelTimeEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("delTime"),
                        queryRequest.getDelTimeEnd()));
            }

            // 删除标志
            if (queryRequest.getDelFlag() != null) {
                predicates.add(cbuild.equal(root.get("delFlag"), queryRequest.getDelFlag()));
            }

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }

}
