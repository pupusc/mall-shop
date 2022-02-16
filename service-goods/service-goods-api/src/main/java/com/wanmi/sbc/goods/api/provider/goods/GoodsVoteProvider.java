package com.wanmi.sbc.goods.api.provider.goods;

import com.wanmi.sbc.common.base.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "${application.goods.name}", contextId = "GoodsVoteProvider")
public interface GoodsVoteProvider {

    @PostMapping("/goods/${application.goods.version}/vote/sync")
    BaseResponse syncVoteNumber();
}
