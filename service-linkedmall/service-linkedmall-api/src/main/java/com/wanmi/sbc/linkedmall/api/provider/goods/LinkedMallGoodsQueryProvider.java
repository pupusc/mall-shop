package com.wanmi.sbc.linkedmall.api.provider.goods;

import com.aliyuncs.linkedmall.model.v20180116.QueryBizItemListResponse;
import com.aliyuncs.linkedmall.model.v20180116.QueryItemDetailResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.linkedmall.api.request.goods.GoodsDetailByIdRequest;
import com.wanmi.sbc.linkedmall.api.request.goods.GoodsDetailQueryRequest;
import com.wanmi.sbc.linkedmall.api.request.goods.LinkedMallGoodsPageRequest;
import com.wanmi.sbc.linkedmall.api.response.goods.LinkedMallGoodsPageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

@FeignClient(value = "${application.linkedmall.name}" ,contextId = "LinkedMallGoodsQueryProvider")
public interface LinkedMallGoodsQueryProvider {
    /**
     * 分页查询linkedmall商品
     * @return
     */
    @PostMapping("linkedmall/${application.linkedmall.version}/goods/page")
    BaseResponse<LinkedMallGoodsPageResponse<QueryBizItemListResponse.Item>> getLinkedMallGoodsPage(@RequestBody @Valid LinkedMallGoodsPageRequest linkedMallGoodsPageRequest);

    /**
     * 根据goodsId查询商品详细信息
     * @return
     */
    @PostMapping("linkedmall/${application.linkedmall.version}/goods/detail/get-by-id")
    BaseResponse<QueryItemDetailResponse.Item> getGoodsDetailById(@RequestBody GoodsDetailByIdRequest goodsDetailByIdRequest);
    /**
     * 批量查询商品详细信息
     * @return
     */
    @PostMapping("linkedmall/${application.linkedmall.version}/goods/detail/batch")
    BaseResponse<List<QueryItemDetailResponse.Item>> getGoodsDetailBatch(@RequestBody @Valid GoodsDetailQueryRequest request);
}
