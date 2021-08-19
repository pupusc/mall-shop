package com.wanmi.sbc.setting.thirdaddress.service;

import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.setting.api.request.thirdaddress.ThirdAddressQueryRequest;
import com.wanmi.sbc.setting.bean.vo.ThirdAddressPageVO;
import com.wanmi.sbc.setting.thirdaddress.model.root.ThirdAddress;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.data.jpa.domain.Specification;
import org.apache.commons.lang3.StringUtils;
import com.wanmi.sbc.common.util.XssUtils;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>第三方地址映射表动态查询条件构建器</p>
 * @author dyt
 * @date 2020-08-14 13:41:44
 */
public class ThirdAddressWhereCriteriaBuilder {
    public static Specification<ThirdAddress> build(ThirdAddressQueryRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 批量查询-第三方地址主键List
            if (CollectionUtils.isNotEmpty(queryRequest.getIdList())) {
                predicates.add(root.get("id").in(queryRequest.getIdList()));
            }

            // 第三方地址主键
            if (StringUtils.isNotEmpty(queryRequest.getId())) {
                predicates.add(cbuild.equal(root.get("id"), queryRequest.getId()));
            }

            // 批量查询-第三方地址主键List
            if (CollectionUtils.isNotEmpty(queryRequest.getThirdAddrIdList())) {
                predicates.add(root.get("thirdAddrId").in(queryRequest.getThirdAddrIdList()));
            }

            // 精确查询 - 第三方地址编码id
            if (StringUtils.isNotEmpty(queryRequest.getThirdAddrId())) {
                predicates.add(cbuild.equal(root.get("thirdAddrId"), queryRequest.getThirdAddrId()));
            }

            // 精确查询 - 第三方父级地址编码id
            if (StringUtils.isNotEmpty(queryRequest.getThirdParentId())) {
                predicates.add(cbuild.equal(root.get("thirdParentId"), queryRequest.getThirdParentId()));
            }

            // 批量查询-平台地址idList
            if (CollectionUtils.isNotEmpty(queryRequest.getPlatformAddrIdList())) {
                predicates.add(root.get("platformAddrId").in(queryRequest.getPlatformAddrIdList()));
            }

            // 模糊查询 - 地址名称
            if (StringUtils.isNotEmpty(queryRequest.getAddrName())) {
                predicates.add(cbuild.like(root.get("addrName"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getAddrName()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 地址层级(0-省级;1-市级;2-区县级;3-乡镇或街道级)
            if (queryRequest.getLevel() != null) {
                predicates.add(cbuild.equal(root.get("level"), queryRequest.getLevel()));
            }

            // 第三方标志 0:likedMall 1:京东
            if (queryRequest.getThirdFlag() != null) {
                predicates.add(cbuild.equal(root.get("thirdFlag"), queryRequest.getThirdFlag()));
            }

            // 精确查询 - 平台地址id
            if (StringUtils.isNotEmpty(queryRequest.getPlatformAddrId())) {
                predicates.add(cbuild.equal(root.get("platformAddrId"), queryRequest.getPlatformAddrId()));
            }

            //为空 - 平台地址id
            if (Boolean.TRUE.equals(queryRequest.getEmplyPlatformAddrId())) {
                predicates.add(root.get("platformAddrId").isNull());
            }

            // 删除标志
            if (queryRequest.getDelFlag() != null) {
                predicates.add(cbuild.equal(root.get("delFlag"), queryRequest.getDelFlag()));
            }

            // 非地址名称标志
            if (StringUtils.isNotBlank(queryRequest.getNotAddrName())) {
                predicates.add(cbuild.notEqual(root.get("addrName"), queryRequest.getNotAddrName()));
            }

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }

    /**
     * 查询对象转换
     *
     * @param sqlResult
     * @return
     */
    public static List<ThirdAddressPageVO> converter(List<Map<String, Object>> sqlResult) {
        return sqlResult.stream().map(item ->
                ThirdAddressPageVO.builder()
                        .provId(toStr(item, "provId"))
                        .provName(toStr(item, "provName"))
                        .platformProvId(toStr(item, "platformProvId"))
                        .cityId(toStr(item, "cityId"))
                        .cityName(toStr(item, "cityName"))
                        .platformCityId(toStr(item, "platformCityId"))
                        .districtId(toStr(item, "areaId"))
                        .districtName(toStr(item, "areaName"))
                        .platformDistrictId(toStr(item, "platformAreaId"))
                        .streetId(toStr(item, "streetId"))
                        .streetName(toStr(item, "streetName"))
                        .platformStreetId(toStr(item, "platformStreetId"))
                        .thirdPlatformType(toType(item, "thirdFlag"))
                        .build()
        ).collect(Collectors.toList());
    }

    private static String toStr(Map<String, Object> map, String key) {
        return map.get(key) != null ? map.get(key).toString() : null;
    }

    private static ThirdPlatformType toType(Map<String, Object> map, String key) {
        return map.get(key) != null ? ThirdPlatformType.fromValue(NumberUtils.toInt(map.get(key).toString(), 0)) : null;
    }
}
