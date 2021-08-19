package com.wanmi.sbc.elastic.provider.impl.goods;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.elastic.api.provider.goods.EsCateBrandProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsBrandUpdateRequest;
import com.wanmi.sbc.elastic.api.request.goods.EsCateDeleteRequest;
import com.wanmi.sbc.elastic.api.request.goods.EsCateUpdateNameRequest;
import com.wanmi.sbc.elastic.goods.service.EsCateBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
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
public class EsCateBrandController implements EsCateBrandProvider {

    @Autowired
    private EsCateBrandService esCateBrandService;

    @Override
    public BaseResponse updateToEs(@RequestBody @Valid EsCateUpdateNameRequest request) {
        esCateBrandService.updateToEs(request.getGoodsCateListVOList());
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse deleteCateFromEs(@RequestBody @Valid EsCateDeleteRequest request) {
        esCateBrandService.deleteCateFromEs(request);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse updateBrandFromEs(@RequestBody @Valid EsBrandUpdateRequest request) {
        esCateBrandService.updateBrandFromEs(request.isDelete(), request.getGoodsBrand());
        return BaseResponse.SUCCESSFUL();
    }
}
