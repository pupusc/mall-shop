package com.wanmi.sbc.goods.provider.impl.presellsale;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.provider.presellsale.PresellSaleProvider;
import com.wanmi.sbc.goods.api.request.presellsale.PresellSaleByIdDeleteRequest;
import com.wanmi.sbc.goods.api.request.presellsale.PresellSaleModifyRequest;
import com.wanmi.sbc.goods.api.request.presellsale.PresellSaleNoticeRequest;
import com.wanmi.sbc.goods.api.request.presellsale.PresellSaleSaveRequest;
import com.wanmi.sbc.goods.api.response.presellsale.PresellSaleResponse;
import com.wanmi.sbc.goods.presellsale.service.PresellSaleGoodsService;
import com.wanmi.sbc.goods.presellsale.service.PresellSaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>对预售活动的操作接口</p>
 * Created by xiaoqianh on 2020-05-25
 */
@RestController
public class PresellSaleController implements PresellSaleProvider {

    @Autowired
    private PresellSaleService presellSaleService;

    /**
     * 根据预售活动信息和商品信息新增预售活动
     *
     * @param request 包含活动信息和商品信息 {@link PresellSaleSaveRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @Override
    public BaseResponse<PresellSaleResponse> addPresellSale( PresellSaleSaveRequest request) {
        PresellSaleResponse response = presellSaleService.add(request);
       return BaseResponse.success(response);
    }

    @Override
    public BaseResponse<PresellSaleResponse> modifyPresellSale(@Valid PresellSaleModifyRequest request) {
        presellSaleService.modify(request);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse<String> delete(PresellSaleByIdDeleteRequest request) {
        presellSaleService.delete(request);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse<String> setSuspended(PresellSaleByIdDeleteRequest request) {
        presellSaleService.suspended(request);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse<List<String>> presellSaleNotice(PresellSaleNoticeRequest request) {
        presellSaleService.notice(request);
        return null;
    }


}
