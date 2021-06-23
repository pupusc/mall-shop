package com.wanmi.sbc.store;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.provider.storeevaluatesum.StoreEvaluateSumQueryProvider;
import com.wanmi.sbc.customer.api.request.storeevaluatesum.StoreEvaluateSumQueryRequest;
import com.wanmi.sbc.customer.api.response.storeevaluatesum.StoreEvaluateSumByIdResponse;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liutao
 * @date 2019/3/1 4:21 PM
 */
@Api(description = "店铺综合评价API" ,tags = "StoreEvaluateSumController")
@RestController
@RequestMapping("/store/evaluate/sum")
public class StoreEvaluateSumController {

    @Autowired
    private StoreEvaluateSumQueryProvider storeEvaluateSumQueryProvider;

    @Autowired
    private CommonUtil commonUtil;

    /**
     * 根据登录的店铺id查询180天内的店铺综合评价
     * @return
     */
    @ApiOperation(value = "根据登录的店铺id查询180天内的店铺综合评价")
    @RequestMapping(value = "/getByStoreId",method = RequestMethod.POST)
    public BaseResponse<StoreEvaluateSumByIdResponse> getByStoreId(@RequestBody StoreEvaluateSumQueryRequest storeEvaluateSumQueryRequest){
        storeEvaluateSumQueryRequest.setStoreId(commonUtil.getStoreId());
        return storeEvaluateSumQueryProvider.getByStoreId(storeEvaluateSumQueryRequest);
    }

}
