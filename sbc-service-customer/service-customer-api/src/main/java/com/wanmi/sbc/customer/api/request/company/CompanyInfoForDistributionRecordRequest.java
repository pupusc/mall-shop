package com.wanmi.sbc.customer.api.request.company;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Created by sunkun on 2017/11/6.
 */
@ApiModel
@Data
public class CompanyInfoForDistributionRecordRequest extends BaseQueryRequest {
    private static final long serialVersionUID = 2304827735774417750L;

    /**
     * 商家编号
     */
    @ApiModelProperty(value = "商家编号")
    @NotNull
    private String companyCode;

}
