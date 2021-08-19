package com.wanmi.sbc.elastic.provider.impl.settlement;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.elastic.api.provider.settlement.EsSettlementProvider;
import com.wanmi.sbc.elastic.api.provider.storeInformation.EsStoreInformationProvider;
import com.wanmi.sbc.elastic.api.request.settlement.EsSettlementListRequest;
import com.wanmi.sbc.elastic.api.request.settlement.EsSettlementRequest;
import com.wanmi.sbc.elastic.api.request.settlement.SettlementQueryRequest;
import com.wanmi.sbc.elastic.api.request.storeInformation.*;
import com.wanmi.sbc.elastic.settlement.service.EsSettlementService;
import com.wanmi.sbc.elastic.storeInformation.service.EsStoreInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @Author yangzhen
 * @Description //商家店铺信息
 * @Date 19:05 2020/12/7
 * @Param
 * @return
 **/
@RestController
@Validated
public class EsSettlementController implements EsSettlementProvider {

    @Autowired
    private EsSettlementService esSettlementService;

    @Override
    public BaseResponse initSettlement(@RequestBody @Valid EsSettlementRequest request) {
        esSettlementService.initSettlement(request);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse initSettlementList(@RequestBody @Valid EsSettlementListRequest request) {
        esSettlementService.initSettlementList(request);
        return BaseResponse.SUCCESSFUL();
    }


    @Override
    public BaseResponse updateSettlementStatus(@RequestBody @Valid SettlementQueryRequest request) {
        esSettlementService.updateSettlementStatus(request);
        return BaseResponse.SUCCESSFUL();
    }
}
