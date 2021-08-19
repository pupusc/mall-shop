package com.wanmi.sbc.goods.api.provider.storegoodstab;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.storegoodstab.*;
import com.wanmi.sbc.goods.api.response.storegoodstab.StoreGoodsTabAddResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @Author: gaomuwei
 * @Date: Created In 上午10:32 2018/12/13
 * @Description:
 */
@FeignClient(value = "${application.goods.name}", contextId = "StoreGoodsTabProvider")
public interface StoreGoodsTabProvider {


    /**
     * 新增店铺商品模板
     *
     * @param request
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/store/goodstab/add")
    BaseResponse<StoreGoodsTabAddResponse> add(@RequestBody @Valid StoreGoodsTabAddRequest request);


    /**
     * 修改店铺商品模板
     *
     * @param request
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/store/goodstab/modify")
    BaseResponse modify(@RequestBody @Valid StoreGoodsTabModifyRequest request);


    /**
     * 删除店铺商品模板
     *
     * @param request
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/store/goodstab/delete")
    BaseResponse delete(@RequestBody @Valid StoreGoodsTabDeleteRequest request);


    /**
     * 修改排序
     * @param request
     */
    @PostMapping("/goods/${application.goods.version}/store/goodstab/edit-sort")
    BaseResponse editSort(@RequestBody @Valid StoreGoodsTabEditSortRequest request);
}
