package com.wanmi.sbc.account.api.request.invoice;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;

import java.io.Serializable;

/**
 * 开票项目获取请求
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceProjectByIdRequest implements Serializable{

    private static final long serialVersionUID = 305384428103023977L;

    /**
     * 开票项目获取请求id
     */
    @ApiModelProperty(value = "开票项目获取请求id")
    @NotBlank
    private String projcetId;
}
