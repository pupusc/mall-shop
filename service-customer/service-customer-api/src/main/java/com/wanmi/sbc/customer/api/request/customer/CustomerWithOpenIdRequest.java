package com.wanmi.sbc.customer.api.request.customer;

import io.swagger.annotations.ApiModel;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import lombok.Data;


@ApiModel
@Data
public class CustomerWithOpenIdRequest extends BaseQueryRequest {

    /**
     * 下次开始id
     */
    private Long startId;

}