package com.wanmi.sbc.customer.api.response.growthvalue;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.customer.bean.vo.CustomerGrowthValueVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>客户成长值明细表分页结果</p>
 *
 * @author yang
 * @since 2019/2/23
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerGrowthValuePageResponse implements Serializable {

    private static final long serialVersionUID = -8166841123696050972L;

    /**
     * 客户成长值表分页结果
     */
    private MicroServicePage<CustomerGrowthValueVO> customerGrowthValueVOPage;
}
