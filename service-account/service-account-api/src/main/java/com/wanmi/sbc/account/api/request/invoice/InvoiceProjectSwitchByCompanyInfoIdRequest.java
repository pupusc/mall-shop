package com.wanmi.sbc.account.api.request.invoice;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 开票项目开关请求
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceProjectSwitchByCompanyInfoIdRequest implements Serializable{

    private static final long serialVersionUID = -4250587506523477558L;

    /**
     * 公司信息Id
     */
    @ApiModelProperty(value = "公司信息Id")
    @NotNull
    private Long companyInfoId;
}
