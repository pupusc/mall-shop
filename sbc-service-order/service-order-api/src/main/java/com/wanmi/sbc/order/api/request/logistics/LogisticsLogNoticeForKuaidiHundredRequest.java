package com.wanmi.sbc.order.api.request.logistics;

import com.wanmi.sbc.order.bean.dto.KuaidiHundredNoticeDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

/**
 * <p>根据快递100的回调通知请求参数</p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogisticsLogNoticeForKuaidiHundredRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "快递100的回调参数")
    private KuaidiHundredNoticeDTO kuaidiHundredNoticeDTO;
}