package com.wanmi.sbc.customer.api.response.invoice;

import com.wanmi.sbc.customer.bean.vo.CustomerInvoiceVO;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

@ApiModel
@Data
@Builder
public class CustomerInvoiceByCustomerIdAndDelFlagResponse extends CustomerInvoiceVO {

    private static final long serialVersionUID = 4352080855247543716L;
}
