package com.wanmi.sbc.goods.api.provider.presellsale;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.presellsale.PresellSaleByIdDeleteRequest;
import com.wanmi.sbc.goods.api.request.presellsale.PresellSaleModifyRequest;
import com.wanmi.sbc.goods.api.request.presellsale.PresellSaleNoticeRequest;
import com.wanmi.sbc.goods.api.request.presellsale.PresellSaleSaveRequest;
import com.wanmi.sbc.goods.api.response.presellsale.PresellSaleResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

/**
 * 预售活动新增，修改，删除接口
 */
@FeignClient(value = "${application.goods.name}", contextId = "PresellSaleProvider")
public interface PresellSaleProvider {


    /**
     * 根据预售活动信息和商品信息新增预售活动
     *
     * @param request 包含活动信息和商品信息 {@link PresellSaleSaveRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/goods/${application.goods.version}/presell/add")
    BaseResponse<PresellSaleResponse> addPresellSale(@RequestBody  PresellSaleSaveRequest request);




    /**
     * 编辑修改预售活动信息
     *
     * @param request 编辑修改预售活动信息 {@link PresellSaleModifyRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/goods/${application.goods.version}/presell/modify")
    BaseResponse<PresellSaleResponse> modifyPresellSale(@RequestBody @Valid PresellSaleModifyRequest request);



    /**
     * 删除预售活动信息
     *
     * @param request 编辑修改预售活动信息 {@link PresellSaleModifyRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/goods/${application.goods.version}/presell/delete")
    BaseResponse<String> delete( @RequestBody PresellSaleByIdDeleteRequest request);


    /**
     * 设置预售活动开始暂停
     *
     * @param request 编辑修改预售活动信息 {@link PresellSaleModifyRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/goods/${application.goods.version}/presell/suspended")
    BaseResponse<String> setSuspended(@RequestBody PresellSaleByIdDeleteRequest request);


//    /**
//     * 预售活动支付
//     * @param request
//     * @return
//     */
//    @PostMapping("/goods/${application.goods.version}/presell/payment")
//    BaseResponse<String>  payment(@RequestBody PresellSalePaymentRequest request);


    /**
     * 定时器监听，预售活动支付尾款时发送消息通知
     *
     * @param request 编辑修改预售活动信息 {@link PresellSaleModifyRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/goods/${application.goods.version}/presell/notice")
    BaseResponse<List<String>> presellSaleNotice(@RequestBody PresellSaleNoticeRequest request);

}
