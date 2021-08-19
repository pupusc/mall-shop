package com.wanmi.sbc.account.api.response.finance.record;

import com.wanmi.sbc.account.bean.vo.SettlementViewVO;
import com.wanmi.sbc.common.base.MicroServicePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 结算新增响应请求
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SettlementPageResponse implements Serializable {

    private static final long serialVersionUID = -5120647741604865530L;

    /**
     * 结算分页数据 {@link SettlementViewVO}
     */
    @ApiModelProperty(value = "结算分页数据")
    private MicroServicePage<SettlementViewVO> settlementViewVOPage;
}
