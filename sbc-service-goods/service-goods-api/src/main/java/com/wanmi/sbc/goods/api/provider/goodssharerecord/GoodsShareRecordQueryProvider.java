package com.wanmi.sbc.goods.api.provider.goodssharerecord;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.goodssharerecord.GoodsShareRecordByIdRequest;
import com.wanmi.sbc.goods.api.request.goodssharerecord.GoodsShareRecordListRequest;
import com.wanmi.sbc.goods.api.request.goodssharerecord.GoodsShareRecordPageRequest;
import com.wanmi.sbc.goods.api.response.goodssharerecord.GoodsShareRecordByIdResponse;
import com.wanmi.sbc.goods.api.response.goodssharerecord.GoodsShareRecordListResponse;
import com.wanmi.sbc.goods.api.response.goodssharerecord.GoodsShareRecordPageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>商品分享查询服务Provider</p>
 *
 * @author zhangwenchang
 * @date 2020-03-06 13:46:24
 */
@FeignClient(value = "${application.goods.name}", contextId = "GoodsShareRecordQueryProvider")
public interface GoodsShareRecordQueryProvider {

    /**
     * 分页查询商品分享API
     *
     * @param goodsShareRecordPageReq 分页请求参数和筛选对象 {@link GoodsShareRecordPageRequest}
     * @return 商品分享分页列表信息 {@link GoodsShareRecordPageResponse}
     * @author zhangwenchang
     */
    @PostMapping("/goods/${application.goods.version}/goodssharerecord/page")
    BaseResponse<GoodsShareRecordPageResponse> page(@RequestBody @Valid GoodsShareRecordPageRequest goodsShareRecordPageReq);

    /**
     * 列表查询商品分享API
     *
     * @param goodsShareRecordListReq 列表请求参数和筛选对象 {@link GoodsShareRecordListRequest}
     * @return 商品分享的列表信息 {@link GoodsShareRecordListResponse}
     * @author zhangwenchang
     */
    @PostMapping("/goods/${application.goods.version}/goodssharerecord/list")
    BaseResponse<GoodsShareRecordListResponse> list(@RequestBody @Valid GoodsShareRecordListRequest goodsShareRecordListReq);

    /**
     * 单个查询商品分享API
     *
     * @param goodsShareRecordByIdRequest 单个查询商品分享请求参数 {@link GoodsShareRecordByIdRequest}
     * @return 商品分享详情 {@link GoodsShareRecordByIdResponse}
     * @author zhangwenchang
     */
    @PostMapping("/goods/${application.goods.version}/goodssharerecord/get-by-id")
    BaseResponse<GoodsShareRecordByIdResponse> getById(@RequestBody @Valid GoodsShareRecordByIdRequest goodsShareRecordByIdRequest);

}

