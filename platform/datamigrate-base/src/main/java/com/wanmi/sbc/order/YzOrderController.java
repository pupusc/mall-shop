package com.wanmi.sbc.order;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.order.api.provider.yzorder.YzOrderProvider;
import com.wanmi.sbc.order.api.request.yzorder.YzOrderPageQueryRequest;
import com.wanmi.sbc.order.request.YzOrderCompensateRequest;
import com.wanmi.sbc.order.request.YzOrderConvertRequest;
import com.wanmi.sbc.order.request.YzOrderMigrateRequest;
import com.wanmi.sbc.order.request.YzOrderUpdateRequest;
import com.wanmi.sbc.order.service.YzOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "YzOrderController", description = "有赞会员数据迁移 API")
@RestController("YzOrderController")
@RequestMapping("/yz")
public class YzOrderController {

    @Autowired
    private YzOrderService yzOrderService;

    @Autowired
    private YzOrderProvider yzOrderProvider;

    /**
     * 迁移订单
     *
     * @param migrateRequest
     * @return
     */
    @ApiOperation(value = "迁移订单")
    @RequestMapping(value = "/order", method = RequestMethod.POST)
    public BaseResponse migrateOrder(@RequestBody YzOrderMigrateRequest migrateRequest) {
        yzOrderService.migrateOrder(migrateRequest);
        return BaseResponse.SUCCESSFUL();
    }


    /**
     * 有赞订单入库
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "有赞订单入库")
    @RequestMapping(value = "/order-convert", method = RequestMethod.POST)
    public BaseResponse convertType(@RequestBody YzOrderConvertRequest request) {
        yzOrderService.convertType(request);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 有赞订单-会员对比
     *
     * @param
     * @return
     */
    @ApiOperation(value = "有赞订单-会员对比")
    @RequestMapping(value = "/order-customer-compare", method = RequestMethod.POST)
    public BaseResponse convertType(@RequestBody YzOrderPageQueryRequest request) {
        yzOrderProvider.compare(request);
        return BaseResponse.SUCCESSFUL();
    }
    /**
     * 有赞订单更新
     *
     * @param
     * @return
     */
    @ApiOperation(value = "有赞订单更新")
    @RequestMapping(value = "/order-update", method = RequestMethod.POST)
    public BaseResponse updateYzOrder(@RequestBody YzOrderUpdateRequest request) {
        yzOrderService.updateOrder(request);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 有赞订单更新
     *
     * @param
     * @return
     */
    @ApiOperation(value = "订单入库补偿")
    @RequestMapping(value = "/order-convert2", method = RequestMethod.POST)
    public BaseResponse updateYzOrder(@RequestBody YzOrderCompensateRequest request) {
        YzOrderPageQueryRequest queryRequest = new YzOrderPageQueryRequest();
        queryRequest.setPageNum(request.getPageNo());
        yzOrderProvider.convert(queryRequest);
        return BaseResponse.SUCCESSFUL();
    }
}
