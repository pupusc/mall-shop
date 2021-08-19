package com.wanmi.sbc.linkedmall.api.request.signature;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignatureVerifyRequest implements Serializable {
    private static final long serialVersionUID = -8540173130102579094L;
    /**
     * 需要验签的参数字符串
     */
    private String originalString;
    /**
     * 签名
     */
    private String signature;
}
