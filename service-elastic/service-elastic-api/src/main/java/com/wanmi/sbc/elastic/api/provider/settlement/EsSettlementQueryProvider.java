package com.wanmi.sbc.elastic.api.provider.settlement;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.elastic.api.request.settlement.EsSettlementListRequest;
import com.wanmi.sbc.elastic.api.request.settlement.EsSettlementPageRequest;
import com.wanmi.sbc.elastic.api.request.settlement.EsSettlementRequest;
import com.wanmi.sbc.elastic.api.request.storeInformation.EsCompanyAccountQueryRequest;
import com.wanmi.sbc.elastic.api.response.settlement.EsSettlementResponse;
import com.wanmi.sbc.elastic.api.response.storeInformation.EsCompanyAccountResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @Author yangzhen
 * @Description // 结算单
 * @Date 18:27 2020/12/11
 * @Param
 * @return
 **/
@FeignClient(value = "${application.elastic.name}", contextId = "EsSettlementQueryProvider")
public interface EsSettlementQueryProvider {


    @PostMapping("/elastic/${application.elastic.version}/storeInformation/es-settlement-page")
    BaseResponse<EsSettlementResponse> esSettlementPage(@RequestBody @Valid EsSettlementPageRequest request);




}
