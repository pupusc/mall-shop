package com.wanmi.sbc.pay.api.response;

import com.wanmi.sbc.pay.bean.vo.PayGatewayConfigVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>支付网关配置response</p>
 * Created by of628-wenzhi on 2018-08-10-下午3:54.
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class PayGatewayConfigResponse extends PayGatewayConfigVO implements Serializable{

    private static final long serialVersionUID = 525866629389449809L;

}
