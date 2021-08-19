package com.wanmi.sbc.marketing.api.request.market;

import com.wanmi.sbc.customer.bean.vo.CommonLevelVO;
import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import com.wanmi.sbc.marketing.bean.vo.GoodsInfoMarketingVO;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Map;

@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InfoForPurchseRequest {

    @NotEmpty
    private List<GoodsInfoMarketingVO> goodsInfos;

    private CustomerVO customer;

    private Map<Long, CommonLevelVO> storeLevelsMap;

}
