package com.wanmi.sbc.elastic.api.request.customerFunds;

import com.wanmi.sbc.elastic.bean.dto.customerFunds.EsCustomerFundsDTO;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Author yangzhen
 * @Description //会员资金信息信息
 * @Date 14:29 2020/12/11
 * @Param
 * @return
 **/
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EsCustomerFundsListRequest implements Serializable {

    private static final long serialVersionUID = 4555211803309442026L;

    /**
     * 会员资金列表
     * @Author yangzhen
     **/
    private List<EsCustomerFundsDTO> esCustomerFundsDTOS;
}
