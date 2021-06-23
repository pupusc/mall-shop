package com.wanmi.sbc.goods.api.provider.flashsalegoods;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.flashsalegoods.*;
import com.wanmi.sbc.goods.api.response.flashsalegoods.FlashSaleGoodsAddResponse;
import com.wanmi.sbc.goods.api.response.flashsalegoods.FlashSaleGoodsModifyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>抢购商品表保存服务Provider</p>
 * @author bob
 * @date 2019-06-11 14:54:31
 */
@FeignClient(value = "${application.goods.name}", contextId = "FlashSaleGoodsSaveProvider")
public interface FlashSaleGoodsSaveProvider {

	/**
	 * 新增抢购商品表API
	 *
	 * @author bob
	 * @param flashSaleGoodsAddRequest 抢购商品表新增参数结构 {@link FlashSaleGoodsBatchAddRequest}
	 * @return 新增的抢购商品表信息 {@link FlashSaleGoodsAddResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/flashsalegoods/batchAdd")
	BaseResponse<FlashSaleGoodsAddResponse> batchAdd(@RequestBody @Valid FlashSaleGoodsBatchAddRequest flashSaleGoodsAddRequest);

	/**
	 * 修改抢购商品表API
	 *
	 * @author bob
	 * @param flashSaleGoodsModifyRequest 抢购商品表修改参数结构 {@link FlashSaleGoodsModifyRequest}
	 * @return 修改的抢购商品表信息 {@link FlashSaleGoodsModifyResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/flashsalegoods/modify")
	BaseResponse<FlashSaleGoodsModifyResponse> modify(@RequestBody @Valid FlashSaleGoodsModifyRequest flashSaleGoodsModifyRequest);

	/**
	 * 单个删除抢购商品表API
	 *
	 * @author bob
	 * @param flashSaleGoodsDelByIdRequest 单个删除参数结构 {@link FlashSaleGoodsDelByIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/flashsalegoods/delete-by-id")
	BaseResponse deleteById(@RequestBody @Valid FlashSaleGoodsDelByIdRequest flashSaleGoodsDelByIdRequest);

	/**
	 * 批量删除抢购商品表API
	 *
	 * @author bob
	 * @param flashSaleGoodsDelByIdListRequest 批量删除参数结构 {@link FlashSaleGoodsDelByIdListRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/flashsalegoods/delete-by-id-list")
	BaseResponse deleteByIdList(@RequestBody @Valid FlashSaleGoodsDelByIdListRequest flashSaleGoodsDelByIdListRequest);

	/**
	 * 根据场次抢购商品表API
	 *
	 * @author yxz
	 * @param flashSaleGoodsDelByTimeListRequest 批量删除参数结构 {@link FlashSaleGoodsDelByTimeListRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/flashsalegoods/delete-by-time-list")
	BaseResponse deleteByTimeList(@RequestBody @Valid FlashSaleGoodsDelByTimeListRequest flashSaleGoodsDelByTimeListRequest);


	/**
	 * @Author lvzhenwei
	 * @Description 扣减秒杀抢购商品库存
	 * @Date 11:01 2019/6/21
	 * @Param [flashSaleGoodsBatchMinusStockRequest]
	 * @return com.wanmi.sbc.common.base.BaseResponse
	 **/
	@PostMapping("/goods/${application.goods.version}/flashsalegoods/batch-minus-stock")
	BaseResponse batchMinusStock(@RequestBody @Valid FlashSaleGoodsBatchMinusStockRequest flashSaleGoodsBatchMinusStockRequest);

	/**
	 * @Author lvzhenwei
	 * @Description 增加秒杀商品库存
	 * @Date 16:06 2019/7/1
	 * @Param [request]
	 * @return com.wanmi.sbc.common.base.BaseResponse
	 **/
	@PostMapping("/goods/${application.goods.version}/flashsalegoods/add-falsh-sale-goods-stock")
	BaseResponse addFlashSaleGoodsStock(@RequestBody @Valid FlashSaleGoodsBatchAddStockRequest request);
	
	/**
	 * @Author lvzhenwei
	 * @Description
	 * @Date 9:50 2019/6/22
	 * @Param [request]
	 * @return com.wanmi.sbc.common.base.BaseResponse
	 **/
	@PostMapping("/goods/${application.goods.version}/flashsalegoods/batch-plus-sales-volume")
	BaseResponse batchPlusSalesVolume(@RequestBody @Valid FlashSaleGoodsBatchPlusSalesVolumeRequest request);

	/**
	 * @Author lvzhenwei
	 * @Description 减少秒杀商品销量
	 * @Date 14:44 2019/8/5
	 * @Param [request]
	 * @return com.wanmi.sbc.common.base.BaseResponse
	 **/
	@PostMapping("/goods/${application.goods.version}/flashsalegoods/sub-sales-volume")
	BaseResponse subSalesVolumeById(@RequestBody @Valid FlashSaleGoodsBatchStockAndSalesVolumeRequest request);

	/**
	 * @Author lvzhenwei
	 * @Description 扣减秒杀抢购商品库存和增加销量
	 * @Date 11:01 2019/6/21
	 * @Param [flashSaleGoodsBatchMinusStockRequest]
	 * @return com.wanmi.sbc.common.base.BaseResponse
	 **/
	@PostMapping("/goods/${application.goods.version}/flashsalegoods/batch-stock-and-sales-volume")
	BaseResponse batchStockAndSalesVolume(@RequestBody @Valid FlashSaleGoodsBatchStockAndSalesVolumeRequest request);

	/**
	 * @Author lvzhenwei
	 * @Description 增加秒杀抢购商品库存和扣减销量
	 * @Date 14:50 2019/8/5
	 * @Param [request]
	 * @return com.wanmi.sbc.common.base.BaseResponse
	 **/
	@PostMapping("/goods/${application.goods.version}/flashsalegoods/sub-stock-and-sales-volume")
	BaseResponse subStockAndSalesVolume(@RequestBody @Valid FlashSaleGoodsBatchStockAndSalesVolumeRequest request);

	/**
	 * @Author minchen
	 * @Description 秒杀活动结束后商品还库存
	 * @Param [flashSaleGoodsBatchMinusStockRequest]
	 * @return com.wanmi.sbc.common.base.BaseResponse
	 **/
	@PostMapping("/goods/${application.goods.version}/flashsalegoods/activity-end-return-stock")
	BaseResponse activityEndReturnStock(@RequestBody @Valid FlashSaleGoodsQueryRequest request);

}

