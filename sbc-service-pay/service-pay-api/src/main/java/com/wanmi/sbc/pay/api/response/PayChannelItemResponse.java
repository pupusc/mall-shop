package com.wanmi.sbc.pay.api.response;

import com.wanmi.sbc.pay.bean.vo.PayChannelItemVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>支付渠道项response</p>
 * Created by of628-wenzhi on 2018-08-10-下午3:53.
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class PayChannelItemResponse extends PayChannelItemVO implements Serializable{
    private static final long serialVersionUID = 298328733716305537L;

}
