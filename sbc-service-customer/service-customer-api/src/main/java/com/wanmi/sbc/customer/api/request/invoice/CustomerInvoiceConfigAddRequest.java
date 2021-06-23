package com.wanmi.sbc.customer.api.request.invoice;


import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 会员增票资质配置-新增Request
 */
@ApiModel
@Data
public class CustomerInvoiceConfigAddRequest extends CustomerBaseRequest implements Serializable{

    private static final long serialVersionUID = 1711437568251466223L;

    @ApiModelProperty(value = "订单开票配置")
    @NotNull
    private Integer status;
}
