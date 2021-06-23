package com.wanmi.sbc.pay.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>支付入参基类</p>
 * Created by of628-wenzhi on 2018-08-21-下午4:15.
 */
@ApiModel
@Data
public class PayBaseRequest implements Serializable {


    /**
     * 统一参数校验入口
     */
    public void checkParam() {
    }
}
