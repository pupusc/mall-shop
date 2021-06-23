package com.wanmi.sbc.goods;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.goods.api.provider.priceadjustmentrecord.PriceAdjustmentRecordProvider;
import com.wanmi.sbc.goods.api.provider.priceadjustmentrecord.PriceAdjustmentRecordQueryProvider;
import com.wanmi.sbc.goods.api.request.priceadjustmentrecord.*;
import com.wanmi.sbc.goods.api.response.price.adjustment.*;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Api(description = "调价记录表管理API", tags = "PriceAdjustmentRecordController")
@RestController
@RequestMapping(value = "/price-adjustment-record")
public class PriceAdjustmentRecordController {

    @Autowired
    private PriceAdjustmentRecordQueryProvider priceAdjustmentRecordQueryProvider;

    @Autowired
    private CommonUtil commonUtil;

    @ApiOperation(value = "分页查询调价记录表")
    @PostMapping("/page")
    public BaseResponse<PriceAdjustmentRecordPageResponse> getPage(@RequestBody @Valid PriceAdjustmentRecordPageRequest pageReq) {
        pageReq.putSort("createTime", "desc");
        // 查询已确认的记录
        pageReq.setConfirmFlag(DefaultFlag.YES.toValue());
        // 查询当前登录商家或供应商的记录
        pageReq.setStoreId(commonUtil.getStoreId());
        return priceAdjustmentRecordQueryProvider.page(pageReq);
    }

    @ApiOperation(value = "根据id查询调价记录表")
    @GetMapping("/{id}")
    public BaseResponse<PriceAdjustmentRecordByIdResponse> getById(@PathVariable String id) {
        if (id == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        PriceAdjustmentRecordByIdRequest idReq = new PriceAdjustmentRecordByIdRequest();
        idReq.setId(id);
        idReq.setStoreId(commonUtil.getStoreId());
        return priceAdjustmentRecordQueryProvider.getById(idReq);
    }

}
