package com.wanmi.sbc.goods.api.response.cyclebuy;

import com.wanmi.sbc.goods.bean.vo.CycleBuySendDateRuleVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CycleBuySendDateRuleResponse {

    /**
     * 发货日期规则列表
     */
    @ApiModelProperty(value = "发货日期规则列表")
    private List<CycleBuySendDateRuleVO> cycleBuySendDateRuleVOList;
}
