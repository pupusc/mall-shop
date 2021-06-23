package com.wanmi.sbc.customer.storereturnaddress.service;

import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.customer.api.request.storereturnaddress.StoreReturnAddressQueryRequest;
import com.wanmi.sbc.customer.storereturnaddress.model.root.StoreReturnAddress;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.jpa.domain.Specification;
import org.apache.commons.lang3.StringUtils;
import com.wanmi.sbc.common.util.XssUtils;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>店铺退货地址表动态查询条件构建器</p>
 * @author dyt
 * @date 2020-11-02 11:38:39
 */
public class StoreReturnAddressWhereCriteriaBuilder {
    public static Specification<StoreReturnAddress> build(StoreReturnAddressQueryRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 批量查询-收货地址IDList
            if (CollectionUtils.isNotEmpty(queryRequest.getAddressIdList())) {
                predicates.add(root.get("addressId").in(queryRequest.getAddressIdList()));
            }

            // 收货地址ID
            if (StringUtils.isNotEmpty(queryRequest.getAddressId())) {
                predicates.add(cbuild.equal(root.get("addressId"), queryRequest.getAddressId()));
            }

            // 公司信息ID
            if (queryRequest.getCompanyInfoId() != null) {
                predicates.add(cbuild.equal(root.get("companyInfoId"), queryRequest.getCompanyInfoId()));
            }

            // 店铺信息ID
            if (queryRequest.getStoreId() != null) {
                predicates.add(cbuild.equal(root.get("storeId"), queryRequest.getStoreId()));
            }

            // 模糊查询 - 收货人
            if (StringUtils.isNotEmpty(queryRequest.getConsigneeName())) {
                predicates.add(cbuild.like(root.get("consigneeName"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getConsigneeName()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 收货人手机号码
            if (StringUtils.isNotEmpty(queryRequest.getConsigneeNumber())) {
                predicates.add(cbuild.like(root.get("consigneeNumber"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getConsigneeNumber()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 省份
            if (queryRequest.getProvinceId() != null) {
                predicates.add(cbuild.equal(root.get("provinceId"), queryRequest.getProvinceId()));
            }

            // 市
            if (queryRequest.getCityId() != null) {
                predicates.add(cbuild.equal(root.get("cityId"), queryRequest.getCityId()));
            }

            // 区
            if (queryRequest.getAreaId() != null) {
                predicates.add(cbuild.equal(root.get("areaId"), queryRequest.getAreaId()));
            }

            // 街道id
            if (queryRequest.getStreetId() != null) {
                predicates.add(cbuild.equal(root.get("streetId"), queryRequest.getStreetId()));
            }

            // 模糊查询 - 详细街道地址
            if (StringUtils.isNotEmpty(queryRequest.getReturnAddress())) {
                predicates.add(cbuild.like(root.get("returnAddress"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getReturnAddress()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 是否是默认地址
            if (queryRequest.getIsDefaultAddress() != null) {
                predicates.add(cbuild.equal(root.get("isDefaultAddress"), queryRequest.getIsDefaultAddress()));
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
            if (queryRequest.getDeleteTimeBegin() != null) {
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("deleteTime"),
                        queryRequest.getDeleteTimeBegin()));
            }
            // 小于或等于 搜索条件:删除时间截止
            if (queryRequest.getDeleteTimeEnd() != null) {
                predicates.add(cbuild.lessThanOrEqualTo(root.get("deleteTime"),
                        queryRequest.getDeleteTimeEnd()));
            }

            // 模糊查询 - 删除人
            if (StringUtils.isNotEmpty(queryRequest.getDeletePerson())) {
                predicates.add(cbuild.like(root.get("deletePerson"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getDeletePerson()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
