package com.wanmi.sbc.aop;

import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.customer.api.provider.department.DepartmentQueryProvider;
import com.wanmi.sbc.customer.api.provider.employee.EmployeeQueryProvider;
import com.wanmi.sbc.customer.api.request.department.DepartmentListAllByManageDepartmentIdsRequest;
import com.wanmi.sbc.customer.api.request.department.DepartmentListByBelongToDepartmentIdsRequest;
import com.wanmi.sbc.customer.api.request.department.DepartmentListByManageDepartmentIdsRequest;
import com.wanmi.sbc.customer.api.request.employee.EmployeeByIdRequest;
import com.wanmi.sbc.customer.api.response.employee.EmployeeByIdResponse;
import com.wanmi.sbc.setting.bean.enums.SystemAccount;
import com.wanmi.sbc.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by sunkun on 2018/3/20.
 */
@Aspect
@Component
@Slf4j
public class DepartmentIsolationSection {

    @Resource
    private CommonUtil commonUtil;

    @Autowired
    private EmployeeQueryProvider employeeQueryProvider;

    @Autowired
    private DepartmentQueryProvider departmentQueryProvider;

    @Pointcut("@annotation(com.wanmi.sbc.aop.DepartmentIsolation)")
    public void departmentIsolation() {
    }

    @Before(value = "departmentIsolation()")
    public void before(JoinPoint joinPoint) throws NoSuchMethodException, IllegalAccessException {
        Operator operator = commonUtil.getOperatorWithNull();
        if (Objects.isNull(operator) || StringUtils.isBlank(operator.getUserId())) {
            return;
        }
        //获取连接点的方法签名对象
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Object target = joinPoint.getTarget();
        //获取到当前执行的方法
        Method method = target.getClass().getMethod(methodSignature.getName(), methodSignature.getParameterTypes());
        //获取方法的注解
        DepartmentIsolation annotation = method.getAnnotation(DepartmentIsolation.class);
        Object[] objects = joinPoint.getArgs();
        for (Object object : objects) {
            if (object instanceof BaseRequest) {
                EmployeeByIdResponse employee = employeeQueryProvider.getById(
                        EmployeeByIdRequest.builder().employeeId(operator.getUserId()).build()
                ).getContext();
                if (SystemAccount.SYSTEM.getDesc().equals(employee.getAccountName()) || Constants.yes.equals(employee.getIsMasterAccount())){
                    try {
                    Class clazz = object.getClass();
                    Field field = clazz.getDeclaredField(annotation.isMaster());
                    field.setAccessible(true);
                    field.set(object, 1);
                    } catch (NoSuchFieldException e) {
                        log.error("employee id assignment failed.",e);
                    }
                    continue;
                }else{
                    try {
                        Class clazz = object.getClass();
                        Field field = clazz.getDeclaredField(annotation.isMaster());
                        field.setAccessible(true);
                        field.set(object, 0);
                    } catch (NoSuchFieldException e) {
                        log.error("employee id assignment failed.",e);
                    }
                }
                boolean isIncluedeParentDepartment = annotation.isIncluedeParentDepartment();
                //主管
                if(StringUtils.isNotBlank(employee.getManageDepartmentIds())){
                    List<String> departmentIds = new ArrayList<>();
                    List<String> manageDepartmentIds;
                    List<String> belongToDepartmentIds;
                    if (isIncluedeParentDepartment){
                        manageDepartmentIds = departmentQueryProvider.ListAllByManageDepartmentIds(new DepartmentListAllByManageDepartmentIdsRequest(employee.getManageDepartmentIds())).getContext().getDeparmentIds();
                        belongToDepartmentIds = departmentQueryProvider.listByBelongToDepartmentIds(new DepartmentListByBelongToDepartmentIdsRequest(employee.getDepartmentIds())).getContext().getDeparmentIds();
                    }else{
                        manageDepartmentIds = departmentQueryProvider.ListByManageDepartmentIds(new DepartmentListByManageDepartmentIdsRequest(employee.getManageDepartmentIds())).getContext().getDeparmentIds();
                        belongToDepartmentIds = Arrays.asList(employee.getDepartmentIds().split(","));
                    }
                     departmentIds.addAll(manageDepartmentIds);
                    departmentIds.addAll(belongToDepartmentIds);
                    departmentIds = departmentIds.stream().distinct().collect(Collectors.toList());

                    Class clazz = object.getClass();
                    try {
                        Field field = clazz.getDeclaredField(annotation.departmentIsolation());
                        field.setAccessible(true);
                        field.set(object, departmentIds);

                        field = clazz.getDeclaredField(annotation.manageDepartmentIdList());
                        field.setAccessible(true);
                        field.set(object, manageDepartmentIds);

                        field = clazz.getDeclaredField(annotation.manageDepartmentIds());
                        field.setAccessible(true);
                        field.set(object, employee.getManageDepartmentIds());

                        field = clazz.getDeclaredField(annotation.belongToDepartmentIds());
                        field.setAccessible(true);
                        field.set(object, belongToDepartmentIds);

                        field = clazz.getDeclaredField(annotation.employeeId());
                        field.setAccessible(true);
                        field.set(object, operator.getUserId());

                    } catch (NoSuchFieldException e) {
                        log.error("employee id assignment failed.",e);
                    }
                }else {
                    //员工
                    String belongToDepartmentIds = employee.getDepartmentIds();
                    //归属部门
                    if (StringUtils.isNotBlank(belongToDepartmentIds)){
                        List<String> belongToDepartmentIdList;
                        if (isIncluedeParentDepartment){
                            belongToDepartmentIdList = departmentQueryProvider.listByBelongToDepartmentIds(new DepartmentListByBelongToDepartmentIdsRequest(employee.getDepartmentIds())).getContext().getDeparmentIds();
                        }else{
                            belongToDepartmentIdList = Arrays.asList(employee.getDepartmentIds().split(","));
                        }
                        Class clazz = object.getClass();
                        try {
                            Field field = clazz.getDeclaredField(annotation.departmentIsolation());
                            field.setAccessible(true);
                            field.set(object, belongToDepartmentIdList);

                            field = clazz.getDeclaredField(annotation.belongToDepartmentIds());
                            field.setAccessible(true);
                            field.set(object, belongToDepartmentIdList);

                            field = clazz.getDeclaredField(annotation.employeeId());
                            field.setAccessible(true);
                            field.set(object, operator.getUserId());

                        } catch (NoSuchFieldException e) {
                            log.error("employee id assignment failed.",e);
                        }
                    }else{
                        Class clazz = object.getClass();
                        try {
                            Field field = clazz.getDeclaredField(annotation.belongToDepartment());
                            field.setAccessible(true);
                            field.set(object, Boolean.FALSE);
                            field = clazz.getDeclaredField(annotation.employeeId());
                            field.setAccessible(true);
                            field.set(object, operator.getUserId());
                        } catch (NoSuchFieldException e) {
                            log.error("employee id assignment failed.",e);
                        }
                    }
                }
            }
        }

    }
}
