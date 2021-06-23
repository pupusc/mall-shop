package com.wanmi.sbc.customer.api.response.customer;

import com.wanmi.sbc.customer.bean.vo.CustomerDetailVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel
@Data
public class DisableCustomerDetailGetByAccountResponse extends CustomerDetailVO {

    private static final long serialVersionUID = 4490114343049955112L;

}
