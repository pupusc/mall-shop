package com.wanmi.sbc.evalute;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.provider.storeevaluate.StoreEvaluateQueryProvider;
import com.wanmi.sbc.customer.api.provider.storeevaluatenum.StoreEvaluateNumQueryProvider;
import com.wanmi.sbc.customer.api.request.storeevaluate.StoreEvaluatePageRequest;
import com.wanmi.sbc.customer.api.request.storeevaluatenum.StoreEvaluateNumListRequest;
import com.wanmi.sbc.customer.api.response.storeevaluate.StoreEvaluatePageResponse;
import com.wanmi.sbc.customer.api.response.storeevaluatenum.StoreEvaluateNumResponse;
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
@Api(description = "平台店铺评价API" ,tags = "BossStoreEvaluateController")
@RestController
@RequestMapping("/boss/store/evaluate")
public class BossStoreEvaluateController {

    @Autowired
    private StoreEvaluateNumQueryProvider storeEvaluateNumQueryProvider;

    @Autowired
    private StoreEvaluateQueryProvider storeEvaluateQueryProvider;

    /**
     * 查询店铺统计评分等级人数统计列表
     * @param listRequest
     * @return
     */
    @ApiOperation(value = "查询店铺统计评分等级人数统计列表")
    @RequestMapping(value = "/storeEvaluateNumByStoreIdAndScoreCycle",method = RequestMethod.POST)
    public BaseResponse<StoreEvaluateNumResponse> listByStoreIdAndScoreCycle(@RequestBody
                                                                                         StoreEvaluateNumListRequest
                                                                               listRequest){
        return storeEvaluateNumQueryProvider.listByStoreIdAndScoreCycle(listRequest);
    }

    /**
     * 分页查询当前店铺的评价历史记录
     * @param pageRequest
     * @return
     */
    @ApiOperation(value = "分页查询当前店铺的评价历史记录")
    @RequestMapping(value = "/page",method = RequestMethod.POST)
    public BaseResponse<StoreEvaluatePageResponse> page(@RequestBody  StoreEvaluatePageRequest pageRequest){
        BaseResponse<StoreEvaluatePageResponse> response =  storeEvaluateQueryProvider.page(pageRequest);
        return response;
    }
}
