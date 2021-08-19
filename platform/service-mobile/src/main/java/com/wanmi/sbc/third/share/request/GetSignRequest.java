package com.wanmi.sbc.third.share.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class GetSignRequest {

    @NotBlank
    private String url;

    private String terminalType;
}
