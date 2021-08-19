package com.wanmi.sbc.goods.api.provider.customergoodsevaluatepraise;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.customergoodsevaluatepraise.CustomerGoodsEvaluatePraiseByIdRequest;
import com.wanmi.sbc.goods.api.request.customergoodsevaluatepraise.CustomerGoodsEvaluatePraiseListRequest;
import com.wanmi.sbc.goods.api.request.customergoodsevaluatepraise.CustomerGoodsEvaluatePraisePageRequest;
import com.wanmi.sbc.goods.api.request.customergoodsevaluatepraise.CustomerGoodsEvaluatePraiseQueryRequest;
import com.wanmi.sbc.goods.api.response.customergoodsevaluatepraise.CustomerGoodsEvaluatePraiseByIdResponse;
import com.wanmi.sbc.goods.api.response.customergoodsevaluatepraise.CustomerGoodsEvaluatePraiseListResponse;
import com.wanmi.sbc.goods.api.response.customergoodsevaluatepraise.CustomerGoodsEvaluatePraisePageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>会员商品评价点赞关联表查询服务Provider</p>
 *
 * @author lvzhenwei
 * @date 2019-05-07 14:25:25
 */
@FeignClient(value = "${application.goods.name}", contextId = "CustomerGoodsEvaluatePraiseQueryProvider")
public interface CustomerGoodsEvaluatePraiseQueryProvider {

    /**
     * 分页查询会员商品评价点赞关联表API
     *
     * @param customerGoodsEvaluatePraisePageReq 分页请求参数和筛选对象 {@link CustomerGoodsEvaluatePraisePageRequest}
     * @return 会员商品评价点赞关联表分页列表信息 {@link CustomerGoodsEvaluatePraisePageResponse}
     * @author lvzhenwei
     */
    @PostMapping("/goods/${application.goods.version}/customergoodsevaluatepraise/page")
    BaseResponse<CustomerGoodsEvaluatePraisePageResponse> page(@RequestBody @Valid CustomerGoodsEvaluatePraisePageRequest customerGoodsEvaluatePraisePageReq);

    /**
     * 列表查询会员商品评价点赞关联表API
     *
     * @param customerGoodsEvaluatePraiseListReq 列表请求参数和筛选对象 {@link CustomerGoodsEvaluatePraiseListRequest}
     * @return 会员商品评价点赞关联表的列表信息 {@link CustomerGoodsEvaluatePraiseListResponse}
     * @author lvzhenwei
     */
    @PostMapping("/goods/${application.goods.version}/customergoodsevaluatepraise/list")
    BaseResponse<CustomerGoodsEvaluatePraiseListResponse> list(@RequestBody @Valid CustomerGoodsEvaluatePraiseListRequest customerGoodsEvaluatePraiseListReq);

    /**
     * 单个查询会员商品评价点赞关联表API
     *
     * @param customerGoodsEvaluatePraiseByIdRequest 单个查询会员商品评价点赞关联表请求参数 {@link CustomerGoodsEvaluatePraiseByIdRequest}
     * @return 会员商品评价点赞关联表详情 {@link CustomerGoodsEvaluatePraiseByIdResponse}
     * @author lvzhenwei
     */
    @PostMapping("/goods/${application.goods.version}/customergoodsevaluatepraise/get-by-id")
    BaseResponse<CustomerGoodsEvaluatePraiseByIdResponse> getById(@RequestBody @Valid CustomerGoodsEvaluatePraiseByIdRequest customerGoodsEvaluatePraiseByIdRequest);

    /**
     * 根据查询条件查询单个会员商品评价点赞关联表信息
     *
     * @param customerGoodsEvaluatePraiseQueryRequest 单个查询会员商品评价点赞关联表请求参数 {@link CustomerGoodsEvaluatePraiseByIdRequest}
     * @return 会员商品评价点赞关联表详情 {@link CustomerGoodsEvaluatePraiseByIdResponse}
     * @author lvzhenwei
     */
    @PostMapping("/goods/${application.goods.version}/customergoodsevaluatepraise/get-customer-goods-evaluate-praise")
    BaseResponse<CustomerGoodsEvaluatePraiseByIdResponse> getCustomerGoodsEvaluatePraise(@RequestBody @Valid CustomerGoodsEvaluatePraiseQueryRequest customerGoodsEvaluatePraiseQueryRequest);

}

