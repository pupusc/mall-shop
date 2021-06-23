package com.wanmi.sbc.customer.api.response.levelrights;

import com.wanmi.sbc.customer.bean.vo.CustomerLevelRightsRelVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>会员等级与权益关联表response</p>
 *
 * @author yang
 * @since 2019/2/27
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerLevelRightsRelResponse implements Serializable {

    private static final long serialVersionUID = 8766888554253189311L;

    /**
     * 会员等级与权益关联表信息
     */
    private CustomerLevelRightsRelVO customerLevelRightsRelVO;
}
