package com.wanmi.sbc.goods.api.provider.goodssharerecord;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.goodssharerecord.GoodsShareRecordAddRequest;
import com.wanmi.sbc.goods.api.response.goodssharerecord.GoodsShareRecordAddResponse;
import com.wanmi.sbc.goods.api.request.goodssharerecord.GoodsShareRecordModifyRequest;
import com.wanmi.sbc.goods.api.response.goodssharerecord.GoodsShareRecordModifyResponse;
import com.wanmi.sbc.goods.api.request.goodssharerecord.GoodsShareRecordDelByIdRequest;
import com.wanmi.sbc.goods.api.request.goodssharerecord.GoodsShareRecordDelByIdListRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>商品分享保存服务Provider</p>
 * @author zhangwenchang
 * @date 2020-03-06 13:46:24
 */
@FeignClient(value = "${application.goods.name}", contextId = "GoodsShareRecordSaveProvider")
public interface GoodsShareRecordSaveProvider {

	/**
	 * 新增商品分享API
	 *
	 * @author zhangwenchang
	 * @param goodsShareRecordAddRequest 商品分享新增参数结构 {@link GoodsShareRecordAddRequest}
	 * @return 新增的商品分享信息 {@link GoodsShareRecordAddResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/goodssharerecord/add")
	BaseResponse<GoodsShareRecordAddResponse> add(@RequestBody @Valid GoodsShareRecordAddRequest goodsShareRecordAddRequest);

	/**
	 * 修改商品分享API
	 *
	 * @author zhangwenchang
	 * @param goodsShareRecordModifyRequest 商品分享修改参数结构 {@link GoodsShareRecordModifyRequest}
	 * @return 修改的商品分享信息 {@link GoodsShareRecordModifyResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/goodssharerecord/modify")
	BaseResponse<GoodsShareRecordModifyResponse> modify(@RequestBody @Valid GoodsShareRecordModifyRequest goodsShareRecordModifyRequest);

	/**
	 * 单个删除商品分享API
	 *
	 * @author zhangwenchang
	 * @param goodsShareRecordDelByIdRequest 单个删除参数结构 {@link GoodsShareRecordDelByIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/goodssharerecord/delete-by-id")
	BaseResponse deleteById(@RequestBody @Valid GoodsShareRecordDelByIdRequest goodsShareRecordDelByIdRequest);

	/**
	 * 批量删除商品分享API
	 *
	 * @author zhangwenchang
	 * @param goodsShareRecordDelByIdListRequest 批量删除参数结构 {@link GoodsShareRecordDelByIdListRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/goodssharerecord/delete-by-id-list")
	BaseResponse deleteByIdList(@RequestBody @Valid GoodsShareRecordDelByIdListRequest goodsShareRecordDelByIdListRequest);

}

