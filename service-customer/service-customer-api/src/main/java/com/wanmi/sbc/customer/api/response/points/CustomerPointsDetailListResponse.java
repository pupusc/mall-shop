package com.wanmi.sbc.customer.api.response.points;

import com.wanmi.sbc.customer.bean.vo.CustomerPointsDetailVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p>会员积分明细列表结果</p>
 * @author minchen
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerPointsDetailListResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 会员积分明细列表结果
     */
    @ApiModelProperty(value = "会员积分明细列表结果")
    private List<CustomerPointsDetailVO> customerPointsDetailVOList;
}
