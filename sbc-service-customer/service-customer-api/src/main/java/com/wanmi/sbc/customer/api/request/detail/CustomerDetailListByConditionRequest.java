package com.wanmi.sbc.customer.api.request.detail;

import com.wanmi.sbc.customer.bean.enums.CustomerStatus;
import com.wanmi.sbc.customer.bean.enums.CustomerType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 会员详情查询参数
 * Created by CHENLI on 2017/4/19.
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDetailListByConditionRequest implements Serializable {

    private static final long serialVersionUID = -7395969401233896552L;
    /**
     * 会员详细信息标识UUID
     */
    @ApiModelProperty(value = "会员详细信息标识UUID")
    private String customerDetailId;

    /**
     * 批量多个会员详细信息标识UUID
     */
    @ApiModelProperty(value = "批量多个会员详细信息标识UUID")
    private List<String> customerDetailIds;

    /**
     * 客户ID
     */
    @ApiModelProperty(value = "客户ID")
    private String customerId;

    /**
     * 账户
     */
    @ApiModelProperty(value = "账户")
    private String customerAccount;

    /**
     * 客户IDs
     */
    @ApiModelProperty(value = "客户IDs")
    private List<String> customerIds;

    /**
     * 客户等级ID
     */
    @ApiModelProperty(value = "客户等级ID")
    private Long customerLevelId;

    /**
     * 会员名称
     */
    @ApiModelProperty(value = "会员名称")
    private String customerName;

    /**
     * 省
     */
    @ApiModelProperty(value = "省")
    private Long provinceId;

    /**
     * 市
     */
    @ApiModelProperty(value = "市")
    private Long cityId;

    /**
     * 区
     */
    @ApiModelProperty(value = "区")
    private Long areaId;

    /**
     * 账号状态 0：启用中  1：禁用中
     */
    @ApiModelProperty(value = "账号状态")
    private CustomerStatus customerStatus;

    /**
     * 删除标志 0未删除 1已删除
     */
    @ApiModelProperty(value = "删除标志")
    private Integer delFlag;

    /**
     * 审核状态 0：待审核 1：已审核 2：审核未通过
     */
    @ApiModelProperty(value = "审核状态(0:待审核,1:已审核,2:审核未通过)",dataType = "com.wanmi.sbc.customer.bean.enums.CheckState")
    private Integer checkState;

    /**
     * 负责业务员
     */
    @ApiModelProperty(value = "负责业务员")
    private String employeeId;

    /**
     * 精确查询-账户
     */
    @ApiModelProperty(value = "精确查询-账户")
    private String equalCustomerAccount;

    /**
     * 精确查找-商家下的客户
     */
    @ApiModelProperty(value = "精确查找-商家下的客户")
    private Long companyInfoId;

    /**
     * 禁用原因
     */
    @ApiModelProperty(value = "禁用原因")
    private String forbidReason;

    /**
     * 用户类型
     */
    @ApiModelProperty(value = "用户类型")
    private CustomerType customerType;
}
