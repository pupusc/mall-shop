package com.wanmi.sbc.customer.api.response.invoice;

import com.wanmi.sbc.customer.bean.vo.CustomerInvoiceVO;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

@ApiModel
@Data
@Builder
public class CustomerInvoiceByCustomerIdAndCheckStateResponse extends CustomerInvoiceVO {

    private static final long serialVersionUID = 1437403039031374452L;
}
