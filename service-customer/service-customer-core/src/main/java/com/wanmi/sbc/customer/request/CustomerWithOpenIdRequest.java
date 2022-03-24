package com.wanmi.sbc.customer.request;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import com.wanmi.sbc.common.base.BaseQueryRequest;

import java.io.Serializable;

@ApiModel
@Data
public class CustomerWithOpenIdRequest extends BaseQueryRequest{

    /**
     * 下次开始id
     */
    private Long startId;

}