package com.wanmi.sbc.customer.provider.impl.quicklogin;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.quicklogin.ThirdLoginRelationQueryProvider;
import com.wanmi.sbc.customer.api.request.customer.CustomerIdsListRequest;
import com.wanmi.sbc.customer.api.request.quicklogin.ThirdLoginRelationByCustomerRequest;
import com.wanmi.sbc.customer.api.request.quicklogin.ThirdLoginRelationByUidRequest;
import com.wanmi.sbc.customer.api.response.company.CompanyInfoGetResponse;
import com.wanmi.sbc.customer.api.response.customer.CustomerIdsListResponse;
import com.wanmi.sbc.customer.api.response.quicklogin.ThirdLoginRelationResponse;
import com.wanmi.sbc.customer.bean.vo.ThirdLoginRelationVO;
import com.wanmi.sbc.customer.quicklogin.model.root.ThirdLoginRelation;
import com.wanmi.sbc.customer.quicklogin.service.ThirdLoginRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 第三方登录方式-第三方登录方式查询API
 *
 * @Author: daiyitian
 * @CreateDate: 2018/9/11 16:25
 * @Version: 1.0
 */
@RestController
@Validated
public class ThirdLoginRelationQueryController implements ThirdLoginRelationQueryProvider {

    @Autowired
    private ThirdLoginRelationService thirdLoginRelationService;

    /**
     * 查询最新第三方登录关系
     *
     * @return 第三方登录方式列表 {@link CompanyInfoGetResponse}
     */
    @Override
    public BaseResponse<ThirdLoginRelationResponse> listThirdLoginRelationByCustomer(@RequestBody @Valid
                                                                                             ThirdLoginRelationByCustomerRequest
                                                                                             request) {
        ThirdLoginRelation relation = thirdLoginRelationService.findFirstByCustomerIdAndThirdType(request
                        .getCustomerId(), request.getThirdLoginType());
        return BaseResponse.success(wraperResponse(relation));
    }

    /**
     * 根据 用户Id&第三方登录方式  查询所有第三方登录关系
     *
     * @return 第三方登录方式列表 {@link CompanyInfoGetResponse}
     */
    @Override
    public BaseResponse<ThirdLoginRelationResponse> thirdLoginRelationByCustomerAndDelFlag(@RequestBody @Valid
                                                                                                       ThirdLoginRelationByCustomerRequest
                                                                                                       request) {
        ThirdLoginRelation relation = thirdLoginRelationService.findAllByCustomerIdAndThirdTypeAndDelFlagAndStoreId(request
                        .getCustomerId(),
                request.getThirdLoginType(), request.getDelFlag(), request.getStoreId());
        return BaseResponse.success(wraperResponse(relation));
    }

    /**
     * 根据 关联Id&第三方登录方式  查询所有第三方登录关系
     *
     * @return 第三方登录方式列表 {@link CompanyInfoGetResponse}
     */
    @Override
    public BaseResponse<ThirdLoginRelationResponse> thirdLoginRelationByUidAndStoreId(@RequestBody @Valid
                                                                                              ThirdLoginRelationByUidRequest
                                                                                              request) {
        ThirdLoginRelation relation = thirdLoginRelationService.findByUnionIdAndThirdTypeAndStoreId(request
                        .getThirdLoginUid(),
                request.getThirdLoginType(),request.getStoreId());
        return BaseResponse.success(wraperResponse(relation));
    }

    /**
     * 根据 关联Id&第三方登录方式  查询所有第三方登录关系
     *
     * @return 第三方登录方式列表 {@link CompanyInfoGetResponse}
     */
    @Override
    public BaseResponse<ThirdLoginRelationResponse> thirdLoginRelationByUidAndDeleteflagAndStoreId(@RequestBody @Valid
                                                                                                     ThirdLoginRelationByUidRequest
                                                                                                     request) {
        ThirdLoginRelation relation = thirdLoginRelationService.findByUnionIdAndThirdTypeAndDelFlagAndStoreId(request
                        .getThirdLoginUid(),
                request.getThirdLoginType(), request.getDelFlag(), request.getStoreId());
        return BaseResponse.success(wraperResponse(relation));
    }

    private ThirdLoginRelationResponse wraperResponse(ThirdLoginRelation relation) {
        if (Objects.nonNull(relation)) {
            ThirdLoginRelationVO vo = new ThirdLoginRelationVO();
            KsBeanUtil.copyPropertiesThird(relation, vo);
            return ThirdLoginRelationResponse.builder().thirdLoginRelation(vo).build();
        }
        return ThirdLoginRelationResponse.builder().build();
    }

    /**
     * 根据 用户Id&第三方登录方式  查询所有第三方登录关系
     *
     * @return 第三方登录方式列表 {@link CompanyInfoGetResponse}
     */
    @Override
    public BaseResponse<CustomerIdsListResponse> listWithImgByCustomerIds(@RequestBody @Valid CustomerIdsListRequest
                                                                                      request) {
        return thirdLoginRelationService.listWithImgByCustomerIds(request);
    }
}
