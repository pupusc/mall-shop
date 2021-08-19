package com.wanmi.sbc.customer.api.response.paidcardcustomerrel;

import com.wanmi.sbc.customer.bean.vo.PaidCardCustomerRelVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>付费会员修改结果</p>
 * @author xuhai
 * @date 2021-01-29 14:03:59
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaidCardCustomerRelModifyResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 已修改的付费会员信息
     */
    @ApiModelProperty(value = "已修改的付费会员信息")
    private PaidCardCustomerRelVO paidCardCustomerRelVO;
}
