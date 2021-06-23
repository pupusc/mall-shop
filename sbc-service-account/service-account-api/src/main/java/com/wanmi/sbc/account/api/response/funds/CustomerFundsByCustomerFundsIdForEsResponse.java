package com.wanmi.sbc.account.api.response.funds;

import com.wanmi.sbc.account.bean.vo.CustomerFundsForEsVO;
import com.wanmi.sbc.account.bean.vo.CustomerFundsVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 会员资金-根据会员资金ID查询
 * @author: yangzhen
 * @createDate: 2020/12/16 11:06
 * @version: 1.0
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel
@Data
public class CustomerFundsByCustomerFundsIdForEsResponse extends CustomerFundsForEsVO implements Serializable {

}
