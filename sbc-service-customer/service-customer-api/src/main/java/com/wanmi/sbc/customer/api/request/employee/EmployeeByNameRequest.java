package com.wanmi.sbc.customer.api.request.employee;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeByNameRequest extends BaseQueryRequest {

    private static final long serialVersionUID = 1280231261244258667L;
    /**
     * 业务员名称
     */
    @ApiModelProperty(value = "业务员名称")
    private String name;

    /**
     * 部门id列表(数据隔离)
     */
    @ApiModelProperty(value = "部门列表")
    private List<String> departmentIsolationIdList;

    /**
     * 管理部门id列表(数据隔离)
     */
    @ApiModelProperty(value = "管理部门列表")
    private List<String> manageDepartmentIdList;

    /**
     * 所属部门id列表(数据隔离)
     */
    @ApiModelProperty(value = "所属部门列表")
    private List<String> belongToDepartmentIdList;

    /**
     * 是否有归属部门或管理部门
     */
    private Boolean belongToDepartment;

    private String employeeId;

    /**
     * 部门id列表
     */
    @ApiModelProperty(value = "部门列表")
    private List<String> departmentIds;

    /**
     * 是否是主账号
     */
    private Integer isMaster;

    /**
     * 管理部门集合
     */
    private String manageDepartmentIds;

    public List<String> getDepartmentIds() {

        if (CollectionUtils.isNotEmpty(departmentIds) && CollectionUtils.isNotEmpty(departmentIsolationIdList)) {
            departmentIds.retainAll(departmentIsolationIdList);
            return departmentIds;
        }
        if (CollectionUtils.isNotEmpty(departmentIsolationIdList)){
            return departmentIsolationIdList;
        }
        return departmentIds;
    }
}
