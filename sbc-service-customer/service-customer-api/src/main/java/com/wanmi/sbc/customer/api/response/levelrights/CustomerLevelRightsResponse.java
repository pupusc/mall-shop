package com.wanmi.sbc.customer.api.response.levelrights;

import com.wanmi.sbc.customer.bean.vo.CustomerLevelRightsVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>根据id查询任意（包含已删除）会员等级权益表信息response</p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerLevelRightsResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 会员等级权益表信息
     */
    private CustomerLevelRightsVO customerLevelRightsVO;
}
