package com.wanmi.sbc.customer.api.response.customer;

import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import com.wanmi.sbc.common.base.MicroServicePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@ApiModel
@Data
public class CustomerWithOpenIdResponse implements Serializable {

    @ApiModelProperty(value = "分页结果")
    private MicroServicePage<CustomerVO> pageCustomer;
}