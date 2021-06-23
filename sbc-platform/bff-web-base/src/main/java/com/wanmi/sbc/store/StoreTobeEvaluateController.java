package com.wanmi.sbc.store;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.provider.storetobeevaluate.StoreTobeEvaluateQueryProvider;
import com.wanmi.sbc.goods.api.request.storetobeevaluate.StoreTobeEvaluatePageRequest;
import com.wanmi.sbc.goods.api.request.storetobeevaluate.StoreTobeEvaluateQueryRequest;
import com.wanmi.sbc.goods.api.response.storetobeevaluate.StoreTobeEvaluatePageResponse;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author lvzhenwei
 * @Description 店铺服务待评价
 * @Date 15:16 2019/4/2
 **/
@RestController
@RequestMapping("/storeTobeEvaluate")
@Api(tags = "StoreTobeEvaluateController", description = "S2B web公用-店铺服务待评价API")
public class StoreTobeEvaluateController {

    @Autowired
    private StoreTobeEvaluateQueryProvider storeTobeEvaluateQueryProvider;

    @Autowired
    private CommonUtil commonUtil;

    /**
     * 分页查待评价询店铺服务列表
     * @param storeTobeEvaluatePageReq
     * @return
     */
    @ApiOperation(value = "分页查询待评价店铺服务列表")
    @RequestMapping(value = "/pageStoreTobeEvaluate", method = RequestMethod.POST)
    public BaseResponse<StoreTobeEvaluatePageResponse> pageStoreTobeEvaluate(@RequestBody StoreTobeEvaluatePageRequest
                                                                                         storeTobeEvaluatePageReq){
        storeTobeEvaluatePageReq.setCustomerId(commonUtil.getCustomer().getCustomerId());
        return storeTobeEvaluateQueryProvider.page(storeTobeEvaluatePageReq);
    }

    /**
     * 查询店铺评价服务数量
     * @return
     */
    @ApiOperation(value = "查询店铺评价服务数量")
    @RequestMapping(value = "/getStoreTobeEvaluateNum", method = RequestMethod.GET)
    public BaseResponse<Long> getStoreTobeEvaluateNum(){
        StoreTobeEvaluateQueryRequest queryReq = new StoreTobeEvaluateQueryRequest();
        queryReq.setCustomerId(commonUtil.getCustomer().getCustomerId());
        return storeTobeEvaluateQueryProvider.getStoreTobeEvaluateNum(queryReq);
    }
}
