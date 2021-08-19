package com.wanmi.sbc.customer.api.response.paidcardrule;

import com.wanmi.sbc.customer.bean.vo.PaidCardRuleVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>付费会员新增结果</p>
 * @author xuhai
 * @date 2021-01-29 14:04:01
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaidCardRuleAddResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 已新增的付费会员信息
     */
    @ApiModelProperty(value = "已新增的付费会员信息")
    private PaidCardRuleVO paidCardRuleVO;
}
