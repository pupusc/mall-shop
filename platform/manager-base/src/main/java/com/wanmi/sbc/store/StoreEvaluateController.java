package com.wanmi.sbc.store;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.provider.storeevaluate.StoreEvaluateStatisticsProvider;
import com.wanmi.sbc.customer.api.provider.storeevaluatesum.StoreEvaluateSumQueryProvider;
import com.wanmi.sbc.customer.api.request.storeevaluatesum.StoreEvaluateSumPageRequest;
import com.wanmi.sbc.customer.api.request.storeevaluatesum.StoreEvaluateSumQueryRequest;
import com.wanmi.sbc.customer.api.response.storeevaluatesum.StoreEvaluateSumByIdResponse;
import com.wanmi.sbc.customer.api.response.storeevaluatesum.StoreEvaluateSumPageResponse;
import com.wanmi.sbc.elastic.api.provider.customer.EsStoreEvaluateSumQueryProvider;
import com.wanmi.sbc.elastic.api.request.customer.EsStoreEvaluateSumPageRequest;
import com.wanmi.sbc.elastic.api.response.customer.EsStoreEvaluateSumPageResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liutao
 * @date 2019/2/23 2:53 PM
 */
@Api(description = "平台商品评价API" ,tags = "StoreEvaluateController")
@RestController
@RequestMapping("/store/evaluate")
public class StoreEvaluateController {

    @Autowired
    private StoreEvaluateSumQueryProvider storeEvaluateSumQueryProvider;

    @Autowired
    private StoreEvaluateStatisticsProvider storeEvaluateStatisticsProvider;

    @Autowired
    private EsStoreEvaluateSumQueryProvider esStoreEvaluateSumQueryProvider;


    /**
     * 分页查询店铺评价列表
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "分页查询店铺评价列表")
    @RequestMapping(value = "/page",method = RequestMethod.POST)
    public BaseResponse<EsStoreEvaluateSumPageResponse> page(@RequestBody EsStoreEvaluateSumPageRequest request){
        return esStoreEvaluateSumQueryProvider.page(request);
    }


    /**
     * 店铺综合评价统计定时任务测试地址
     *
     * @return
     */
    @ApiOperation(value = "店铺综合评价统计定时任务测试地址")
    @RequestMapping(value = "/statistics/test",method = RequestMethod.GET)
    public void test(){
        storeEvaluateStatisticsProvider.statistics();
    }

    /**
     * @Author lvzhenwei
     * @Description 根据店铺id查询店铺评价信息 30 90 180
     * @Date 18:46 2019/4/17
     * @Param [storeEvaluateSumQueryRequest]
     **/
    @ApiOperation(value = "根据店铺id查询店铺评价信息 30 90 180")
    @RequestMapping(value = "/getByStoreId", method = RequestMethod.POST)
    public BaseResponse<StoreEvaluateSumByIdResponse> getByStoreId(@RequestBody StoreEvaluateSumQueryRequest storeEvaluateSumQueryRequest){
        return storeEvaluateSumQueryProvider.getByStoreId(storeEvaluateSumQueryRequest);
    }

}
