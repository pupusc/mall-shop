package com.wanmi.sbc.account.api.request.invoice;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 开票项目查询请求
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceProjectQueryRequest implements Serializable{

    private static final long serialVersionUID = 1191946758969711112L;

    /**
     * 开票项目名称
     */
    @ApiModelProperty(value = "开票项目名称")
    @NotBlank
    private String projectName;

    /**
     * 公司信息Id
     */
    @ApiModelProperty(value = "公司信息Id")
    @NotNull
    private Long companyInfoId;
}
