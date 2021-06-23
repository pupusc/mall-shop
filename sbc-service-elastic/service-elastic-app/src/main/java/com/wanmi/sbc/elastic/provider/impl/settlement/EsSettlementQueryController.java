package com.wanmi.sbc.elastic.provider.impl.settlement;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.elastic.api.provider.settlement.EsSettlementQueryProvider;
import com.wanmi.sbc.elastic.api.request.settlement.EsSettlementPageRequest;
import com.wanmi.sbc.elastic.api.request.storeInformation.EsCompanyAccountQueryRequest;
import com.wanmi.sbc.elastic.api.response.settlement.EsSettlementResponse;
import com.wanmi.sbc.elastic.api.response.storeInformation.EsCompanyAccountResponse;
import com.wanmi.sbc.elastic.settlement.service.EsSettlementService;
import com.wanmi.sbc.elastic.storeInformation.service.EsCompanyAccountService;
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
public class EsSettlementQueryController implements EsSettlementQueryProvider {

    @Autowired
    private EsSettlementService esSettlementService;



    @Override
    public BaseResponse<EsSettlementResponse> esSettlementPage(@Valid EsSettlementPageRequest request) {
        return  BaseResponse.success(esSettlementService.querySettlementPage(request));
    }
}
