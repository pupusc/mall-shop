package com.wanmi.sbc.customer.api.request.enterpriseinfo;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import lombok.*;
import javax.validation.constraints.NotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>单个删除企业信息表请求参数</p>
 * @author TangLian
 * @date 2020-03-03 14:11:45
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnterpriseInfoDelByIdRequest extends CustomerBaseRequest {
private static final long serialVersionUID = 1L;

    /**
     * 企业Id
     */
    @ApiModelProperty(value = "企业Id")
    @NotNull
    private String enterpriseId;
}
