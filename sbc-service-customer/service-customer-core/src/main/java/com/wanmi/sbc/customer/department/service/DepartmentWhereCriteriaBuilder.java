package com.wanmi.sbc.customer.department.service;

import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.customer.api.request.department.DepartmentQueryRequest;
import com.wanmi.sbc.customer.department.model.root.Department;
import com.wanmi.sbc.customer.util.XssUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>部门管理动态查询条件构建器</p>
 * @author wanggang
 * @date 2020-02-26 19:02:40
 */
public class DepartmentWhereCriteriaBuilder {
    public static Specification<Department> build(DepartmentQueryRequest queryRequest) {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 批量查询-主键List
            if (CollectionUtils.isNotEmpty(queryRequest.getDepartmentIdList())) {
                predicates.add(root.get("departmentId").in(queryRequest.getDepartmentIdList()));
            }

            // 主键
            if (queryRequest.getDepartmentId() != null) {
                predicates.add(cbuild.equal(root.get("departmentId"), queryRequest.getDepartmentId()));
            }

            // 主键
            if (CollectionUtils.isNotEmpty(queryRequest.getDepartmentIsolationIdList())) {
                predicates.add(root.get("departmentId").in(queryRequest.getDepartmentIsolationIdList()));
            }

            // 模糊查询 - 部门名称
            if (StringUtils.isNotEmpty(queryRequest.getDepartmentName())) {
                predicates.add(cbuild.like(root.get("departmentName"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getDepartmentName()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 公司ID
            if (queryRequest.getCompanyInfoId() != null) {
                predicates.add(cbuild.equal(root.get("companyInfoId"), queryRequest.getCompanyInfoId()));
            }

            // 层级
            if (queryRequest.getDepartmentGrade() != null) {
                predicates.add(cbuild.equal(root.get("departmentGrade"), queryRequest.getDepartmentGrade()));
            }

            // 模糊查询 - 员工名称
            if (StringUtils.isNotEmpty(queryRequest.getEmployeeName())) {
                predicates.add(cbuild.like(root.get("employeeName"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getEmployeeName()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 排序
            if (queryRequest.getDepartmentSort() != null) {
                predicates.add(cbuild.equal(root.get("departmentSort"), queryRequest.getDepartmentSort()));
            }

            // 父部门id（上一级）
            if (queryRequest.getParentDepartmentId() != null) {
                predicates.add(cbuild.equal(root.get("parentDepartmentId"), queryRequest.getParentDepartmentId()));
            }

            // 员工数
            if (queryRequest.getEmployeeNum() != null) {
                predicates.add(cbuild.equal(root.get("employeeNum"), queryRequest.getEmployeeNum()));
            }

            //删除标记
            if(Objects.nonNull(queryRequest.getDelFlag())){
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

            // 模糊查询 - 更新人
            if (StringUtils.isNotEmpty(queryRequest.getUpdatePerson())) {
                predicates.add(cbuild.like(root.get("updatePerson"), StringUtil.SQL_LIKE_CHAR
                        .concat(XssUtils.replaceLikeWildcard(queryRequest.getUpdatePerson()))
                        .concat(StringUtil.SQL_LIKE_CHAR)));
            }

            // 模糊查询 - 父部门id集合（多级）
            if (StringUtils.isNotEmpty(queryRequest.getParentDepartmentIds())) {
                predicates.add(cbuild.like(root.get("parentDepartmentIds"), XssUtils.replaceLikeWildcard(queryRequest.getParentDepartmentIds()).concat(StringUtil.SQL_LIKE_CHAR)));
            }

            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }
}
