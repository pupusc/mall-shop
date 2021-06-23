package com.wanmi.sbc.customer.api.request.invoice;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.customer.bean.enums.CheckState;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 会员增票资质-分页Request
 */
@ApiModel
@Data
public class CustomerInvoicePageRequest extends BaseQueryRequest {

    private static final long serialVersionUID = 5045583580715163210L;
    /**
     * 会员名称
     */
    @ApiModelProperty(value = "会员名称")
    private String customerName;

    /**
     * 客户ID
     */
    @ApiModelProperty(value = "客户ID")
    private List<String> customerIds;

    /**
     * 单位全称
     */
    @ApiModelProperty(value = "单位全称")
    private String companyName;

    /**
     * 增票资质审核状态  0:待审核 1:已审核通过 2:审核未通过
     */
    @ApiModelProperty(value = "增票资质审核状态")
    private CheckState checkState;

    /**
     * 负责业务员
     */
    @ApiModelProperty(value = "负责业务员")
    private String employeeId;

    /**
     * 负责业务员ID集合
     */
    @ApiModelProperty(value = "负责业务员ID集合")
    private List<String> employeeIds;
}
