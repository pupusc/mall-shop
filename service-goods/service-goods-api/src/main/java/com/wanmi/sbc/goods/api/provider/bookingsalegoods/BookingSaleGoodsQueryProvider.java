package com.wanmi.sbc.goods.api.provider.bookingsalegoods;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.bookingsale.BookingGoodsInfoSimplePageRequest;
import com.wanmi.sbc.goods.api.request.bookingsalegoods.BookingSaleGoodsByIdRequest;
import com.wanmi.sbc.goods.api.request.bookingsalegoods.BookingSaleGoodsListRequest;
import com.wanmi.sbc.goods.api.request.bookingsalegoods.BookingSaleGoodsPageRequest;
import com.wanmi.sbc.goods.api.response.bookingsalegoods.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>预售商品信息查询服务Provider</p>
 *
 * @author dany
 * @date 2020-06-05 10:51:35
 */
@FeignClient(value = "${application.goods.name}", contextId = "BookingSaleGoodsQueryProvider")
public interface BookingSaleGoodsQueryProvider {

    /**
     * 分页查询预售商品信息API
     *
     * @param bookingSaleGoodsPageReq 分页请求参数和筛选对象 {@link BookingSaleGoodsPageRequest}
     * @return 预售商品信息分页列表信息 {@link BookingSaleGoodsPageResponse}
     * @author dany
     */
    @PostMapping("/goods/${application.goods.version}/bookingsalegoods/page")
    BaseResponse<BookingSaleGoodsPageResponse> page(@RequestBody @Valid BookingSaleGoodsPageRequest bookingSaleGoodsPageReq);

    /**
     * 列表查询预售商品信息API
     *
     * @param bookingSaleGoodsListReq 列表请求参数和筛选对象 {@link BookingSaleGoodsListRequest}
     * @return 预售商品信息的列表信息 {@link BookingSaleGoodsListResponse}
     * @author dany
     */
    @PostMapping("/goods/${application.goods.version}/bookingsalegoods/list")
    BaseResponse<BookingSaleGoodsListResponse> list(@RequestBody @Valid BookingSaleGoodsListRequest bookingSaleGoodsListReq);

    /**
     * 单个查询预售商品信息API
     *
     * @param bookingSaleGoodsByIdRequest 单个查询预售商品信息请求参数 {@link BookingSaleGoodsByIdRequest}
     * @return 预售商品信息详情 {@link BookingSaleGoodsByIdResponse}
     * @author dany
     */
    @PostMapping("/goods/${application.goods.version}/bookingsalegoods/get-by-id")
    BaseResponse<BookingSaleGoodsByIdResponse> getById(@RequestBody @Valid BookingSaleGoodsByIdRequest bookingSaleGoodsByIdRequest);


    /**
     * 预售魔方列表
     *
     * @param request
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/bookingsalegoods/page-boss")
    BaseResponse<BookingResponse> pageBoss(@RequestBody @Valid BookingGoodsInfoSimplePageRequest request);


    /**
     * 预售魔方H5
     *
     * @param request
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/bookingsalegoods/pagebookinggoodsinfo")
    BaseResponse<BookingGoodsResponse> pageBookingGoodsInfo(@RequestBody @Valid BookingGoodsInfoSimplePageRequest request);
}

