package com.wanmi.sbc.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 部门数据隔离
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DepartmentIsolation {

    /**
     * 是否主账号
     * @return
     */
    String isMaster() default "isMaster";

    /**
     * 是否包含父级部门结构（部门树默认true、员工数据需设置为false）
     * @return
     */
    boolean isIncluedeParentDepartment() default true;

    /**
     * 管理部门/所属部门集合
     * @return
     */
    String departmentIsolation() default "departmentIsolationIdList";

    /**
     * 管理部门
     * @return
     */
    String manageDepartmentIdList() default "manageDepartmentIdList";

    /**
     * 所属部门
     * @return
     */
    String belongToDepartmentIds() default "belongToDepartmentIdList";

    /**
     * 有无归属部门/管理部门
     * @return
     */
    String belongToDepartment() default "belongToDepartment";

    /**
     * 员工ID
     * @return
     */
    String employeeId() default "employeeId";

    /**
     * 管理部门集合
     */
    String manageDepartmentIds() default "manageDepartmentIds";
}
