package com.wanmi.sbc.order.api.request.trade;

import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.common.enums.BoolFlag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class TradeGetByIdListRequest extends BaseRequest {

    private static final long serialVersionUID = -4153498256754887224L;
    /**
     * 交易id
     */
    @ApiModelProperty(value = "交易idList")
    @NotEmpty
    private List<String> tidList;

    /**
     * 是否查询子单
     */
    @ApiModelProperty(value = "是否查询子单")
    private BoolFlag boolFlag;

}
