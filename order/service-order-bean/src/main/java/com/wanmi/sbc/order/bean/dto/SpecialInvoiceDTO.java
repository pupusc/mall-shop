package com.wanmi.sbc.order.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 增值税/专用发票
 * Created by jinwei on 7/5/2017.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class SpecialInvoiceDTO {

    /**
     * 增值税发票id，新增与修改时必传
     */
    @ApiModelProperty(value = "增值税发票id，新增与修改时必传")
    private Long id;

    /**
     * 以下信息无需传入
     **/
    @ApiModelProperty(value = "公司名称")
    private String companyName;

    @ApiModelProperty(value = "公司编号")
    private String companyNo;

    @ApiModelProperty(value = "联系方式")
    private String phoneNo;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "公司账号")
    private String account;

    @ApiModelProperty(value = "开户银行")
    private String bank;

    /**
     * 纳税人识别号
     */
    @ApiModelProperty(value = "纳税人识别号")
    private String identification;
}
