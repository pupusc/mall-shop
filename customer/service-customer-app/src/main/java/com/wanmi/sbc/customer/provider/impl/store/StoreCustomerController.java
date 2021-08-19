package com.wanmi.sbc.customer.provider.impl.store;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.store.StoreCustomerProvider;
import com.wanmi.sbc.customer.api.request.store.StoreCustomerRelaAddRequest;
import com.wanmi.sbc.customer.api.request.store.StoreCustomerRelaDeleteRequest;
import com.wanmi.sbc.customer.api.request.store.StoreCustomerRelaUpdateRequest;
import com.wanmi.sbc.customer.api.response.store.StoreCustomerRelaResponse;
import com.wanmi.sbc.customer.bean.dto.StoreCustomerRelaDTO;
import com.wanmi.sbc.customer.storecustomer.root.StoreCustomerRela;
import com.wanmi.sbc.customer.storecustomer.service.StoreCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Objects;

@RestController
@Validated
public class StoreCustomerController implements StoreCustomerProvider {
    @Autowired
    private StoreCustomerService storeCustomerService;

    /**
     * 修改平台客户，只能修改等级
     *
     * {@link StoreCustomerService#updateByCustomerId}
     */
    @Override

    public BaseResponse<StoreCustomerRelaResponse> modifyByCustomerId(@RequestBody @Valid StoreCustomerRelaUpdateRequest
                                                                                  storeCustomerRelaUpdateRequest) {
        StoreCustomerRela storeCustomerRela = new StoreCustomerRela();
        KsBeanUtil.copyPropertiesThird(storeCustomerRelaUpdateRequest.getStoreCustomerRelaDTO(), storeCustomerRela);

        StoreCustomerRela res = storeCustomerService.updateByCustomerId(storeCustomerRelaUpdateRequest.getCustomerId(),
                storeCustomerRela, storeCustomerRelaUpdateRequest.getEmployeeId());

        StoreCustomerRelaResponse response = new StoreCustomerRelaResponse();

        KsBeanUtil.copyPropertiesThird(res, response);

        return BaseResponse.success(response);
    }

    /**
     * 添加平台客户
     *
     * {@link StoreCustomerService#addPlatformRelated}
     */
    @Override

    public BaseResponse<StoreCustomerRelaResponse> addPlatformRelated(@RequestBody @Valid StoreCustomerRelaAddRequest storeCustomerRelaAddRequest) {
        StoreCustomerRela storeCustomerRela = new StoreCustomerRela();

        KsBeanUtil.copyPropertiesThird(storeCustomerRelaAddRequest.getStoreCustomerRelaDTO(), storeCustomerRela);

        StoreCustomerRela res = storeCustomerService.addPlatformRelated(storeCustomerRela);

        StoreCustomerRelaResponse response = new StoreCustomerRelaResponse();

        KsBeanUtil.copyPropertiesThird(res, response);

        return BaseResponse.success(response);
    }

    /**
     * 删除平台客户关系
     *
     * {@link StoreCustomerService#deletePlatformRelated}
     */
    @Override

    public BaseResponse deletePlatformRelated(@RequestBody @Valid StoreCustomerRelaDeleteRequest storeCustomerRelaDeleteRequest) {
        StoreCustomerRela storeCustomerRela = new StoreCustomerRela();

        KsBeanUtil.copyPropertiesThird(storeCustomerRelaDeleteRequest.getStoreCustomerRelaDTO(), storeCustomerRela);

        storeCustomerService.deletePlatformRelated(storeCustomerRela);

        return BaseResponse.SUCCESSFUL();
    }


    /**
     * 客户下单满足条件升级，更新店铺客户关系
     */
    @Override
    @Transactional
    public BaseResponse updateStoreCustomerRela(@RequestBody @Valid StoreCustomerRelaUpdateRequest storeCustomerRelaUpdateRequest) {
        StoreCustomerRelaDTO dto = storeCustomerRelaUpdateRequest.getStoreCustomerRelaDTO();
        StoreCustomerRela related = storeCustomerService.findCustomerRelatedForAll(dto.getCustomerId(),
                dto.getCompanyInfoId());
        // 查不到关联关系则新增，只有升级后的等级大于当前等级才修改
        if (Objects.isNull(related)) {
            StoreCustomerRela storeCustomerRela = new StoreCustomerRela();
            KsBeanUtil.copyPropertiesThird(storeCustomerRelaUpdateRequest.getStoreCustomerRelaDTO(), storeCustomerRela);
            storeCustomerService.updateStoreCustomerRela(storeCustomerRela);
        } else if (dto.getStoreLevelId().longValue() > related.getStoreLevelId().longValue()) {
            related.setStoreLevelId(dto.getStoreLevelId());
            storeCustomerService.updateStoreCustomerRela(related);
        }
        return BaseResponse.SUCCESSFUL();
    }

}
