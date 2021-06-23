package com.wanmi.sbc.third.login.util;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: sbc-micro-service
 * @description:
 * @create: 2020-05-08 10:58
 **/
@Data
public class WeChatSession implements Serializable {

    private static final long serialVersionUID = -1863880613181827978L;
    /**
     * 用户唯一标识
     */
    @JSONField(name = "openid")
    private String openId;
    /**
     * 会话密钥
     */
    @JSONField(name = "session_key")
    private String sessionKey;
    /**
     * 用户在开放平台的唯一标识符，在满足 UnionID 下发条件的情况下会返回
     */
    @JSONField(name = "uinionid")
    private String unionId;
    /**
     * 错误码
     */
    @JSONField(name = "errcode")
    private Long   errCode;
    /**
     * 错误信息
     */
    @JSONField(name = "errMsg")
    private String errMsg;
}