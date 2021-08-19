package com.wanmi.sbc.customer.api.response.points;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.customer.bean.vo.CustomerPointsDetailVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>会员积分明细分页结果</p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerPointsDetailPageResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 会员积分明细分页结果
     */
    private MicroServicePage<CustomerPointsDetailVO> customerPointsDetailVOPage;
}
