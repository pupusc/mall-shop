package com.wanmi.sbc.customer.api.response.invoice;

import com.wanmi.sbc.customer.bean.vo.CustomerInvoiceVO;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@ApiModel
@EqualsAndHashCode(callSuper = true)
@Builder
@Data
public class CustomerInvoiceModifyResponse extends CustomerInvoiceVO implements Serializable {

    private static final long serialVersionUID = -3454482352550618857L;
}
