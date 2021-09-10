package com.wanmi.sbc.goods;

import com.wanmi.sbc.common.annotation.MultiSubmit;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.response.storeevaluate.StoreEvaluateAddResponse;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsInfoElasticProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInfoRequest;
import com.wanmi.sbc.goods.api.provider.goodsevaluate.GoodsEvaluateQueryProvider;
import com.wanmi.sbc.goods.api.provider.goodsevaluate.GoodsEvaluateSaveProvider;
import com.wanmi.sbc.goods.api.provider.goodsevaluateimage.GoodsEvaluateImageSaveProvider;
import com.wanmi.sbc.goods.api.request.goodsevaluate.*;
import com.wanmi.sbc.goods.api.response.goodsevaluate.GoodsEvaluateAnswerResponse;
import com.wanmi.sbc.goods.api.response.goodsevaluate.GoodsEvaluateByIdResponse;
import com.wanmi.sbc.goods.api.response.goodsevaluate.GoodsEvaluatePageResponse;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Objects;

/**
 * @author liutao
 * @date 2019/2/25 3:35 PM
 */
@Api(description = "boss商品评价Api",tags = "BossGoodsEvaluateController")
@RestController
@RequestMapping("/goods/evaluate")
public class GoodsEvaluateController {

    @Autowired
    private GoodsEvaluateQueryProvider goodsEvaluateQueryProvider;

    @Autowired
    private GoodsEvaluateSaveProvider goodsEvaluateSaveProvider;

    @Autowired
    GoodsEvaluateImageSaveProvider goodsEvaluateImageSaveProvider;

    @Autowired
    private EsGoodsInfoElasticProvider esGoodsInfoElasticProvider;

    @Autowired
    private CommonUtil commonUtil;

    /**
     * @description 分页查询商品评价列表
     * @menu 评论信息
     * @param goodsEvaluatePageRequest
     * @status done
     */
    @ApiOperation(value = "分页查询商品评价列表")
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public BaseResponse<GoodsEvaluatePageResponse> page(@RequestBody @Valid GoodsEvaluatePageRequest goodsEvaluatePageRequest) {
        goodsEvaluatePageRequest.setStoreId(commonUtil.getStoreId());
        return goodsEvaluateQueryProvider.page(goodsEvaluatePageRequest);
    }

    /**
     * @description 添加书友说评价
     * @param BookFriendEvaluateAddRequest
     * @menu 评论信息
     * @status undone
     */
    @ApiOperation(value = "添加书友说评价")
    @RequestMapping(value = "/bookFriend/add", method = RequestMethod.POST)
    @MultiSubmit
    public BaseResponse<StoreEvaluateAddResponse> addBookFriendEvaluate(@RequestBody BookFriendEvaluateAddRequest bookFriendEvaluateAddRequest) {
        bookFriendEvaluateAddRequest.setStoreId(commonUtil.getStoreId());
        goodsEvaluateSaveProvider.addBookFriendEvaluate(bookFriendEvaluateAddRequest);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * @description 编辑书友说评价
     * @param BookFriendEvaluateEditRequest
     * @menu 评论信息
     * @status undone
     */
    @ApiOperation(value = "编辑书友说评价")
    @RequestMapping(value = "/bookFriend/edit", method = RequestMethod.POST)
    @MultiSubmit
    public BaseResponse<StoreEvaluateAddResponse> editBookFriendEvaluate(@RequestBody BookFriendEvaluateEditRequest bookFriendEvaluateEditRequest) {
        bookFriendEvaluateEditRequest.setStoreId(commonUtil.getStoreId());
        goodsEvaluateSaveProvider.editBookFriendEvaluate(bookFriendEvaluateEditRequest);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 获取商品评价详情
     * @param goodsEvaluateByIdRequest
     * @return
     */
    @ApiOperation(value = "获取商品评价详情信息")
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    public BaseResponse<GoodsEvaluateByIdResponse> info(@RequestBody @Valid GoodsEvaluateByIdRequest goodsEvaluateByIdRequest) {
        BaseResponse<GoodsEvaluateByIdResponse> response = goodsEvaluateQueryProvider.getById(goodsEvaluateByIdRequest);
        return  response;
    }

    /**
     * 商品评价回复
     * @param request
     * @return
     */
    @ApiOperation(value = "回复商品评价")
    @RequestMapping(value = "/answer", method = RequestMethod.POST)
    public BaseResponse<GoodsEvaluateAnswerResponse> answer(@RequestBody @Valid GoodsEvaluateAnswerRequest request) {

        request.setEvaluateAnswerAccountName(commonUtil.getAccountName());
        request.setEvaluateAnswerEmployeeId(commonUtil.getOperator().getAdminId());

        BaseResponse<GoodsEvaluateAnswerResponse> response = goodsEvaluateSaveProvider.answer(request);
        //更新es信息中的评价数
        if(Objects.nonNull(response.getContext()) && Objects.nonNull(response.getContext().getGoodsEvaluateVO())){
            esGoodsInfoElasticProvider.initEsGoodsInfo(EsGoodsInfoRequest.builder().goodsId(response.getContext().getGoodsEvaluateVO().getGoodsId()).build());
        }
        return  response;
    }


}