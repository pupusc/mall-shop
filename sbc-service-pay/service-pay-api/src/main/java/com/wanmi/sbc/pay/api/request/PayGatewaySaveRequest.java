package com.wanmi.sbc.pay.api.request;

import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.pay.bean.dto.PayChannelItemDTO;
import com.wanmi.sbc.pay.bean.dto.PayGatewayConfigDTO;
import com.wanmi.sbc.pay.bean.enums.IsOpen;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by sunkun on 2017/8/9.
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel
@Data
public class PayGatewaySaveRequest extends BaseRequest {

    private static final long serialVersionUID = 4304124969782172682L;

    /**
     * 商户id-boss端取默认值
     */
    @ApiModelProperty(value = "店铺id")
    private Long storeId;

    /**
     * 网关id
     */
    @ApiModelProperty(value = "网关id")
    private Long id;

    /**
     * 网关名称（英文名称，全大写）
     */
    @ApiModelProperty(value = "网关名称（英文名称，全大写）")
    @NotNull
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
    private Boolean type = true;

    /**
     * 网关配置
     */
    @ApiModelProperty(value = "网关配置")
    @Valid
    private PayGatewayConfigDTO payGatewayConfig;

    /**
     * 支付渠道项
     */
    @ApiModelProperty(value = "支付渠道项")
    private List<PayChannelItemDTO> channelItemList;
}
