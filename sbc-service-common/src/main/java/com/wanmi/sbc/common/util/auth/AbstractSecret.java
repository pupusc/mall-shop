package com.wanmi.sbc.common.util.auth;

/**
 * \* Author: zgl
 * \* Date: 2020-3-25
 * \* Time: 19:09
 * \* Description:
 * \
 */
public abstract class AbstractSecret {
    public abstract byte[] action(byte[] data) throws Exception;
}
