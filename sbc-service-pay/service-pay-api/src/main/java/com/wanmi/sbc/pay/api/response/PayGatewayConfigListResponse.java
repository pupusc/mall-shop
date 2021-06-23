package com.wanmi.sbc.pay.api.response;

import com.wanmi.sbc.pay.bean.vo.PayGatewayConfigVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p>支付网关配置response</p>
 * Created by of628-wenzhi on 2018-08-10-下午3:54.
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayGatewayConfigListResponse implements Serializable{


    private static final long serialVersionUID = -3201076230587487971L;

    /**
     * 网关配置列表
     */
    @ApiModelProperty(value = "网关配置列表")
    private List<PayGatewayConfigVO> gatewayConfigVOList;
}
