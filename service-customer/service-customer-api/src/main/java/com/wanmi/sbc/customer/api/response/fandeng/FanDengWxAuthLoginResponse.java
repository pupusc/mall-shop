package com.wanmi.sbc.customer.api.response.fandeng;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FanDengWxAuthLoginResponse {

    private String msg;
    private String status;
    private String systemMsg;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime systemTime;
    private WxAuthLoginData data;

    @Data
    public static class WxAuthLoginData{
        private String userNo;
        private Integer userStatus;
        private LocalDateTime vipStartTime;
        private LocalDateTime vipEndTime;
        private String nickName;
        private String profilePhoto;
    }
}
