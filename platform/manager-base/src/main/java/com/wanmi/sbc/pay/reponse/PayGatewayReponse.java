package com.wanmi.sbc.pay.reponse;

import com.wanmi.sbc.pay.bean.enums.IsOpen;
import com.wanmi.sbc.pay.bean.vo.PayChannelItemVO;
import com.wanmi.sbc.pay.bean.vo.PayGatewayConfigVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * Created by sunkun on 2017/8/10.
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PayGatewayReponse {

    /**
     * 网关id
     */
    @ApiModelProperty(value = "网关id")
    private Long id;

    /**
     * 网关名称
     */
    @ApiModelProperty(value = "网关名称")
    private String name;

    /**
     * 是否开启
     */
    @ApiModelProperty(value = "是否开启")
    private IsOpen isOpen;

    /**
     * 是否聚合支付
     */
    @ApiModelProperty(value = "是否聚合支付", dataType = "com.wanmi.sbc.common.enums.BoolFlag")
    private Boolean type;

    /**
     * 网关配置
     */
    @ApiModelProperty(value = "网关配置")
    private PayGatewayConfigVO payGatewayConfig;

    /**
     * 支付渠道项
     */
    @ApiModelProperty(value = "支付渠道项")
    private List<PayChannelItemVO> channelItemList;
}
