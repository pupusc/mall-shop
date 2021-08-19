package com.wanmi.sbc.pay.api.response;

import com.wanmi.sbc.pay.bean.vo.PayChannelItemVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>支付渠道项response</p>
 * Created by of628-wenzhi on 2018-08-10-下午3:53.
 */
@ApiModel
@Data
public class PayChannelItemListResponse  implements Serializable{

    private static final long serialVersionUID = -8883400339908780089L;

    /**
     * 支付渠道项列表
     */
    @ApiModelProperty(value = "支付渠道项列表")
    private List<PayChannelItemVO> payChannelItemVOList;
}
