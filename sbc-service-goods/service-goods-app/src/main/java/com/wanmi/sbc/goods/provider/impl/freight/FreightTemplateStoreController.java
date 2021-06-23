package com.wanmi.sbc.goods.provider.impl.freight;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.provider.freight.FreightTemplateStoreProvider;
import com.wanmi.sbc.goods.api.request.freight.FreightTemplateStoreDeleteByIdAndStoreIdRequest;
import com.wanmi.sbc.goods.api.request.freight.FreightTemplateStoreSaveRequest;
import com.wanmi.sbc.goods.freight.service.FreightTemplateStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>对店铺运费模板操作接口</p>
 * Created by daiyitian on 2018-11-1-下午6:23.
 */
@RestController
@Validated
public class FreightTemplateStoreController implements FreightTemplateStoreProvider {

    @Autowired
    private FreightTemplateStoreService freightTemplateStoreService;
    
    /**
     * 新增或更新店铺运费模板
     *
     * @param request 店铺运费模板保存请求结构 {@link FreightTemplateStoreSaveRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @Override

    public BaseResponse save(@RequestBody @Valid FreightTemplateStoreSaveRequest request){
        freightTemplateStoreService.renewalFreightTemplateStore(request);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 根据运费模板id和店铺id删除店铺运费模板
     *
     * @param request 包含运费模板id和店铺id的删除请求结构 {@link FreightTemplateStoreDeleteByIdAndStoreIdRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @Override

    public BaseResponse deleteByIdAndStoreId(@RequestBody @Valid FreightTemplateStoreDeleteByIdAndStoreIdRequest
                                                         request){
        freightTemplateStoreService.delById(request.getFreightTempId(), request.getStoreId());
        return BaseResponse.SUCCESSFUL();
    }

}
