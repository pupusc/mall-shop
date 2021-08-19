package com.wanmi.sbc.account.api.response.invoice;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author: wanggang
 * @createDate: 2018/12/5 11:35
 * @version: 1.0
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceProjectQueryResponse implements Serializable {
    private static final long serialVersionUID = -4728900937964580948L;

    @ApiModelProperty(value = "开票项目是否存在")
    private Boolean result;
}
