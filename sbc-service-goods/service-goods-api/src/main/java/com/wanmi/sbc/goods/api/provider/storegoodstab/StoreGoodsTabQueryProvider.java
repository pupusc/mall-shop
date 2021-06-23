package com.wanmi.sbc.goods.api.provider.storegoodstab;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.storegoodstab.StoreGoodsTabListByStoreIdRequest;
import com.wanmi.sbc.goods.api.response.storegoodstab.StoreGoodsTabListByStoreIdResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @Author: gaomuwei
 * @Date: Created In 下午5:41 2018/12/21
 * @Description:
 */
@FeignClient(value = "${application.goods.name}", contextId = "StoreGoodsTabQueryProvider")
public interface StoreGoodsTabQueryProvider {

    /**
     * 根据店铺id查询商品模板列表
     *
     * @param request {@link StoreGoodsTabListByStoreIdRequest}
     * @return 商品模板列表 {@link StoreGoodsTabListByStoreIdResponse}
     */
    @PostMapping("/goods/${application.goods.version}/store/goodstab/list-by-store-id")
    BaseResponse<StoreGoodsTabListByStoreIdResponse> listByStoreId(
            @RequestBody @Valid StoreGoodsTabListByStoreIdRequest request);

}
