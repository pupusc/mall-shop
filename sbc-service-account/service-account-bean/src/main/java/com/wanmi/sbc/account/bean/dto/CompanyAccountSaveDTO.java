package com.wanmi.sbc.account.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商家收款账户参数
 * Created by CHENLI on 2017/4/27.
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyAccountSaveDTO implements Serializable {

    private static final long serialVersionUID = 1462266013674905380L;

    /**
     * 账户id
     */
    @ApiModelProperty(value = "账户id")
    private Long accountId;

    /**
     * 账户名称
     */
    @ApiModelProperty(value = "账户名称")
    private String accountName;

    /**
     * 开户银行
     */
    @ApiModelProperty(value = "开户银行")
    private String bankName;

    /**
     * 账号
     */
    @ApiModelProperty(value = "账号")
    private String bankNo;

    /**
     * 银行账号编码
     */
    @ApiModelProperty(value = "银行账号编码")
    private String bankCode;

    /**
     * 公司信息ID
     */
    @ApiModelProperty(value = "公司信息ID")
    private Long companyInfoId;

    /**
     * 支行信息
     */
    @ApiModelProperty(value = "支行信息")
    private String bankBranch;

    /**
     * 打款金额
     */
    @ApiModelProperty(value = "打款金额")
    private BigDecimal remitPrice;

}
