package com.wanmi.sbc.goods.provider.impl.goodslabel;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.goodslabel.GoodsLabelSaveProvider;
import com.wanmi.sbc.goods.api.request.goodslabel.*;
import com.wanmi.sbc.goods.api.response.goodslabel.GoodsLabelAddResponse;
import com.wanmi.sbc.goods.api.response.goodslabel.GoodsLabelModifyResponse;
import com.wanmi.sbc.goods.api.response.goodslabel.GoodsLabelModifySortResponse;
import com.wanmi.sbc.goods.goodslabel.model.root.GoodsLabel;
import com.wanmi.sbc.goods.goodslabel.service.GoodsLabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>商品标签保存服务接口实现</p>
 *
 * @author dyt
 * @date 2020-09-29 13:57:19
 */
@RestController
@Validated
public class GoodsLabelSaveController implements GoodsLabelSaveProvider {
    @Autowired
    private GoodsLabelService goodsLabelService;

    @Override
    public BaseResponse<GoodsLabelAddResponse> add(@RequestBody @Valid GoodsLabelAddRequest goodsLabelAddRequest) {
        GoodsLabel goodsLabel = new GoodsLabel();
        KsBeanUtil.copyPropertiesThird(goodsLabelAddRequest, goodsLabel);
        return BaseResponse.success(new GoodsLabelAddResponse(
                goodsLabelService.wrapperVo(goodsLabelService.add(goodsLabel))));
    }

    @Override
    public BaseResponse<GoodsLabelModifyResponse> modify(@RequestBody @Valid GoodsLabelModifyRequest goodsLabelModifyRequest) {
        GoodsLabel goodsLabel = new GoodsLabel();
        KsBeanUtil.copyPropertiesThird(goodsLabelModifyRequest, goodsLabel);
        return BaseResponse.success(new GoodsLabelModifyResponse(
                goodsLabelService.wrapperVo(goodsLabelService.modify(goodsLabel))));
    }

    @Override
    public BaseResponse modifyVisible(@RequestBody @Valid GoodsLabelModifyVisibleRequest request) {
        goodsLabelService.modifyVisible(request);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse deleteById(@RequestBody @Valid GoodsLabelDelByIdRequest goodsLabelDelByIdRequest) {
        goodsLabelService.deleteById(goodsLabelDelByIdRequest.getGoodsLabelId(), goodsLabelDelByIdRequest.getStoreId());
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse deleteByIdList(@RequestBody @Valid GoodsLabelDelByIdListRequest goodsLabelDelByIdListRequest) {
        goodsLabelService.deleteByIdList(goodsLabelDelByIdListRequest.getGoodsLabelIdList(),
                goodsLabelDelByIdListRequest.getStoreId());
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse<GoodsLabelModifySortResponse> editSort(@RequestBody @Valid GoodsLabelSortRequest goodsLabelSortRequest) {
        return BaseResponse.success(goodsLabelService.editSort(goodsLabelSortRequest));
    }

}

