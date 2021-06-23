package com.wanmi.sbc.account.api.response.finance.record;

import com.wanmi.sbc.account.bean.vo.SettlementViewVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 结算单列表
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SettlementListResponse implements Serializable {

    private static final long serialVersionUID = -4920262534476730225L;

    /**
     * 待结算数量
     */
    @ApiModelProperty(value = "结算单列表")
    private List<SettlementViewVO> lists;
}
