package com.wanmi.sbc.setting.api.request;

import com.wanmi.sbc.setting.bean.dto.TradeConfigDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
@ApiModel
@Data
public class TradeConfigsModifyRequest extends SettingBaseRequest {
    private static final long serialVersionUID = -3575807209280500446L;
    /**
     * 订单配置列表
     */
    @ApiModelProperty(value = "订单配置列表")
    @NotNull
    @Valid
    private List<TradeConfigDTO> tradeConfigDTOList;
}
