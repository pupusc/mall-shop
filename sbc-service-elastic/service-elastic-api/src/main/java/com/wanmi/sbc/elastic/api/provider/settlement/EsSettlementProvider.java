package com.wanmi.sbc.elastic.api.provider.settlement;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.elastic.api.request.settlement.EsSettlementListRequest;
import com.wanmi.sbc.elastic.api.request.settlement.EsSettlementRequest;
import com.wanmi.sbc.elastic.api.request.settlement.SettlementQueryRequest;
import com.wanmi.sbc.elastic.api.request.storeInformation.*;
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
@FeignClient(value = "${application.elastic.name}", contextId = "EsSettlementProvider")
public interface EsSettlementProvider {


    @PostMapping("/elastic/${application.elastic.version}/settlement/init")
    BaseResponse initSettlement(@RequestBody @Valid EsSettlementRequest request);

    @PostMapping("/elastic/${application.elastic.version}/settlement/init-settlement-list")
    BaseResponse initSettlementList(@RequestBody @Valid EsSettlementListRequest request);

    @PostMapping("/elastic/${application.elastic.version}/settlement/update-settlement-status")
    BaseResponse updateSettlementStatus(@RequestBody @Valid SettlementQueryRequest request);




}
