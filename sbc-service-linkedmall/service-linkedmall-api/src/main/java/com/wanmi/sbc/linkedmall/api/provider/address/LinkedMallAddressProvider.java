package com.wanmi.sbc.linkedmall.api.provider.address;

import com.aliyuncs.linkedmall.model.v20180116.QueryAddressResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.linkedmall.api.request.address.SbcAddressQueryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "${application.linkedmall.name}", contextId = "LinkedMallAddressProvider")
public interface LinkedMallAddressProvider {
    /**
     * 查询业务库全量类目
     * @return
     */
    @PostMapping("/linkedmall/${application.linkedmall.version}/address/query")
    BaseResponse<List<QueryAddressResponse.DivisionAddressItem>> query(@RequestBody SbcAddressQueryRequest request);

    /**
     * 查询业务库全量类目
     * @return
     */
    @PostMapping("/linkedmall/${application.linkedmall.version}/address/init")
    BaseResponse init();
}
