package com.wanmi.sbc.customer.api.response.growthvalue;

import com.wanmi.sbc.customer.bean.vo.CustomerGrowthValueVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author yang
 * @since 2019/2/22
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerGrowthValueResponse implements Serializable {

    private static final long serialVersionUID = -2756671148184870255L;

    /**
     * 客户成长值明细表信息
     */
    private CustomerGrowthValueVO customerGrowthValueVO;
}
