package com.wanmi.sbc.customer.api.response.customer;

import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel
@Data
public class CustomerAddForBossResponse extends CustomerVO {
    private static final long serialVersionUID = -7738037981104056619L;
}
