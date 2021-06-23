package com.wanmi.sbc.goods.provider.impl.priceadjustmentrecorddetail;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.priceadjustmentrecorddetail.PriceAdjustmentRecordDetailProvider;
import com.wanmi.sbc.goods.api.request.adjustprice.*;
import com.wanmi.sbc.goods.api.request.priceadjustmentrecorddetail.PriceAdjustmentRecordDetailAddBatchRequest;
import com.wanmi.sbc.goods.api.request.priceadjustmentrecorddetail.PriceAdjustmentRecordDetailAddRequest;
import com.wanmi.sbc.goods.api.request.priceadjustmentrecorddetail.PriceAdjustmentRecordDetailModifyRequest;
import com.wanmi.sbc.goods.api.response.price.adjustment.AdjustPriceExecuteResponse;
import com.wanmi.sbc.goods.api.response.price.adjustment.PriceAdjustmentRecordDetailAddResponse;
import com.wanmi.sbc.goods.api.response.price.adjustment.PriceAdjustmentRecordDetailModifyResponse;
import com.wanmi.sbc.goods.priceadjustmentrecorddetail.model.root.PriceAdjustmentRecordDetail;
import com.wanmi.sbc.goods.priceadjustmentrecorddetail.service.PriceAdjustmentRecordDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>调价单详情表保存服务接口实现</p>
 *
 * @author chenli
 * @date 2020-12-09 19:55:41
 */
@RestController
@Validated
public class PriceAdjustmentRecordDetailController implements PriceAdjustmentRecordDetailProvider {
    @Autowired
    private PriceAdjustmentRecordDetailService priceAdjustmentRecordDetailService;

    @Override
    public BaseResponse<PriceAdjustmentRecordDetailAddResponse> add(@RequestBody @Valid PriceAdjustmentRecordDetailAddRequest priceAdjustmentRecordDetailAddRequest) {
        PriceAdjustmentRecordDetail priceAdjustmentRecordDetail = KsBeanUtil.convert(priceAdjustmentRecordDetailAddRequest, PriceAdjustmentRecordDetail.class);
        return BaseResponse.success(new PriceAdjustmentRecordDetailAddResponse(
                priceAdjustmentRecordDetailService.wrapperVo(priceAdjustmentRecordDetailService.add(priceAdjustmentRecordDetail))));
    }

    @Override
    public BaseResponse addBatch(@RequestBody @Valid PriceAdjustmentRecordDetailAddBatchRequest priceAdjustmentRecordDetailAddBatchRequest) {
        List<PriceAdjustmentRecordDetail> dataList = KsBeanUtil.convertList(priceAdjustmentRecordDetailAddBatchRequest.getDataList(),
                PriceAdjustmentRecordDetail.class);
        priceAdjustmentRecordDetailService.addBatch(dataList);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse<PriceAdjustmentRecordDetailModifyResponse> modify(@RequestBody @Valid PriceAdjustmentRecordDetailModifyRequest priceAdjustmentRecordDetailModifyRequest) {
        PriceAdjustmentRecordDetail priceAdjustmentRecordDetail = KsBeanUtil.convert(priceAdjustmentRecordDetailModifyRequest, PriceAdjustmentRecordDetail.class);
        return BaseResponse.success(new PriceAdjustmentRecordDetailModifyResponse(
                priceAdjustmentRecordDetailService.wrapperVo(priceAdjustmentRecordDetailService.modify(priceAdjustmentRecordDetail))));
    }

    @Override
    public BaseResponse modifyMarketingPrice(@RequestBody @Valid MarketingPriceAdjustDetailModifyRequest request) {
        PriceAdjustmentRecordDetail detail = new PriceAdjustmentRecordDetail();
        detail.setAdjustedMarketPrice(request.getMarketingPrice());
        detail.setPriceAdjustmentNo(request.getAdjustNo());
        detail.setId(request.getAdjustDetailId());
        detail.setGoodsInfoId(request.getGoodsInfoId());
        priceAdjustmentRecordDetailService.modifyDetail(detail);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse modifyCustomerLevelPrice(@RequestBody @Valid CustomerLevelPriceAdjustDetailModifyRequest request) {
        PriceAdjustmentRecordDetail detail = new PriceAdjustmentRecordDetail();
        detail.setAdjustedMarketPrice(request.getMarketingPrice());
        detail.setPriceAdjustmentNo(request.getAdjustNo());
        detail.setId(request.getAdjustDetailId());
        detail.setGoodsInfoId(request.getGoodsInfoId());
        detail.setLeverPrice(JSONObject.toJSONString(request.getLevelPriceList()));
        priceAdjustmentRecordDetailService.modifyDetail(detail);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse modifyIntervalPrice(@RequestBody @Valid IntervalPriceAdjustDetailModifyRequest request) {
        PriceAdjustmentRecordDetail detail = new PriceAdjustmentRecordDetail();
        detail.setAdjustedMarketPrice(request.getMarketingPrice());
        detail.setPriceAdjustmentNo(request.getAdjustNo());
        detail.setId(request.getAdjustDetailId());
        detail.setGoodsInfoId(request.getGoodsInfoId());
        detail.setIntervalPrice(JSONObject.toJSONString(request.getIntervalPriceList()));
        priceAdjustmentRecordDetailService.modifyDetail(detail);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse delete(@RequestBody @Valid AdjustPriceDetailDeleteRequest request) {
        priceAdjustmentRecordDetailService.deleteDetail(request.getAdjustDetailId(), request.getAdjustNo());
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse modifySupplyPrice(@RequestBody @Valid SupplyPriceAdjustDetailModifyRequest request) {
        PriceAdjustmentRecordDetail detail = new PriceAdjustmentRecordDetail();
        detail.setAdjustSupplyPrice(request.getSupplyPrice());
        detail.setPriceAdjustmentNo(request.getAdjustNo());
        detail.setId(request.getAdjustDetailId());
        priceAdjustmentRecordDetailService.modifyDetail(detail);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse adjustPriceNow(@RequestBody @Valid AdjustPriceNowRequest request) {
        priceAdjustmentRecordDetailService.adjustPriceNow(request.getAdjustNo(), request.getStoreId());
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse adjustPriceConfirm(@RequestBody @Valid AdjustPriceConfirmRequest request) {
        priceAdjustmentRecordDetailService.confirmAdjust(request.getAdjustNo(), request.getStoreId(), request.getEffectiveTime());
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse<AdjustPriceExecuteResponse> adjustPriceExecute(@RequestBody @Valid AdjustPriceExecuteRequest request) {
        AdjustPriceExecuteResponse response = priceAdjustmentRecordDetailService.adjustPriceTaskExecute(request.getAdjustNo(), request.getStoreId());
        return BaseResponse.success(response);
    }

    @Override
    public BaseResponse adjustPriceExecuteFail(@RequestBody @Valid AdjustPriceExecuteFailRequest request) {
        priceAdjustmentRecordDetailService.executeFail(request.getAdjustNo(), request.getResult(), request.getFailReason());
        return BaseResponse.SUCCESSFUL();
    }
}

