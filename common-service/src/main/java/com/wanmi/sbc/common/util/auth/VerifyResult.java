package com.wanmi.sbc.common.util.auth;

import lombok.Data;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2019-04-17 11:30
 */
@Data
public class VerifyResult {

    private String code;

    private String context;

    public VerifyResult(String code, String context) {
        this.code = code;
        this.context = context;
    }

    public VerifyResult() {
    }
}
