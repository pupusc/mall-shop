package com.wanmi.sbc.account.api.response.funds;

import com.wanmi.sbc.account.bean.vo.CustomerFundsForEsVO;
import com.wanmi.sbc.common.util.SensitiveUtils;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 会员资金-根据会员ID查询
 * @author: yangzhen
 * @createDate: 2020/12/16 11:06
 * @version: 1.0
 */
@ApiModel
@Data
@Builder
public class CustomerFundsByCustomerIdForEsResponse extends CustomerFundsForEsVO implements Serializable {


}
