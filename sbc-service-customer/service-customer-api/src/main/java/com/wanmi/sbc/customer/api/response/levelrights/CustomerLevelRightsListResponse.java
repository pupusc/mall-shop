package com.wanmi.sbc.customer.api.response.levelrights;

import com.wanmi.sbc.customer.bean.vo.CustomerLevelRightsVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.io.Serializable;

/**
 * <p>会员等级权益表列表结果</p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerLevelRightsListResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 会员等级权益表列表结果
     */
    private List<CustomerLevelRightsVO> customerLevelRightsVOList;
}
