package com.wanmi.sbc.customer.provider.impl.quicklogin;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.quicklogin.ThirdLoginRelationProvider;
import com.wanmi.sbc.customer.api.request.quicklogin.ThirdLoginRelationDeleteByCustomerRequest;
import com.wanmi.sbc.customer.api.request.quicklogin.ThirdLoginRelationModifyRequest;
import com.wanmi.sbc.customer.quicklogin.model.root.ThirdLoginRelation;
import com.wanmi.sbc.customer.quicklogin.service.ThirdLoginRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 第三方登录方式-第三方登录方式添加/修改/删除API
 *
 * @Author: daiyitian
 * @CreateDate: 2018/9/11 16:25
 * @Version: 1.0
 */
@RestController
@Validated
public class ThirdLoginRelationController implements ThirdLoginRelationProvider {

    @Autowired
    private ThirdLoginRelationService thirdLoginRelationService;

    @Override
    public BaseResponse modifyThirdLoginRelation(@RequestBody @Valid ThirdLoginRelationModifyRequest request) {
        ThirdLoginRelation relation = new ThirdLoginRelation();
        KsBeanUtil.copyPropertiesThird(request, relation);
        thirdLoginRelationService.save(relation);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse deleteThirdLoginRelationByCustomerIdAndStoreId(@RequestBody @Valid
                                                                     ThirdLoginRelationDeleteByCustomerRequest
                                                                     request) {
        thirdLoginRelationService.deleteByCustomerIdAndStoreId(request.getCustomerId(), request.getThirdLoginType(),request.getStoreId());
        return BaseResponse.SUCCESSFUL();
    }
}
