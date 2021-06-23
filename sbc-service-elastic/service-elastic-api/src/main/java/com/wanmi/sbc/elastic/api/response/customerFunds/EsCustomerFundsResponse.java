package com.wanmi.sbc.elastic.api.response.customerFunds;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.elastic.bean.vo.customerFunds.EsCustomerFundsVO;
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
 * @Description // 会员资金列表
 * @Date 14:42 2020/12/15
 * @Param
 * @return
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class EsCustomerFundsResponse {

    /**
     * 索引SKU
     */
    @ApiModelProperty(value = "索引SKU")
    private MicroServicePage<EsCustomerFundsVO> esCustomerFundsVOPage = new MicroServicePage<>(new ArrayList<>());

}
