package com.wanmi.sbc.goods.api.provider.bookingsale;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.bookingsale.*;
import com.wanmi.sbc.goods.api.response.bookingsale.*;
import com.wanmi.sbc.goods.api.response.bookingsalegoods.BookingSaleInProgressAllGoodsInfoIdsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>预售信息查询服务Provider</p>
 *
 * @author dany
 * @date 2020-06-05 10:47:21
 */
@FeignClient(value = "${application.goods.name}", contextId = "BookingSaleQueryProvider")
public interface BookingSaleQueryProvider {

    /**
     * 分页查询预售信息API
     *
     * @param bookingSalePageReq 分页请求参数和筛选对象 {@link BookingSalePageRequest}
     * @return 预售信息分页列表信息 {@link BookingSalePageResponse}
     * @author dany
     */
    @PostMapping("/goods/${application.goods.version}/bookingsale/page")
    BaseResponse<BookingSalePageResponse> page(@RequestBody @Valid BookingSalePageRequest bookingSalePageReq);

    /**
     * 列表查询预售信息API
     *
     * @param bookingSaleListReq 列表请求参数和筛选对象 {@link BookingSaleListRequest}
     * @return 预售信息的列表信息 {@link BookingSaleListResponse}
     * @author dany
     */
    @PostMapping("/goods/${application.goods.version}/bookingsale/list")
    BaseResponse<BookingSaleListResponse> list(@RequestBody @Valid BookingSaleListRequest bookingSaleListReq);

    /**
     * 单个查询预售信息API
     *
     * @param bookingSaleByIdRequest 单个查询预售信息请求参数 {@link BookingSaleByIdRequest}
     * @return 预售信息详情 {@link BookingSaleByIdResponse}
     * @author dany
     */
    @PostMapping("/goods/${application.goods.version}/bookingsale/get-by-id")
    BaseResponse<BookingSaleByIdResponse> getById(@RequestBody @Valid BookingSaleByIdRequest bookingSaleByIdRequest);

    /**
     * 单个查询预售信息API
     *
     * @param bookingSaleByIdRequest 单个查询预售信息请求参数 {@link BookingSaleByIdRequest}
     * @return 预售信息详情 {@link BookingSaleByIdResponse}
     * @author dany
     */
    @PostMapping("/goods/${application.goods.version}/bookingsale/get-one")
    BaseResponse<BookingSaleByIdResponse> getOne(@RequestBody @Valid BookingSaleByIdRequest bookingSaleByIdRequest);


    /**
     * 根据skuid获取哪些正在预售活动中
     *
     * @param request
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/bookingsale/in-progress-booking-sale-info")
    BaseResponse<BookingSaleListResponse> inProgressBookingSaleInfoByGoodsInfoIdList(@RequestBody @Valid BookingSaleInProgressRequest request);


    /**
     * @param request
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/bookingsale/is-in-progress")
    BaseResponse<BookingSaleIsInProgressResponse> isInProgress(@RequestBody @Valid BookingSaleIsInProgressRequest request);


    /**
     * 查询商品SPU维度是否存在预售活动中的商品（未开始，进行中的）
     *
     * @param request 查询商品是否正在进行预售活动请求参数 {@link BookingSaleByGoodsIdRequest}
     * @return 预购详情 {@link BookingSaleNotEndResponse}
     * @author dany
     */
    @PostMapping("/goods/${application.goods.version}/bookingsale/get-not-end-activity")
    BaseResponse<BookingSaleNotEndResponse> getNotEndActivity(@RequestBody @Valid BookingSaleByGoodsIdRequest request);

    /**
     * 根据skuid获取哪些正在预售活动中/预约活动中
     *
     * @param request 查询商品是否正在进行预售活动请求参数 {@link BookingSaleByGoodsIdRequest}
     * @return 预购详情 {@link BookingSaleNotEndResponse}
     * @author dany
     */
    @PostMapping("/goods/${application.goods.version}/bookingsale/in-progress-all-by-goods-info-ids")
    BaseResponse<BookingSaleInProgressAllGoodsInfoIdsResponse> inProgressAllByGoodsInfoIds(@RequestBody @Valid BookingSaleInProgressAllGoodsInfoIdsRequest request);

    /**
     * 分页查询预售信息API
     *
     * @param bookingSalePageReq 分页请求参数和筛选对象 {@link BookingSalePageRequest}
     * @return 预售信息分页列表信息 {@link BookingSalePageResponse}
     * @author dany
     */
    @PostMapping("/goods/${application.goods.version}/bookingsale/page-new")
    BaseResponse<BookingSalePageResponse> pageNew(@RequestBody @Valid BookingSalePageRequest bookingSalePageReq);
}

