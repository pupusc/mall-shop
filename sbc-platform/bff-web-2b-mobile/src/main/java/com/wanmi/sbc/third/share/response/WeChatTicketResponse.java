package com.wanmi.sbc.third.share.response;

import lombok.Data;

@Data
public class WeChatTicketResponse {

    private Integer errcode;

    private String errmsg;

    private String ticket;

    private Integer expires_in;
}
