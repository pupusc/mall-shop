package com.wanmi.sbc.customer.api.response.paidcardbuyrecord;

import com.wanmi.sbc.customer.bean.vo.PaidCardBuyRecordVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>付费会员列表结果</p>
 * @author xuhai
 * @date 2021-01-29 14:03:58
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaidCardBuyRecordListResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 付费会员列表结果
     */
    @ApiModelProperty(value = "付费会员列表结果")
    private List<PaidCardBuyRecordVO> paidCardBuyRecordVOList;
}
