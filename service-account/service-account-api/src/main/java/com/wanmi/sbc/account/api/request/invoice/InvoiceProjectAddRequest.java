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
 * 开票项目新增请求
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceProjectAddRequest implements Serializable {

    private static final long serialVersionUID = -3074074422684733794L;

    /**
     * 开票项目id
     */
    @ApiModelProperty(value = "开票项目id")
    private String projectId;

    /**
     * 开票项目名称
     */
    @ApiModelProperty(value = "开票项目名称")
    @NotBlank
    private String projectName;

    /**
     * 公司信息ID
     */
    @ApiModelProperty(value = "公司信息ID")
    @NotNull
    private Long companyInfoId;

    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    @NotBlank
    private String operatePerson;

}
