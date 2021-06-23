package com.wanmi.sbc.goods.provider.impl.contract;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.contract.ContractProvider;
import com.wanmi.sbc.goods.api.request.contract.ContractSaveRequest;
import com.wanmi.sbc.goods.api.response.contract.ContractSaveResponse;
import com.wanmi.sbc.goods.contract.request.ContractRequest;
import com.wanmi.sbc.goods.contract.service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>对签约信息操作接口</p>
 * Created by daiyitian on 2018-10-31-下午6:23.
 */
@RestController
@Validated
public class ContractController implements ContractProvider {

    @Autowired
    private ContractService contractService;

    /**
     * 更新签约信息
     *
     * @param request 保存签约信息数据结构 {@link ContractSaveRequest}
     * @return 保存签约信息响应结构 {@link ContractSaveResponse}
     */
    @Override

    public BaseResponse<ContractSaveResponse> save(@RequestBody @Valid ContractSaveRequest request){
        return BaseResponse.success(ContractSaveResponse.builder()
                .brandIds(contractService.renewal(KsBeanUtil.convert(request, ContractRequest.class)))
                .build());
    }
}
