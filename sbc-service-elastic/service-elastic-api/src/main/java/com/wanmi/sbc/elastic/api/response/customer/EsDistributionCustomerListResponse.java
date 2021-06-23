package com.wanmi.sbc.elastic.api.response.customer;




import com.wanmi.sbc.customer.bean.vo.DistributionCustomerShowPhoneVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author houshuai
 * @date 2020/12/9 17:45
 * @description <p> </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EsDistributionCustomerListResponse {

    private List<DistributionCustomerShowPhoneVO> list;
}
