package com.wanmi.sbc.elastic.api.response.settlement;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.elastic.bean.vo.settlement.EsSettlementVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

/**
 * @Author yangzhen
 * @Description // 结算单
 * @Date 14:42 2020/12/11
 * @Param
 * @return
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class EsSettlementResponse {

    /**
     * 索引SKU
     */
    @ApiModelProperty(value = "索引SKU")
    private MicroServicePage<EsSettlementVO> esSettlementVOPage = new MicroServicePage<>(new ArrayList<>());

}
