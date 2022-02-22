package com.wanmi.sbc.customer;

import com.alibaba.fastjson.JSONObject;
import com.google.code.kaptcha.Producer;
import com.soybean.mall.wx.mini.user.bean.request.WxGetUserPhoneAndOpenIdRequest;
import com.soybean.mall.wx.mini.user.bean.response.WxGetUserPhoneAndOpenIdResponse;
import com.soybean.mall.wx.mini.user.controller.WxUserApiController;
import com.wanmi.ms.autoconfigure.JwtProperties;
import com.wanmi.sbc.common.annotation.MultiSubmit;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.constant.MQConstant;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.TerminalType;
import com.wanmi.sbc.common.enums.VASConstants;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.redis.CacheKeyConstant;
import com.wanmi.sbc.common.util.*;
import com.wanmi.sbc.customer.api.provider.customer.CustomerProvider;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.provider.detail.CustomerDetailQueryProvider;
import com.wanmi.sbc.customer.api.provider.distribution.DistributionCustomerSaveProvider;
import com.wanmi.sbc.customer.api.provider.employee.EmployeeQueryProvider;
import com.wanmi.sbc.customer.api.provider.enterpriseinfo.EnterpriseInfoQueryProvider;
import com.wanmi.sbc.customer.api.provider.fandeng.ExternalProvider;
import com.wanmi.sbc.customer.api.provider.growthvalue.CustomerGrowthValueProvider;
import com.wanmi.sbc.customer.api.provider.loginregister.CustomerSiteProvider;
import com.wanmi.sbc.customer.api.provider.loginregister.CustomerSiteQueryProvider;
import com.wanmi.sbc.customer.api.provider.points.CustomerPointsDetailSaveProvider;
import com.wanmi.sbc.customer.api.request.customer.*;
import com.wanmi.sbc.customer.api.request.detail.CustomerDetailByCustomerIdRequest;
import com.wanmi.sbc.customer.api.request.detail.CustomerDetailWithNotDeleteByCustomerIdRequest;
import com.wanmi.sbc.customer.api.request.distribution.DistributionCustomerModifyCustomerIdRequest;
import com.wanmi.sbc.customer.api.request.employee.EmployeeByIdRequest;
import com.wanmi.sbc.customer.api.request.enterpriseinfo.EnterpriseInfoByCustomerIdRequest;
import com.wanmi.sbc.customer.api.request.fandeng.*;
import com.wanmi.sbc.customer.api.request.growthvalue.CustomerGrowthValueAddRequest;
import com.wanmi.sbc.customer.api.request.loginregister.*;
import com.wanmi.sbc.customer.api.request.loginregister.CustomerModifyRequest;
import com.wanmi.sbc.customer.api.request.points.CustomerPointsDetailAddRequest;
import com.wanmi.sbc.customer.api.response.customer.CustomerAccountModifyResponse;
import com.wanmi.sbc.customer.api.response.customer.CustomerGetByIdResponse;
import com.wanmi.sbc.customer.api.response.customer.NoDeleteCustomerGetByAccountResponse;
import com.wanmi.sbc.customer.api.response.employee.EmployeeByIdResponse;
import com.wanmi.sbc.customer.api.response.enterpriseinfo.EnterpriseInfoByCustomerIdResponse;
import com.wanmi.sbc.customer.api.response.fandeng.*;
import com.wanmi.sbc.customer.api.response.loginregister.*;
import com.wanmi.sbc.customer.bean.dto.CustomerDTO;
import com.wanmi.sbc.customer.bean.enums.*;
import com.wanmi.sbc.customer.bean.vo.CustomerDetailVO;
import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import com.wanmi.sbc.customer.bean.vo.DistributionCustomerVO;
import com.wanmi.sbc.customer.request.LoginRequest;
import com.wanmi.sbc.customer.request.LoginVerificationCodeRequest;
import com.wanmi.sbc.customer.request.RegisterRequest;
import com.wanmi.sbc.customer.response.LoginResponse;
import com.wanmi.sbc.customer.response.RegisterResponse;
import com.wanmi.sbc.customer.service.DistributionInviteNewService;
import com.wanmi.sbc.customer.service.LoginBaseService;
import com.wanmi.sbc.customer.validGroups.*;
import com.wanmi.sbc.distribute.DistributionCacheService;
import com.wanmi.sbc.goods.api.provider.mini.goods.WxMiniGoodsProvider;
import com.wanmi.sbc.marketing.api.provider.coupon.CouponActivityProvider;
import com.wanmi.sbc.marketing.api.request.coupon.GetCouponGroupRequest;
import com.wanmi.sbc.marketing.api.response.coupon.GetRegisterOrStoreCouponResponse;
import com.wanmi.sbc.marketing.bean.constant.Constant;
import com.wanmi.sbc.marketing.bean.enums.CouponActivityType;
import com.wanmi.sbc.marketing.bean.enums.RegisterLimitType;
import com.wanmi.sbc.mq.producer.WebBaseProducerService;
import com.wanmi.sbc.order.api.provider.trade.TradeQueryProvider;
import com.wanmi.sbc.order.api.request.trade.TradePageCriteriaRequest;
import com.wanmi.sbc.order.api.response.trade.TradePageCriteriaResponse;
import com.wanmi.sbc.order.bean.dto.TradeQueryDTO;
import com.wanmi.sbc.order.bean.vo.TradeVO;
import com.wanmi.sbc.redis.RedisService;
import com.wanmi.sbc.setting.api.provider.AuditQueryProvider;
import com.wanmi.sbc.setting.api.response.UserAuditResponse;
import com.wanmi.sbc.setting.bean.enums.ConfigKey;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.vas.bean.vo.IepSettingVO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.compression.CompressionCodecs;
import io.seata.spring.annotation.GlobalTransactional;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * 会员
 * Created by Daiyitian on 2017/4/19.
 */
@RestController
@Slf4j
@Api(tags = "LoginBaseController", description = "S2B web公用-会员登录信息API")
public class LoginBaseController {

    @Autowired
    private CustomerSiteQueryProvider customerSiteQueryProvider;

    @Autowired
    private CustomerSiteProvider customerSiteProvider;

    @Autowired
    private CustomerQueryProvider customerQueryProvider;

    @Autowired
    private CustomerProvider customerProvider;

    @Autowired
    private CustomerDetailQueryProvider customerDetailQueryProvider;

    @Autowired
    private EmployeeQueryProvider employeeQueryProvider;

    @Autowired
    private RedisService redisService;

    @Value("${jwt.secret-key}")
    private String jwtSecretKey;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private Producer captchaProducer;

    @Autowired
    private AuditQueryProvider auditQueryProvider;

    @Autowired
    private CouponActivityProvider couponActivityProvider;

    @Autowired
    private CustomerGrowthValueProvider customerGrowthValueProvider;

    /**
     * 注入分销邀新service
     */
    @Autowired
    private DistributionInviteNewService distributionInviteNewService;

    @Autowired
    private CustomerPointsDetailSaveProvider customerPointsDetailSaveProvider;

    @Autowired
    private LoginBaseService loginBaseService;

    @Autowired
    private DistributionCacheService distributionCacheService;

    @Autowired
    private EnterpriseInfoQueryProvider enterpriseInfoQueryProvider;

    @Autowired
    private WebBaseProducerService webBaseProducerService;
    @Autowired
    private ExternalProvider externalProvider;
    @Autowired
    private TradeQueryProvider tradeQueryProvider;
    @Autowired
    private DistributionCustomerSaveProvider distributionCustomerSaveProvider;
//    @Autowired
//    private WxUserApiController wxUserApiController;
    @Autowired
    private WxMiniGoodsProvider wxMiniGoodsProvider;

    /**
     * 注册用户
     * 审核开关关闭并且信息完善开关关闭，则直接登录
     * 其他情况则跳转到对应页面
     *
     * @param registerRequest
     * @return LoginResponse
     */
    @ApiOperation(value = "注册用户")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    // @Transactional
    @GlobalTransactional
    public BaseResponse<LoginResponse> register(@Validated({NotCustomerAccount.class, NotPassword.class, NotVerify
            .class}) @RequestBody RegisterRequest registerRequest) {
        log.info("enter register method ......");

        // 校验及封装校验后请求参数
        String smsKey = CacheKeyConstant.VERIFY_CODE_KEY.concat(registerRequest.getCustomerAccount());
        // 校验短信验证码
        this.verifySmsCode(registerRequest);
        DistributionCustomerVO distributionCustomerVO = new DistributionCustomerVO();
        // 校验邀请码,若为pc端普通会员注册，无分销则不进行校验
        if (!TerminalType.PC.equals(registerRequest.getTerminalType())) {
            distributionCustomerVO =
                    loginBaseService.checkInviteIdAndInviteCode(registerRequest.getInviteeId(),
                            registerRequest.getInviteCode());
        }

        CustomerDTO customer = new CustomerDTO();
        customer.setCustomerAccount(registerRequest.getCustomerAccount());
        customer.setCustomerPassword(registerRequest.getCustomerPassword());
        if (StringUtils.isNotBlank(registerRequest.getEmployeeId())) {
            EmployeeByIdRequest idRequest = new EmployeeByIdRequest();
            idRequest.setEmployeeId(registerRequest.getEmployeeId());
            if (Objects.isNull(employeeQueryProvider.getById(idRequest).getContext())) {
                registerRequest.setEmployeeId(null);
            }
        }
        CustomerRegisterRequest customerRegisterRequest = new CustomerRegisterRequest();
        customerRegisterRequest.setEmployeeId(registerRequest.getEmployeeId());
        customerRegisterRequest.setCustomerDTO(customer);
        BaseResponse<CustomerRegisterResponse> customerRegisterResponseBaseResponse = customerSiteProvider.register(customerRegisterRequest);
        CustomerVO customerVO = customerRegisterResponseBaseResponse.getContext();
        if (customerVO != null) {
            log.info("register customer success ......注册信息详情：{}", customerVO);
            //删除验证码缓存
            redisService.delete(smsKey);
            redisService.hdelete(CacheKeyConstant.KAPTCHA_KEY, registerRequest.getUuid());
            LoginResponse loginResponse = commonUtil.getLoginResponse(customerVO, jwtSecretKey);
            //审核开关关闭并且信息完善开关关闭，则直接登录
            boolean isPerfectCustomer = auditQueryProvider.isPerfectCustomerInfo().getContext().isPerfect();
            boolean isCustomerAudit = auditQueryProvider.isCustomerAudit().getContext().isAudit();
            if (!isPerfectCustomer && !isCustomerAudit) {
                log.info("to home page ......");
                loginResponse.setIsLoginFlag(Boolean.TRUE);

                // 领取注册赠券
                GetRegisterOrStoreCouponResponse couponResponse = this.getCouponGroup(customerVO.getCustomerId(), CouponActivityType.REGISTERED_COUPON, Constant.BOSS_DEFAULT_STORE_ID);
                //有注册赠券，则设置参数
                if (couponResponse != null) {
                    loginResponse.setCouponResponse(couponResponse);
                }
            } else {

                // 领取注册赠券 ,如果完善信息开关打开则不能参加注册赠券活动
                if (!isPerfectCustomer) {
                    GetRegisterOrStoreCouponResponse couponResponse = this.getCouponGroup(customerVO.getCustomerId(), CouponActivityType.REGISTERED_COUPON, Constant.BOSS_DEFAULT_STORE_ID);
                    loginResponse.setCouponResponse(couponResponse);
                }
                log.info("to information improve page ......");
                loginResponse.setCustomerId(customerVO.getCustomerId());
                loginResponse.setIsLoginFlag(Boolean.FALSE);

            }
            webBaseProducerService.sendMQForCustomerRegister(customerVO);

            // 新增邀新记录/成长值/积分等
            this.addOtherInfos(customerVO, distributionCustomerVO, registerRequest);

            return BaseResponse.success(loginResponse);
        }
        log.info("register customer failed ......");
        return BaseResponse.FAILED();
    }

    @ApiOperation(value = "注册企业用户")
    @RequestMapping(value = "/registerEnterprise", method = RequestMethod.POST)
    @Transactional
    @GlobalTransactional
    public BaseResponse registerEnterprise(@RequestBody RegisterRequest registerRequest) {
        log.info("enter registerEnterprise method ......");

        // 首先判断是否购买企业购增值服务,未购买或未设置自动抛出异常
        IepSettingVO iepSettingVO = commonUtil.getIepSettingInfo();
        if (Objects.isNull(iepSettingVO.getEnterpriseCustomerAuditFlag())) {
            throw new SbcRuntimeException(SiteResultCode.ERROR_040018);
        }

        if (StringUtils.isNotEmpty(registerRequest.getCustomerId())) {

            // 被驳回后再次注册
            LoginResponse loginResponse = this.registerAgain(registerRequest, iepSettingVO);
            return BaseResponse.success(loginResponse);

        } else {

            // 首次注册校验及封装校验后请求参数
            if (StringUtils.isBlank(registerRequest.getCustomerAccount()) || StringUtils.isBlank(registerRequest.getCustomerPassword())
                    || StringUtils.isBlank(registerRequest.getVerifyCode())) {
                throw new SbcRuntimeException(SiteResultCode.ERROR_000009);
            }
            // 校验邀请码
            DistributionCustomerVO distributionCustomerVO =
                    loginBaseService.checkInviteIdAndInviteCode(registerRequest.getInviteeId(),
                            registerRequest.getInviteCode());

            // 如果是第一次请求，返回成功，校验通过进入公司信息页面
            if (Objects.nonNull(registerRequest.getFirstRegisterFlag()) && registerRequest.getFirstRegisterFlag()) {

                // 校验短信验证码
                this.verifySmsCode(registerRequest);
                //删除验证码缓存
                String smsKey = CacheKeyConstant.VERIFY_CODE_KEY.concat(registerRequest.getCustomerAccount());
                redisService.delete(smsKey);
                redisService.hdelete(CacheKeyConstant.KAPTCHA_KEY, registerRequest.getUuid());
                return BaseResponse.success(Boolean.TRUE);
            }

            // 校验公司信息参数
            if (StringUtils.isBlank(registerRequest.getEnterpriseName()) || Objects.isNull(registerRequest.getBusinessNatureType())
                    || StringUtils.isBlank(registerRequest.getSocialCreditCode()) || StringUtils.isBlank(registerRequest.getBusinessLicenseUrl())) {
                throw new SbcRuntimeException(SiteResultCode.ERROR_000009);
            }

            CustomerDTO customer = new CustomerDTO();
            customer.setCustomerAccount(registerRequest.getCustomerAccount());
            customer.setCustomerPassword(registerRequest.getCustomerPassword());
            if (StringUtils.isNotBlank(registerRequest.getEmployeeId())) {
                EmployeeByIdRequest idRequest = new EmployeeByIdRequest();
                idRequest.setEmployeeId(registerRequest.getEmployeeId());
                if (Objects.isNull(employeeQueryProvider.getById(idRequest).getContext())) {
                    registerRequest.setEmployeeId(null);
                }
            }

            CustomerRegisterRequest customerRegisterRequest =
                    CustomerRegisterRequest.builder().employeeId(registerRequest.getEmployeeId()).customerDTO(customer)
                            .enterpriseName(registerRequest.getEnterpriseName()).businessNatureType(registerRequest.getBusinessNatureType())
                            .socialCreditCode(registerRequest.getSocialCreditCode()).businessLicenseUrl(registerRequest.getBusinessLicenseUrl())
                            .enterpriseCustomerAuditFlag(iepSettingVO.getEnterpriseCustomerAuditFlag()).build();

            BaseResponse<CustomerRegisterResponse> customerRegisterResponseBaseResponse = customerSiteProvider.registerEnterprise(customerRegisterRequest);
            CustomerVO customerVO = customerRegisterResponseBaseResponse.getContext();

            if (Objects.nonNull(customerVO)) {
                log.info("register enterpriseCustomer success ......企业用户注册信息详情：{}", customerVO);

                LoginResponse loginResponse = commonUtil.getLoginResponse(customerVO, jwtSecretKey);
                // 根据审核开关状态判断
                if (DefaultFlag.NO.equals(iepSettingVO.getEnterpriseCustomerAuditFlag())) {
                    // 无需审核
                    log.info("to home page ......");
                    loginResponse.setIsLoginFlag(Boolean.TRUE);

                    // 领取企业会员注册赠券
                    GetRegisterOrStoreCouponResponse couponResponse = this.getCouponGroup(customerVO.getCustomerId(), CouponActivityType.ENTERPRISE_REGISTERED_COUPON, Constant.BOSS_DEFAULT_STORE_ID);
                    //有注册赠券，则设置参数
                    if (couponResponse != null) {
                        loginResponse.setCouponResponse(couponResponse);
                    }
                } else {
                    // 领取企业会员注册赠券 ,参加注册赠券活动
                    GetRegisterOrStoreCouponResponse couponResponse = this.getCouponGroup(customerVO.getCustomerId(), CouponActivityType.ENTERPRISE_REGISTERED_COUPON, Constant.BOSS_DEFAULT_STORE_ID);
                    loginResponse.setCouponResponse(couponResponse);
                    log.info("information to be confirmed  ......");
                    loginResponse.setCustomerId(customerVO.getCustomerId());
                    loginResponse.setIsLoginFlag(Boolean.FALSE);
                }

                webBaseProducerService.sendMQForCustomerRegister(customerVO);

                // 新增邀新记录/成长值/积分等
                this.addOtherInfos(customerVO, distributionCustomerVO, registerRequest);

                return BaseResponse.success(loginResponse);
            }
        }

        log.info("register enterpriseCustomer failed ......");
        return BaseResponse.FAILED();
    }

    /**
     * 企业会员审核被驳回后再次注册
     *
     * @param registerRequest
     * @param iepSettingVO
     * @return
     */
    public LoginResponse registerAgain(RegisterRequest registerRequest, IepSettingVO iepSettingVO) {

        // 校验公司信息参数
        if (StringUtils.isBlank(registerRequest.getEnterpriseName()) || Objects.isNull(registerRequest.getBusinessNatureType())
                || StringUtils.isBlank(registerRequest.getSocialCreditCode()) || StringUtils.isBlank(registerRequest.getBusinessLicenseUrl())) {
            throw new SbcRuntimeException(SiteResultCode.ERROR_000009);
        }

        CustomerRegisterRequest customerRegisterRequest =
                CustomerRegisterRequest.builder().employeeId(registerRequest.getEmployeeId()).customerDTO(new CustomerDTO()).customerId(registerRequest.getCustomerId())
                        .enterpriseName(registerRequest.getEnterpriseName()).businessNatureType(registerRequest.getBusinessNatureType())
                        .socialCreditCode(registerRequest.getSocialCreditCode()).businessLicenseUrl(registerRequest.getBusinessLicenseUrl())
                        .enterpriseCustomerAuditFlag(iepSettingVO.getEnterpriseCustomerAuditFlag()).build();
        // 再次注册
        CustomerVO customerVO =
                customerSiteProvider.registerEnterpriseAgain(customerRegisterRequest).getContext();

        LoginResponse loginResponse = new LoginResponse();
        if (Objects.nonNull(customerVO)) {
            loginResponse = commonUtil.getLoginResponse(customerVO, jwtSecretKey);
            if (DefaultFlag.NO.equals(customerRegisterRequest.getEnterpriseCustomerAuditFlag())) {
                // 无需审核
                log.info("to home page ......");
                loginResponse.setIsLoginFlag(Boolean.TRUE);

            } else {
                log.info("information to be confirmed  ......");
                loginResponse.setCustomerId(customerVO.getCustomerId());
                loginResponse.setIsLoginFlag(Boolean.FALSE);
            }
        }
        return loginResponse;
    }

    /**
     * 校验请求短信验证码参数
     */
    public void verifySmsCode(RegisterRequest registerRequest) {

        //累计验证错误次数
        String errKey = CacheKeyConstant.REGISTER_ERR.concat(registerRequest.getCustomerAccount());
        String smsKey = CacheKeyConstant.VERIFY_CODE_KEY.concat(registerRequest.getCustomerAccount());
        if (NumberUtils.toInt(redisService.getString(errKey)) >= 3) {
            redisService.delete(smsKey);
            throw new SbcRuntimeException(SiteResultCode.ERROR_000016);
        }

        //验证验证码
        String t_verifyCode = redisService.getString(smsKey);
        if (t_verifyCode == null || (!t_verifyCode.equalsIgnoreCase(registerRequest.getVerifyCode()))) {
            redisService.incrKey(errKey);
            redisService.expireBySeconds(errKey, 60L);
            throw new SbcRuntimeException(SiteResultCode.ERROR_000010);
        }
    }

    /**
     * 新增邀新记录/成长值/积分等
     */
    public void addOtherInfos(CustomerVO customerVO, DistributionCustomerVO distributionCustomerVO,
                              RegisterRequest registerRequest) {

        //新增分销员信息
        loginBaseService.addDistributionCustomer(customerVO.getCustomerId(), customerVO.getCustomerAccount(),
                customerVO.getCustomerDetail().getCustomerName(), StringUtils.isNotBlank(distributionCustomerVO.getInviteCustomerIds()) ?
                        StringUtils.join(distributionCustomerVO.getCustomerId(), ",", StringUtils.split(distributionCustomerVO.getInviteCustomerIds(), ",")[0]) :
                        distributionCustomerVO.getCustomerId()
        );

        // 新增邀新记录
        distributionInviteNewService.addRegisterInviteNewRecord(customerVO.getCustomerId(),
                distributionCustomerVO.getCustomerId(), commonUtil.getTerminal());

        // 增加成长值
        customerGrowthValueProvider.increaseGrowthValue(CustomerGrowthValueAddRequest.builder()
                .customerId(customerVO.getCustomerId())
                .type(OperateType.GROWTH)
                .serviceType(GrowthValueServiceType.REGISTER)
                .build());
        // 增加积分
        customerPointsDetailSaveProvider.add(CustomerPointsDetailAddRequest.builder()
                .customerId(customerVO.getCustomerId())
                .type(OperateType.GROWTH)
                .serviceType(PointsServiceType.REGISTER)
                .build());

        String shareUserId = StringUtils.isNotBlank(registerRequest.getShareUserId()) ? registerRequest.getShareUserId() : distributionCustomerVO.getCustomerId();
        // 分享注册增加成长值积分
        if (StringUtils.isNotBlank(shareUserId)) {
            customerGrowthValueProvider.increaseGrowthValue(
                    CustomerGrowthValueAddRequest.builder()
                            .customerId(shareUserId)
                            .type(OperateType.GROWTH)
                            .serviceType(GrowthValueServiceType.SHAREREGISTER)
                            .build());
            customerPointsDetailSaveProvider.add(CustomerPointsDetailAddRequest.builder()
                    .customerId(shareUserId)
                    .type(OperateType.GROWTH)
                    .serviceType(PointsServiceType.SHAREREGISTER)
                    .build());
        }
    }

    /**
     * 会员登录
     *
     * @param loginRequest
     * @return LoginResponse
     */
    @ApiOperation(value = "会员登录")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public BaseResponse<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("enter account&password login method ......");
        CustomerLoginRequest request = new CustomerLoginRequest();
        request.setCustomerAccount(new String(Base64.getUrlDecoder().decode(loginRequest.getCustomerAccount()
                .getBytes())));
        request.setPassword(new String(Base64.getUrlDecoder().decode(loginRequest.getCustomerPassword()
                .getBytes())));
        BaseResponse<CustomerLoginResponse> customerLoginResponseBaseResponse = customerSiteQueryProvider.login(request);

        CustomerLoginResponse customerLoginResponse = customerLoginResponseBaseResponse.getContext();
        LoginResponse loginResponse = LoginResponse.builder().build();
        if (Objects.nonNull(customerLoginResponse)) {
            CustomerVO customerVO = new CustomerVO();
            KsBeanUtil.copyPropertiesThird(customerLoginResponse, customerVO);
            //返回值
            loginResponse = commonUtil.getLoginResponse(customerVO, jwtSecretKey);
            loginResponse.setNewFlag(Boolean.FALSE);
            if (Objects.isNull(customerLoginResponse.getLoginTime())) {
                loginResponse.setNewFlag(Boolean.TRUE);
            }
        }
        log.info("login success ......");
        return BaseResponse.success(loginResponse);
    }

    /**
     * 查询单条会员信息
     *
     * @param customerId
     * @return
     */
    @ApiOperation(value = "查询单条会员信息")
    @RequestMapping(value = "/enterprise/customer/{customerId}", method = RequestMethod.GET)
    public BaseResponse<CustomerGetByIdResponse> findById(@PathVariable String customerId) {
        CustomerGetByIdResponse customer =
                customerQueryProvider.getCustomerById(new CustomerGetByIdRequest(customerId)).getContext();
        //查询企业信息
        if (commonUtil.findVASBuyOrNot(VASConstants.VAS_IEP_SETTING)) {
            BaseResponse<EnterpriseInfoByCustomerIdResponse> enterpriseInfo = enterpriseInfoQueryProvider.getByCustomerId(EnterpriseInfoByCustomerIdRequest.builder()
                    .customerId(customerId)
                    .build());
            if (Objects.nonNull(enterpriseInfo.getContext())) {
                customer.setEnterpriseInfoVO(enterpriseInfo.getContext().getEnterpriseInfoVO());
            }
        }

        if (Objects.nonNull(customer)) {
            CustomerVO customerVO = new CustomerVO();
            KsBeanUtil.copyPropertiesThird(customer, customerVO);
            customer.setInviteCode(commonUtil.getLoginResponse(customerVO, jwtSecretKey).getInviteCode());
        }

        return BaseResponse.success(customer);
    }

    /**
     * 验证图片验证码
     *
     * @param registerRequest
     * @return
     */
    @ApiOperation(value = "验证图片验证码")
    @RequestMapping(value = "/patchca", method = RequestMethod.POST)
    public BaseResponse patchca(@Validated(NotPatchca.class) @RequestBody RegisterRequest registerRequest) {
        if (registerRequest.getPatchca().equalsIgnoreCase(redisService.hget(CacheKeyConstant.KAPTCHA_KEY,
                registerRequest.getUuid()))) {
            return BaseResponse.SUCCESSFUL();
        }
        return BaseResponse.FAILED();
    }

    /**
     * 获取验证
     *
     * @param uuid
     * @return
     */
    @ApiOperation(value = "获取验证码")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "uuid", value = "图片验证码key", required = true)
    @RequestMapping(value = "/patchca/{uuid}", method = RequestMethod.GET)
    public BaseResponse<String> patchca(@PathVariable("uuid") String uuid) {
        String code = captchaProducer.createText().toUpperCase();
        redisService.hset(CacheKeyConstant.KAPTCHA_KEY, uuid, code);
        ByteArrayOutputStream os = null;
        try {
            os = new ByteArrayOutputStream();
            ImageIO.write(captchaProducer.createImage(code), "png", os);
            return BaseResponse.success("data:image/png;base64,".concat(Base64.getEncoder().encodeToString(os
                    .toByteArray())));
        } catch (Exception ex) {
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (Exception e) {
                }
            }
        }
        return BaseResponse.FAILED();
    }

    /**
     * 完善用户信息
     *
     * @param registerRequest
     * @return LoginResponse
     */
    @ApiOperation(value = "完善用户信息")
    @RequestMapping(value = "/perfect", method = RequestMethod.POST)
    public BaseResponse<LoginResponse> perfect(@Validated(NotCustomerId.class) @RequestBody RegisterRequest
                                                       registerRequest) {
        log.info("enter information improve perfect method ......");
        //需要完善信息
        CustomerConsummateRegisterRequest customerConsummateRegisterRequest = new CustomerConsummateRegisterRequest();
        KsBeanUtil.copyProperties(registerRequest, customerConsummateRegisterRequest);
        BaseResponse<CustomerConsummateRegisterResponse> customerConsummateRegisterResponseBaseResponse = customerSiteProvider.registerConsummate(customerConsummateRegisterRequest);
        CustomerConsummateRegisterResponse customerConsummateRegisterResponse = customerConsummateRegisterResponseBaseResponse.getContext();
        if (Objects.nonNull(customerConsummateRegisterResponse)) {
            LoginResponse loginResponse = new LoginResponse();
            CustomerVO customerVO = new CustomerVO();
            KsBeanUtil.copyPropertiesThird(customerConsummateRegisterResponse, customerVO);

            //审核开关关闭并且信息完善开关关闭，则直接登录
            if (!auditQueryProvider.isCustomerAudit().getContext().isAudit()) {
                log.info("to home page ......");

                loginResponse = commonUtil.getLoginResponse(customerVO, jwtSecretKey);
                loginResponse.setIsLoginFlag(Boolean.TRUE);

                // 领取注册赠券
                GetRegisterOrStoreCouponResponse couponResponse = this.getCouponGroup(customerVO.getCustomerId(), CouponActivityType.REGISTERED_COUPON, Constant.BOSS_DEFAULT_STORE_ID);
                loginResponse.setCouponResponse(couponResponse);
            } else {
                log.info("to information improve page ......");
                loginResponse.setCustomerId(customerConsummateRegisterResponse.getCustomerId());
                loginResponse.setIsLoginFlag(Boolean.FALSE);

                // 完善信息之后，尝试领取注册赠券
                if (customerConsummateRegisterResponse.getCheckState() == CheckState.WAIT_CHECK) {
                    // 领取注册赠券
                    GetRegisterOrStoreCouponResponse couponResponse = this.getCouponGroup(customerConsummateRegisterResponse.getCustomerId(), CouponActivityType.REGISTERED_COUPON, Constant.BOSS_DEFAULT_STORE_ID);
                    loginResponse.setCouponResponse(couponResponse);
                }
            }

            webBaseProducerService.sendMQForCustomerRegister(customerVO);
            return BaseResponse.success(loginResponse);
        }
        return BaseResponse.FAILED();
    }

    /**
     * 检测用户名是否存在
     *
     * @return LoginResponse
     */
    @ApiOperation(value = "检测用户名是否存在")
    @RequestMapping(value = "/checkAccount", method = RequestMethod.POST)
    public BaseResponse<Integer> checkAccount(@Validated(NotCustomerAccount.class) @RequestBody RegisterRequest
                                                      registerRequest) {
        CustomerByAccountRequest request = new CustomerByAccountRequest();
        request.setCustomerAccount(registerRequest.getCustomerAccount());
        BaseResponse<CustomerByAccountResponse> responseBaseResponse = customerSiteQueryProvider.getCustomerByCustomerAccount(request);
        CustomerByAccountResponse response = responseBaseResponse.getContext();
        if (Objects.nonNull(response)) {
            return BaseResponse.success(Constants.yes);
        }
        return BaseResponse.success(Constants.no);
    }

    /**
     * 发送验证码(注册时)
     *
     * @param registerRequest
     * @return LoginResponse
     */
    @ApiOperation(value = "注册时发送验证码")
    @RequestMapping(value = "/checkSmsByRegister", method = RequestMethod.POST)
    public BaseResponse checkSmsByRegister(@Validated({NotCustomerAccount.class}) @RequestBody RegisterRequest
                                                   registerRequest) {
        //同一个手机是否操作频繁
        CustomerValidateSendMobileCodeRequest request = new CustomerValidateSendMobileCodeRequest();
        request.setMobile(registerRequest.getCustomerAccount());
        if (!customerSiteProvider.validateSendMobileCode(request).getContext().getResult()) {
            throw new SbcRuntimeException(SiteResultCode.ERROR_000016);
        }
        CustomerByAccountRequest customerByAccountRequest = new CustomerByAccountRequest();
        customerByAccountRequest.setCustomerAccount(registerRequest.getCustomerAccount());
        BaseResponse<CustomerByAccountResponse> responseBaseResponse = customerSiteQueryProvider.getCustomerByCustomerAccount(customerByAccountRequest);
        CustomerByAccountResponse customerByAccountResponse = responseBaseResponse.getContext();
        if (customerByAccountResponse != null) {
            CustomerDetailByAccountRequest customerDetailByAccountRequest = new CustomerDetailByAccountRequest();
            customerDetailByAccountRequest.setCustomerAccount(customerByAccountResponse.getCustomerAccount());
            BaseResponse<CustomerDetailByAccountResponse> customerDetailByAccountResponseBaseResponse = customerSiteQueryProvider.getCustomerDetailByCustomerAccount(customerDetailByAccountRequest);
            CustomerDetailByAccountResponse customerDetailByAccountResponse = customerDetailByAccountResponseBaseResponse.getContext();
            if (Objects.nonNull(customerDetailByAccountResponse)) {
                if (CustomerStatus.DISABLE.toValue() == customerDetailByAccountResponse.getCustomerStatus().toValue()) {
                    throw new SbcRuntimeException(SiteResultCode.ERROR_010002, new Object[]{"，原因为：" + customerDetailByAccountResponse
                            .getForbidReason()});
                }
            }
            throw new SbcRuntimeException(SiteResultCode.ERROR_010101);
        }
        CustomerSendMobileCodeRequest customerSendMobileCodeRequest = new CustomerSendMobileCodeRequest();
        customerSendMobileCodeRequest.setMobile(registerRequest.getCustomerAccount());
        customerSendMobileCodeRequest.setRedisKey(CacheKeyConstant.VERIFY_CODE_KEY);
        customerSendMobileCodeRequest.setSmsTemplate(SmsTemplate.REGISTRY);
        //发送验证码
        if (Constants.yes.equals(customerSiteProvider.sendMobileCode(customerSendMobileCodeRequest).getContext().getResult())) {
            //59秒
            redisService.expireByMinutes(CacheKeyConstant.KAPTCHA_KEY.concat(registerRequest.getUuid()), 1L);
            return BaseResponse.SUCCESSFUL();
        }
        return BaseResponse.FAILED();
    }

    /**
     * 发送验证码（忘记密码时）
     *
     * @param registerRequest
     * @return LoginResponse
     */
    @ApiOperation(value = "忘记密码时发送验证码")
    @RequestMapping(value = "/checkSmsByForgot", method = RequestMethod.POST)
    public BaseResponse checkSmsByForgot(@Validated(NotCustomerAccount.class) @RequestBody RegisterRequest
                                                 registerRequest) {
        CustomerValidateSendMobileCodeRequest customerValidateSendMobileCodeRequest = new CustomerValidateSendMobileCodeRequest();
        customerValidateSendMobileCodeRequest.setMobile(registerRequest.getCustomerAccount());
        //是否可以发送
        if (!customerSiteProvider.validateSendMobileCode(customerValidateSendMobileCodeRequest).getContext().getResult()) {
            throw new SbcRuntimeException(SiteResultCode.ERROR_000016);
        }

        CustomerByAccountRequest request = new CustomerByAccountRequest();
        request.setCustomerAccount(registerRequest.getCustomerAccount());
        BaseResponse<CustomerByAccountResponse> responseBaseResponse = customerSiteQueryProvider.getCustomerByCustomerAccount(request);
        CustomerByAccountResponse response = responseBaseResponse.getContext();
        if (response == null) {
            throw new SbcRuntimeException(SiteResultCode.ERROR_010005);
        }

        CustomerDetailVO customerDetail = this.findCustomerDetailByCustomerId(response.getCustomerId());
        if (customerDetail == null) {
            throw new SbcRuntimeException(SiteResultCode.ERROR_010005);
        }

        //是否禁用
        if (CustomerStatus.DISABLE.toValue() == customerDetail.getCustomerStatus().toValue()) {
            throw new SbcRuntimeException(SiteResultCode.ERROR_010002, new Object[]{"，原因为：" + customerDetail
                    .getForbidReason()});
        }

        CustomerSendMobileCodeRequest customerSendMobileCodeRequest = new CustomerSendMobileCodeRequest();
        customerSendMobileCodeRequest.setMobile(registerRequest.getCustomerAccount());
        customerSendMobileCodeRequest.setRedisKey(registerRequest.getIsForgetPassword() != null && registerRequest.getIsForgetPassword() ?
                CacheKeyConstant.YZM_FORGET_PWD_KEY : CacheKeyConstant.YZM_UPDATE_PWD_KEY);
        customerSendMobileCodeRequest.setSmsTemplate(registerRequest.getIsForgetPassword() != null && registerRequest.getIsForgetPassword() ? SmsTemplate
                .REFUND_PASSWORD : SmsTemplate.CHANGE_PASSWORD);
        //发送验证码
        if (Constants.yes.equals(customerSiteProvider.sendMobileCode(customerSendMobileCodeRequest).getContext().getResult())) {
            return BaseResponse.SUCCESSFUL();
        }
        return BaseResponse.FAILED();
    }

    /**
     * 验证验证码（忘记密码时）
     *
     * @param registerRequest
     * @return LoginResponse<客户编号>
     */
    @ApiOperation(value = "忘记密码时验证验证码")
    @RequestMapping(value = "/validateSmsByForgot", method = RequestMethod.POST)
    public BaseResponse<String> validateSmsByForgot(@Validated({NotCustomerAccount.class, NotVerify.class})
                                                    @RequestBody RegisterRequest registerRequest) {
        //验证验证码
        String t_verifyCode = redisService.getString(registerRequest.getIsForgetPassword() != null && registerRequest
                .getIsForgetPassword() ? CacheKeyConstant.YZM_FORGET_PWD_KEY.concat(registerRequest
                .getCustomerAccount()) : CacheKeyConstant.YZM_UPDATE_PWD_KEY.concat(registerRequest
                .getCustomerAccount()));
        if (t_verifyCode == null || (!t_verifyCode.equalsIgnoreCase(registerRequest.getVerifyCode()))) {
            throw new SbcRuntimeException(SiteResultCode.ERROR_000010);
        }
        CustomerByAccountRequest request = new CustomerByAccountRequest();
        request.setCustomerAccount(registerRequest.getCustomerAccount());
        BaseResponse<CustomerByAccountResponse> responseBaseResponse = customerSiteQueryProvider.getCustomerByCustomerAccount(request);
        CustomerByAccountResponse response = responseBaseResponse.getContext();
        if (Objects.isNull(response)) {
            throw new SbcRuntimeException(SiteResultCode.ERROR_010005);
        }
        CustomerDetailVO customerDetail = this.findCustomerDetailByCustomerId(response.getCustomerId());
        if (customerDetail == null) {
            throw new SbcRuntimeException(SiteResultCode.ERROR_010005);
        }

        //是否禁用
        if (CustomerStatus.DISABLE.toValue() == customerDetail.getCustomerStatus().toValue()) {
            throw new SbcRuntimeException(SiteResultCode.ERROR_010002, new Object[]{"，原因为：" + customerDetail
                    .getForbidReason()});
        }

        return BaseResponse.success(response.getCustomerId());
    }

    /**
     * 修改验证码（忘记密码时）
     *
     * @param registerRequest
     * @return LoginResponse<客户编号>
     */
    @ApiOperation(value = "忘记密码时修改验证码")
    @RequestMapping(value = "/passwordByForgot", method = RequestMethod.POST)
    public BaseResponse passwordByForgot(@Validated({NotCustomerId.class, NotPassword.class, NotVerify.class})
                                         @RequestBody RegisterRequest registerRequest) {
        CustomerGetByIdResponse customer = customerQueryProvider.getCustomerById(new CustomerGetByIdRequest(registerRequest
                .getCustomerId())).getContext();
        if (customer == null) {
            throw new SbcRuntimeException(SiteResultCode.ERROR_010005);
        }
        CustomerDetailVO customerDetail = this.findCustomerDetailByCustomerId(registerRequest.getCustomerId());
        if (customerDetail == null) {
            throw new SbcRuntimeException(SiteResultCode.ERROR_010005);
        }

        //验证验证码
        String t_verifyCode = redisService.getString(registerRequest.getIsForgetPassword() ? CacheKeyConstant
                .YZM_FORGET_PWD_KEY.concat(customer.getCustomerAccount()) : CacheKeyConstant.YZM_UPDATE_PWD_KEY
                .concat(customer.getCustomerAccount()));
        if (t_verifyCode == null || (!t_verifyCode.equalsIgnoreCase(registerRequest.getVerifyCode()))) {
            throw new SbcRuntimeException(SiteResultCode.ERROR_000010);
        }

        //是否禁用
        if (CustomerStatus.DISABLE.toValue() == customerDetail.getCustomerStatus().toValue()) {
            throw new SbcRuntimeException(SiteResultCode.ERROR_010002, new Object[]{"，原因为：" + customerDetail
                    .getForbidReason()});
        }

        //
        customer.setCustomerPassword(registerRequest.getCustomerPassword());
        CustomerModifyRequest customerModifyRequest = new CustomerModifyRequest();
        KsBeanUtil.copyPropertiesThird(customer, customerModifyRequest);
        customerSiteProvider.modifyCustomerPwd(customerModifyRequest);
        //删除验证码缓存
        redisService.delete(registerRequest.getIsForgetPassword() != null && registerRequest.getIsForgetPassword() ?
                CacheKeyConstant.YZM_FORGET_PWD_KEY.concat(customer.getCustomerAccount()) : CacheKeyConstant
                .YZM_UPDATE_PWD_KEY.concat(customer.getCustomerAccount()));
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 验证TOKEN，实现自动登录
     *
     * @return LoginResponse
     */
    @ApiOperation(value = "验证TOKEN，实现自动登录")
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public BaseResponse<String> login() {
        String jwtHeaderKey = StringUtils.isNotBlank(jwtProperties.getJwtHeaderKey()) ? jwtProperties.getJwtHeaderKey
                () : "Authorization";
        String jwtHeaderPrefix = StringUtils.isNotBlank(jwtProperties.getJwtHeaderPrefix()) ? jwtProperties
                .getJwtHeaderPrefix() : "Bearer ";

        String authHeader = HttpUtil.getRequest().getHeader(jwtHeaderKey);
        if (StringUtils.isBlank(authHeader) || !authHeader.startsWith(jwtHeaderPrefix)) {
            return BaseResponse.FAILED();
        }

        //当token失效,直接返回失败
        String token = authHeader.substring(jwtHeaderPrefix.length());
        Claims claims = Jwts.parser().setSigningKey(this.jwtSecretKey).parseClaimsJws(token).getBody();
        if (claims == null) {
            return BaseResponse.FAILED();
        }
        log.info("验证TOKEN，实现自动登录,会员ID:{}", claims.get("customerId"));

        CustomerGetByIdResponse customer = customerQueryProvider.getCustomerById(new CustomerGetByIdRequest(String.valueOf(claims
                .get("customerId")))).getContext();
        //当用户不存在,直接返回失败
        if (customer == null) {
            return BaseResponse.FAILED();
        }

        log.info("验证TOKEN，实现自动登录,获取会员详情信息如下：{}", customer);

        //当用户被逻辑删除,直接返回失效token
        if (DeleteFlag.YES.toValue() == customer.getDelFlag().toValue()) {
            throw new SbcRuntimeException(SiteResultCode.ERROR_010014);
        }

        //当用户被禁用,直接返回失效token
        CustomerDetailVO detail = this.findAnyCustomerDetailByCustomerId(customer.getCustomerId());
        if (detail == null || CustomerStatus.DISABLE.toValue() == detail.getCustomerStatus().toValue()) {
            throw new SbcRuntimeException(SiteResultCode.ERROR_010002, new Object[]{"，原因为：" + detail
                    .getForbidReason()});
        }

        Date date = new Date();
        Date expiration = claims.getExpiration();
        Map<String, String> vasList = redisService.hgetall(ConfigKey.VALUE_ADDED_SERVICES.toString());
        JwtBuilder builder = Jwts.builder().setSubject(customer.getCustomerAccount())
                .compressWith(CompressionCodecs.DEFLATE)
                .signWith(SignatureAlgorithm.HS256, jwtSecretKey)
                .setIssuedAt(date)
                .claim("customerId", customer.getCustomerId())
                .claim("customerAccount", customer.getCustomerAccount())
                .claim("customerName", customer.getCustomerDetail().getCustomerName())
                .claim("ip", customer.getLoginIp())
                .claim("terminalToken", String.valueOf(claims.get("terminalToken")))
                .claim("firstLogin", claims.get("firstLogin"))//是否首次登陆
                .claim(ConfigKey.VALUE_ADDED_SERVICES.toString(), JSONObject.toJSONString(vasList));

        //当超时时间与当前时间不足1个小时，自动加30分钟
        if (DateUtils.addHours(expiration, 1).after(date)) {
            expiration = DateUtils.addMinutes(expiration, 30);
        }
        return BaseResponse.success(builder.setExpiration(expiration).compact());
    }

    /**
     * 发送验证码（使用验证码登录）
     *
     * @param customerAccount
     * @return LoginResponse
     */
    @ApiOperation(value = "使用验证码登录发送验证码")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "customerAccount", value = "会员账号", required = true)
    @RequestMapping(value = "/login/verification/{customerAccount}", method = RequestMethod.POST)
    public BaseResponse sendLoginCode(@PathVariable("customerAccount") String customerAccount) {
        //验证输入的手机号码格式
        if (!ValidateUtil.isPhone(customerAccount)) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        //是否可以发送
        CustomerValidateSendMobileCodeRequest customerValidateSendMobileCodeRequest =
                new CustomerValidateSendMobileCodeRequest();
        customerValidateSendMobileCodeRequest.setMobile(customerAccount);
        if (!customerSiteProvider.validateSendMobileCode(customerValidateSendMobileCodeRequest).getContext().getResult()) {
            throw new SbcRuntimeException(SiteResultCode.ERROR_000016);
        }

        //账号是否注册
        CustomerByAccountRequest request = new CustomerByAccountRequest();
        request.setCustomerAccount(customerAccount);
        BaseResponse<CustomerByAccountResponse> responseBaseResponse = customerSiteQueryProvider.getCustomerByCustomerAccount(request);
        CustomerByAccountResponse response = responseBaseResponse.getContext();
        if (Objects.isNull(response)) {
            throw new SbcRuntimeException(SiteResultCode.ERROR_010005);
        }
        validateAccountStatus(response);
        CustomerDetailVO customerDetail = this.findCustomerDetailByCustomerId(response.getCustomerId());
        if (customerDetail == null) {
            throw new SbcRuntimeException(SiteResultCode.ERROR_010005);
        }

        //是否禁用
        if (CustomerStatus.DISABLE.toValue() == customerDetail.getCustomerStatus().toValue()) {
            throw new SbcRuntimeException(SiteResultCode.ERROR_010002, new Object[]{"，原因为：" + customerDetail
                    .getForbidReason()});
        }

        //发送验证码
        CustomerSendMobileCodeRequest customerSendMobileCodeRequest = new CustomerSendMobileCodeRequest();
        customerSendMobileCodeRequest.setMobile(customerAccount);
        customerSendMobileCodeRequest.setRedisKey(CacheKeyConstant.YZM_CUSTOMER_LOGIN);
        customerSendMobileCodeRequest.setSmsTemplate(SmsTemplate.CUSTOMER_LOGIN);
        if (Constants.yes.equals(customerSiteProvider.sendMobileCode(customerSendMobileCodeRequest).getContext().getResult())) {
            return BaseResponse.SUCCESSFUL();
        }
        return BaseResponse.FAILED();
    }

    /**
     * 校验用户是否锁定
     *
     * @param
     */
    public void validateAccountStatus(CustomerByAccountResponse customer) {
        //锁定时间
        LocalDateTime now = LocalDateTime.now();
        if (customer.getLoginLockTime() != null) {
            if (now.isBefore(customer.getLoginLockTime().plus(30, ChronoUnit.MINUTES))) {
                long minutes = ChronoUnit.MINUTES.between(customer.getLoginLockTime().toLocalTime(), now.toLocalTime());
                minutes = 30 - minutes;
                if (minutes < 1) {
                    minutes = 1;
                }
                throw new SbcRuntimeException("K-010004", new Object[]{minutes});
            }
        }
    }

    /**
     * 验证发送的验证码
     * <p>
     * 使用验证码登录
     *
     * @param loginRequest
     * @return
     */
    @ApiOperation(value = "使用验证码登录验证发送的验证码并登录")
    @RequestMapping(value = "/login/verification", method = RequestMethod.POST)
    public BaseResponse<LoginResponse> loginWithVerificationCode(@Valid @RequestBody LoginVerificationCodeRequest
                                                                         loginRequest) {
        //验证手机号格式
        if (!ValidateUtil.isPhone(loginRequest.getCustomerAccount())) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        //验证验证码格式
        if (!ValidateUtil.isVerificationCode(loginRequest.getVerificationCode())) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }

        //账号是否注册
        CustomerByAccountRequest request = new CustomerByAccountRequest();
        request.setCustomerAccount(loginRequest.getCustomerAccount());
        BaseResponse<CustomerByAccountResponse> responseBaseResponse = customerSiteQueryProvider.getCustomerByCustomerAccount(request);
        CustomerByAccountResponse response = responseBaseResponse.getContext();
        if (Objects.isNull(response)) {
            throw new SbcRuntimeException(SiteResultCode.ERROR_010005);
        }

        CustomerDetailVO customerDetail = this.findCustomerDetailByCustomerId(response.getCustomerId());
        if (customerDetail == null) {
            throw new SbcRuntimeException(SiteResultCode.ERROR_010005);
        }

        //是否禁用
        if (CustomerStatus.DISABLE.toValue() == customerDetail.getCustomerStatus().toValue()) {
            throw new SbcRuntimeException(SiteResultCode.ERROR_010002, new Object[]{"，原因为：" + customerDetail
                    .getForbidReason()});
        }

        //验证验证码
        String t_verifyCode = redisService.getString(CacheKeyConstant.YZM_CUSTOMER_LOGIN.concat(loginRequest
                .getCustomerAccount()));
        if (t_verifyCode == null || (!t_verifyCode.equalsIgnoreCase(loginRequest.getVerificationCode()))) {
            throw new SbcRuntimeException(CommonErrorCode.VERIFICATION_CODE_ERROR);
        }
        //删除验证码缓存
        redisService.delete(CacheKeyConstant.YZM_CUSTOMER_LOGIN.concat(loginRequest.getCustomerAccount()));

        //返回值
        CustomerVO customerVO = new CustomerVO();
        KsBeanUtil.copyPropertiesThird(response, customerVO);
        LoginResponse loginResponse = commonUtil.getLoginResponse(customerVO, jwtSecretKey);
        loginResponse.setCustomerDetail(customerDetail);
        return BaseResponse.success(loginResponse);
    }

    /**
     * 根据会员获取
     *
     * @param customerId
     * @return
     */
    private CustomerDetailVO findCustomerDetailByCustomerId(String customerId) {
        return customerDetailQueryProvider.getCustomerDetailWithNotDeleteByCustomerId(
                CustomerDetailWithNotDeleteByCustomerIdRequest.builder().customerId(customerId).build()).getContext();
    }

    /**
     * 根据会员获取
     *
     * @param customerId
     * @return
     */
    private CustomerDetailVO findAnyCustomerDetailByCustomerId(String customerId) {
        return customerDetailQueryProvider.getCustomerDetailByCustomerId(
                CustomerDetailByCustomerIdRequest.builder().customerId(customerId).build()).getContext();
    }

    /**
     * 访问是否需要登录
     *
     * @return
     */
    @ApiOperation(value = "查询访问是否需要登录")
    @RequestMapping(value = "/userSetting/isVisitWithLogin", method = RequestMethod.POST)
    public BaseResponse<UserAuditResponse> isVisitWithLogin() {
        UserAuditResponse userAuditResponse = new UserAuditResponse();
        userAuditResponse.setAudit(auditQueryProvider.getIsVisitWithLogin().getContext().isAudit());
        return BaseResponse.success(userAuditResponse);
    }

    /**
     * 弹框里的注册发送验证码
     *
     * @param customerAccount
     * @return
     */
    @ApiOperation(value = "开放访问注册时发送验证码")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "customerAccount", value = "会员账号", required = true)
    @RequestMapping(value = "/checkSmsByRegister/web/modal/{customerAccount}", method = RequestMethod.POST)
    public BaseResponse checkSmsByWebModalRegister(@PathVariable("customerAccount") String customerAccount) {
        //同一个手机是否操作频繁
        CustomerValidateSendMobileCodeRequest customerValidateSendMobileCodeRequest = new CustomerValidateSendMobileCodeRequest();
        customerValidateSendMobileCodeRequest.setMobile(customerAccount);
        if (!customerSiteProvider.validateSendMobileCode(customerValidateSendMobileCodeRequest).getContext().getResult()) {
            throw new SbcRuntimeException(SiteResultCode.ERROR_000016);
        }
        //账号是否注册
        CustomerByAccountRequest request = new CustomerByAccountRequest();
        request.setCustomerAccount(customerAccount);
        BaseResponse<CustomerByAccountResponse> responseBaseResponse = customerSiteQueryProvider.getCustomerByCustomerAccount(request);
        CustomerByAccountResponse customer = responseBaseResponse.getContext();

        if (customer != null) {
            CustomerDetailByAccountRequest customerDetailByAccountRequest = new CustomerDetailByAccountRequest();
            customerDetailByAccountRequest.setCustomerAccount(customerAccount);
            BaseResponse<CustomerDetailByAccountResponse> customerDetailByAccountResponseBaseResponse = customerSiteQueryProvider.getCustomerDetailByCustomerAccount(customerDetailByAccountRequest);
            CustomerDetailByAccountResponse customerDetail = customerDetailByAccountResponseBaseResponse.getContext();
            if (customerDetail != null) {
                if (CustomerStatus.DISABLE.toValue() == customerDetail.getCustomerStatus().toValue()) {
                    throw new SbcRuntimeException(SiteResultCode.ERROR_010002, new Object[]{"，原因为：" + customerDetail
                            .getForbidReason()});
                }
            }
            throw new SbcRuntimeException(SiteResultCode.ERROR_010101);
        }
        //发送验证码
        CustomerSendMobileCodeRequest customerSendMobileCodeRequest = new CustomerSendMobileCodeRequest();
        customerSendMobileCodeRequest.setMobile(customerAccount);
        customerSendMobileCodeRequest.setRedisKey(CacheKeyConstant.REGISTER_MODAL_CODE);
        customerSendMobileCodeRequest.setSmsTemplate(SmsTemplate.REGISTRY);
        if (Constants.yes.equals(customerSiteProvider.sendMobileCode(customerSendMobileCodeRequest).getContext().getResult())) {
            return BaseResponse.SUCCESSFUL();
        }
        return BaseResponse.FAILED();
    }

    /**
     * 弹框里的注册
     *
     * @param registerRequest
     * @return
     */
    @ApiOperation(value = "开放访问注册")
    @RequestMapping(value = "/register/modal", method = RequestMethod.POST)
    @GlobalTransactional
    public BaseResponse<LoginResponse> registerModal(@Validated({NotCustomerAccount.class, NotPassword.class, NotVerify
            .class}) @RequestBody RegisterRequest registerRequest) {
        log.info("enter register method ......");

        //累计验证错误次数
        String errKey = CacheKeyConstant.REGISTER_ERR.concat(registerRequest.getCustomerAccount());
        String smsKey = CacheKeyConstant.REGISTER_MODAL_CODE.concat(registerRequest.getCustomerAccount());
        if (NumberUtils.toInt(redisService.getString(errKey)) >= 3) {
            redisService.delete(smsKey);
            throw new SbcRuntimeException(SiteResultCode.ERROR_000016);
        }

        //验证验证码
        String t_verifyCode = redisService.getString(smsKey);
        if (StringUtils.isBlank(t_verifyCode) || (!t_verifyCode.equalsIgnoreCase(registerRequest.getVerifyCode()))) {
            redisService.incrKey(errKey);
            redisService.expireBySeconds(errKey, 60L);
            throw new SbcRuntimeException(SiteResultCode.ERROR_000010);
        }

        //验证邀请ID、邀请码是否正确
        DistributionCustomerVO distributionCustomerVO = loginBaseService.checkInviteIdAndInviteCode(registerRequest.getInviteeId(), registerRequest.getInviteCode());

        CustomerDTO customer = new CustomerDTO();
        customer.setCustomerAccount(registerRequest.getCustomerAccount());
        customer.setCustomerPassword(registerRequest.getCustomerPassword());
        if (StringUtils.isNotBlank(registerRequest.getEmployeeId())) {
            EmployeeByIdResponse employee = employeeQueryProvider.getById(
                    EmployeeByIdRequest.builder().employeeId(registerRequest.getEmployeeId()).build()
            ).getContext();
            if (Objects.isNull(employee)) {
                registerRequest.setEmployeeId(null);
            }
        }

        CustomerRegisterRequest customerRegisterRequest = new CustomerRegisterRequest();
        customerRegisterRequest.setEmployeeId(registerRequest.getEmployeeId());
        customerRegisterRequest.setCustomerDTO(customer);
        BaseResponse<CustomerRegisterResponse> customerRegisterResponseBaseResponse = customerSiteProvider.register(customerRegisterRequest);
        CustomerVO customerVO = customerRegisterResponseBaseResponse.getContext();

        if (customerVO != null) {
            log.info("register customer success ......注册信息详情：{}", customerVO);
            //删除验证码缓存
            redisService.delete(smsKey);
            LoginResponse loginResponse = commonUtil.getLoginResponse(customerVO, jwtSecretKey);
            //审核开关关闭并且信息完善开关关闭，则直接登录
            boolean isPerfectCustomer = auditQueryProvider.isPerfectCustomerInfo().getContext().isPerfect();
            boolean isCustomerAudit = auditQueryProvider.isCustomerAudit().getContext().isAudit();
            if (!isPerfectCustomer && !isCustomerAudit) {
                log.info("to home page ......");
                loginResponse.setIsLoginFlag(Boolean.TRUE);
                // 领取注册赠券
                GetRegisterOrStoreCouponResponse couponResponse = this.getCouponGroup(customerVO.getCustomerId(), CouponActivityType.REGISTERED_COUPON, Constant.BOSS_DEFAULT_STORE_ID);
                //有注册赠券，则设置参数
                if (couponResponse != null) {
                    loginResponse.setCouponResponse(couponResponse);
                }
            } else {
                // 领取注册赠券 ,如果完善信息开关打开则不能参加注册赠券活动
                if (!isPerfectCustomer) {
                    GetRegisterOrStoreCouponResponse couponResponse = this.getCouponGroup(customerVO.getCustomerId(), CouponActivityType.REGISTERED_COUPON, Constant.BOSS_DEFAULT_STORE_ID);
                    loginResponse.setCouponResponse(couponResponse);
                }
                log.info("to information improve page ......");
                loginResponse.setCustomerId(customerVO.getCustomerId());
                loginResponse.setIsLoginFlag(Boolean.FALSE);
            }

            webBaseProducerService.sendMQForCustomerRegister(customerVO);

            // 新增邀新记录/成长值/积分等
            this.addOtherInfos(customerVO, distributionCustomerVO, registerRequest);

            return BaseResponse.success(loginResponse);
        }
        log.info("register customer failed ......");
        return BaseResponse.FAILED();
    }

    /**
     * 查询注册赠券
     *
     * @param customerId
     * @param type
     * @param storeId
     * @return
     */
    GetRegisterOrStoreCouponResponse getCouponGroup(String customerId, CouponActivityType type, Long storeId) {
        GetCouponGroupRequest getCouponGroupRequest = new GetCouponGroupRequest();
        getCouponGroupRequest.setCustomerId(customerId);
        getCouponGroupRequest.setType(type);
        getCouponGroupRequest.setStoreId(storeId);
        return couponActivityProvider.getCouponGroup(getCouponGroupRequest).getContext();
    }

    /**
     * 注册验证
     *
     * @param registerRequest
     * @return
     */
    @ApiOperation(value = "注册验证")
    @RequestMapping(value = "/register/check", method = RequestMethod.POST)
    public BaseResponse checkRegister(@RequestBody RegisterCheckRequest registerRequest) {
        //累计验证错误次数
        String errKey = CacheKeyConstant.REGISTER_ERR.concat(registerRequest.getCustomerAccount());
        String smsKey = CacheKeyConstant.REGISTER_MODAL_CODE.concat(registerRequest.getCustomerAccount());
        if (registerRequest.getFromPage().equals(NumberUtils.INTEGER_ZERO)) {
            smsKey = CacheKeyConstant.VERIFY_CODE_KEY.concat(registerRequest.getCustomerAccount());
        }
        if (NumberUtils.toInt(redisService.getString(errKey)) >= 3) {
            redisService.delete(smsKey);
            throw new SbcRuntimeException(SiteResultCode.ERROR_000016);
        }

        //验证验证码
        String t_verifyCode = redisService.getString(smsKey);
        if (StringUtils.isBlank(t_verifyCode) || (!t_verifyCode.equalsIgnoreCase(registerRequest.getVerifyCode()))) {
            redisService.incrKey(errKey);
            redisService.expireBySeconds(errKey, 60L);
            throw new SbcRuntimeException(SiteResultCode.ERROR_000010);
        }

        customerSiteQueryProvider.checkByAccount(new CustomerCheckByAccountRequest(registerRequest.getCustomerAccount()));
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 获取注册限制、是否开启分销
     *
     * @return
     */
    @ApiOperation(value = "获取注册限制、是否开启分销")
    @RequestMapping(value = "/getRegisterLimitType", method = RequestMethod.POST)
    public BaseResponse<RegisterResponse> getRegisterLimitType() {
        RegisterLimitType registerLimitType = distributionCacheService.getRegisterLimitType();
        DefaultFlag openFlag = distributionCacheService.queryOpenFlag();
        return BaseResponse.success(new RegisterResponse(registerLimitType, openFlag));
    }

    /**
     * 樊登免密登陆
     *
     * @param loginRequest
     * @return FanDengLoginRequest
     */
    @ApiOperation(value = "樊登免密登陆")
    @RequestMapping(value = "/autoLogin", method = RequestMethod.POST)
    @GlobalTransactional
    @MultiSubmit
    public BaseResponse<LoginResponse> autoLogin(@Valid @RequestBody FanDengLoginRequest loginRequest) {
        log.info("enter autoLogin method ......");
        log.info(" autoLogin param{}", JSONObject.toJSONString(loginRequest));
        BaseResponse<FanDengLoginResponse> loginConfirm = externalProvider.loginConfirm(loginRequest);
        CustomerVO customerVO = extractLogin(loginConfirm.getContext());
        LoginResponse loginResponse = LoginResponse.builder().build();
        if (Objects.nonNull(customerVO)) {
            loginResponse = commonUtil.getLoginResponse(customerVO, jwtSecretKey);
            loginResponse.setNewFlag(Boolean.FALSE);
            if (Objects.isNull(customerVO.getLoginTime())) {
                webBaseProducerService.sendMQForCustomerRegister(customerVO);
                loginResponse.setNewFlag(Boolean.TRUE);
            }
        }
        log.info("autoLogin success ......");
        return BaseResponse.success(loginResponse);
    }

    public CustomerVO extractLogin(FanDengLoginResponse response) {
        //根据樊登的id去查找
        NoDeleteCustomerGetByFanDengRequest request = new NoDeleteCustomerGetByFanDengRequest();
        request.setFanDengId(response.getUserNo());
        NoDeleteCustomerGetByAccountResponse customer = customerQueryProvider.getNoDeleteCustomerByFanDengId(request).getContext();
        if (customer == null) {
            //根据樊登的电话去查找
            NoDeleteCustomerGetByAccountRequest accountRequest = new NoDeleteCustomerGetByAccountRequest();
            accountRequest.setCustomerAccount(response.getMobile());
            customer = customerQueryProvider.getNoDeleteCustomerByAccount(accountRequest).getContext();
            if (customer != null) {
                //如果存在电话的，则吧该电话的客户信息更改成 樊登的id
                    //合并用户
                    customer.setFanDengUserNo(response.getUserNo());
                    customer.setLoginTime(LocalDateTime.now());
                    CustomerFandengModifyRequest modifyRequest = new CustomerFandengModifyRequest();
                    modifyRequest.setLoginIp(HttpUtil.getIpAddr());
                    modifyRequest.setCustomerId(customer.getCustomerId());
                    modifyRequest.setFanDengId(response.getUserNo());
                    customerProvider.modifyCustomerFanDengIdTime(modifyRequest);
            } else {
                //创建用户
                //如果电话和id都不存在，则直接创建用户
                FanDengModifyCustomerRequest customerRequest = FanDengModifyCustomerRequest.builder()
                        .fanDengUserNo(response.getUserNo())
                        .nickName(response.getNickName())
                        .customerAccount(response.getMobile())
                        .profilePhoto(response.getProfilePhoto()).build();
                customer = externalProvider.modifyCustomer(customerRequest).getContext();
            }
        } else {
            //当前用户樊登的id存在
            customer.setLoginTime(LocalDateTime.now());
            //如果樊登的id存在，但是电话为空或者 电话和樊登给过的电话不同
            if (StringUtils.isBlank(customer.getCustomerAccount()) || !customer.getCustomerAccount().equals(response.getMobile())) {
                //如果樊登id 一样 并且手机号码不一样
                NoDeleteCustomerGetByAccountRequest accountRequest = new NoDeleteCustomerGetByAccountRequest();
                accountRequest.setCustomerAccount(response.getMobile());
                NoDeleteCustomerGetByAccountResponse newCustomerVO =
                        customerQueryProvider.getNoDeleteCustomerByAccount(accountRequest).getContext();

                if (newCustomerVO != null) {
                    TradeQueryDTO tradeQueryDTO = new TradeQueryDTO();
                    tradeQueryDTO.putSort("createTime", "desc");
                    tradeQueryDTO.setBuyerId(customer.getCustomerId());
                    TradePageCriteriaRequest criteriaRequest = TradePageCriteriaRequest.builder().tradePageDTO(tradeQueryDTO).build();
                    //有樊登id  根据樊登id 获取的客户id，对应的订单信息
                    TradePageCriteriaResponse oldCustomerTrade = tradeQueryProvider.pageCriteria(criteriaRequest).getContext();
                    tradeQueryDTO.setBuyerId(newCustomerVO.getCustomerId());
                    //有手机号码  根据手机樊登手机号码获取的用户id 对应的订单信息
                    TradePageCriteriaResponse newCustomerTrade = tradeQueryProvider.pageCriteria(criteriaRequest).getContext();
                    List<TradeVO> oldTradeVOS = oldCustomerTrade.getTradePage().getContent();
                    List<TradeVO> newTradeVOS = newCustomerTrade.getTradePage().getContent();
                    DistributionCustomerModifyCustomerIdRequest customerIdRequest = new DistributionCustomerModifyCustomerIdRequest();

                    CustomersDeleteRequest  deleteRequest = new CustomersDeleteRequest();
                    if ((CollectionUtils.isEmpty(oldTradeVOS) && CollectionUtils.isEmpty(newTradeVOS))
                    || (!CollectionUtils.isEmpty(oldTradeVOS) && CollectionUtils.isEmpty(newTradeVOS))){
                        CustomerAccountModifyRequest accountModifyRequest = new CustomerAccountModifyRequest();
                        accountModifyRequest.setCustomerAccount(response.getMobile());
                        accountModifyRequest.setCustomerId(customer.getCustomerId());
                        customerProvider.modifyCustomerAccount(accountModifyRequest);
                        List<String> customerIdList = Arrays.asList(newCustomerVO.getCustomerId());
                        deleteRequest.setCustomerIds(customerIdList);
                        customerProvider.deleteCustomers(deleteRequest);

                        webBaseProducerService.sendMQForModifyCustomerAccount(customer.getCustomerId(),response.getMobile());

                        webBaseProducerService.sendMQForDelCustomerInfo(newCustomerVO.getCustomerId());

                        customerIdRequest.setCustomerId(customer.getCustomerId());
                        customerIdRequest.setOldCustomerId(newCustomerVO.getCustomerId());

                    }else if (CollectionUtils.isEmpty(oldTradeVOS) && !CollectionUtils.isEmpty(newTradeVOS)) {
                        CustomerFandengModifyRequest modifyRequest = new CustomerFandengModifyRequest();
                        modifyRequest.setLoginIp(HttpUtil.getIpAddr());
                        modifyRequest.setCustomerId(newCustomerVO.getCustomerId());
                        modifyRequest.setFanDengId(response.getUserNo());
                        customerProvider.modifyCustomerFanDengIdTime(modifyRequest);
                        customer = newCustomerVO;

                        List<String> customerIdList = Arrays.asList(customer.getCustomerId());
                        deleteRequest.setCustomerIds(customerIdList);
                        customerProvider.deleteCustomers(deleteRequest);
                        webBaseProducerService.sendMQForDelCustomerInfo(customer.getCustomerId());
                        customerIdRequest.setCustomerId(newCustomerVO.getCustomerId());
                        customerIdRequest.setOldCustomerId(customer.getCustomerId());
                    }else if (!CollectionUtils.isEmpty(oldTradeVOS) && !CollectionUtils.isEmpty(newTradeVOS)) {
                        List<TradeVO> tradeVOList  = new ArrayList<>();
                        tradeVOList.addAll(newTradeVOS);
                        tradeVOList.addAll(oldTradeVOS);
                        TradeVO newTradeVo = tradeVOList.stream().max(Comparator.comparing(tradeVO -> tradeVO.getTradeState().getCreateTime())).get();
                        String tradeCustomerId = newTradeVo.getBuyer().getId();
                        if (tradeCustomerId.equals(customer.getCustomerId())){
                            CustomerAccountModifyRequest accountModifyRequest = new CustomerAccountModifyRequest();
                            accountModifyRequest.setCustomerAccount(response.getMobile());
                            accountModifyRequest.setCustomerId(customer.getCustomerId());
                            customerProvider.modifyCustomerAccount(accountModifyRequest);

                            webBaseProducerService
                                    .sendMQForModifyCustomerAccount(customer.getCustomerId()
                                            ,response.getMobile());


                            List<String> customerIdList = Arrays.asList(newCustomerVO.getCustomerId());
                            deleteRequest.setCustomerIds(customerIdList);
                            customerProvider.deleteCustomers(deleteRequest);

                            webBaseProducerService.sendMQForDelCustomerInfo(newCustomerVO.getCustomerId());

                            customerIdRequest.setCustomerId(customer.getCustomerId());
                            customerIdRequest.setOldCustomerId(newCustomerVO.getCustomerId());

                        }else {
                            CustomerFandengModifyRequest modifyRequest = new CustomerFandengModifyRequest();
                            modifyRequest.setLoginIp(HttpUtil.getIpAddr());
                            modifyRequest.setCustomerId(newCustomerVO.getCustomerId());
                            modifyRequest.setFanDengId(response.getUserNo());
                            customerProvider.modifyCustomerFanDengIdTime(modifyRequest);


                            List<String> customerIdList = Arrays.asList(customer.getCustomerId());
                            deleteRequest.setCustomerIds(customerIdList);
                            customerProvider.deleteCustomers(deleteRequest);

                            webBaseProducerService.sendMQForDelCustomerInfo(customer.getCustomerId());
                            customer = newCustomerVO;
                            customerIdRequest.setCustomerId(newCustomerVO.getCustomerId());
                            customerIdRequest.setOldCustomerId(customer.getCustomerId());
                        }
                    }
                    distributionCustomerSaveProvider.modifyCustomerId(customerIdRequest);
                }else {
                    //并且新手机号码不存在用户库  修改手机号码
                    CustomerAccountModifyRequest accountModifyRequest = new CustomerAccountModifyRequest();
                    accountModifyRequest.setCustomerAccount(response.getMobile());
                    accountModifyRequest.setCustomerId(customer.getCustomerId());
                    customerProvider.modifyCustomerAccount(accountModifyRequest);
                    webBaseProducerService
                            .sendMQForModifyCustomerAccount(customer.getCustomerId()
                                    ,response.getMobile());
                }
            }

                FanDengModifyCustomerLoginTimeRequest loginTimeRequest = FanDengModifyCustomerLoginTimeRequest.builder()
                        .loginIp(HttpUtil.getIpAddr())
                        .customerId(customer.getCustomerId()).build();
                externalProvider.modifyCustomerLoginTime(loginTimeRequest);


        }

        FanDengModifyCustomerLoginTimeRequest loginTimeRequest = FanDengModifyCustomerLoginTimeRequest.builder()
                .nickName(response.getNickName())
                .profilePhoto(response.getProfilePhoto())
                .customerId(customer.getCustomerId()).build();
        externalProvider.modifyCustomerNameAndImg(loginTimeRequest);

        FanDengModifyPaidCustomerRequest paidCustomerRequest = FanDengModifyPaidCustomerRequest.builder()
                .userStatus(response.getUserStatus())
                .vipEndTime(response.getVipEndTime())
                .vipStartTime(response.getVipStartTime())
                .customerId(customer.getCustomerId()).build();
        externalProvider.modifyPaidCustomer(paidCustomerRequest);
        return customer;
    }

    public LoginResponse afterWxAuthLogin(FanDengWxAuthLoginResponse.WxAuthLoginData resData, String mobile, String openId, String unionId){
        FanDengLoginResponse response = new FanDengLoginResponse();
        response.setMobile(mobile);
        response.setNickName(resData.getNickName());
        response.setProfilePhoto(resData.getProfilePhoto());
        response.setUserNo(resData.getUserNo());
        response.setVipEndTime(Timestamp.valueOf(resData.getVipEndTime()).getTime());
        response.setVipStartTime(Timestamp.valueOf(resData.getVipStartTime()).getTime());
        CustomerVO customerVO = extractLogin(response);

        LoginResponse loginResponse = LoginResponse.builder().build();
        if (Objects.nonNull(customerVO)) {
            //返回值
            loginResponse = commonUtil.getLoginResponse(customerVO, jwtSecretKey);
            loginResponse.setNewFlag(Boolean.FALSE);
            loginResponse.setFanDengUserStates(resData.getUserStatus());
            if (Objects.isNull(customerVO.getLoginTime())) {
                customerProvider.modifyCustomerOpenIdAndUnionId(customerVO.getCustomerId(), openId, unionId);
                webBaseProducerService.sendMQForCustomerRegister(customerVO);
                //todo 小程序新用户埋点
                loginResponse.setNewFlag(Boolean.TRUE);
            }
        }
        return loginResponse;
    }

    /**
     * 樊登授权登陆接口
     *
     * @param loginRequest
     * @return FanDengAuthLoginRequest
     */
    @ApiOperation(value = "樊登授权登陆接口")
    @RequestMapping(value = "/authLogin", method = RequestMethod.POST)
    @MultiSubmit
    @GlobalTransactional
    public BaseResponse<LoginResponse> authLogin(@Valid @RequestBody FanDengAuthLoginRequest loginRequest) {
        log.info("enter autoLogin method ......");
        BaseResponse<FanDengLoginResponse> authLogin = externalProvider.authLogin(loginRequest);
        authLogin.getContext().setMobile(loginRequest.getMobile());
        CustomerVO customerVO = extractLogin(authLogin.getContext());
        LoginResponse loginResponse = LoginResponse.builder().build();
        if (Objects.nonNull(customerVO)) {
            //返回值
            loginResponse = commonUtil.getLoginResponse(customerVO, jwtSecretKey);
            loginResponse.setNewFlag(Boolean.FALSE);
            loginResponse.setFanDengUserStates(authLogin.getContext().getUserStatus());
            if (Objects.isNull(customerVO.getLoginTime())) {
                webBaseProducerService.sendMQForCustomerRegister(customerVO);
                loginResponse.setNewFlag(Boolean.TRUE);
            }
        }
        log.info("autoLogin success ......");
        return BaseResponse.success(loginResponse);
    }

    /**
     * 微信小程序授权登录
     * @param wxGetUserPhoneAndOpenIdRequest
     */
//    @ApiOperation(value = "微信授权登陆接口")
//    @RequestMapping(value = "/wxAuthLogin", method = RequestMethod.POST)
//    @MultiSubmit
//    @GlobalTransactional
//    public BaseResponse<LoginResponse> wxAuthLogin(@RequestBody WxGetUserPhoneAndOpenIdRequest wxGetUserPhoneAndOpenIdRequest) {
//        String codeForOpenid = wxGetUserPhoneAndOpenIdRequest.getCodeForOpenid();
//        wxGetUserPhoneAndOpenIdRequest.setCodeForOpenid(null);
//        BaseResponse<WxGetUserPhoneAndOpenIdResponse> phoneAndOpenid = wxUserApiController.getPhoneAndOpenid(wxGetUserPhoneAndOpenIdRequest);
//
//        NoDeleteCustomerGetByAccountRequest accountRequest = new NoDeleteCustomerGetByAccountRequest();
//        accountRequest.setCustomerAccount(phoneAndOpenid.getContext().getPhoneNumber());
//        NoDeleteCustomerGetByAccountResponse newCustomerVO = customerQueryProvider.getNoDeleteCustomerByAccount(accountRequest).getContext();
//
//        String unionId;
//        String openId;
//        if(newCustomerVO == null){
//            //如果是新用户，就调微信接口获取opeid
//            wxGetUserPhoneAndOpenIdRequest.setCodeForPhone(null);
//            wxGetUserPhoneAndOpenIdRequest.setCodeForOpenid(codeForOpenid);
//            BaseResponse<WxGetUserPhoneAndOpenIdResponse> phoneAndOpenid2 = wxUserApiController.getPhoneAndOpenid(wxGetUserPhoneAndOpenIdRequest);
//            unionId = phoneAndOpenid2.getContext().getUnionId();
//            openId = phoneAndOpenid2.getContext().getOpenId();
//        }else {
//            unionId = newCustomerVO.getWxMiniUnionId();
//            openId = newCustomerVO.getWxMiniOpenId();
//        }
//
//        FanDengWxAuthLoginRequest authLoginRequest = FanDengWxAuthLoginRequest.builder().unionId(unionId).openId(openId).areaCode("+86").registerSource("miniApp")
//                .mobile(phoneAndOpenid.getContext().getPhoneNumber()).build();
//        BaseResponse<FanDengWxAuthLoginResponse.WxAuthLoginData> wxAuthLoginDataBaseResponse = externalProvider.wxAuthLogin(authLoginRequest);
//
//        LoginResponse loginResponse = afterWxAuthLogin(wxAuthLoginDataBaseResponse.getContext(), phoneAndOpenid.getContext().getPhoneNumber(), openId, unionId);
//        return BaseResponse.success(loginResponse);
//    }

    /**
     * 调用樊登发送验证码前置接口
     *
     * @param request
     * @return FanDengAuthLoginRequest
     */
    @ApiOperation(value = "调用樊登发送验证码前置接口")
    @RequestMapping(value = "/preposition", method = RequestMethod.POST)
    public BaseResponse<FanDengPrepositionResponse> preposition(@Valid @RequestBody FanDengPrepositionRequest request) {
        request.setClientIp(HttpUtil.getIpAddr());
        return externalProvider.preposition(request);
    }

    /**
     * 调用樊登极验API
     *
     * @param request
     * @return FanDengVerifyRequest
     */
    @ApiOperation(value = "调用樊登极验API")
    @RequestMapping(value = "/verify", method = RequestMethod.POST)
    public BaseResponse<FanDengVerifyResponse> verify(@Valid @RequestBody FanDengVerifyRequest request) {
        return externalProvider.verify(request);
    }

    /**
     * 获取樊登验证码
     *
     * @param request
     * @return FanDengSendCodeRequest
     */
    @ApiOperation(value = "获取樊登验证码")
    @RequestMapping(value = "/sendCode", method = RequestMethod.POST)
    public BaseResponse<FanDengSengCodeResponse> sendCode(@Valid @RequestBody FanDengSendCodeRequest request) {
        return externalProvider.sendCode(request);
    }

}
