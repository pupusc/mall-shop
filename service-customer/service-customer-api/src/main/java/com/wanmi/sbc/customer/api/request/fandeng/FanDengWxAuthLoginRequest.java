package com.wanmi.sbc.customer.api.request.fandeng;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FanDengWxAuthLoginRequest {

    private String unionId;
    private String openId;
    private String areaCode;
    private String mobile;
    private String registerSource;
}
