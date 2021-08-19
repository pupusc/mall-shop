package com.wanmi.sbc.goods.provider.impl.presellsale;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.presellsale.PresellSaleGoodsQueryProvider;
import com.wanmi.sbc.goods.api.request.presellsale.PresellSaleGoodsByGoodsInfoRequest;
import com.wanmi.sbc.goods.api.request.presellsale.PresellSaleGoodsByIdRequest;
import com.wanmi.sbc.goods.api.response.presellsale.PresellSaleGoodsResponse;
import com.wanmi.sbc.goods.bean.vo.PresellSaleGoodsVO;
import com.wanmi.sbc.goods.presellsale.service.PresellSaleGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class PresellSaleGoodsQueryController implements PresellSaleGoodsQueryProvider {

    @Autowired
    private PresellSaleGoodsService presellSaleGoodsService;
    /**
     * @param request 包含预售活动关联商品id查询请求结构 {@link PresellSaleGoodsByIdRequest}
     * @return
     */
    @Override
    public BaseResponse<PresellSaleGoodsResponse> presellSaleGoodsById(@Valid PresellSaleGoodsByIdRequest request) {
        PresellSaleGoodsVO presellSaleGoodsVO = presellSaleGoodsService.findPresellSaleGoodsById(request.getPresellSaleGoodsId());
        PresellSaleGoodsResponse convert = KsBeanUtil.convert(presellSaleGoodsVO, PresellSaleGoodsResponse.class);
        return BaseResponse.success(convert);
    }

    @Override
    public BaseResponse<PresellSaleGoodsResponse> presellSaleGoodsByGoodsInfoId(PresellSaleGoodsByGoodsInfoRequest request) {
        PresellSaleGoodsVO presellSaleGoodsVO = presellSaleGoodsService.findPresellSaleGoodsByPresellSaleIdAndGoodsInfoId(request.getPresellSaleId(), request.getGoodsInfoId());
        PresellSaleGoodsResponse convert = KsBeanUtil.convert(presellSaleGoodsVO, PresellSaleGoodsResponse.class);
        return BaseResponse.success(convert);
    }
}
