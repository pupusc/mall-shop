package com.wanmi.sbc.goods.provider.impl.brand;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.provider.brand.ContractBrandProvider;
import com.wanmi.sbc.goods.api.request.brand.ContractBrandTransferByStoreIdRequest;
import com.wanmi.sbc.goods.brand.service.ContractBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>对签约品牌操作接口</p>
 * Created by daiyitian on 2018-10-31-下午6:23.
 */
@RestController
@Validated
public class ContractBrandController implements ContractBrandProvider {

    @Autowired
    private ContractBrandService contractBrandService;

    /**
     * 根据店铺id迁移签约品牌
     *
     * @param request 包含店铺id的数据结构 {@link ContractBrandTransferByStoreIdRequest}
     * @return 操作结构 {@link BaseResponse}
     */
    @Override

    public BaseResponse transferByStoreId(@RequestBody @Valid ContractBrandTransferByStoreIdRequest request){
        contractBrandService.transfer(request.getStoreId());
        return BaseResponse.SUCCESSFUL();
    }


}
