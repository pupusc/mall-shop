package com.wanmi.sbc.distribute;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.provider.distribution.DistributionCustomerRankingQueryProvider;
import com.wanmi.sbc.customer.api.request.distribution.DistributionCustomerRankingPageRequest;
import com.wanmi.sbc.customer.api.response.distribution.DistributionCustomerRankingResponse;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.time.LocalDate;

/**
 * @author: lq
 * @CreateTime:2019-04-19 15:20
 * @Description:todo
 */
@Api(tags = "DistributionCustomerRankingController", description = "用户分销排行榜查询API")
@RestController
@RequestMapping("/distributeRanking")
public class DistributionCustomerRankingController {

    @Autowired
    private DistributionCustomerRankingQueryProvider distributionCustomerRankingQueryProvider;


    @Resource
    private CommonUtil commonUtil;

    /**
     * 查询分销员的销售业绩
     * @return
     */
    @ApiOperation(value = "查询排行榜")
    @RequestMapping(value = "/sales/ranking", method = RequestMethod.POST)
    public BaseResponse<DistributionCustomerRankingResponse> getRanking(@RequestBody @Valid DistributionCustomerRankingPageRequest request) {
        request.setSortColumn(request.getType());
        request.setSortType("DESC");
        request.setPageSize(100);
        request.setTargetDate(LocalDate.now());
        request.setMyCustomerId(commonUtil.getOperatorId());
        return distributionCustomerRankingQueryProvider.ranking(request);
    }


}
