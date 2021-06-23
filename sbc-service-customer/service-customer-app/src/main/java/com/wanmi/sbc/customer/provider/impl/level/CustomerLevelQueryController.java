package com.wanmi.sbc.customer.provider.impl.level;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.level.CustomerLevelQueryProvider;
import com.wanmi.sbc.customer.api.request.level.*;
import com.wanmi.sbc.customer.api.request.levelrights.CustomerLevelRightsQueryRequest;
import com.wanmi.sbc.customer.api.request.levelrights.CustomerLevelRightsRelRequest;
import com.wanmi.sbc.customer.api.response.level.*;
import com.wanmi.sbc.customer.bean.enums.ThirdLoginType;
import com.wanmi.sbc.customer.bean.vo.*;
import com.wanmi.sbc.customer.detail.model.root.CustomerDetail;
import com.wanmi.sbc.customer.detail.service.CustomerDetailService;
import com.wanmi.sbc.customer.level.model.root.CustomerLevel;
import com.wanmi.sbc.customer.level.request.CustomerLevelQueryRequest;
import com.wanmi.sbc.customer.level.service.CustomerLevelService;
import com.wanmi.sbc.customer.levelrights.model.root.CustomerLevelRights;
import com.wanmi.sbc.customer.levelrights.model.root.CustomerLevelRightsRel;
import com.wanmi.sbc.customer.levelrights.service.CustomerLevelRightsRelService;
import com.wanmi.sbc.customer.levelrights.service.CustomerLevelRightsService;
import com.wanmi.sbc.customer.model.root.Customer;
import com.wanmi.sbc.customer.model.root.CustomerBase;
import com.wanmi.sbc.customer.quicklogin.model.root.ThirdLoginRelation;
import com.wanmi.sbc.customer.quicklogin.service.ThirdLoginRelationService;
import com.wanmi.sbc.customer.service.CustomerService;
import com.wanmi.sbc.customer.storelevel.model.root.CommonLevel;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 会员等级-会员等级查询API
 *
 * @Author: daiyitian
 * @CreateDate: 2018/9/11 16:25
 * @Version: 1.0
 */
@RestController
@Validated
public class CustomerLevelQueryController implements CustomerLevelQueryProvider {

    @Autowired
    private CustomerLevelService customerLevelService;

    @Autowired
    private CustomerLevelRightsRelService customerLevelRightsRelService;

    @Autowired
    private CustomerLevelRightsService customerLevelRightsService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerDetailService customerDetailService;
    @Autowired
    private ThirdLoginRelationService thirdLoginRelationService;


    @Override

    public BaseResponse<CustomerLevelPageResponse> pageCustomerLevel(@RequestBody @Valid
                                                                             CustomerLevelPageRequest
                                                                             request) {
        CustomerLevelQueryRequest queryRequest = new CustomerLevelQueryRequest();
        KsBeanUtil.copyPropertiesThird(request, queryRequest);
        Page<CustomerLevel> levelPage = customerLevelService.page(queryRequest);
        List<CustomerLevelVO> voList = wrapperVos(levelPage.getContent());
        CustomerLevelPageResponse response = CustomerLevelPageResponse.builder()
                .customerLevelVOPage(new MicroServicePage<>(voList, request.getPageable(), levelPage.getTotalElements()))
                .build();
        return BaseResponse.success(response);
    }

    @Override

    public BaseResponse<CustomerLevelCountResponse> countCustomerLevel() {
        CustomerLevelCountResponse response = CustomerLevelCountResponse.builder()
                .count(customerLevelService.countCustomerLevel()).build();
        return BaseResponse.success(response);
    }

    @Override

    public BaseResponse<CustomerLevelListResponse> listAllCustomerLevel() {
        List<CustomerLevelVO> customerLevelVOS = wrapperVos(customerLevelService.findAllLevel());
        customerLevelVOS.forEach(
                customerLevelVO -> {
                    List<CustomerLevelRightsRel> list = customerLevelRightsRelService.listByLevelId(CustomerLevelRightsRelRequest.builder()
                            .customerLevelId(customerLevelVO.getCustomerLevelId())
                            .build());
                    List<CustomerLevelRightsVO> customerLevelRightsVOS = list.stream()
                            .map(customerLevelRightsRel -> customerLevelRightsService.getById(customerLevelRightsRel.getRightsId()))
                            .map(customerLevelRights -> customerLevelRightsService.wrapperVo(customerLevelRights))
                            .collect(Collectors.toList());
                    customerLevelVO.setCustomerLevelRightsVOS(customerLevelRightsVOS);
                }
        );
        return BaseResponse.success(CustomerLevelListResponse.builder().customerLevelVOList(customerLevelVOS).build());
    }

    @Override

    public BaseResponse<CustomerLevelGetResponse> getCustomerLevel(@RequestBody CustomerLevelGetRequest request) {
        CustomerLevelQueryRequest queryRequest = new CustomerLevelQueryRequest();
        KsBeanUtil.copyPropertiesThird(request, queryRequest);
        CustomerLevelGetResponse response = new CustomerLevelGetResponse();
        wrapperVo(customerLevelService.findOne(queryRequest), response);
        return BaseResponse.success(response);
    }

    @Override

    public BaseResponse<CustomerLevelByIdResponse> getCustomerLevelById(@RequestBody @Valid CustomerLevelByIdRequest
                                                                                request) {
        CustomerLevelByIdResponse response = new CustomerLevelByIdResponse();
        wrapperVo(customerLevelService.findById(request.getCustomerLevelId()).orElse(null), response);
        return BaseResponse.success(response);
    }

    @Override

    public BaseResponse<CustomerLevelWithDefaultByIdResponse> getCustomerLevelWithDefaultById(@RequestBody @Valid
                                                                                                      CustomerLevelWithDefaultByIdRequest
                                                                                                      request) {
        CustomerLevelWithDefaultByIdResponse response = new CustomerLevelWithDefaultByIdResponse();
        wrapperVo(customerLevelService.findLevelById(request.getCustomerLevelId()), response);
        return BaseResponse.success(response);
    }

    @Override

    public BaseResponse<CustomerLevelWithDefaultByCustomerIdResponse> getCustomerLevelWithDefaultByCustomerId(@RequestBody @Valid
                                                                                                                      CustomerLevelWithDefaultByCustomerIdRequest
                                                                                                                      request) {
        CustomerLevelWithDefaultByCustomerIdResponse response = new CustomerLevelWithDefaultByCustomerIdResponse();
        wrapperCommonLevelVo(customerLevelService.findLevelByCustomerId(request.getCustomerId()), response);
        return BaseResponse.success(response);
    }

    @Override

    public BaseResponse<CustomerLevelByIdsResponse> listCustomerLevelByIds(@RequestBody @Valid
                                                                                   CustomerLevelByIdsRequest
                                                                                   request) {
        CustomerLevelByIdsResponse response = CustomerLevelByIdsResponse.builder()
                .customerLevelVOList(wrapperVos(
                        customerLevelService.findByCustomerLevelIds(request.getCustomerLevelIds())))
                .build();
        return BaseResponse.success(response);
    }

    @Override

    public BaseResponse<CustomerLevelMapGetResponse> listCustomerLevelMapByCustomerIdAndIds(@RequestBody @Valid
                                                                                                    CustomerLevelMapByCustomerIdAndStoreIdsRequest
                                                                                                    request) {
        Map<Long, CommonLevel> commonLevelMap =
                customerLevelService.findLevelByCustomer(request.getCustomerId(), request.getStoreIds());
        HashMap<Long, CommonLevelVO> customerLevelVOMap = new HashMap<>();

        if (MapUtils.isNotEmpty(commonLevelMap)) {
            commonLevelMap.forEach((storeId, commonLevel) -> {
                CommonLevelVO vo = new CommonLevelVO();
                KsBeanUtil.copyPropertiesThird(commonLevel, vo);
                customerLevelVOMap.put(storeId, vo);
            });
        }
        return BaseResponse.success(CustomerLevelMapGetResponse.builder().commonLevelVOMap(customerLevelVOMap)
                .build());
    }

    @Override

    public BaseResponse<CustomerLevelByCustomerIdAndStoreIdResponse> getCustomerLevelByCustomerIdAndStoreId(@RequestBody @Valid
                                                                                                                    CustomerLevelByCustomerIdAndStoreIdRequest
                                                                                                                    request) {
        CustomerLevelByCustomerIdAndStoreIdResponse response = new CustomerLevelByCustomerIdAndStoreIdResponse();
        wrapperCommonLevelVo(customerLevelService.findLevelByCustomer(request.getCustomerId(), request.getStoreId()), response);
        return BaseResponse.success(response);
    }

    @Override

    public BaseResponse<CustomerLevelDefaultResponse> getDefaultCustomerLevel() {
        CustomerLevelDefaultResponse response = new CustomerLevelDefaultResponse();
        wrapperVo(customerLevelService.getDefaultLevel(), response);
        return BaseResponse.success(response);
    }

    @Override
    public BaseResponse<CustomerLevelWithRightsByCustomerIdResponse> getCustomerLevelRightsInfos(@RequestBody @Valid
                                                                                               CustomerLevelByCustomerIdRequest request) {
        // 用户等级信息
        Customer customer = customerService.findById(request.getCustomerId());
        Long customerLevelId = customer.getCustomerLevelId();
        CustomerDetail customerDetail = customerDetailService.findAnyByCustomerId(request.getCustomerId());
        CustomerLevel customerLevel = customerLevelService.findById(customerLevelId).get();
        CustomerLevelWithRightsByCustomerIdResponse response = new CustomerLevelWithRightsByCustomerIdResponse();
        wrapperVo(customerLevel, response);
        response.setCustomerGrowthValue(customer.getGrowthValue());
        response.setCustomerName(customerDetail.getCustomerName());
        ThirdLoginRelation thirdLoginRelation = thirdLoginRelationService.findFirstByCustomerIdAndThirdType(request.getCustomerId(), ThirdLoginType.WECHAT);
        if(Objects.nonNull(thirdLoginRelation)){
            response.setHeadImg(thirdLoginRelation.getHeadimgurl());
        }
        // 等级权益信息
        List<CustomerLevelRightsRel> levelRightsRels = customerLevelRightsRelService.listByLevelId(CustomerLevelRightsRelRequest.builder().customerLevelId(customerLevelId).build());
        List<CustomerLevelRights> levelRightsList = levelRightsRels.stream()
                .map(levelRightsRel -> customerLevelRightsService.getById(levelRightsRel.getRightsId()))
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(levelRightsList)) {
            List<CustomerLevelRightsVO> levelRightsVOS = levelRightsList.stream()
                    .map(levelRights -> {
                        CustomerLevelRightsVO vo = new CustomerLevelRightsVO();
                        KsBeanUtil.copyPropertiesThird(levelRights, vo);
                        return vo;
                    }).collect(Collectors.toList());
            response.setCustomerLevelRightsVOS(levelRightsVOS);
        }
        return BaseResponse.success(response);
    }

    @Override
    public BaseResponse<CustomerLevelWithRightsResponse> listCustomerLevelRightsInfo() {
        List<CustomerLevel> customerLevels = customerLevelService.findAllLevel();
        List<CustomerLevelVO> customerLevelVOList = new ArrayList<>();
        customerLevels.forEach(customerLevel -> {
            CustomerLevelVO customerLevelVO = new CustomerLevelVO();
            wrapperVo(customerLevel, customerLevelVO);
            List<CustomerLevelRightsVO> levelRightsVOS = customerLevelRightsRelService.listByLevelId(CustomerLevelRightsRelRequest.builder()
                    .customerLevelId(customerLevel.getCustomerLevelId()).build())
                    .stream()
                    .map(levelRightsRel ->customerLevelRightsService.list(CustomerLevelRightsQueryRequest.builder()
                            .delFlag(DeleteFlag.NO)
                            .rightsId(levelRightsRel.getRightsId()).build()))
                    .flatMap(Collection::stream)
                    .map(levelRights -> {
                        CustomerLevelRightsVO vo = new CustomerLevelRightsVO();
                        KsBeanUtil.copyPropertiesThird(levelRights, vo);
                        return vo;
                    }).collect(Collectors.toList());
            customerLevelVO.setCustomerLevelRightsVOS(levelRightsVOS);
            customerLevelVOList.add(customerLevelVO);
        });
        CustomerLevelWithRightsResponse response = new CustomerLevelWithRightsResponse();
        response.setCustomerLevelVOList(customerLevelVOList);
        return BaseResponse.success(response);
    }

    @Override
    public BaseResponse<CustomerLevelListByCustomerIdsResponse> listByCustomerIds(@RequestBody @Valid CustomerLevelListByCustomerIdsRequest request){
        List<CustomerBase> customerLevelList = customerLevelService.findCustomerLevelIdByCustomerIds(request.getCustomerIds());
        if (CollectionUtils.isEmpty(customerLevelList)){
            return BaseResponse.success(new CustomerLevelListByCustomerIdsResponse());
        }
        List<CustomerBaseVO> customerLevelVOList = KsBeanUtil.convert(customerLevelList,CustomerBaseVO.class);
        return BaseResponse.success(new CustomerLevelListByCustomerIdsResponse(customerLevelVOList));
    }

    @Override
    public BaseResponse<CustomerLevelListByCustomerLevelNameResponse> listByCustomerLevelName(@Valid CustomerLevelListByCustomerLevelNameRequest request) {
        List<MarketingCustomerLevelVO> marketingCustomerLevelVOS = customerLevelService.listCustomerLevelName(request);
        return BaseResponse.success(new CustomerLevelListByCustomerLevelNameResponse(marketingCustomerLevelVOS));
    }

    @Override
    public BaseResponse<CustomerLevelListBaseResponse> listAllCustomerLevelNew() {
        List<CustomerLevel> customerLevels = customerLevelService.findAllLevel();
        List<CustomerLevelBaseVO> customerLevelVOList = customerLevels.stream().map(customerLevel -> {
            CustomerLevelBaseVO vo = new CustomerLevelBaseVO();
            vo.setCustomerLevelId(customerLevel.getCustomerLevelId());
            vo.setCustomerLevelName(customerLevel.getCustomerLevelName());
            return vo;
        }).collect(Collectors.toList());
        return BaseResponse.success(CustomerLevelListBaseResponse.builder().customerLevelVOList(customerLevelVOList).build());
    }

    private List<CustomerLevelVO> wrapperVos(List<CustomerLevel> customerLevels) {
        if (CollectionUtils.isNotEmpty(customerLevels)) {
            return customerLevels.stream().map(customerLevel -> {
                CustomerLevelVO vo = new CustomerLevelVO();
                KsBeanUtil.copyPropertiesThird(customerLevel, vo);
                return vo;
            }).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    private void wrapperVo(CustomerLevel customerLevel, CustomerLevelVO vo) {
        if (Objects.nonNull(customerLevel)) {
            KsBeanUtil.copyPropertiesThird(customerLevel, vo);
        }
    }

    private void wrapperCommonLevelVo(CommonLevel commonLevel, CommonLevelVO vo) {
        if (Objects.nonNull(commonLevel)) {
            KsBeanUtil.copyPropertiesThird(commonLevel, vo);
        }
    }
}
