package com.wanmi.sbc.goods.api.provider.bookingsalegoods;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.bookingsale.BookingSaleGoodsCountRequest;
import com.wanmi.sbc.goods.api.request.bookingsalegoods.BookingSaleGoodsAddRequest;
import com.wanmi.sbc.goods.api.response.bookingsalegoods.BookingSaleGoodsAddResponse;
import com.wanmi.sbc.goods.api.request.bookingsalegoods.BookingSaleGoodsModifyRequest;
import com.wanmi.sbc.goods.api.response.bookingsalegoods.BookingSaleGoodsModifyResponse;
import com.wanmi.sbc.goods.api.request.bookingsalegoods.BookingSaleGoodsDelByIdRequest;
import com.wanmi.sbc.goods.api.request.bookingsalegoods.BookingSaleGoodsDelByIdListRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>预售商品信息保存服务Provider</p>
 * @author dany
 * @date 2020-06-05 10:51:35
 */
@FeignClient(value = "${application.goods.name}", contextId = "BookingSaleGoodsProvider")
public interface BookingSaleGoodsProvider {

	/**
	 * 新增预售商品信息API
	 *
	 * @author dany
	 * @param bookingSaleGoodsAddRequest 预售商品信息新增参数结构 {@link BookingSaleGoodsAddRequest}
	 * @return 新增的预售商品信息信息 {@link BookingSaleGoodsAddResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/bookingsalegoods/add")
	BaseResponse<BookingSaleGoodsAddResponse> add(@RequestBody @Valid BookingSaleGoodsAddRequest bookingSaleGoodsAddRequest);

	/**
	 * 修改预售商品信息API
	 *
	 * @author dany
	 * @param bookingSaleGoodsModifyRequest 预售商品信息修改参数结构 {@link BookingSaleGoodsModifyRequest}
	 * @return 修改的预售商品信息信息 {@link BookingSaleGoodsModifyResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/bookingsalegoods/modify")
	BaseResponse<BookingSaleGoodsModifyResponse> modify(@RequestBody @Valid BookingSaleGoodsModifyRequest bookingSaleGoodsModifyRequest);

	/**
	 * 单个删除预售商品信息API
	 *
	 * @author dany
	 * @param bookingSaleGoodsDelByIdRequest 单个删除参数结构 {@link BookingSaleGoodsDelByIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/bookingsalegoods/delete-by-id")
	BaseResponse deleteById(@RequestBody @Valid BookingSaleGoodsDelByIdRequest bookingSaleGoodsDelByIdRequest);

	/**
	 * 批量删除预售商品信息API
	 *
	 * @author dany
	 * @param bookingSaleGoodsDelByIdListRequest 批量删除参数结构 {@link BookingSaleGoodsDelByIdListRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/bookingsalegoods/delete-by-id-list")
	BaseResponse deleteByIdList(@RequestBody @Valid BookingSaleGoodsDelByIdListRequest bookingSaleGoodsDelByIdListRequest);


	/**
	 * @param request
	 * @return
	 */
	@PostMapping("/goods/${application.goods.version}/bookingsalegoods/subCanBookingCount")
	BaseResponse subCanBookingCount(@RequestBody @Valid BookingSaleGoodsCountRequest request);

	/**
	 * @param request
	 * @return
	 */
	@PostMapping("/goods/${application.goods.version}/bookingsalegoods/addCanBookingCount")
	BaseResponse addCanBookingCount(@RequestBody @Valid BookingSaleGoodsCountRequest request);

	/**
	 * 增加全款支付数量
	 * @param request
	 * @return
	 */
	@PostMapping("/goods/${application.goods.version}/bookingsalegoods/addBookingPaycount")
	BaseResponse addBookingPayCount(@RequestBody @Valid BookingSaleGoodsCountRequest request);

	/**
	 * 增加定金支付数量
	 * @param request
	 * @return
	 */
	@PostMapping("/goods/${application.goods.version}/bookingsalegoods/addBookinghandSelCount")
	BaseResponse addBookinghandSelCount(@RequestBody @Valid BookingSaleGoodsCountRequest request);


	/**
	 * 增加定金支付数量
	 * @param request
	 * @return
	 */
	@PostMapping("/goods/${application.goods.version}/bookingsalegoods/addBookingTailCount")
	BaseResponse addBookingTailCount(@RequestBody @Valid BookingSaleGoodsCountRequest request);
}

