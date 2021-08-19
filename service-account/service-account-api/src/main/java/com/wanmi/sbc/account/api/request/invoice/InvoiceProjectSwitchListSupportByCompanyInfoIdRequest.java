package com.wanmi.sbc.account.api.request.invoice;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 开票项目列表请求
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceProjectSwitchListSupportByCompanyInfoIdRequest implements Serializable{

    private static final long serialVersionUID = 4081904291749594295L;

    /**
     * 批量公司信息Id
     */
    @ApiModelProperty(value = "批量公司信息Id")
    @NotNull
    private List<Long> companyInfoIds;
}
