package com.wanmi.sbc.account.api.request.company;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商家收款账户打款请求
 * Created by daiyitian on 2018/10/15.
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyAccountRemitRequest implements Serializable {

    private static final long serialVersionUID = 1462266013674905380L;

    /**
     * 账户id
     */
    @ApiModelProperty(value = "账户id")
    @NotNull
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
    @NotNull
    private BigDecimal remitPrice;

}
