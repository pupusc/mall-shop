package com.wanmi.sbc.order.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 普通发票
 * Created by jinwei on 7/5/2017.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class GeneralInvoiceDTO {

    /**
     * 0:个人 1:单位，必传
     */
    @ApiModelProperty(value = "发票类型", dataType = "com.wanmi.sbc.account.bean.enums.InvoiceType")
    private Integer flag;

    /**
     * 抬头，单位发票必传
     */
    @ApiModelProperty(value = "抬头")
    private String title;

    /**
     * 纸质发票单位纳税人识别码
     */
    @ApiModelProperty(value = "纸质发票单位纳税人识别码")
    private String identification;
}
