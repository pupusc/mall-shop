package com.wanmi.sbc.customer.provider.impl.storelevel;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.storelevel.StoreLevelSaveProvider;
import com.wanmi.sbc.customer.api.request.storelevel.*;
import com.wanmi.sbc.customer.api.response.storelevel.StoreLevelAddResponse;
import com.wanmi.sbc.customer.api.response.storelevel.StoreLevelModifyResponse;
import com.wanmi.sbc.customer.bean.vo.StoreLevelVO;
import com.wanmi.sbc.customer.storelevel.model.root.StoreLevel;
import com.wanmi.sbc.customer.storelevel.service.StoreLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>商户客户等级表保存服务接口实现</p>
 *
 * @author yang
 * @date 2019-02-27 19:51:30
 */
@RestController
@Validated
public class StoreLevelSaveController implements StoreLevelSaveProvider {
    @Autowired
    private StoreLevelService storeLevelService;

    @Override
    public BaseResponse<StoreLevelAddResponse> add(@RequestBody @Valid StoreLevelAddRequest storeLevelAddRequest) {
        StoreLevel storeLevel = new StoreLevel();
        KsBeanUtil.copyPropertiesThird(storeLevelAddRequest, storeLevel);
        return BaseResponse.success(new StoreLevelAddResponse(
                storeLevelService.wrapperVo(storeLevelService.add(storeLevel))));
    }

    @Override
    public BaseResponse<StoreLevelModifyResponse> modify(@RequestBody @Valid StoreLevelModifyRequest storeLevelModifyRequest) {
        StoreLevel storeLevel = storeLevelService.getById(storeLevelModifyRequest.getStoreLevelId());
        storeLevel.setLevelName(storeLevelModifyRequest.getLevelName());
        storeLevel.setDiscountRate(storeLevelModifyRequest.getDiscountRate());
        storeLevel.setAmountConditions(storeLevelModifyRequest.getAmountConditions());
        storeLevel.setOrderConditions(storeLevelModifyRequest.getOrderConditions());
        storeLevel.setUpdatePerson(storeLevelModifyRequest.getUpdatePerson());
        storeLevel.setUpdateTime(storeLevelModifyRequest.getUpdateTime());
        return BaseResponse.success(new StoreLevelModifyResponse(
                storeLevelService.wrapperVo(storeLevelService.modify(storeLevel))));
    }

    @Override
    public BaseResponse modifyList(@RequestBody @Valid StoreLevelListModifyRequest storeLevelListModifyRequest) {
        StoreLevelQueryRequest queryReq = new StoreLevelQueryRequest();
        queryReq.setDelFlag(DeleteFlag.NO.toValue());
        queryReq.setStoreLevelIdList(storeLevelListModifyRequest.getStoreLevelVOList().stream().map(StoreLevelVO::getStoreLevelId)
                .collect(Collectors.toList()));
        List<StoreLevel> storeLevelList = storeLevelService.list(queryReq);
        storeLevelList.forEach(storeLevel -> {
            StoreLevelVO storeLevelVO = storeLevelListModifyRequest.getStoreLevelVOList().stream().filter(s -> s
                    .getStoreLevelId().equals(storeLevel.getStoreLevelId())).findFirst().orElse(null);
            storeLevel.setDiscountRate(storeLevelVO.getDiscountRate());
            storeLevel.setUpdatePerson(storeLevelVO.getUpdatePerson());
            storeLevel.setUpdateTime(storeLevelVO.getUpdateTime());
        });

        storeLevelService.modifyList(storeLevelList);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse deleteById(@RequestBody @Valid StoreLevelDelByIdRequest storeLevelDelByIdRequest) {
        storeLevelService.deleteById(storeLevelDelByIdRequest.getStoreLevelId());
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse initStoreLevel(@RequestBody @Valid StoreLevelInitRequest request) {
        StoreLevel storeLevel = new StoreLevel();
        storeLevel.setStoreId(request.getStoreId());
        storeLevel.setLevelName("普通");
        storeLevel.setDiscountRate(BigDecimal.ONE);
        storeLevel.setOrderConditions(1);
        storeLevel.setCreatePerson(request.getCreatePerson());
        storeLevel.setCreateTime(request.getCreateTime());
        storeLevelService.add(storeLevel);
        return BaseResponse.SUCCESSFUL();
    }

}

