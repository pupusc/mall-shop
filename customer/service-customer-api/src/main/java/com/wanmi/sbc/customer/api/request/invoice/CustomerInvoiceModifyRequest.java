package com.wanmi.sbc.customer.api.request.invoice;


import com.wanmi.sbc.customer.bean.dto.CustomerInvoiceDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 会员增专票
 * Created by CHENLI on 2017/4/21.
 */
@ApiModel
@Data
public class CustomerInvoiceModifyRequest extends CustomerInvoiceDTO implements Serializable {


    private static final long serialVersionUID = -2152602681278906420L;

    @ApiModelProperty(value = "负责业务员")
    private String employeeId;
}
