package com.wanmi.sbc.account.api.constant;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * 基础银行信息
 * Created by sunkun on 2017/12/6.
 */
@ApiModel
@Data
@AllArgsConstructor
@Builder
public class BaseBank {

    /**
     * 银行名称
     */
    @ApiModelProperty(value = "银行名称")
    private String bankName;

    /**
     * 银行编号
     */
    @ApiModelProperty(value = "银行编号")
    private String bankCode;
}
