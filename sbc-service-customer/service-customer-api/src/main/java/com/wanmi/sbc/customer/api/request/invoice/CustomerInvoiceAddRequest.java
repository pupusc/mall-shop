package com.wanmi.sbc.customer.api.request.invoice;


import com.wanmi.sbc.customer.bean.dto.CustomerInvoiceDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 会员增票资质-新增Request
 */
@ApiModel
@Data
public class CustomerInvoiceAddRequest extends CustomerInvoiceDTO implements Serializable{

    private static final long serialVersionUID = 1506443440451685006L;

    @ApiModelProperty(value = "负责业务员")
    private String employeeId;
}
