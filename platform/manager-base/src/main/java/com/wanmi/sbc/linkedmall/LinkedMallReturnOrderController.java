package com.wanmi.sbc.linkedmall;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.order.api.provider.linkedmall.LinkedMallReturnOrderProvider;
import com.wanmi.sbc.order.api.provider.linkedmall.LinkedMallReturnOrderQueryProvider;
import com.wanmi.sbc.order.api.request.linkedmall.LinkedMallReturnOrderApplyRequest;
import com.wanmi.sbc.order.api.request.linkedmall.LinkedMallReturnOrderReasonRequest;
import com.wanmi.sbc.order.api.response.linkedmall.LinkedMallReturnReasonResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * linkedMall退货申请
 * Created by dyt on 29/8/2020.
 */
@Api(tags = "LinkedMallReturnOrderController", description = "linkedMall退单 Api")
@RestController
@RequestMapping("/return/linkedMall")
public class LinkedMallReturnOrderController {

    @Autowired
    private LinkedMallReturnOrderProvider linkedMallReturnOrderProvider;

    @Autowired
    private LinkedMallReturnOrderQueryProvider linkedMallReturnOrderQueryProvider;

    @ApiOperation(value = "linkedMall退单原因查询")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "rid", value = "退单Id", required = true)
    @GetMapping("/reasons/{rid}")
    public BaseResponse<LinkedMallReturnReasonResponse> reasons(@PathVariable String rid) {
        return linkedMallReturnOrderQueryProvider.listReturnReason(LinkedMallReturnOrderReasonRequest.builder().rid(rid).build());
    }

    @ApiOperation(value = "linkedMall退单申请")
    @PutMapping("/apply")
    public BaseResponse apply(@RequestBody @Valid LinkedMallReturnOrderApplyRequest request) {
        return linkedMallReturnOrderProvider.apply(request);
    }
}
