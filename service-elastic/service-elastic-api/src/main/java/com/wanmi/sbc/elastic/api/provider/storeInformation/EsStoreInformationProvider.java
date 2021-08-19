package com.wanmi.sbc.elastic.api.provider.storeInformation;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.elastic.api.request.storeInformation.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @Author yangzhen
 * @Description // 商家店铺信息
 * @Date 18:27 2020/12/7
 * @Param
 * @return
 **/
@FeignClient(value = "${application.elastic.name}", contextId = "StoreInformationProvider")
public interface EsStoreInformationProvider {


    @PostMapping("/elastic/${application.elastic.version}/storeInformation/init")
    BaseResponse initStoreInformation(@RequestBody @Valid StoreInformationRequest request);

    @PostMapping("/elastic/${application.elastic.version}/storeInformation/init-store-info-list")
    BaseResponse initStoreInformationList(@RequestBody @Valid EsInitStoreInfoRequest request);


    @PostMapping("/elastic/${application.elastic.version}/storeInformation/modify/modify-store-basic-info")
    BaseResponse<Boolean> modifyStoreBasicInfo(@RequestBody @Valid StoreInfoModifyRequest request);

    @PostMapping("/elastic/${application.elastic.version}/storeInformation/modify/modify-store-audit-state")
    BaseResponse<Boolean> modifyStoreState(@RequestBody @Valid StoreInfoStateModifyRequest request);

    @PostMapping("/elastic/${application.elastic.version}/storeInformation/modify/modify-store-reject")
    BaseResponse<Boolean> modifyStoreReject(@RequestBody @Valid StoreInfoRejectModifyRequest request);

    @PostMapping("/elastic/${application.elastic.version}/storeInformation/modify/modify-store-contractInfo")
    BaseResponse<Boolean> modifyStoreContractInfo(@RequestBody @Valid StoreInfoContractRequest request);

}
