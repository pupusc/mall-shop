package com.wanmi.sbc.linkedmall.api.provider.cate;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.linkedmall.api.request.cate.CateByIdRequest;
import com.wanmi.sbc.linkedmall.api.request.cate.CateChainByGoodsIdRequest;
import com.wanmi.sbc.linkedmall.api.response.cate.CategoryChainByGoodsIdResponse;
import com.wanmi.sbc.linkedmall.api.response.cate.LinkedMallCateGetResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "${application.linkedmall.name}" ,contextId = "LinkedMallCateQueryProvider")
public interface LinkedMallCateQueryProvider {
    /**
     * 查询业务库全量类目
     * @return
     */
    @PostMapping("/linkedmall/${application.linkedmall.version}/cate/get-all-linkedmall-cate")
    BaseResponse<LinkedMallCateGetResponse> getAllLinkedMallCate();

    /**
     * 根据类目id查询类目信息
     * @return
     */
    @PostMapping("/linkedmall/${application.linkedmall.version}/cate/get-linkedmallcate-By-id")
    BaseResponse<LinkedMallCateGetResponse> getLinkedMallCateById(@RequestBody CateByIdRequest cateByIdRequest);

    /**
     * 根据goodsId查询类目链
     * @return
     */
    @PostMapping("/linkedmall/${application.linkedmall.version}/cate/get-gategory-chain-by-goodsId")
    BaseResponse<CategoryChainByGoodsIdResponse> getCategoryChainByGoodsId(@RequestBody CateChainByGoodsIdRequest cateChainByGoodsIdRequest);
}
