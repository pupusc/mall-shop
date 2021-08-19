package com.wanmi.sbc.elastic.api.request.employee;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.enums.AccountType;
import com.wanmi.sbc.customer.bean.enums.AccountState;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import java.util.Arrays;
import java.util.List;

@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EsEmployeePageRequest extends BaseQueryRequest {
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

    public NativeSearchQueryBuilder esCriteria() {

        NativeSearchQueryBuilder queryBuilder= new NativeSearchQueryBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        //员工账户
        if (StringUtils.isNotEmpty(this.getAccountName())) {
            boolQueryBuilder.must(QueryBuilders.wildcardQuery("accountName", "*" + this.getAccountName()+ "*"));
        }
        //账号类型
        if (this.getAccountType() != null) {
            boolQueryBuilder.must(QueryBuilders.termQuery("accountType", this.getAccountType().toValue()));
        }

        //商家id
        if (this.getCompanyInfoId() != null) {
            boolQueryBuilder.must(QueryBuilders.termQuery("companyInfoId", this.getCompanyInfoId()));
        }

        //员工姓名
        if (StringUtils.isNotEmpty(this.getUserName())) {
            boolQueryBuilder.must(QueryBuilders.wildcardQuery("employeeName", "*" + this.getUserName() + "*"));
        }

        //员工手机
        if (StringUtils.isNotEmpty(this.getUserPhone())) {
            boolQueryBuilder.must(QueryBuilders.wildcardQuery("employeeMobile", "*" + this.getUserPhone() + "*"));
        }

        //员工工号
        if (StringUtils.isNotEmpty(this.getJobNo())) {
            boolQueryBuilder.must(QueryBuilders.wildcardQuery("jobNo", "*" + this.getJobNo() + "*"));
        }

        // 员工状态
        if (this.getAccountState() != null) {
            boolQueryBuilder.must(QueryBuilders.termQuery("accountState", this.getAccountState().toValue()));
        }

        // 是否主管
        if (this.getIsLeader() != null) {
            boolQueryBuilder.must(QueryBuilders.termQuery("isLeader", this.getIsLeader()));
        }

        // 是否业务员
        if (this.getIsEmployee() != null) {
            boolQueryBuilder.must(QueryBuilders.termQuery("isEmployee", this.getIsEmployee()));
        }

        // 是否激活会员账户
        if (this.getBecomeMember() != null) {
            boolQueryBuilder.must(QueryBuilders.termQuery("becomeMember", this.getBecomeMember()));
        }
        // 批量查询-部门id
        if (CollectionUtils.isNotEmpty(this.getDepartmentIds())) {
            boolQueryBuilder.must(QueryBuilders.termsQuery("departmentIds",this.getDepartmentIds()));
        }
        // 批量查询-角色id
        if (CollectionUtils.isNotEmpty(this.getRoleIds())) {
            boolQueryBuilder.must(QueryBuilders.termsQuery("roleIds",this.getRoleIds()));
        }
        queryBuilder.withQuery(boolQueryBuilder);
        return queryBuilder;
    }
}
