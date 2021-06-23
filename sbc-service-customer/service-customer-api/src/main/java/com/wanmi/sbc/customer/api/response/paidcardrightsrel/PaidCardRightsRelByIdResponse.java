package com.wanmi.sbc.customer.api.response.paidcardrightsrel;

import com.wanmi.sbc.customer.bean.vo.PaidCardRightsRelVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>根据id查询任意（包含已删除）付费会员信息response</p>
 * @author xuhai
 * @date 2021-01-29 14:04:00
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaidCardRightsRelByIdResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 付费会员信息
     */
    @ApiModelProperty(value = "付费会员信息")
    private PaidCardRightsRelVO paidCardRightsRelVO;
}
