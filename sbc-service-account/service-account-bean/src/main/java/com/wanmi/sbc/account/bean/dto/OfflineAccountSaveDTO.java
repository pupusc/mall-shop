package com.wanmi.sbc.account.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by CHENLI on 2017/4/27.
 */
@ApiModel
@Data
public class OfflineAccountSaveDTO {
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
     * 公司信息ID
     */
    @ApiModelProperty(value = "公司信息ID")
    private Long companyInfoId;

    /**
     * 支行信息
     */
    @ApiModelProperty(value = "支行信息")
    private String bankBranch;

}
