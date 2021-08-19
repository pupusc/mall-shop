package com.wanmi.sbc.pay.api.response;

import com.wanmi.sbc.pay.bean.vo.PayGatewayVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p>支付网关信息Response</p>
 * Created by of628-wenzhi on 2018-08-10-下午3:23.
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayGatewayListResponse  implements Serializable{

    private static final long serialVersionUID = -6042671420023325781L;

    /**
     * 网关列表
     */
    @ApiModelProperty(value = "网关列表")
    private List<PayGatewayVO> payGatewayVOList;
}
