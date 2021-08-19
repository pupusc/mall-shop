package com.wanmi.sbc.goods.api.provider.cyclebuy;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.cyclebuy.*;
import com.wanmi.sbc.goods.api.response.cyclebuy.CycleBuyAddResponse;
import com.wanmi.sbc.goods.api.response.cyclebuy.CycleBuyByIdResponse;
import com.wanmi.sbc.goods.api.response.cyclebuy.CycleBuyModifyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;


/**
 * <p>周期购操作接口</p>
 * @author weiwenhao
 * @date 2021-01-21 09:15:37
 */
@FeignClient(value = "${application.goods.name}", contextId = "CycleBuySaveProvider")
public interface CycleBuySaveProvider {


    /**
     * 新增周期购活动
     * @param cycleBuyAddRequest
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/cycle-buy/add")
    BaseResponse<CycleBuyAddResponse> add(@RequestBody @Valid CycleBuyAddRequest cycleBuyAddRequest) ;



    /**
     * 修改周期购活动
     * @param cycleBuyModifyRequest
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/cycle-buy/modify")
    BaseResponse<CycleBuyModifyResponse> modify(@RequestBody @Valid CycleBuyModifyRequest cycleBuyModifyRequest) ;


    /**
     * 根据id删除周期购信息
     * @param cycleBuyDelByIdRequest
     */
    @PostMapping("/goods/${application.goods.version}/cycle-buy/delete-by-id")
    BaseResponse<CycleBuyByIdResponse> deleteById(@RequestBody @Valid CycleBuyDelByIdRequest cycleBuyDelByIdRequest);

    /**
     * 上下架接口
     * @param cycleBuySaleRequest
     */
    @PostMapping("/goods/${application.goods.version}/cycle-buy/loading")
    BaseResponse<CycleBuyByIdResponse> loading(@RequestBody @Valid CycleBuySaleRequest cycleBuySaleRequest);


}
