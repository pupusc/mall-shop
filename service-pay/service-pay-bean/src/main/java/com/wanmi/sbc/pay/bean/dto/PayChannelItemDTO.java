package com.wanmi.sbc.pay.bean.dto;

import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.pay.bean.enums.IsOpen;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by sunkun on 2017/8/9.
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel
@Data
public class PayChannelItemDTO extends BaseRequest {

    @ApiModelProperty(value = "支付渠道id")
    private Long id;

    @ApiModelProperty(value = "是否开启")
    private IsOpen IsOpen;
}
