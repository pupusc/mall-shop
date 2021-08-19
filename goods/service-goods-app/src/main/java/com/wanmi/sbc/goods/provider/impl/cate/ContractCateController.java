package com.wanmi.sbc.goods.provider.impl.cate;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.cate.ContractCateProvider;
import com.wanmi.sbc.goods.api.request.cate.*;
import com.wanmi.sbc.goods.cate.request.ContractCateSaveRequest;
import com.wanmi.sbc.goods.cate.service.ContractCateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2018-11-07 19:34
 */
@Validated
@RestController
public class ContractCateController implements ContractCateProvider {

    @Autowired
    private ContractCateService contractCateService;

    /**
     * @param request 新增签约分类服务 {@link ContractCateAddRequest}
     * @return
     */

    @Override
    public BaseResponse add(@RequestBody @Valid ContractCateAddRequest request) {

        ContractCateSaveRequest contractCateSaveRequest = new ContractCateSaveRequest();

        KsBeanUtil.copyPropertiesThird(request, contractCateSaveRequest);

        contractCateService.add(contractCateSaveRequest);

        return BaseResponse.SUCCESSFUL();
    }

    /**
     * @param request 根据主键id删除签约分类 {@link ContractCateDeleteByIdRequest}
     * @return
     */

    @Override
    public BaseResponse deleteById(@RequestBody @Valid ContractCateDeleteByIdRequest request) {

        contractCateService.delete(request.getContractCateId());

        return BaseResponse.SUCCESSFUL();
    }

    /**
     * @param request 根据主键集合和店铺id删除签约分类 {@link ContractCateDeleteByIdsAndStoreIdRequest}
     * @return
     */

    @Override
    public BaseResponse deleteByIdsAndStoreId(@RequestBody @Valid ContractCateDeleteByIdsAndStoreIdRequest request) {

        contractCateService.deleteByIds(request.getIds(), request.getStoreId());

        return BaseResponse.SUCCESSFUL();
    }

    /**
     * @param request 修改签约分类 {@link ContractCateModifyRequest}
     * @return
     */

    @Override
    public BaseResponse modify(@RequestBody @Valid ContractCateModifyRequest request) {

        ContractCateSaveRequest contractCateSaveRequest = new ContractCateSaveRequest();
        KsBeanUtil.copyPropertiesThird(request, contractCateSaveRequest);
        contractCateService.update(contractCateSaveRequest);

        return BaseResponse.SUCCESSFUL();
    }

    /**
     * @param request 根据平台分类主键列表修改签约分类扣率 {@link ContractCateModifyCateRateByCateIdsRequest}
     * @return
     */

    @Override
    public BaseResponse modifyCateRateBycateIds(@RequestBody @Valid ContractCateModifyCateRateByCateIdsRequest request) {
        contractCateService.updateCateRate(request.getCateRate(), request.getCateIds());
        return BaseResponse.SUCCESSFUL();
    }
}
