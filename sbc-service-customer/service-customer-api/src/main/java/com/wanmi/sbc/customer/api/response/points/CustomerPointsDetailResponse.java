package com.wanmi.sbc.customer.api.response.points;

import com.wanmi.sbc.customer.bean.vo.CustomerPointsDetailVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>会员积分明细新增结果</p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerPointsDetailResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 已新增的会员积分明细信息
     */
    private CustomerPointsDetailVO customerPointsDetailVO;
}
