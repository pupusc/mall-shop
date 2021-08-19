package com.wanmi.sbc.order.api.request.esCustomerFunds;

import com.wanmi.sbc.account.bean.vo.CustomerFundsForEsVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Author yangzhen
 * @Description //新增会员-初始化会员资金信息Request对象
 * @Date 13:53 2020/12/16
 * @Param
 * @return
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EsCustomerFundsSaveListRequest implements Serializable {

    private List<CustomerFundsForEsVO>  customerFundsForEsVOs;



}
