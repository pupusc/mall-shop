package com.wanmi.sbc.customer.department.repository;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.customer.department.model.root.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>部门管理DAO</p>
 * @author wanggang
 * @date 2020-02-26 19:02:40
 */
@Repository
public interface DepartmentRepository extends JpaRepository<Department, String>,
        JpaSpecificationExecutor<Department> {

    /**
     * 根据部门名称、父部门id、查询未删除的部门信息
     * @param parentDepartmentId
     * @param DepartmentName
     * @param delFlag
     * @return
     */
    Department findByParentDepartmentIdAndDepartmentNameAndDelFlagAndCompanyInfoId(String parentDepartmentId,String DepartmentName, DeleteFlag delFlag,Long companyInfoId );

    /**
     * 根据部门id删除部门信息
     * @param departmentId
     * @return
     */
    @Query("update Department e set e.delFlag = 1 , updateTime = now() where e.departmentId = ?1 ")
    @Modifying
    int modifyByDepartmentId(String departmentId);

    /**
     * 删除子部门信息
     * @param parentDepartmentIds
     * @return
     */
    @Query("update Department e set e.delFlag = 1 , updateTime = now() where e.parentDepartmentIds like ?1% ")
    @Modifying
    int modifyByParentDepartmentIdIsStartingWith(String parentDepartmentIds);

    /**
     * 根据部门id列表查询
     * @param ids
     * @return
     */
    List<Department> findByDepartmentIdIn(List<String> ids);

    /**
     * 减部门数量
     * @param
     * @param ids
     */
    @Modifying
    @Query(value = "update department set employee_num = IF(employee_num < 1, 0, employee_num - 1) where department_id in (?1)", nativeQuery = true)
    void reduceEmployeeNum(List<String> ids);

    /**
     * 加部门数量
     * @param
     * @param ids
     */
    @Modifying
    @Query(value = "update Department set employeeNum = employeeNum + 1 where departmentId in (?1)")
    void addEmployeeNum(List<String> ids);


    /**
     * 根据部门id修改主管
     * @param departmentId
     * @return
     */
    @Query("update Department e set e.employeeId = ?2, e.employeeName = ?3 , updateTime = now() where e.departmentId = ?1 ")
    @Modifying
    int modifyLeaderByDepartmentId(String departmentId,String employeeId,String employeeName);

    /**
     * 根据主管id修改主管
     * @param employeeId
     * @param employeeName
     * @return
     */
    @Query("update Department e set  e.employeeName = ?2 , updateTime = now() where e.employeeId = ?1 ")
    @Modifying
    int modifyEmployeeNameByEmployeeId(String employeeId,String employeeName);

    /**
     * 主管被调换部门后，不可管理之前部门
     * @param departmentIds
     * @return
     */
    @Modifying
    @Query("update Department d set d.employeeId = null , d.employeeName = null  where departmentId in (?1)")
    int initDepartmentLeader(List<String> departmentIds);

    /**
     * 根据部门id查询为删除的部门
     * @return
     */
    Department findByDepartmentIdAndDelFlag(String id, DeleteFlag deleteFlag);

    /**
     * 查询以父级部门开始的部门集合信息（like 'abc|%'）
     * @param parentDepartmentIds
     * @return
     */
    List<Department> findByParentDepartmentIdsStartingWith(String parentDepartmentIds);

    long countByCompanyInfoIdAndDelFlag(Long companyInfoId,DeleteFlag deleteFlag);
}
