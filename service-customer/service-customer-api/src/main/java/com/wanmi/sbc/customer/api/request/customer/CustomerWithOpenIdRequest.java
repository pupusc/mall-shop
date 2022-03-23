package com.wanmi.sbc.customer.api.request.customer;

import com.wanmi.sbc.customer.bean.enums.CustomerStatus;
import com.wanmi.sbc.customer.bean.enums.CustomerType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel
@Data
public class CustomerWithOpenIdRequest extends BaseQueryRequest {

    /**
     * 下次开始id
     */
    private Long startId;

}