package com.wanmi.sbc.account.bean.vo;

import com.wanmi.sbc.common.enums.DefaultFlag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 开票项目开关返回结果
 * Created by chenli on 2017/12/12.
 */
@ApiModel
@Data
public class InvoiceProjectSwitchSupportVO implements Serializable {

    /**
     * 公司信息id
     */
    @ApiModelProperty(value = "公司信息id")
    private Long companyInfoId;

    /**
     * 是否支持开票 0 不支持 1 支持
     */
    @ApiModelProperty(value = "是否支持开票")
    private DefaultFlag supportInvoice = DefaultFlag.NO;
}
