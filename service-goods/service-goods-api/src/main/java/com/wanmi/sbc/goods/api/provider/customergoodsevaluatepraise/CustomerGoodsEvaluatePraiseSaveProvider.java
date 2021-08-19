package com.wanmi.sbc.goods.api.provider.customergoodsevaluatepraise;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.customergoodsevaluatepraise.CustomerGoodsEvaluatePraiseAddRequest;
import com.wanmi.sbc.goods.api.request.customergoodsevaluatepraise.CustomerGoodsEvaluatePraiseDelByIdListRequest;
import com.wanmi.sbc.goods.api.request.customergoodsevaluatepraise.CustomerGoodsEvaluatePraiseDelByIdRequest;
import com.wanmi.sbc.goods.api.request.customergoodsevaluatepraise.CustomerGoodsEvaluatePraiseModifyRequest;
import com.wanmi.sbc.goods.api.response.customergoodsevaluatepraise.CustomerGoodsEvaluatePraiseAddResponse;
import com.wanmi.sbc.goods.api.response.customergoodsevaluatepraise.CustomerGoodsEvaluatePraiseModifyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>会员商品评价点赞关联表保存服务Provider</p>
 *
 * @author lvzhenwei
 * @date 2019-05-07 14:25:25
 */
@FeignClient(value = "${application.goods.name}", contextId = "CustomerGoodsEvaluatePraiseSaveProvider")
public interface CustomerGoodsEvaluatePraiseSaveProvider {

    /**
     * 新增会员商品评价点赞关联表API
     *
     * @param customerGoodsEvaluatePraiseAddRequest 会员商品评价点赞关联表新增参数结构 {@link CustomerGoodsEvaluatePraiseAddRequest}
     * @return 新增的会员商品评价点赞关联表信息 {@link CustomerGoodsEvaluatePraiseAddResponse}
     * @author lvzhenwei
     */
    @PostMapping("/goods/${application.goods.version}/customergoodsevaluatepraise/add")
    BaseResponse<CustomerGoodsEvaluatePraiseAddResponse> add(@RequestBody @Valid CustomerGoodsEvaluatePraiseAddRequest customerGoodsEvaluatePraiseAddRequest);

    /**
     * 修改会员商品评价点赞关联表API
     *
     * @param customerGoodsEvaluatePraiseModifyRequest 会员商品评价点赞关联表修改参数结构 {@link CustomerGoodsEvaluatePraiseModifyRequest}
     * @return 修改的会员商品评价点赞关联表信息 {@link CustomerGoodsEvaluatePraiseModifyResponse}
     * @author lvzhenwei
     */
    @PostMapping("/goods/${application.goods.version}/customergoodsevaluatepraise/modify")
    BaseResponse<CustomerGoodsEvaluatePraiseModifyResponse> modify(@RequestBody @Valid CustomerGoodsEvaluatePraiseModifyRequest customerGoodsEvaluatePraiseModifyRequest);

    /**
     * 单个删除会员商品评价点赞关联表API
     *
     * @param customerGoodsEvaluatePraiseDelByIdRequest 单个删除参数结构 {@link CustomerGoodsEvaluatePraiseDelByIdRequest}
     * @return 删除结果 {@link BaseResponse}
     * @author lvzhenwei
     */
    @PostMapping("/goods/${application.goods.version}/customergoodsevaluatepraise/delete-by-id")
    BaseResponse deleteById(@RequestBody @Valid CustomerGoodsEvaluatePraiseDelByIdRequest customerGoodsEvaluatePraiseDelByIdRequest);

    /**
     * 批量删除会员商品评价点赞关联表API
     *
     * @param customerGoodsEvaluatePraiseDelByIdListRequest 批量删除参数结构 {@link CustomerGoodsEvaluatePraiseDelByIdListRequest}
     * @return 删除结果 {@link BaseResponse}
     * @author lvzhenwei
     */
    @PostMapping("/goods/${application.goods.version}/customergoodsevaluatepraise/delete-by-id-list")
    BaseResponse deleteByIdList(@RequestBody @Valid CustomerGoodsEvaluatePraiseDelByIdListRequest customerGoodsEvaluatePraiseDelByIdListRequest);

}

