package com.wanmi.sbc.customer.provider.impl.customer;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.customer.api.constant.CustomerErrorCode;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.request.customer.*;
import com.wanmi.sbc.customer.api.request.growthvalue.CustomerByGrowthValueRequest;
import com.wanmi.sbc.customer.api.response.customer.*;
import com.wanmi.sbc.customer.api.response.level.CustomerLevelPageResponse;
import com.wanmi.sbc.customer.bean.enums.CheckState;
import com.wanmi.sbc.customer.bean.enums.CustomerStatus;
import com.wanmi.sbc.customer.bean.enums.ThirdLoginType;
import com.wanmi.sbc.customer.bean.vo.*;
import com.wanmi.sbc.customer.detail.model.root.CustomerDetail;
import com.wanmi.sbc.customer.enterpriseinfo.model.root.EnterpriseInfo;
import com.wanmi.sbc.customer.enterpriseinfo.service.EnterpriseInfoService;
import com.wanmi.sbc.customer.fandeng.ExternalService;
import com.wanmi.sbc.customer.level.model.root.CustomerLevel;
import com.wanmi.sbc.customer.model.root.Customer;
import com.wanmi.sbc.customer.model.root.CustomerBase;
import com.wanmi.sbc.customer.quicklogin.model.root.ThirdLoginRelation;
import com.wanmi.sbc.customer.quicklogin.service.ThirdLoginRelationService;
import com.wanmi.sbc.customer.request.CustomerWithOpenIdRequest;
import com.wanmi.sbc.customer.service.CustomerService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@Validated
public class CustomerQueryController implements CustomerQueryProvider {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private ThirdLoginRelationService thirdLoginRelationService;

    @Autowired
    private EnterpriseInfoService enterpriseInfoService;
    @Autowired
    private ExternalService externalService;
    /**
     * 条件查询会员带分页
     * <p>
     * {@link CustomerService#page}
     */
    @Override
    public BaseResponse<CustomerDetailPageResponse> page(@RequestBody @Valid CustomerDetailPageRequest request) {
        CustomerDetailPageResponse response = customerService.page(request);
        if(Boolean.TRUE.equals(request.getShowAreaFlag())){
            customerService.fillArea(response.getDetailResponseList());
        }
        return BaseResponse.success(response);
    }

    /**
     * 企业购条件查询会员带分页
     * <p>
     * {@link CustomerService#page}
     */
    @Override
    public BaseResponse<CustomerDetailPageResponse> pageForEnterpriseCustomer(@Valid @RequestBody EnterpriseCustomerPageRequest request) {
        CustomerDetailPageResponse response = customerService.pageForEnterpriseCustomer(request);
        //组装企业会员对应的企业信息
        List<String> customerIdList =
                response.getDetailResponseList().stream().map(CustomerDetailForPageVO::getCustomerId).collect(Collectors.toList());
        List<EnterpriseInfo> enterpriseInfoVOList = enterpriseInfoService.listByCustomerIds(customerIdList);
        response.getDetailResponseList().forEach(customerVO -> {
            EnterpriseInfo enterpriseInfoVO = enterpriseInfoVOList.stream()
                    .filter(vo -> vo.getCustomerId().equals(customerVO.getCustomerId()))
                    .findFirst().orElse(null);
            if (Objects.nonNull(enterpriseInfoVO)){
                customerVO.setEnterpriseName(enterpriseInfoVO.getEnterpriseName());
                customerVO.setBusinessNatureType(enterpriseInfoVO.getBusinessNatureType());
            }
        });
        return BaseResponse.success(response);
    }

    /**
     * 条件查询会员带分页(S2b)
     * <p>
     * {@link CustomerService#pageForS2bSupplier}
     */
    @Override
    public BaseResponse<CustomerDetailPageForSupplierResponse> pageForS2bSupplier(@RequestBody @Valid
                                                                                          CustomerDetailPageForSupplierRequest request) {
        CustomerDetailPageForSupplierResponse response = customerService.pageForS2bSupplier(request);
        if(Boolean.TRUE.equals(request.getShowAreaFlag())){
            customerService.fillArea(response.getDetailResponseList());
        }
        return BaseResponse.success(response);
    }

    /**
     * 多条件查询会员详细信息
     * <p>
     * {@link CustomerService#findDetailByCondition}
     */
    @Override

    public BaseResponse<CustomerDetailListByConditionResponse> listCustomerDetailByCondition(@RequestBody @Valid
                                                                                                     CustomerDetailListByConditionRequest request) {
        List<CustomerDetailVO> detailResponseList = customerService.findDetailByCondition(request).get().stream().map
                (customerDetail -> {
                    CustomerDetailVO vo = new CustomerDetailVO();
                    KsBeanUtil.copyPropertiesThird(customerDetail, vo);
                    return vo;
                }).collect(Collectors.toList());

        return BaseResponse.success(new CustomerDetailListByConditionResponse(detailResponseList));
    }

    /**
     * 代客下单 autocomplete
     * <p>
     * {@link CustomerService#findDetailForOrder}
     */
    @Override

    public BaseResponse<CustomerDetailListForOrderResponse> listCustomerDetailForOrder(@RequestBody @Valid
                                                                                               CustomerDetailListForOrderRequest request) {
        List<CustomerDetail> customerDetailList = customerService.findDetailForOrder(request);

        List<CustomerDetailVO> voList = convertToVO(customerDetailList);

        return BaseResponse.success(new CustomerDetailListForOrderResponse(voList));
    }

    /**
     * 分页获取客户列表
     * <p>
     * {@link CustomerService#findDetailByPage}
     */
    @Override
    public BaseResponse<CustomerDetailListByPageResponse> listCustomerDetailForDistributionRecord(@RequestBody @Valid
                                                                                                          CustomerDetailListByPageFordrRequest request) {

        CustomerDetailListByPageRequest pageRequest = new CustomerDetailListByPageRequest();
        KsBeanUtil.copyPropertiesThird(request, pageRequest);
        pageRequest.setCheckState(CheckState.CHECKED.toValue());
        pageRequest.setCustomerStatus(CustomerStatus.ENABLE);
        List<CustomerDetail> customerDetailList = customerService.findDetailByPage(pageRequest);
        List<CustomerDetailVO> voList = convertToVO(customerDetailList);
        return BaseResponse.success(new CustomerDetailListByPageResponse(voList));
    }

    /**
     * 分页获取客户列表
     * <p>
     * {@link CustomerService#findDetailByPage}
     */
    @Override

    public BaseResponse<CustomerDetailListByPageResponse> listCustomerDetailByPage(@RequestBody @Valid
                                                                                           CustomerDetailListByPageRequest request) {
        List<CustomerDetail> customerDetailList = customerService.findDetailByPage(request);

        List<CustomerDetailVO> voList = convertToVO(customerDetailList);

        return BaseResponse.success(new CustomerDetailListByPageResponse(voList));
    }

    /**
     * 根据业务员id获取客户列表
     * <p>
     * {@link CustomerService#findDetaileByEmployeeId}
     */
    @Override

    public BaseResponse<CustomerIdListResponse> listCustomerId(@RequestBody @Valid CustomerIdListRequest request) {
        List<String> idList = customerService.findDetaileByEmployeeId(request);

        if (idList == null) {
            idList = Collections.EMPTY_LIST;
        }

        return BaseResponse.success(new CustomerIdListResponse(idList));
    }

    /**
     * 获取客户id列表
     * <p>
     * {@link CustomerService#queryAllCustomerId}
     */
    @Override
    public BaseResponse<CustomerIdListResponse> listCustomerId() {
        List<String> idList = customerService.queryAllCustomerId();
        if (idList == null) {
            idList = Collections.EMPTY_LIST;
        }
        return BaseResponse.success(new CustomerIdListResponse(idList));
    }

    /**
     * 模糊查询会员信息
     * <p>
     * {@link CustomerService#findCustomerByCondition}
     */
    @Override

    public BaseResponse<CustomerListByConditionResponse> listCustomerByCondition(@RequestBody @Valid
                                                                                         CustomerListByConditionRequest request) {
        List<CustomerVO> customerVOList = customerService.findCustomerByCondition(request).get().stream().map
                (customer -> {
                    CustomerVO vo = new CustomerVO();
                    KsBeanUtil.copyPropertiesThird(customer, vo);

                    return vo;
                }).collect(Collectors.toList());

        return BaseResponse.success(new CustomerListByConditionResponse(customerVOList));
    }

    /**
     * 查询单条会员信息
     * <p>
     * {@link CustomerService#findById} {@link CustomerService#findInfoById}
     */
    @Override
    public BaseResponse<CustomerGetByIdResponse> getCustomerById(@RequestBody @Valid CustomerGetByIdRequest request) {
        CustomerGetByIdResponse response = new CustomerGetByIdResponse();

        Customer customer = customerService.findById(request.getCustomerId());
        Long currentPoint = 0L;
/*        if(StringUtils.isNotBlank(customer.getFanDengUserNo())){
            currentPoint = externalService.getByUserNoPoint(
                    FanDengPointRequest.builder().userNo(customer.getFanDengUserNo())
                            .build()).getContext().getCurrentPoint();
        }*/
        customer.setPointsAvailable(currentPoint);
        copyProperties(customer, response);
        ThirdLoginRelation thirdLoginRelation =
                thirdLoginRelationService.findFirstByCustomerIdAndThirdType
                (request.getCustomerId(),
                ThirdLoginType.WECHAT);
        if(Objects.nonNull(thirdLoginRelation)){
            response.setHeadImg(thirdLoginRelation.getHeadimgurl());
        }
        return BaseResponse.success(response);
    }


    /**
     * 删除标记
     * <p>
     * {@link CustomerService#findCustomerDelFlag}
     */
    @Override
    public BaseResponse<CustomerDelFlagGetResponse> getCustomerDelFlag(@RequestBody @Valid CustomerDelFlagGetRequest
                                                                               request) {
        Boolean delFlag = customerService.findCustomerDelFlag(request.getCustomerId());

        return BaseResponse.success(new CustomerDelFlagGetResponse(delFlag));
    }

    /**
     * 是否有客户获取了成长值
     * @return
     */
    @Override
    public BaseResponse<Boolean> hasObtainedGrowthValue() {
        return BaseResponse.success(customerService.hasObtainedGrowthValue());
    }

    /**
     * 检验账户是否存在
     * <p>
     * {@link CustomerService#findByCustomerAccountAndDelFlag}
     */
    @Override

    public BaseResponse<NoDeleteCustomerGetByAccountResponse> getNoDeleteCustomerByAccount(@RequestBody @Valid
                                                                                                   NoDeleteCustomerGetByAccountRequest
                                                                                                   request) {
        Customer customer = customerService.findByCustomerAccountAndDelFlag(request.getCustomerAccount());
        if (Objects.isNull(customer)) {
            return BaseResponse.SUCCESSFUL();
        }
        NoDeleteCustomerGetByAccountResponse response = KsBeanUtil.convert(customer, NoDeleteCustomerGetByAccountResponse.class);
        return BaseResponse.success(response);
    }

    /**
     * 检验账户是否存在
     * <p>
     * {@link CustomerService#findByCustomerAccountAndDelFlag}
     */
    @Override
    public BaseResponse<NoDeleteCustomerGetByAccountResponse> getNoDeleteCustomerByFanDengId(@RequestBody @Valid
                                                                                                         NoDeleteCustomerGetByFanDengRequest
                                                                                                   request) {
        Customer customer = customerService.findByFanDengUserNoAndDelFlag(request.getFanDengId());
        if (Objects.isNull(customer)) {
            return BaseResponse.SUCCESSFUL();
        }
        NoDeleteCustomerGetByAccountResponse response =
                KsBeanUtil.convert(customer, NoDeleteCustomerGetByAccountResponse.class);
        return BaseResponse.success(response);
    }

    /**
     * 检查账户是否禁用
     * <p>
     * {@link CustomerService#findDisableCustomer}
     */
    @Override

    public BaseResponse<DisableCustomerDetailGetByAccountResponse> getDisableCustomerDetailByAccount(@RequestBody @Valid
                                                                                                             DisableCustomerDetailGetByAccountRequest request) {
        CustomerDetail customerDetail = customerService.findDisableCustomer(request.getCustomerAccount());

        DisableCustomerDetailGetByAccountResponse response = new DisableCustomerDetailGetByAccountResponse();

        if (customerDetail != null) {
            KsBeanUtil.copyPropertiesThird(customerDetail, response);
        }

        return BaseResponse.success(response);
    }

    /**
     * 根据审核状态统计用户
     * <p>
     * {@link CustomerService#countCustomerByState}
     */
    @Override

    public BaseResponse<CustomerCountByStateResponse> countCustomerByState(@RequestBody @Valid
                                                                                   CustomerCountByStateRequest
                                                                                   request) {
        long count = customerService.countCustomerByState(request);

        return BaseResponse.success(new CustomerCountByStateResponse(count));
    }

    /**
     * 根据审核状态统计增票资质
     * <p>
     * {@link CustomerService#countInvoiceByState}
     */
    @Override

    public BaseResponse<InvoiceCountByStateResponse> countInvoiceByState(@RequestBody @Valid
                                                                                 InvoiceCountByStateRequest
                                                                                 request) {
        long count = customerService.countInvoiceByState(request.getCheckState());

        return BaseResponse.success(new InvoiceCountByStateResponse(count));
    }

    /**
     * 按照客户名称模糊匹配，当前商家未关联的平台客户
     * <p>
     * {@link CustomerService#getCustomerNotRelated}
     */
    @Override

    public BaseResponse<CustomerNotRelatedListResponse> listCustomerNotRelated(@RequestBody @Valid
                                                                                       CustomerNotRelatedListRequest
                                                                                       request) {
        List<Map<String, Object>> customers = customerService.getCustomerNotRelated(request.getCompanyInfoId(), request
                .getCustomerAccount());

        return BaseResponse.success(new CustomerNotRelatedListResponse(customers));
    }

    /**
     * 获取客户详情信息，包括业务员名称，等级，等级扣率
     * <p>
     * {@link CustomerService#getCustomerResponseForSupplier}
     */
    @Override

    public BaseResponse<CustomerGetForSupplierResponse> getCustomerForSupplier(@RequestBody @Valid
                                                                                       CustomerGetForSupplierRequest
                                                                                       request) {
        CustomerGetForSupplierResponse response = customerService.getCustomerResponseForSupplier(
                request.getCustomerId(), request.getCompanyInfoId(), request.getStoreId());

        return BaseResponse.success(response);
    }

    @Override
    public BaseResponse<CustomerIdListResponse> listCustomerIdByGrowthValue(@RequestBody @Valid CustomerByGrowthValueRequest request){
        List<String> idList = customerService.findByGrowthValue(request.getGrowthValue());
        if (idList == null) {
            idList = Collections.EMPTY_LIST;
        }
        return BaseResponse.success(new CustomerIdListResponse(idList));
    }

    @Override
    public BaseResponse<CustomerListCustomerIdByPageableResponse> listCustomerIdByPageable(@RequestBody @Valid CustomerListCustomerIdByPageableRequest request){
         List<String> list = customerService.findCustomerIdByPageable(request.getCustomerLevelIds(),request.getPageRequest());
         return BaseResponse.success(new CustomerListCustomerIdByPageableResponse(list));
    }

    /**
     * 实体bean转换成vo
     *
     * @param customerDetailList
     * @return
     */
    private List<CustomerDetailVO> convertToVO(List<CustomerDetail> customerDetailList) {
        List<CustomerDetailVO> voList;

        if (CollectionUtils.isNotEmpty(customerDetailList)) {
            voList = customerDetailList.stream().map(customerDetail -> {
                if (Objects.nonNull(customerDetail.getCustomer()) && StringUtils.isNotEmpty(customerDetail.getCustomer
                        ().getCustomerAccount())) {
                    String dexAccount = getDexAccount(customerDetail.getCustomer().getCustomerAccount());
                    Customer customer = customerDetail.getCustomer();
                    customer.setCustomerAccount(dexAccount);
                    customerDetail.setCustomer(customer);
                }
                CustomerDetailVO vo = new CustomerDetailVO();
                KsBeanUtil.copyPropertiesThird(customerDetail, vo);
                vo.setCustomerVO(KsBeanUtil.convert(customerDetail.getCustomer(), CustomerVO.class));
                return vo;
            }).collect(Collectors.toList());
        } else {
            voList = Collections.EMPTY_LIST;
        }

        return voList;
    }

    /**
     * 属性拷贝
     *
     * @param customer
     * @param response
     */
    private void copyProperties(Customer customer, CustomerGetByIdResponse response) {
        if (customer != null) {
            KsBeanUtil.copyPropertiesThird(customer, response);

            if (customer.getCustomerDetail() != null) {
                CustomerDetailVO vo = new CustomerDetailVO();

                KsBeanUtil.copyPropertiesThird(customer.getCustomerDetail(), vo);

                response.setCustomerDetail(vo);
            }
        }else {
            throw new SbcRuntimeException(CustomerErrorCode.NOT_EXIST);
        }
    }

    private String getDexAccount(String bankNo) {
        String middle = "****";
        if (bankNo.length() > 4) {
            if (bankNo.length() <= 8) {
                return middle;
            } else {
                bankNo = bankNo.substring(0, 4) + middle + bankNo.substring(bankNo.length() - 4);
            }
        } else {
            return middle;
        }
        return bankNo;
    }

    @Override
    public BaseResponse<CustomerBaseByCustomerIdAndDeleteFlagResponse> getBaseCustomer(@RequestBody @Valid CustomerBaseByCustomerIdAndDeleteFlagRequest request){
        CustomerBase customerBase = customerService.getBaseCustomerByCustomerIdAndDeleteFlag(request.getCustomerId(),request.getDeleteFlag());
        if (Objects.isNull(customerBase)){
            return BaseResponse.success(new CustomerBaseByCustomerIdAndDeleteFlagResponse());
        }
        CustomerBaseVO customerBaseVO = KsBeanUtil.convert(customerBase,CustomerBaseVO.class);
        return BaseResponse.success(new CustomerBaseByCustomerIdAndDeleteFlagResponse(customerBaseVO));
    }





    @Override
    public BaseResponse<CustomerBaseListByIdsResponse> getCustomerListByIds(@RequestBody @Valid CustomerIdsListRequest
                                                                             request){
        List<CustomerVO> customerVOList = customerService.findCustomerByCustomerIds(request.getCustomerIds()).stream().map
                (customer -> {
                    CustomerVO vo = new CustomerVO();
                    KsBeanUtil.copyPropertiesThird(customer, vo);
                    return vo;
                }).collect(Collectors.toList());

        return BaseResponse.success( new CustomerBaseListByIdsResponse(customerVOList));
    }


    /**
     * 根据会员ID查询会员的可用积分
     * @param request
     * @return
     */
    @Override
    public BaseResponse<CustomerPointsAvailableByCustomerIdResponse> getPointsAvailable(@RequestBody @Valid CustomerPointsAvailableByIdRequest request){
        Long pointsAvailable = customerService.getPointsAvailable(request.getCustomerId());
        return BaseResponse.success(new CustomerPointsAvailableByCustomerIdResponse(pointsAvailable));
    }

    @Override
    public BaseResponse<CustomerByPhonesResponse> getCustomersByPhones(List<String> phones) {
        List<String> customersByPhones = customerService.getCustomersByPhones(phones);
        CustomerByPhonesResponse customerByPhonesResponse = new CustomerByPhonesResponse();
        customerByPhonesResponse.setCustomerPhones(customersByPhones);
        return BaseResponse.success(customerByPhonesResponse);
    }

    @Override
    public BaseResponse<CustomerSimplifyByIdResponse> simplifyById(@Valid CustomerSimplifyByIdRequest request) {
        Customer customer = customerService.findById(request.getCustomerId());
        CustomerSimplifyByIdResponse simplify = new CustomerSimplifyByIdResponse();
        simplify.setCustomerAccount(customer.getCustomerAccount());
        simplify.setCustomerId(customer.getCustomerId());
        simplify.setCustomerLevelId(customer.getCustomerLevelId());
        simplify.setEnterpriseCheckState(customer.getEnterpriseCheckState());
        simplify.setPointsAvailable(customer.getPointsAvailable());
        simplify.setStoreCustomerRelaListByAll(KsBeanUtil.convert(customer.getStoreCustomerRelaListByAll(),StoreCustomerRelaVO.class));
        simplify.setFanDengUserNo(customer.getFanDengUserNo());
        simplify.setOpenId(customer.getWxMiniOpenId());
        CustomerDetailSimplifyVO customerDetailSimplifyVO = new CustomerDetailSimplifyVO();
        CustomerDetail customerDetail = customer.getCustomerDetail();
        if (Objects.nonNull(customerDetail)) {
            customerDetailSimplifyVO.setContactPhone(customerDetail.getContactPhone());
            customerDetailSimplifyVO.setCustomerName(customerDetail.getCustomerName());
            customerDetailSimplifyVO.setEmployeeId(customerDetail.getEmployeeId());
            customerDetailSimplifyVO.setCustomerStatus(customerDetail.getCustomerStatus());
        }
        simplify.setCustomerDetail(customerDetailSimplifyVO);
        return BaseResponse.success(simplify);
    }

    @Override
    public BaseResponse<CustomerDetailListByCustomerIdsResponse> listByCustomerIds(@RequestBody @Valid CustomerDetailListByCustomerIdsRequest request) {
        List<CustomerDetailFromEsVO>  list = customerService.listByCustomerIds(request);
        return BaseResponse.success(new CustomerDetailListByCustomerIdsResponse(list));
    }

    @Override
    public BaseResponse<CustomerDetailInitEsResponse> listByPage(@Valid CustomerDetailInitEsRequest request) {
        List<CustomerDetailInitEsVO> customerDetailInitEsVOS =  customerService.listByPage(request);
        return BaseResponse.success(new CustomerDetailInitEsResponse(customerDetailInitEsVOS));
    }


    @Override
    public BaseResponse<CustomerDetailInitEsResponse> listByPaidMembersPage(@Valid CustomerDetailInitEsRequest request) {
        List<CustomerDetailInitEsVO> customerDetailInitEsVOS =  customerService.listByPaidMembersPage(request);
        return BaseResponse.success(new CustomerDetailInitEsResponse(customerDetailInitEsVOS));
    }

    @Override
    public BaseResponse<CustomerListByConditionResponse> getCustomerByYzUserId(@RequestBody @Valid CustomerYzUidListRequest request) {
        CustomerListByConditionResponse response = new CustomerListByConditionResponse();
        List<CustomerVO> customers = new ArrayList<>();
        request.getYzUids().forEach(id -> {
            Customer customer = customerService.findCustomerByYzUid(id);
            if(Objects.nonNull(customer)) {
                customers.add(KsBeanUtil.convert(customer, CustomerVO.class));
            }
        });
        response.setCustomerVOList(customers);
        return BaseResponse.success(response);
    }

    @Override
    public BaseResponse<List<Long>> findYzUidNotExist(@Valid CustomerYzUidListRequest request) {
        List<Long> ids = customerService.findYzUidNotExist(request.getYzUids());
        return BaseResponse.success(ids);
    }

    @Override
    public BaseResponse<MicroServicePage<CustomerSimplerVO>> listCustomerWithOpenId(CustomerWithOpenIdPageRequest request) {
        MicroServicePage<CustomerSimplerVO> page = customerService.listCustomerWithOpenId(KsBeanUtil.convert(request, CustomerWithOpenIdRequest.class));
        return  BaseResponse.success(page);
    }

    @Override
    public BaseResponse<Long> countCustomerWithOpenId() {
        return BaseResponse.success(customerService.countCustomerWithOpenId());
    }
}
