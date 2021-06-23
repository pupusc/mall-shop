package com.wanmi.sbc.customer.api.response.invoice;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.customer.bean.vo.CustomerInvoiceVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerInvoicePageResponse implements Serializable{


    private static final long serialVersionUID = 2824768282829052007L;

    @ApiModelProperty(value = "会员增票信息分页")
    private MicroServicePage<CustomerInvoiceVO> customerInvoiceVOPage;

}
