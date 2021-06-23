package com.wanmi.sbc.order.api.provider.follow;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.order.api.request.follow.*;
import com.wanmi.sbc.order.api.response.follow.FollowCountResponse;
import com.wanmi.sbc.order.api.response.follow.FollowHaveInvalidGoodsResponse;
import com.wanmi.sbc.order.api.response.follow.FollowListResponse;
import com.wanmi.sbc.order.api.response.follow.IsFollowResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;


/**
 * <p>商品收藏查询服务</p>
 * author: sunpeng
 * Date: 2018-12-5
 */
@FeignClient(value = "${application.order.name}", contextId = "FollowQueryProvider")
public interface FollowQueryProvider {

    /**
     *  商品收藏查询
     * @param request  {@link  FollowListRequest}
     * @return   商品收藏列表结果 {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/follow/list")
    BaseResponse<FollowListResponse> list(@RequestBody @Valid FollowListRequest request);

    /**
     * 是否有失效商品
     * @param request  {@link  HaveInvalidGoodsRequest}
     * @return   {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/follow/haveinvalidgoods")
    BaseResponse<FollowHaveInvalidGoodsResponse> haveInvalidGoods(@RequestBody @Valid HaveInvalidGoodsRequest request);

    /**
     * 验证SKU是否已收藏
     * @param request  {@link IsFollowRequest}
     * @return  已收藏的SkuId  {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/follow/isfollow")
    BaseResponse<IsFollowResponse> isFollow(@RequestBody @Valid IsFollowRequest request);

    /**
     * 统计收藏表
     * @param request  {@link FollowCountRequest }
     * @return   {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/follow/count")
    BaseResponse<FollowCountResponse> count(@RequestBody @Valid FollowCountRequest request);

}
