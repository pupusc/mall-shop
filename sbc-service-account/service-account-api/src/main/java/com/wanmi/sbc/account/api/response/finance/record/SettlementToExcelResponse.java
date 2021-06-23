package com.wanmi.sbc.account.api.response.finance.record;

import com.wanmi.sbc.account.bean.vo.SettlementViewVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel
@Data
public class SettlementToExcelResponse {

    /**
     * 财务结算未结算集合
     */
    @ApiModelProperty(value = "财务结算未结算集合")
    private List<SettlementViewVO> notSettledSettlements;

    /**
     * 财务结算已结算集合
     */
    @ApiModelProperty(value = "财务结算已结算集合")
    private List<SettlementViewVO> settledSettlements;

    /**
     * 财务结算暂不处理集合
     */
    @ApiModelProperty(value = "财务结算暂不处理集合")
    private List<SettlementViewVO> settleLaterSettlements;
}
