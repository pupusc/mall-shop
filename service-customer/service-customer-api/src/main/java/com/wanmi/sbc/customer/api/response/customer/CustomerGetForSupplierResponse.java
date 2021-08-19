package com.wanmi.sbc.customer.api.response.customer;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.customer.bean.enums.CheckState;
import com.wanmi.sbc.customer.bean.enums.CustomerType;
import com.wanmi.sbc.customer.bean.enums.EnterpriseCheckState;
import com.wanmi.sbc.customer.bean.vo.CustomerDetailVO;
import com.wanmi.sbc.customer.bean.vo.EnterpriseInfoVO;
import com.wanmi.sbc.customer.bean.vo.PaidCardCustomerRelVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@ApiModel
@Data
public class CustomerGetForSupplierResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 客户ID
     */
    @ApiModelProperty(value = "客户ID")
    private String customerId;

    /**
     * 客户等级ID
     */
    @ApiModelProperty(value = "客户等级ID")
    private Long customerLevelId;

    /**
     * 客户成长值
     */
    @ApiModelProperty(value = "客户成长值")
    private Long growthValue;

    /**
     * 可用积分
     */
    @ApiModelProperty(value = "可用积分")
    private Long pointsAvailable;

    /**
     * 已用积分
     */
    @ApiModelProperty(value = "已用积分")
    private Long pointsUsed;

    /**
     * 账户
     */
    @ApiModelProperty(value = "账户")
    private String customerAccount;

    /**
     * 审核状态 0：待审核 1：已审核 2：审核未通过
     */
    @ApiModelProperty(value = "审核状态")
    private CheckState checkState;

    /**
     * 删除标志 0未删除 1已删除
     */
    @ApiModelProperty(value = "删除标志")
    private DeleteFlag delFlag;

    /**
     * 会员的详细信息
     */
    @ApiModelProperty(value = "会员的详细信息")
    private CustomerDetailVO customerDetail;

    /**
     * 客户类型（0:平台客户,1:商家客户）
     */
    @ApiModelProperty(value = "客户类型")
    private CustomerType customerType;

    /**
     * 商家名称
     */
    @ApiModelProperty(value = "商家名称")
    private String supplierName;

    /**
     * 客户等级名称
     */
    @ApiModelProperty(value = "客户等级名称")
    private String customerLevelName;
    /**
     * 客户等级折扣
     */
    @ApiModelProperty(value = "客户等级折扣")
    private BigDecimal customerLevelDiscount;

    /**
     * 员工商家关系Id
     */
    @ApiModelProperty(value = "员工商家关系Id")
    private String storeCustomerRelaId;

    /**
     * 业务员名称
     */
    @ApiModelProperty(value = "业务员名称")
    private String employeeName;

    /**
     * 是否是商家客户
     */
    @ApiModelProperty(value = "是否是商家客户")
    private boolean isMyCustomer;

    /**
     * 头像
     */
    @ApiModelProperty(value = "头像")
    private String headImg;

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
     * 街道
     */
    @ApiModelProperty(value = "街道")
    private Long streetId;

    /**
     * 详细地址
     */
    @ApiModelProperty(value = "详细地址")
    private String customerAddress;

    /**
     * 企业购会员审核状态  0：无状态 1：待审核 2：已审核 3：审核未通过
     */
    @ApiModelProperty(value = "企业购会员审核状态")
    private EnterpriseCheckState enterpriseCheckState;
    /**
     * 樊登会员id
     */
    @ApiModelProperty(value = "樊登会员id")
    private String fanDengUserNo;
    /**
     * 企业购会员审核原因
     */
    @ApiModelProperty(value = "企业信息")
    private EnterpriseInfoVO enterpriseInfo;

    private List<PaidCardCustomerRelVO> paidCardCustomerRelList;
}
