package com.wanmi.sbc.order.api.response.settlement;

import com.wanmi.sbc.order.bean.vo.SettlementDetailVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p>根据计算单id查询结算明细列表返回结构</p>
 * Created by of628-wenzhi on 2018-10-13-下午7:14.
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SettlementDetailListBySettleUuidResponse implements Serializable{
    private static final long serialVersionUID = 5554646165003525870L;

    /**
     * 结算明细列表 {@link SettlementDetailVO}
     */
    @ApiModelProperty(value = "结算明细列表")
    private List<SettlementDetailVO> settlementDetailVOList;
}
