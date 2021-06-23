package com.wanmi.sbc.customer.api.response.customer;

import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel
@Data
public class CustomerGetByIdResponse extends CustomerVO {
    private static final long serialVersionUID = 2124512794369414457L;
}
