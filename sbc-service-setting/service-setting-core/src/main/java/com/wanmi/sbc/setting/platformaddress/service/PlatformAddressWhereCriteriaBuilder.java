package com.wanmi.sbc.setting.platformaddress.service;

import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.setting.api.request.platformaddress.PlatformAddressQueryRequest;
import com.wanmi.sbc.setting.platformaddress.model.root.PlatformAddress;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>平台地址信息动态查询条件构建器</p>
 * @author dyt
 * @date 2020-03-30 14:39:57
 */
public class PlatformAddressWhereCriteriaBuilder {
    public static Specification<PlatformAddress> build(PlatformAddressQueryRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 批量查询-主键idList
            if (CollectionUtils.isNotEmpty(queryRequest.getIdList())) {
                predicates.add(root.get("id").in(queryRequest.getIdList()));
            }

            // 主键id
            if (StringUtils.isNotEmpty(queryRequest.getId())) {
                predicates.add(cbuild.equal(root.get("id"), queryRequest.getId()));
            }

            // 模糊查询 - 地址id
            if (StringUtils.isNotEmpty(queryRequest.getAddrId())) {
                predicates.add(cbuild.equal(root.get("addrId"), queryRequest.getAddrId()));
            }

            // 批量查询-地址idList
            if (CollectionUtils.isNotEmpty(queryRequest.getAddrIdList())) {
                predicates.add(root.get("addrId").in(queryRequest.getAddrIdList()));
            }

            // 精确查询 - 地址名称
            if (StringUtils.isNotEmpty(queryRequest.getAddrName())) {
                predicates.add(cbuild.equal(root.get("addrName"), XssUtils.replaceLikeWildcard(queryRequest.getAddrName())));
            }

            // 模糊查询 - 父地址ID
            if (StringUtils.isNotEmpty(queryRequest.getAddrParentId())) {
                predicates.add(cbuild.equal(root.get("addrParentId"), queryRequest.getAddrParentId()));
            }

            // 批量查询-父地址idList
            if (CollectionUtils.isNotEmpty(queryRequest.getAddrParentIdList())) {
                predicates.add(root.get("addrParentId").in(queryRequest.getAddrParentIdList()));
            }

            // 地址层级(0-省级;1-市级;2-区县级;3-乡镇或街道级)
            if (queryRequest.getAddrLevel() != null) {
                predicates.add(cbuild.equal(root.get("addrLevel"), queryRequest.getAddrLevel()));
            }

            // 批量查询-地址层级(0-省级;1-市级;2-区县级;3-乡镇或街道级)
            if (CollectionUtils.isNotEmpty(queryRequest.getAddrLevels())) {
                predicates.add(root.get("addrLevel").in(queryRequest.getAddrLevels()));
            }

            // 大于或等于 搜索条件:入库时间开始
            if (queryRequest.getCreateTimeBegin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("createTime"),
                        queryRequest.getCreateTimeBegin()));
            }
            // 小于或等于 搜索条件:入库时间截止
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

            // 是否删除标志 0：否，1：是；默认0
            if (queryRequest.getDelFlag() != null) {
                predicates.add(cbuild.equal(root.get("delFlag"), queryRequest.getDelFlag()));
            }

            // 大于或等于 搜索条件:删除时间开始
            if (queryRequest.getDeleteTimeBegin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("deleteTime"),
                        queryRequest.getDeleteTimeBegin()));
            }
            // 小于或等于 搜索条件:删除时间截止
            if (queryRequest.getDeleteTimeEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("deleteTime"),
                        queryRequest.getDeleteTimeEnd()));
            }

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
