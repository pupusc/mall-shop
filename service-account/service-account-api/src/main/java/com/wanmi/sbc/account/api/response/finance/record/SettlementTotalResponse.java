package com.wanmi.sbc.account.api.response.finance.record;

import com.wanmi.sbc.account.bean.vo.SettlementTotalVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 结算单响应请求
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SettlementTotalResponse implements Serializable {

    private static final long serialVersionUID = 4287955217503178411L;

    /**
     * 结算单统计分组结果 {@link SettlementTotalVO}
     */
    @ApiModelProperty(value = "结算单统计分组结果")
    private List<SettlementTotalVO> settlementTotalVOList;
}
