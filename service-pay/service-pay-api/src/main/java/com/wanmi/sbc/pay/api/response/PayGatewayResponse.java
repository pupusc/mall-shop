package com.wanmi.sbc.pay.api.response;

import com.wanmi.sbc.pay.bean.vo.PayGatewayVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>支付网关信息Response</p>
 * Created by of628-wenzhi on 2018-08-10-下午3:23.
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class PayGatewayResponse extends PayGatewayVO implements Serializable{
    private static final long serialVersionUID = 6154395088233591917L;

}
