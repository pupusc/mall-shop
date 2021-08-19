package com.wanmi.sbc.account.api.request.invoice;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;

import java.io.Serializable;

/**
 * 开票项目删除请求
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceProjectDeleteByIdRequest implements Serializable {

    private static final long serialVersionUID = 6804820986783368738L;

    /**
     * 开票项目id
     */
    @ApiModelProperty(value = "开票项目id")
    @NotBlank
    private String projectId;
}
