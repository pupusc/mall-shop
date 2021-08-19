package com.wanmi.sbc.account.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by CHENLI on 2017/12/12.
 */
@ApiModel
@Data
public class InvoiceProjectSwitchRequest implements Serializable {
    /**
     * 商家id集合
     */
    @ApiModelProperty(value = "商家id集合")
    private List<Long> companyInfoIds;
}
