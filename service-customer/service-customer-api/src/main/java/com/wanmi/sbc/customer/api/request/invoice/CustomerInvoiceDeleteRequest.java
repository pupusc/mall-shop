package com.wanmi.sbc.customer.api.request.invoice;


import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhangjin on 2017/5/4.
 */
@ApiModel
@Data
public class CustomerInvoiceDeleteRequest extends CustomerBaseRequest implements Serializable {

    private static final long serialVersionUID = 8899885851471095746L;
    /**
     * 专票ids
     */
    @ApiModelProperty(value = "专票ids")
    private List<Long> customerInvoiceIds;


}
