package com.wanmi.sbc.account.api.request.finance.record;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>账务请求参数基类</p>
 * Created by of628-wenzhi on 2018-10-13-下午1:52.
 */
@ApiModel
@Data
public class AccountBaseRequest implements Serializable{
    /**
     * 统一参数校验入口
     */
    public void checkParam(){}
}
