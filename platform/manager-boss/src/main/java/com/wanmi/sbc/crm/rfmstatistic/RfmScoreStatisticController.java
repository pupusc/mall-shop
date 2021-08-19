package com.wanmi.sbc.crm.rfmstatistic;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.crm.api.provider.rfmstatistic.RfmScoreStatisticQueryProvider;
import com.wanmi.sbc.crm.api.request.rfmstatistic.RfmScoreStatisticRequest;
import com.wanmi.sbc.crm.bean.vo.RfmStatisticVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: zgl
 * \* Date: 2019-10-18
 * \* Time: 14:08
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
@Api(description = "RFM模型分析", tags = "RfmScoreStatisticController")
@RestController
@RequestMapping(value = "/crm/rfmstatistic")
public class RfmScoreStatisticController {
    @Autowired
    private RfmScoreStatisticQueryProvider rfmScoreStatisticQueryProvider;

    @ApiOperation(value = "RFM分段分布概况")
    @PostMapping("/rfmscore/list")
    public BaseResponse<RfmStatisticVo> getList(@RequestBody RfmScoreStatisticRequest request) {

        return rfmScoreStatisticQueryProvider.list(request);
    }
    @ApiOperation(value = "会员rfm分析")
    @GetMapping("/rfmscore/customerInfo")
    public BaseResponse<RfmStatisticVo> customerInfo(String customerId) {

        RfmScoreStatisticRequest request = new RfmScoreStatisticRequest();
        request.setCustomerId(customerId);
        return rfmScoreStatisticQueryProvider.customerInfo(request);
    }
}
