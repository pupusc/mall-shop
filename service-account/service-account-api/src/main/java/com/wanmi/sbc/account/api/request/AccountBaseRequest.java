package com.wanmi.sbc.account.api.request;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@ApiModel
@Data
public class AccountBaseRequest implements Serializable {
    private static final long serialVersionUID = -1546886006695211582L;

    /**
     * 统一参数校验入口
     */
    public void checkParam(){}
}
