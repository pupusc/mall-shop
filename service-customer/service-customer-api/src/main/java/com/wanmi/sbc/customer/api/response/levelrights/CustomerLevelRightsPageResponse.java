package com.wanmi.sbc.customer.api.response.levelrights;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.customer.bean.vo.CustomerLevelRightsVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>会员等级权益表分页结果</p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerLevelRightsPageResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 会员等级权益表分页结果
     */
    private MicroServicePage<CustomerLevelRightsVO> customerLevelRightsVOPage;
}
