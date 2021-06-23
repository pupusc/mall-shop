package com.wanmi.sbc.goods;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.provider.priceadjustmentrecorddetail.PriceAdjustmentRecordDetailProvider;
import com.wanmi.sbc.goods.api.provider.priceadjustmentrecorddetail.PriceAdjustmentRecordDetailQueryProvider;
import com.wanmi.sbc.goods.api.request.priceadjustmentrecorddetail.PriceAdjustmentRecordDetailPageRequest;
import com.wanmi.sbc.goods.api.response.price.adjustment.PriceAdjustmentRecordDetailPageByNoResponse;
import com.wanmi.sbc.goods.api.response.price.adjustment.PriceAdjustmentRecordDetailPageResponse;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@Api(description = "调价单详情表管理API", tags = "PriceAdjustmentRecordDetailController")
@RestController
@RequestMapping(value = "/price-adjustment-record-detail")
public class PriceAdjustmentRecordDetailController {

    @Autowired
    private PriceAdjustmentRecordDetailQueryProvider priceAdjustmentRecordDetailQueryProvider;

    @Autowired
    private CommonUtil commonUtil;

    @ApiOperation(value = "分页查询调价单详情表")
    @PostMapping("/page")
    public BaseResponse<PriceAdjustmentRecordDetailPageByNoResponse> getPage(@RequestBody @Valid PriceAdjustmentRecordDetailPageRequest pageReq) {
        pageReq.setBaseStoreId(commonUtil.getStoreId());
        return priceAdjustmentRecordDetailQueryProvider.page(pageReq);
    }

    @ApiOperation(value = "确认调价分页查询调价单详情表")
    @PostMapping("/page/confirm")
    public BaseResponse<PriceAdjustmentRecordDetailPageResponse> pageForConfirm(@RequestBody @Valid PriceAdjustmentRecordDetailPageRequest pageReq) {
        pageReq.setBaseStoreId(commonUtil.getStoreId());
        return priceAdjustmentRecordDetailQueryProvider.pageForConfirm(pageReq);
    }

}
