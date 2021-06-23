package com.wanmi.sbc.order.api.response.settlement;

import com.wanmi.sbc.account.bean.vo.SettlementViewVO;
import com.wanmi.sbc.order.bean.vo.SettlementDetailVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 返回用于刷入es
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SettlementForEsResponse implements Serializable{
    private static final long serialVersionUID = -4542541344345887311L;

    /**
     * 结算单 {@link SettlementDetailVO}
     */
    @ApiModelProperty(value = "结算单")
    private List<SettlementViewVO> lists;
}
