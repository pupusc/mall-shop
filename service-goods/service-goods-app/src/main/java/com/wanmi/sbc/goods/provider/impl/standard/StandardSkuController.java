package com.wanmi.sbc.goods.provider.impl.standard;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.standard.StandardSkuProvider;
import com.wanmi.sbc.goods.api.request.standard.StandardSkuModifyRequest;
import com.wanmi.sbc.goods.api.response.standard.StandardSkuModifyResponse;
import com.wanmi.sbc.goods.standard.request.StandardSkuSaveRequest;
import com.wanmi.sbc.goods.standard.service.StandardSkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>对商品库操作接口</p>
 * Created by daiyitian on 2018-11-5-下午6:23.
 */
@RestController
@Validated
public class StandardSkuController implements StandardSkuProvider {

    @Autowired
    private StandardSkuService standardSkuService;

    /**
     * 修改商品库信息
     *
     * @param request 商品库信息修改结构 {@link StandardSkuModifyRequest}
     * @return 商品库信息 {@link StandardSkuModifyResponse}
     */
    @Override
    
    public BaseResponse<StandardSkuModifyResponse> modify(@RequestBody @Valid StandardSkuModifyRequest request){
        StandardSkuSaveRequest saveRequest =  KsBeanUtil.convert(request, StandardSkuSaveRequest.class);
        StandardSkuModifyResponse response = new StandardSkuModifyResponse();
        KsBeanUtil.copyPropertiesThird(standardSkuService.edit(saveRequest), response);
        return BaseResponse.success(response);
    }

}
