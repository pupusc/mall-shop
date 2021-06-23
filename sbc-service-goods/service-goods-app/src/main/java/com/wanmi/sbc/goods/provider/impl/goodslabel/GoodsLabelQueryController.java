package com.wanmi.sbc.goods.provider.impl.goodslabel;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.goodslabel.GoodsLabelQueryProvider;
import com.wanmi.sbc.goods.api.request.goodslabel.GoodsLabelByIdRequest;
import com.wanmi.sbc.goods.api.request.goodslabel.GoodsLabelListRequest;
import com.wanmi.sbc.goods.api.request.goodslabel.GoodsLabelPageRequest;
import com.wanmi.sbc.goods.api.request.goodslabel.GoodsLabelQueryRequest;
import com.wanmi.sbc.goods.api.response.goodslabel.GoodsLabelByIdResponse;
import com.wanmi.sbc.goods.api.response.goodslabel.GoodsLabelCacheListResponse;
import com.wanmi.sbc.goods.api.response.goodslabel.GoodsLabelListResponse;
import com.wanmi.sbc.goods.api.response.goodslabel.GoodsLabelPageResponse;
import com.wanmi.sbc.goods.bean.vo.GoodsLabelVO;
import com.wanmi.sbc.goods.goodslabel.model.root.GoodsLabel;
import com.wanmi.sbc.goods.goodslabel.service.GoodsLabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>商品标签查询服务接口实现</p>
 *
 * @author dyt
 * @date 2020-09-29 13:57:19
 */
@RestController
@Validated
public class GoodsLabelQueryController implements GoodsLabelQueryProvider {
    @Autowired
    private GoodsLabelService goodsLabelService;

    @Override
    public BaseResponse<GoodsLabelPageResponse> page(@RequestBody @Valid GoodsLabelPageRequest goodsLabelPageReq) {
        GoodsLabelQueryRequest queryReq = new GoodsLabelQueryRequest();
        KsBeanUtil.copyPropertiesThird(goodsLabelPageReq, queryReq);
        Page<GoodsLabel> goodsLabelPage = goodsLabelService.page(queryReq);
        Page<GoodsLabelVO> newPage = goodsLabelPage.map(entity -> goodsLabelService.wrapperVo(entity));
        MicroServicePage<GoodsLabelVO> microPage = new MicroServicePage<>(newPage, goodsLabelPageReq.getPageable());
        GoodsLabelPageResponse finalRes = new GoodsLabelPageResponse(microPage);
        return BaseResponse.success(finalRes);
    }

    @Override
    public BaseResponse<GoodsLabelListResponse> list(@RequestBody @Valid GoodsLabelListRequest goodsLabelListReq) {
        GoodsLabelQueryRequest queryReq = new GoodsLabelQueryRequest();
        KsBeanUtil.copyPropertiesThird(goodsLabelListReq, queryReq);
        List<GoodsLabel> goodsLabelList = goodsLabelService.list(queryReq);
        List<GoodsLabelVO> newList =
				goodsLabelList.stream().map(entity -> goodsLabelService.wrapperVo(entity)).collect(Collectors.toList());
        return BaseResponse.success(new GoodsLabelListResponse(newList));
    }

    @Override
    public BaseResponse<GoodsLabelCacheListResponse> cacheList(){
        List<GoodsLabel> goodsLabelList = goodsLabelService.listWithCache();
        List<GoodsLabelVO> newList =
                goodsLabelList.stream().map(entity -> goodsLabelService.wrapperVo(entity)).collect(Collectors.toList());
        return BaseResponse.success(new GoodsLabelCacheListResponse(newList));
    }

    @Override
    public BaseResponse<GoodsLabelByIdResponse> getById(@RequestBody @Valid GoodsLabelByIdRequest goodsLabelByIdRequest) {
        GoodsLabel goodsLabel = goodsLabelService.getById(goodsLabelByIdRequest.getGoodsLabelId());
        return BaseResponse.success(new GoodsLabelByIdResponse(goodsLabelService.wrapperVo(goodsLabel)));
    }
}

