package com.wanmi.sbc.goods.provider.impl.standard;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.provider.standard.StandardGoodsProvider;
import com.wanmi.sbc.goods.api.request.standard.*;
import com.wanmi.sbc.goods.api.response.standard.StandardGoodsDeleteProviderGoodsIdsResponse;
import com.wanmi.sbc.goods.api.response.standard.StandardGoodsListUsedGoodsIdResponse;
import com.wanmi.sbc.goods.standard.request.StandardSaveRequest;
import com.wanmi.sbc.goods.standard.service.StandardGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2018-11-08 15:27
 */
@Validated
@RestController
public class StandardGoodsController implements StandardGoodsProvider {

    @Autowired
    private StandardGoodsService standardGoodsService;

    /**
     * @param request 商品库新增 {@link StandardGoodsAddRequest}
     * @return
     */
    @Override
    
    public BaseResponse<String> add(@RequestBody @Valid StandardGoodsAddRequest request) {

        StandardSaveRequest standardSaveRequest = StandardConvert.convertAddRequest2saveRequest(request);

        return BaseResponse.success(standardGoodsService.add(standardSaveRequest));
    }

    /**
     * @param request 商品库更新 {@link StandardGoodsModifyRequest}
     * @return
     */
    @Override
    
    public BaseResponse modify(@RequestBody @Valid StandardGoodsModifyRequest request) {

        StandardSaveRequest standardSaveRequest = StandardConvert.convertModifyRequest2saveRequest(request);

        return BaseResponse.success(standardGoodsService.edit(standardSaveRequest));
    }

    /**
     * @param request 商品库删除 {@link StandardGoodsDeleteByGoodsIdsRequest}
     * @return
     */
    @Override
    
    public BaseResponse delete(@RequestBody @Valid StandardGoodsDeleteByGoodsIdsRequest request) {
        standardGoodsService.delete(request.getGoodsIds());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * @param request 供货商品库删除 {@link StandardGoodsDeleteByGoodsIdsRequest}
     * @return
     */
    @Override

    public BaseResponse deleteProvider(@RequestBody @Valid StandardGoodsDeleteProviderByGoodsIdsRequest request) {
        standardGoodsService.deleteProvider(request.getGoodsIds());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * @param request 供货商品库删除 并添加 删除原因 {@link StandardGoodsDeleteByGoodsIdsRequest}
     * @return
     */
    @Override
    public BaseResponse<StandardGoodsDeleteProviderGoodsIdsResponse> deleteProviderAddReason(@RequestBody @Valid StandardGoodsDeleteProviderByGoodsIdsRequest request) {
        List<String> stringList = standardGoodsService.deleteProviderAddReason(request.getGoodsIds(), request.getDeleteReason());
        return BaseResponse.success(new StandardGoodsDeleteProviderGoodsIdsResponse(stringList));
    }
}
