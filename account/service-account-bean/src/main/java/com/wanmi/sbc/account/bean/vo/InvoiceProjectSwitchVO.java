package com.wanmi.sbc.account.bean.vo;

import com.wanmi.sbc.common.enums.DefaultFlag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 开票项目开关
 * Created by chenli on 2017/12/12.
 */
@ApiModel
@Data
public class InvoiceProjectSwitchVO implements Serializable {

    private static final long serialVersionUID = 6598775399896326309L;

    /**
     * 主键
     */
    @ApiModelProperty(value = "开票项目开关id")
    private String invoiceProjectSwitchId;

    /**
     * 公司信息ID
     */
    @ApiModelProperty(value = "公司信息id")
    private Long companyInfoId;

    /**
     * 是否支持开票 0 不支持 1 支持
     */
    @ApiModelProperty(value = "是否支持开票")
    private DefaultFlag isSupportInvoice = DefaultFlag.NO;

    /**
     * 纸质发票 0 不支持 1 支持
     */
    @ApiModelProperty(value = "纸质发票")
    private DefaultFlag isPaperInvoice = DefaultFlag.NO;

    /**
     * 增值税发票 0 不支持 1 支持
     */
    @ApiModelProperty(value = "增值税发票")
    private DefaultFlag isValueAddedTaxInvoice = DefaultFlag.NO;
}
