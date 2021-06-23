package com.wanmi.sbc.elastic.provider.impl.goods;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsLabelProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsLabelDeleteByIdsRequest;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsLabelUpdateNameRequest;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsLabelUpdateSortRequest;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsLabelUpdateVisibleRequest;
import com.wanmi.sbc.elastic.goods.service.EsGoodsLabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @Author: songhanlin
 * @Date: Created In 18:19 2020/11/30
 * @Description: TODO
 */
@RestController
@Validated
public class EsGoodsLabelController implements EsGoodsLabelProvider {

    @Autowired
    private EsGoodsLabelService esGoodsLabelService;

    @Override
    public BaseResponse updateLabelName(@RequestBody @Valid EsGoodsLabelUpdateNameRequest request) {
        esGoodsLabelService.updateLabelName(request.getGoodsLabelVO());
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse updateLabelVisible(@RequestBody @Valid EsGoodsLabelUpdateVisibleRequest request) {
        esGoodsLabelService.updateLabelVisible(request.getGoodsLabelVO());
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse updateGoodsLabelSort(@RequestBody @Valid EsGoodsLabelUpdateSortRequest request) {
        esGoodsLabelService.updateGoodsLabelSort(request.getGoodsLabelVOList());
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse deleteSomeLabel(@RequestBody @Valid EsGoodsLabelDeleteByIdsRequest request) {
        esGoodsLabelService.deleteSomeLabel(request.getIds());
        return BaseResponse.SUCCESSFUL();
    }
}
