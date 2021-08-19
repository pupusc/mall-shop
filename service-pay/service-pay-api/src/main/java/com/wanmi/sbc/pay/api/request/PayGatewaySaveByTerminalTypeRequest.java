package com.wanmi.sbc.pay.api.request;

import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.pay.bean.enums.PayGatewayEnum;
import com.wanmi.sbc.pay.bean.enums.TerminalType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * Created by sunkun on 2017/8/9.
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel
@Data
public class PayGatewaySaveByTerminalTypeRequest extends BaseRequest {

    private static final long serialVersionUID = 4304124969782172682L;

    @ApiModelProperty(value = "店铺id")
    @NotNull
    private Long storeId;

    @ApiModelProperty(value = "网关名称（英文名称，全大写）")
    @NotNull
    private PayGatewayEnum payGatewayEnum;

    @ApiModelProperty(value = "终端类型")
    @NotNull
    private TerminalType terminalType;

    @ApiModelProperty(value = "secret key")
    @Length(max = 60)
    private String secret;

    @ApiModelProperty(value = "第三方应用标识")
    @Length(max = 40)
    private String appId;


}
