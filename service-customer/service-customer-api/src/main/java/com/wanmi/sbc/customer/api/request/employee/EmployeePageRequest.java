package com.wanmi.sbc.customer.api.request.employee;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.enums.AccountType;
import com.wanmi.sbc.customer.bean.enums.AccountState;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

@ApiModel
@Data
public class EmployeePageRequest extends BaseQueryRequest {


    private static final long serialVersionUID = -7484586049707426692L;

    private String employeeId;

    /**
     * 员工姓名
     */
    @ApiModelProperty(value = "员工姓名")
    private String userName;

    /**
     * 用户手机
     */
    @ApiModelProperty(value = "用户手机")
    private String userPhone;

    /**
     * 账户名称
     */
    @ApiModelProperty(value = "账户名称")
    private String accountName;

    /**
     * 是否是业务员
     */
    @ApiModelProperty(value = "是否是业务员(0 是 1否)")
    private Integer isEmployee;

    /**
     * 账户状态
     */
    @ApiModelProperty(value = "账户状态")
    private AccountState accountState;

    /**
     * 角色id
     */
    @ApiModelProperty(value = "角色列表")
    private List<String> roleIds;

    @ApiModelProperty(value = "员工编号列表")
    private List<String> employeeIds;

    /**
     * 账号类型 0 b2b账号 1 s2b平台端账号 2 s2b商家端账号
     */
    @ApiModelProperty(value = "账号类型")
    private AccountType accountType;

    /**
     * 商家id
     */
    @ApiModelProperty(value = "商家id")
    private Long companyInfoId;

    /**
     * 员工禁用原因
     */
    @ApiModelProperty(value = "员工禁用原因")
    private String accountDisableReason;

    /**
     * 是否主账号
     */
    @ApiModelProperty(value = "是否主账号")
    private Integer isMasterAccount;

    /**
     * 工号
     */
    @ApiModelProperty(value = "工号")
    private String jobNo;

    /**
     * 是否主管，0：否，1：是
     */
    @ApiModelProperty(value = "是否主管，0：否，1：是")
    private Integer isLeader;

    /**
     * 是否激活会员账号，0：否，1：是
     */
    @ApiModelProperty(value = "是否激活会员账号，0：否，1：是")
    private Integer becomeMember;

    /**
     * 部门id列表
     */
    @ApiModelProperty(value = "部门列表")
    private List<String> departmentIds;



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
     * 业务员名称（仅限业务员交接使用）
     */
    @ApiModelProperty(value = "业务员名称（仅限业务员交接使用）")
    private String employeeName;

    /**
     * 仅限业务员交接使用
     */
    @ApiModelProperty(value = "是否查询业务员")
    private Boolean isEmployeeSearch;

    /**
     * 关键词
     */
    @ApiModelProperty(value = "关键词")
    private String keywords;

    /**
     * 是否隐藏离职员工
     */
    @ApiModelProperty(value = "是否隐藏离职员工 0: 否  1：是")
    private Integer isHiddenDimission;

    /**
     * 是否有归属部门或管理部门
     */
    private Boolean belongToDepartment;

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
