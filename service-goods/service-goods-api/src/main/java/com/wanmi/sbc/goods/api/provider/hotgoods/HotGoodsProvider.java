package com.wanmi.sbc.goods.api.provider.hotgoods;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.bean.dto.HotGoodsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;


@FeignClient(value = "${application.goods.name}", contextId = "HotGoodsProvider")
public interface HotGoodsProvider {

    /**
     * 刷新排序
     */
    @PostMapping("/hotGoods/${application.goods.version}/updateSort")
    BaseResponse<Boolean> updateSort();

    /**
     * 刷新排序
     */
    @PostMapping("/hotGoods/${application.goods.version}/selectAllBySort")
    BaseResponse<List<HotGoodsDto>> selectAllBySort();

}
