package com.wanmi.sbc.order.api.request;

import com.wanmi.sbc.common.base.BaseRequest;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;


@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class OrderBaseRequest extends BaseRequest implements Serializable {


    private static final long serialVersionUID = -773729223859003117L;

    /**
     * 统一参数校验入口
     */
    public void checkParam(){}
}
