package com.wanmi.sbc.customer;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.redis.CacheKeyConstant;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.util.SiteResultCode;
import com.wanmi.sbc.customer.api.constant.CustomerLabel;
import com.wanmi.sbc.customer.api.provider.customer.CustomerProvider;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.provider.detail.CustomerDetailProvider;
import com.wanmi.sbc.customer.api.provider.employee.EmployeeQueryProvider;
import com.wanmi.sbc.customer.api.provider.enterpriseinfo.EnterpriseInfoQueryProvider;
import com.wanmi.sbc.customer.api.provider.fandeng.ExternalProvider;
import com.wanmi.sbc.customer.api.provider.growthvalue.CustomerGrowthValueProvider;
import com.wanmi.sbc.customer.api.provider.growthvalue.CustomerGrowthValueQueryProvider;
import com.wanmi.sbc.customer.api.provider.level.CustomerLevelQueryProvider;
import com.wanmi.sbc.customer.api.provider.loginregister.CustomerSiteProvider;
import com.wanmi.sbc.customer.api.provider.points.CustomerPointsDetailQueryProvider;
import com.wanmi.sbc.customer.api.provider.points.CustomerPointsDetailSaveProvider;
import com.wanmi.sbc.customer.api.request.customer.CustomerAccountModifyRequest;
import com.wanmi.sbc.customer.api.request.customer.CustomerGetByIdRequest;
import com.wanmi.sbc.customer.api.request.customer.NoDeleteCustomerGetByAccountRequest;
import com.wanmi.sbc.customer.api.request.detail.CustomerDetailModifyRequest;
import com.wanmi.sbc.customer.api.request.employee.EmployeeOptionalByIdRequest;
import com.wanmi.sbc.customer.api.request.enterpriseinfo.EnterpriseInfoByCustomerIdRequest;
import com.wanmi.sbc.customer.api.request.fandeng.FanDengPointRequest;
import com.wanmi.sbc.customer.api.request.growthvalue.CustomerGrowthValueAddRequest;
import com.wanmi.sbc.customer.api.request.growthvalue.CustomerGrowthValuePageRequest;
import com.wanmi.sbc.customer.api.request.level.CustomerLevelWithDefaultByIdRequest;
import com.wanmi.sbc.customer.api.request.loginregister.CustomerSendMobileCodeRequest;
import com.wanmi.sbc.customer.api.request.loginregister.CustomerValidateSendMobileCodeRequest;
import com.wanmi.sbc.customer.api.request.points.CustomerPointsDetailAddRequest;
import com.wanmi.sbc.customer.api.request.points.CustomerPointsDetailQueryRequest;
import com.wanmi.sbc.customer.api.response.customer.CustomerGetByIdResponse;
import com.wanmi.sbc.customer.api.response.customer.CustomerPointsAvailableByCustomerIdResponse;
import com.wanmi.sbc.customer.api.response.customer.GrowthValueAndPointResponse;
import com.wanmi.sbc.customer.api.response.customer.NoDeleteCustomerGetByAccountResponse;
import com.wanmi.sbc.customer.api.response.employee.EmployeeOptionalByIdResponse;
import com.wanmi.sbc.customer.api.response.enterpriseinfo.EnterpriseInfoByCustomerIdResponse;
import com.wanmi.sbc.customer.bean.dto.CounselorDto;
import com.wanmi.sbc.customer.bean.enums.*;
import com.wanmi.sbc.customer.bean.vo.CustomerDetailVO;
import com.wanmi.sbc.customer.bean.vo.CustomerLevelVO;
import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import com.wanmi.sbc.customer.bean.vo.EnterpriseInfoVO;
import com.wanmi.sbc.customer.request.CustomerBaseInfoRequest;
import com.wanmi.sbc.customer.request.CustomerMobileRequest;
import com.wanmi.sbc.customer.response.CustomerBaseInfoResponse;
import com.wanmi.sbc.customer.response.CustomerCenterResponse;
import com.wanmi.sbc.customer.response.CustomerSafeResponse;
import com.wanmi.sbc.customer.response.IsCounselorVo;
import com.wanmi.sbc.mq.producer.WebBaseProducerService;
import com.wanmi.sbc.redis.RedisService;
import com.wanmi.sbc.setting.api.provider.systemconfig.SystemConfigQueryProvider;
import com.wanmi.sbc.setting.api.request.ConfigQueryRequest;
import com.wanmi.sbc.setting.bean.enums.ConfigType;
import com.wanmi.sbc.setting.bean.vo.ConfigVO;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.vas.bean.vo.IepSettingVO;
import io.seata.spring.annotation.GlobalTransactional;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * @menu 商城配合知识顾问
 * @tag feature_d_cps3
 * @status undone
 */
@RestController
@RequestMapping("/customer")
@Validated
@Api(tags = "CustomerBaseController", description = "S2B web公用-客户信息API")
public class CustomerBaseController {
    @Autowired
    private CustomerQueryProvider customerQueryProvider;

    @Autowired
    private CustomerProvider customerProvider;

    @Autowired
    private CustomerSiteProvider customerSiteProvider;

    @Autowired
    private CustomerDetailProvider customerDetailProvider;

    @Autowired
    private CustomerLevelQueryProvider customerLevelQueryProvider;

    @Autowired
    private EmployeeQueryProvider employeeQueryProvider;

    @Autowired
    private CustomerPointsDetailSaveProvider customerPointsDetailSaveProvider;

    @Autowired
    private CustomerGrowthValueProvider customerGrowthValueProvider;

    @Autowired
    private RedisService redisService;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private SystemConfigQueryProvider systemConfigQueryProvider;

    @Autowired
    private EnterpriseInfoQueryProvider enterpriseInfoQueryProvider;
    @Autowired
    private CustomerGrowthValueQueryProvider customerGrowthValueQueryProvider;
    @Autowired
    private CustomerPointsDetailQueryProvider customerPointsDetailQueryProvider;

    @Autowired
    private WebBaseProducerService webBaseProducerService;
    @Autowired
    private ExternalProvider externalProvider;

    /**
     * 查询会员基本信息数据
     *
     * @return
     */
    @ApiOperation(value = "查询会员基本信息数据")
    @RequestMapping(value = "/customerBase", method = RequestMethod.GET)
    public BaseResponse<CustomerBaseInfoResponse> findCustomerBaseInfo() {
        String customerId = commonUtil.getOperatorId();
        CustomerVO customer =
                customerQueryProvider.getCustomerById(new CustomerGetByIdRequest(customerId)).getContext();

        CustomerLevelVO customerLevel = customerLevelQueryProvider.getCustomerLevelWithDefaultById(
                CustomerLevelWithDefaultByIdRequest.builder().customerLevelId(customer.getCustomerLevelId()).build())
                .getContext();

        EmployeeOptionalByIdRequest idRequest = new EmployeeOptionalByIdRequest();
        idRequest.setEmployeeId(customer.getCustomerDetail().getEmployeeId());

        EmployeeOptionalByIdResponse employee = new EmployeeOptionalByIdResponse();

        if (StringUtils.isNotBlank(customer.getCustomerDetail().getEmployeeId())) {
            employee = employeeQueryProvider.getOptionalById(idRequest).getContext();
        }

        //企业信息
        Boolean isEnterpriseCustomer = !EnterpriseCheckState.INIT.equals(customer.getEnterpriseCheckState());
        EnterpriseInfoVO enterpriseInfo=null;
        if(isEnterpriseCustomer){
            BaseResponse<EnterpriseInfoByCustomerIdResponse> enterpriseInfoResponse = enterpriseInfoQueryProvider.getByCustomerId(EnterpriseInfoByCustomerIdRequest.builder()
                    .customerId(customerId)
                    .build());
            if(Objects.nonNull(enterpriseInfoResponse.getContext())){
                enterpriseInfo=enterpriseInfoResponse.getContext().getEnterpriseInfoVO();
            }
        }

        return BaseResponse.success(CustomerBaseInfoResponse.builder()
                .customerDetailId(customer.getCustomerDetail().getCustomerDetailId())
                .customerId(customerId)
                .customerAccount(customer.getCustomerAccount())
                .customerName(customer.getCustomerDetail().getCustomerName())
                .customerLevelName(customerLevel.getCustomerLevelName())
                .provinceId(customer.getCustomerDetail().getProvinceId())
                .cityId(customer.getCustomerDetail().getCityId())
                .areaId(customer.getCustomerDetail().getAreaId())
                .streetId(customer.getCustomerDetail().getStreetId())
                .customerAddress(customer.getCustomerDetail().getCustomerAddress())
                .contactName(customer.getCustomerDetail().getContactName())
                .contactPhone(customer.getCustomerDetail().getContactPhone())
                .employeeName(employee.getEmployeeName())
                .birthDay(customer.getCustomerDetail().getBirthDay())
                .gender(customer.getCustomerDetail().getGender())
                .headImg(customer.getHeadImg())
                .isEnterpriseCustomer(isEnterpriseCustomer)
                .enterpriseInfo(enterpriseInfo)
                .build()
        );
    }

    /**
     * 修改会员基本信息
     *
     * @param customerEditRequest
     * @return
     */
    @ApiOperation(value = "修改会员基本信息")
    @RequestMapping(value = "/customerBase", method = RequestMethod.PUT)
    @GlobalTransactional
    public BaseResponse updateCustomerBaseInfo(@Valid @RequestBody CustomerBaseInfoRequest customerEditRequest) {
        if (StringUtils.isEmpty(customerEditRequest.getCustomerId())) {
            return BaseResponse.error("参数不正确");
        }

        //防止越权
        if (!commonUtil.getOperatorId().equals(customerEditRequest.getCustomerId())) {
            return BaseResponse.error("非法越权操作");
        }

        //”基本信息->联系方式”中的联系方式，在前端进行了格式校验，后端没有校验
        if (StringUtils.isNotEmpty(customerEditRequest.getContactPhone()) &&
                !Pattern.matches(commonUtil.REGEX_MOBILE, customerEditRequest.getContactPhone())) {
            return BaseResponse.error("手机号格式不正确");
        }

        CustomerDetailModifyRequest modifyRequest = new CustomerDetailModifyRequest();
        KsBeanUtil.copyProperties(customerEditRequest, modifyRequest);
        customerDetailProvider.modifyCustomerDetail(modifyRequest);

        webBaseProducerService.sendMQForModifyBaseInfo(modifyRequest);

        if (customerEditRequest.getAreaId() != null && customerEditRequest.getCustomerAddress() != null
                && customerEditRequest.getBirthDay() != null && customerEditRequest.getGender() != null) {
            //完善信息积分和成长值都没有获取过才考虑新增
            if (CollectionUtils.isEmpty(customerGrowthValueQueryProvider.page(CustomerGrowthValuePageRequest.builder()
                    .growthValueServiceType(GrowthValueServiceType.PERFECTINFO)
                    .customerId(customerEditRequest.getCustomerId()).type(OperateType.GROWTH).build()).getContext()
                    .getCustomerGrowthValueVOPage().getContent())
                    && CollectionUtils.isEmpty(customerPointsDetailQueryProvider.list(CustomerPointsDetailQueryRequest.builder()
                    .serviceType(PointsServiceType.PERFECTINFO).type(OperateType.GROWTH)
                    .customerId(customerEditRequest.getCustomerId()).build()).getContext().getCustomerPointsDetailVOList())) {
                // 增加成长值
                customerGrowthValueProvider.increaseGrowthValue(CustomerGrowthValueAddRequest.builder()
                        .customerId(commonUtil.getOperatorId())
                        .type(OperateType.GROWTH)
                        .serviceType(GrowthValueServiceType.PERFECTINFO)
                        .build());
                // 增加积分
                customerPointsDetailSaveProvider.add(CustomerPointsDetailAddRequest.builder()
                        .customerId(commonUtil.getOperatorId())
                        .type(OperateType.GROWTH)
                        .serviceType(PointsServiceType.PERFECTINFO)
                        .build());
            }

        }

        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 查询会员中心主页面数据
     *
     * @return
     */
    @ApiOperation(value = "查询会员中心主页面数据")
    @RequestMapping(value = "/customerCenter", method = RequestMethod.GET)
    public BaseResponse<CustomerCenterResponse> findCustomerCenterInfo() {
        String customerId = commonUtil.getOperatorId();
        CustomerGetByIdResponse customer = customerQueryProvider.getCustomerById(new CustomerGetByIdRequest
                (customerId)).getContext();
        CustomerLevelVO customerLevel = new CustomerLevelVO();
        if (Objects.nonNull(customer.getCustomerLevelId())) {
            customerLevel = customerLevelQueryProvider.getCustomerLevelWithDefaultById(
                    CustomerLevelWithDefaultByIdRequest.builder().customerLevelId(customer.getCustomerLevelId()).build())
                    .getContext();
        }

        CustomerCenterResponse customerCenterResponse = CustomerCenterResponse.builder()
                .customerId(customerId)
                .customerAccount(StringUtils.substring(customer.getCustomerAccount(), 0, 3).concat("****").concat(StringUtils.substring(customer.getCustomerAccount(), 7)))
                .customerName(customer.getCustomerDetail().getCustomerName())
                .customerLevelName(customerLevel.getCustomerLevelName())
                .growthValue(customer.getGrowthValue())
                .rankBadgeImg(customerLevel.getRankBadgeImg())
                .headImg(customer.getHeadImg())
                .pointsAvailable(customer.getPointsAvailable())
                .customerLabelList(new ArrayList<>())
                .build();
        if (EnterpriseCheckState.CHECKED.equals(customer.getEnterpriseCheckState())){
            customerCenterResponse.getCustomerLabelList().add(CustomerLabel.EnterpriseCustomer);
            IepSettingVO iepSettingInfo = commonUtil.getIepSettingInfo();
            customerCenterResponse.setEnterpriseCustomerName(iepSettingInfo.getEnterpriseCustomerName());
            customerCenterResponse.setEnterpriseCustomerLogo(iepSettingInfo.getEnterpriseCustomerLogo());
        }
        return BaseResponse.success(customerCenterResponse);
    }

    /**
     * 查询可用积分
     *
     * @return
     */
    @ApiOperation(value = "查询可用积分")
    @RequestMapping(value = "/getPointsAvailable", method = RequestMethod.GET)
    public BaseResponse<CustomerPointsAvailableByCustomerIdResponse> getPointsAvailable() {
//       String customerId = commonUtil.getOperatorId();
//        return customerQueryProvider.getPointsAvailable(new CustomerPointsAvailableByIdRequest
//                (customerId));
        String fanDengUserNo = commonUtil.getCustomer().getFanDengUserNo();
        Long currentPoint = externalProvider.
                getByUserNoPoint(FanDengPointRequest.builder().
                        userNo(fanDengUserNo).build()).getContext().getCurrentPoint();
        CustomerPointsAvailableByCustomerIdResponse  customerIdResponse = new CustomerPointsAvailableByCustomerIdResponse();
        customerIdResponse.setPointsAvailable(currentPoint);
        return BaseResponse.success(customerIdResponse);
    }

    /**
     * 会员中心查询会员绑定的手机号
     *
     * @return
     */
    @ApiOperation(value = "会员中心查询会员绑定的手机号")
    @RequestMapping(value = "/customerMobile", method = RequestMethod.GET)
    public BaseResponse<CustomerSafeResponse> findCustomerMobile() {
        String customerAccount = commonUtil.getOperator().getAccount();
        if (Objects.nonNull(customerAccount)) {
            return BaseResponse.success(CustomerSafeResponse.builder().customerAccount(customerAccount).build());
        }
        return BaseResponse.FAILED();
    }

    /**
     * 会员中心 修改绑定手机号 给原号码发送验证码
     *
     * @param customerAccount
     * @return
     */
    @ApiOperation(value = "会员中心 修改绑定手机号 给原号码发送验证码")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "customerAccount", value = "会员账户", required =
            true)
    @RequestMapping(value = "/customerVerified/{customerAccount}", method = RequestMethod.POST)
    public BaseResponse sendVerifiedCode(@PathVariable String customerAccount) {
        CustomerValidateSendMobileCodeRequest request = new CustomerValidateSendMobileCodeRequest();
        request.setMobile(customerAccount);
        //是否可以发送
        if (!customerSiteProvider.validateSendMobileCode(request).getContext().getResult()) {
            throw new SbcRuntimeException(SiteResultCode.ERROR_000016);
        }
        //验证原手机号状态
        String result = this.checkOldCustomerAccount(customerAccount);
        if (StringUtils.isNotEmpty(result)) {
            return BaseResponse.error(result);
        }
        CustomerSendMobileCodeRequest customerSendMobileCodeRequest = new CustomerSendMobileCodeRequest();
        customerSendMobileCodeRequest.setMobile(customerAccount);
        customerSendMobileCodeRequest.setRedisKey(CacheKeyConstant.YZM_MOBILE_OLD_KEY);
        customerSendMobileCodeRequest.setSmsTemplate(SmsTemplate.CHANGE_PHONE);
        //发送验证码
        if (Constants.yes.equals(customerSiteProvider.sendMobileCode(customerSendMobileCodeRequest).getContext().getResult())) {
            return BaseResponse.SUCCESSFUL();
        }
        return BaseResponse.FAILED();
    }

    /**
     * 验证原手机号状态
     *
     * @param customerAccount
     * @return
     */
    private String checkOldCustomerAccount(String customerAccount) {
        String result = "";
        NoDeleteCustomerGetByAccountResponse customer =
                customerQueryProvider.getNoDeleteCustomerByAccount(new NoDeleteCustomerGetByAccountRequest
                (customerAccount)).getContext();
        if (Objects.isNull(customer)) {
            result = "该账号不存在！";
        } else {
            //如果该手机号已存在
            CustomerDetailVO customerDetail = customer.getCustomerDetail();
            //是否禁用
            if (CustomerStatus.DISABLE.toValue() == customerDetail.getCustomerStatus().toValue()) {
                result = "该手机号已被禁用！";
            }
        }

        return result;
    }

    /**
     * 会员中心 修改绑定手机号 验证原号码发送的验证码
     *
     * @param mobileRequest
     * @return
     */
    @ApiOperation(value = "会员中心 修改绑定手机号 验证原号码发送的验证码")
    @RequestMapping(value = "/oldMobileCode", method = RequestMethod.POST)
    public BaseResponse<String> validateVerifiedCode(@RequestBody CustomerMobileRequest mobileRequest) {
        //验证原手机号状态
        String result = this.checkOldCustomerAccount(mobileRequest.getCustomerAccount());
        if (StringUtils.isNotEmpty(result)) {
            return BaseResponse.error(result);
        }
        //验证验证码
        String t_verifyCode =
                redisService.getString(CacheKeyConstant.YZM_MOBILE_OLD_KEY.concat(mobileRequest.getCustomerAccount()));
        if (t_verifyCode == null || (!t_verifyCode.equalsIgnoreCase(mobileRequest.getVerifyCode()))) {
            throw new SbcRuntimeException(CommonErrorCode.VERIFICATION_CODE_ERROR);
        }

        //为了最后修改新手机号码用
        redisService.setString(CacheKeyConstant.YZM_MOBILE_OLD_KEY_AGAIN.concat(mobileRequest.getVerifyCode()),
                mobileRequest.getVerifyCode());
        //删除验证码缓存
        redisService.delete(CacheKeyConstant.YZM_MOBILE_OLD_KEY.concat(mobileRequest.getCustomerAccount()));
        return BaseResponse.success(mobileRequest.getVerifyCode());
    }

    /**
     * 会员中心 修改绑定手机号
     * 1）验证新输入的手机号
     * 2）发送验证码给新手机号
     *
     * @param customerAccount
     * @return
     */
    @ApiOperation(value = "会员中心 修改绑定手机号 发送验证码给新手机号")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "customerAccount", value = "会员账户", required =
            true)
    @RequestMapping(value = "/newCustomerVerified/{customerAccount}", method = RequestMethod.POST)
    public BaseResponse sendVerifiedCodeNew(@PathVariable String customerAccount) {
        CustomerValidateSendMobileCodeRequest request = new CustomerValidateSendMobileCodeRequest();
        request.setMobile(customerAccount);
        //是否可以发送
        if (!customerSiteProvider.validateSendMobileCode(request).getContext().getResult()) {
            throw new SbcRuntimeException(SiteResultCode.ERROR_000016);
        }
        //验证新输入的手机号
        String result = this.checkCustomerAccount(customerAccount);
        if (StringUtils.isNotEmpty(result)) {
            return BaseResponse.error(result);
        }

        CustomerSendMobileCodeRequest customerSendMobileCodeRequest = new CustomerSendMobileCodeRequest();
        customerSendMobileCodeRequest.setMobile(customerAccount);
        customerSendMobileCodeRequest.setRedisKey(CacheKeyConstant.YZM_MOBILE_NEW_KEY);
        customerSendMobileCodeRequest.setSmsTemplate(SmsTemplate.CHANGE_PHONE);
        //发送验证码
        if (Constants.yes.equals(customerSiteProvider.sendMobileCode(customerSendMobileCodeRequest).getContext().getResult())) {
            return BaseResponse.SUCCESSFUL();
        }

        return BaseResponse.FAILED();
    }

    /**
     * 会员中心 修改绑定手机号
     * 1）验证新手机号码的验证码是否正确
     * 2）更换绑定手机号码
     *
     * @param mobileRequest
     * @return
     */
    @ApiOperation(value = "会员中心 修改绑定手机号")
    @RequestMapping(value = "/newMobileCode", method = RequestMethod.POST)
    public BaseResponse changeNewMobile(@RequestBody CustomerMobileRequest mobileRequest) {
        if (StringUtils.isEmpty(mobileRequest.getOldVerifyCode())) {
            return BaseResponse.error("操作失败");
        }

        //查询原验证码
        String oldVerifyCode =
                redisService.getString(CacheKeyConstant.YZM_MOBILE_OLD_KEY_AGAIN.concat(mobileRequest.getOldVerifyCode()));
        if (Objects.isNull(oldVerifyCode)) {
            return BaseResponse.error("操作失败");
        }

        String result = this.checkCustomerAccount(mobileRequest.getCustomerAccount());
        if (StringUtils.isNotEmpty(result)) {
            return BaseResponse.error(result);
        }

        //验证验证码
        String t_verifyCode =
                redisService.getString(CacheKeyConstant.YZM_MOBILE_NEW_KEY.concat(mobileRequest.getCustomerAccount()));
        if (t_verifyCode == null || (!t_verifyCode.equalsIgnoreCase(mobileRequest.getVerifyCode()))) {
            throw new SbcRuntimeException(CommonErrorCode.VERIFICATION_CODE_ERROR);
        }

        //更换绑定手机号码
//        if (Constants.yes == customerService.updateCustomerAccount(commonUtil.getOperatorId(), mobileRequest
//        .getCustomerAccount())) {
        if (Constants.yes == customerProvider.modifyCustomerAccount(new CustomerAccountModifyRequest(commonUtil
                .getOperatorId(), mobileRequest
                .getCustomerAccount())
        ).getContext().getCount()) {
            webBaseProducerService.sendMQForModifyCustomerAccount(commonUtil.getOperatorId(),mobileRequest.getCustomerAccount());
            //删除验证码缓存
            redisService.delete(CacheKeyConstant.YZM_MOBILE_NEW_KEY.concat(mobileRequest.getCustomerAccount()));
            redisService.delete(CacheKeyConstant.YZM_MOBILE_OLD_KEY_AGAIN.concat(mobileRequest.getOldVerifyCode()));

            return BaseResponse.SUCCESSFUL();
        }

        return BaseResponse.FAILED();
    }

    /**
     * 验证手机号码是否存在或禁用
     *
     * @param customerAccount
     * @return
     */
    private String checkCustomerAccount(String customerAccount) {
        //原手机号
        String customerAccountOld = commonUtil.getOperator().getAccount();

        String result = "";
//        Customer customer = customerService.findByCustomerAccountAndDelFlag(customerAccount);
        NoDeleteCustomerGetByAccountResponse customer =
                customerQueryProvider.getNoDeleteCustomerByAccount(new NoDeleteCustomerGetByAccountRequest
                (customerAccount)).getContext();
        if (Objects.nonNull(customer)) {
            //如果该手机号已存在
            CustomerDetailVO customerDetail = customer.getCustomerDetail();
            //是否禁用
            if (CustomerStatus.DISABLE.toValue() == customerDetail.getCustomerStatus().toValue()) {
                result = "该手机号已被禁用！";
            } else {
                //如果新手机号不是原手机号，则新手机号已被绑定
                if (!customerAccount.equals(customerAccountOld)) {
                    result = "该手机号已被绑定！";
                }
            }
        }
        return result;
    }

    /**
     * 根据用户ID查询用户详情
     *
     * @param
     * @return
     */
    @ApiOperation(value = "根据用户ID查询用户详情")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "customerId", value = "会员id", required = true)
    @RequestMapping(value = "/customerInfoById/{customerId}", method = RequestMethod.GET)
    public BaseResponse<CustomerDetailVO> getCustomerBaseInfo(@PathVariable String customerId) {
        if (StringUtils.isEmpty(customerId)) {
            return BaseResponse.error("参数不正确");
        }
        CustomerGetByIdResponse customer =
                customerQueryProvider.getCustomerById(new CustomerGetByIdRequest(customerId)).getContext();
        return BaseResponse.success(customer.getCustomerDetail());
    }

    /**
     * 验证token
     *
     * @return
     */
    @ApiOperation(value = "验证token")
    @RequestMapping(value = "/check-token", method = RequestMethod.GET)
    public BaseResponse checkToken() {
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 获取当前登录人信息
     *
     * @return
     */
    @ApiOperation(value = "获取当前登录人信息")
    @RequestMapping(value = "/getLoginCustomerInfo", method = RequestMethod.GET)
    public BaseResponse<CustomerGetByIdResponse> getLoginCustomerInfo() {
        String customerId = commonUtil.getOperatorId();
        return customerQueryProvider.getCustomerById(new CustomerGetByIdRequest(customerId));
    }

    /**
     * 获取完善信息可得成长值和积分
     *
     * @return
     */
    @ApiOperation(value = "获取当前登录人信息")
    @RequestMapping(value = "/getGrowthValueAndPoint", method = RequestMethod.GET)
    public BaseResponse<GrowthValueAndPointResponse> getGrowthValueAndPoint() {
        String customerId = commonUtil.getOperatorId();
        BaseResponse<CustomerGetByIdResponse> customerById = customerQueryProvider.getCustomerById(
                new CustomerGetByIdRequest(customerId));
        GrowthValueAndPointResponse response = new GrowthValueAndPointResponse();
        //判断完善信息获取积分设置是否开启
        ConfigQueryRequest pointsRequest = new ConfigQueryRequest();
        pointsRequest.setConfigType(ConfigType.POINTS_BASIC_RULE_COMPLETE_INFORMATION.toValue());
        pointsRequest.setDelFlag(DeleteFlag.NO.toValue());
        ConfigVO pointsConfig =
                systemConfigQueryProvider.findByConfigTypeAndDelFlag(pointsRequest).getContext().getConfig();
        if (pointsConfig != null && pointsConfig.getStatus() == 1) {
            response.setPointFlag(true);
            response.setPoint(this.getValue(pointsConfig.getContext()));
        } else {
            response.setPointFlag(false);
            response.setPoint(0L);
        }
        //判断完善信息获取成长值设置是否开启
        ConfigQueryRequest request = new ConfigQueryRequest();
        request.setConfigType(ConfigType.GROWTH_VALUE_BASIC_RULE_COMPLETE_INFORMATION.toValue());
        request.setDelFlag(DeleteFlag.NO.toValue());
        ConfigVO growthConfig = systemConfigQueryProvider.findByConfigTypeAndDelFlag(request).getContext().getConfig();
        if (growthConfig != null && growthConfig.getStatus() == 1) {
            response.setGrowthFlag(true);
            response.setGrowthValue(this.getValue(growthConfig.getContext()));
        } else {
            response.setGrowthFlag(false);
            response.setGrowthValue(0L);
        }
        //只要获取过完善有礼的任意成长值或积分奖励，将不再获得这些完善奖励
        if (CollectionUtils.isNotEmpty(customerGrowthValueQueryProvider.page(CustomerGrowthValuePageRequest.builder()
                .growthValueServiceType(GrowthValueServiceType.PERFECTINFO)
                .customerId(customerId).type(OperateType.GROWTH).build()).getContext()
                .getCustomerGrowthValueVOPage().getContent())
        ||CollectionUtils.isNotEmpty(customerPointsDetailQueryProvider.list(CustomerPointsDetailQueryRequest.builder()
                .serviceType(PointsServiceType.PERFECTINFO).type(OperateType.GROWTH)
                .customerId(customerId).build()).getContext().getCustomerPointsDetailVOList())) {
            response.setGrowthValue(0L);
            response.setPoint(0L);
        }
        return BaseResponse.success(response);
    }

    /**
     * 积分/成长值转换
     *
     * @param value
     * @return
     */
    private Long getValue(String value) {
        if (StringUtils.isNotBlank(value)) {
            return JSONObject.parseObject(value).getLong("value");
        } else {
            return null;
        }

    }

    /**
     * @description 是否是知识顾问
     * @menu 商城配合知识顾问
     * @tag feature_d_cps_v3
     * @status done
     */
    @ApiOperation(value = "是否是知识顾问")
    @RequestMapping(value = "/isCounselor", method = RequestMethod.POST)
    public BaseResponse<IsCounselorVo> isCounselor() {
        IsCounselorVo isCounselorVo = new IsCounselorVo();
        CustomerVO customer = commonUtil.getCanNullCustomer();
        if (customer == null || StringUtils.isEmpty(customer.getFanDengUserNo())) {
            isCounselorVo.setIsCounselor(Boolean.FALSE);
            return BaseResponse.success(isCounselorVo);
        }
        isCounselorVo.setCustomerId(customer.getCustomerId());
        CounselorDto counselorDto = customerProvider.isCounselor(Integer.valueOf(customer.getFanDengUserNo())).getContext();
        if (counselorDto == null) {
            isCounselorVo.setIsCounselor(Boolean.FALSE);
            return BaseResponse.success(isCounselorVo);
        }
        isCounselorVo.setIsCounselor(Boolean.TRUE);
        isCounselorVo.setProfit(counselorDto.getProfit());
        isCounselorVo.setCustomerId(customer.getCustomerId());
        return BaseResponse.success(isCounselorVo);
    }

}
