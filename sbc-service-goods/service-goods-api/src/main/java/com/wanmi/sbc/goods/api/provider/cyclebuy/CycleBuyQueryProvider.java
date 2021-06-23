package com.wanmi.sbc.goods.api.provider.cyclebuy;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.cyclebuy.*;
import com.wanmi.sbc.goods.api.response.cyclebuy.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;


/**
 * <p>周期购操作接口</p>
 * @author weiwenhao
 * @date 2021-01-21 09:15:37
 */
@FeignClient(value = "${application.goods.name}", contextId = "CycleBuyQueryProvider")
public interface CycleBuyQueryProvider {

    /**
     * 分页查询周期购活动
     * @param cycleBuyPageReq
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/cycle-buy/page")
    BaseResponse<CycleBuyPageResponse> page(@RequestBody @Valid CycleBuyPageRequest cycleBuyPageReq);


    /**
     * List查询周期购活动
     * @param cycleBuyListReq
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/cycle-buy/list")
    BaseResponse<CycleBuyListResponse> list(@RequestBody @Valid CycleBuyListRequest cycleBuyListReq);

    /**
     * 查询单个周期购活动
     * @param cycleBuyByIdRequest
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/cycle-buy/get-id")
    BaseResponse<CycleBuyByIdResponse> getById(@RequestBody @Valid CycleBuyByIdRequest cycleBuyByIdRequest);


    /**
     * 根据goodsId查询单个周期购活动
     * @param cycleBuyByGoodsIdRequest
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/cycle-buy/get-by-goods-details-id")
    BaseResponse<CycleBuyByGoodsIdResponse> getByGoodsDetailsId(@RequestBody @Valid CycleBuyByGoodsIdRequest cycleBuyByGoodsIdRequest);

    /**
     * 根据goodsId查询单个周期购活动
     * @param cycleBuyByGoodsIdRequest
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/cycle-buy/get-by-goods-id")
    BaseResponse<CycleBuyByGoodsIdResponse> getByGoodsId(@RequestBody @Valid CycleBuyByGoodsIdRequest cycleBuyByGoodsIdRequest);

    /**
     * 查询发货规则对象
     * @param cycleBuySendDateRuleRequest
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/cycle-buy/get-send-date-rule-list")
    BaseResponse<CycleBuySendDateRuleResponse> getSendDateRuleList(@RequestBody @Valid CycleBuySendDateRuleRequest cycleBuySendDateRuleRequest);

}
