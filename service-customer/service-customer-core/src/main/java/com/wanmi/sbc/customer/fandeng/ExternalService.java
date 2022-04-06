package com.wanmi.sbc.customer.fandeng;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.constant.MQConstant;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.BusinessCodeGenUtils;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.HttpUtil;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.util.SecurityUtil;
import com.wanmi.sbc.common.util.UUIDUtil;
import com.wanmi.sbc.customer.api.request.customer.CustomerAccountModifyRequest;
import com.wanmi.sbc.customer.api.request.fandeng.*;
import com.wanmi.sbc.customer.api.response.fandeng.*;
import com.wanmi.sbc.customer.ares.CustomerAresService;
import com.wanmi.sbc.customer.bean.enums.CheckState;
import com.wanmi.sbc.customer.bean.enums.CustomerStatus;
import com.wanmi.sbc.customer.bean.enums.CustomerType;
import com.wanmi.sbc.customer.bean.enums.EnterpriseCheckState;
import com.wanmi.sbc.customer.bean.enums.ThirdLoginType;
import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import com.wanmi.sbc.customer.detail.model.root.CustomerDetail;
import com.wanmi.sbc.customer.detail.repository.CustomerDetailRepository;
import com.wanmi.sbc.customer.distribution.repository.DistributionCustomerRepository;
import com.wanmi.sbc.customer.fdpaidcast.model.root.FdPaidCast;
import com.wanmi.sbc.customer.fdpaidcast.repository.FdPaidCastRepository;
import com.wanmi.sbc.customer.level.service.CustomerLevelService;
import com.wanmi.sbc.customer.model.root.Customer;
import com.wanmi.sbc.customer.mq.ProducerService;
import com.wanmi.sbc.customer.paidcardcustomerrel.model.root.PaidCardCustomerRel;
import com.wanmi.sbc.customer.paidcardcustomerrel.repository.PaidCardCustomerRelRepository;
import com.wanmi.sbc.customer.quicklogin.model.root.ThirdLoginRelation;
import com.wanmi.sbc.customer.quicklogin.repository.ThirdLoginRelationRepository;
import com.wanmi.sbc.customer.redis.RedisService;
import com.wanmi.sbc.customer.repository.CustomerRepository;
import com.wanmi.sbc.customer.service.CustomerService;
import com.wanmi.sbc.customer.util.HttpUtils;
import com.wanmi.sbc.customer.util.SHAUtils;
import com.wanmi.sbc.customer.util.SafeLevelUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.binding.BinderAwareChannelResolver;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.validation.Valid;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * @program: sbc-background
 * @description: 调用樊登接口业务层
 * @author: Mr.Tian
 * @create: 2021-01-28 15:25
 **/
@Service
@SuppressWarnings("all")
@Slf4j
public class ExternalService {

    @Value("${fandeng.host}")
    private String host;
    @Value("${fandeng.appid}")
    private String appid;
    @Value("${fandeng.appsecret}")
    private String appsecret;
    @Autowired
    private RedisService redisService;
    @Autowired
    private CustomerLevelService customerLevelService;
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerService customerService;
    @Autowired
    private CustomerDetailRepository customerDetailRepository;
    @Autowired
    private ProducerService producerService;
    @Autowired
    private FdPaidCastRepository fdPaidCastRepository;

    @Autowired
    private PaidCardCustomerRelRepository paidCardCustomerRelRepository;

    @Autowired
    private CustomerAresService customerAresService;
    @Autowired
    private DistributionCustomerRepository distributionCustomerRepository;

    @Autowired
    private ThirdLoginRelationRepository thirdLoginRelationRepository;
    /**
     * 获取Access Token url
     */
    public static final String OAUTH_TOKEN_URL = "/oauth/token?grant_type=client_credentials&client_id=%s&client_secret=%s";

    /**
     * 通过token检验用户有效登录url 免登接口访问地址
     */
    public static final String LOGIN_CONFIRM_URL = "/member/login/confirm";

    /**
     * 发短信前的验证接口
     */
    public static final String VALIDATION_CHECK_URL = "/validation/check/geetest";

    /**
     * 樊登实现的极验API
     */
    public static final String VALIDATION_JY_URL = "/validation/jy/api";

    /**
     * 获取樊登验证码用于用户登录注册
     */
    public static final String ACQUIRE_ACCESSTOKEN_URL = "/member/acquire/accessToken";

    /**
     * web商城登陆及注册
     */
    public static final String MEMBER_AUTH_LOGIN_URL = "/member/auth/login";

    /**
     * 微信授权登录
     */
    public static final String WX_AUTH_LOGIN_URL = "/member/wechat/login";

    /**
     * 通过用户编号查询用户积分余额
     */
    public static final String POINT_BALANCE_SEARCH_URL = "/point/balance/search";

    /**
     * 通过用户编号查询用户积分余额
     */
    public static final String KNOWLEDGE_BALANCE_SEARCH_URL = "/open/findCounselorByUserId";

    /**
     * 会员锁定积分
     */
    public static final String POINT_LOCK_URL = "/point/lock";

    /**
     * 会员锁定知豆
     */
    public static final String KNOWLEDGE_LOCK_URL = "/lockBeans";

    /**
     * 通过抵扣码扣除积分
     */
    public static final String POINT_DEDUCT_URL = "/point/deduct";
    /**
     * 通过抵扣码扣除知豆
     */
    public static final String KNOWLEDGE_DEDUCT_URL = "/deductBeans";

    /**
     * 会员锁定的积分返还接口
     */
    public static final String POINT_LOCK_FALLBACK_URL = "/point/lock/fallback";
    /**
     * 会员锁定的知豆返还接口
     */
    public static final String KNOWLEDGE_LOCK_FALLBACK_URL = "/fallbackLockBeans";

    /**
     * 用户退积分
     */
    public static final String POINT_FALLBACK_URL = "/point/fallback";
    /**
     * 用户退知豆
     */
    public static final String KNOWLEDGE_FALLBACK_URL = "/fallbackBeans";

    /**
     * 用户退积分
     */
    public static final String MATERIAL_CHENCK_URL = "/file/image/uploadWithVerify";

    /**
     * 调用樊登接口公共参数
     */
    public static final String PARAMETER = "?appid=%s&sign=%s&access_token=%s";

    /**
     * 樊登token 失效时间
     */
    public static final Long FANDENG_TIME = 7000L;
    @Autowired
    private BinderAwareChannelResolver resolver;

    /**
     * 樊登免登
     *
     * @param request
     * @return
     */

    public BaseResponse<FanDengLoginResponse> loginConfirm(@Valid FanDengLoginRequest request) {
        String body = JSON.toJSONString(request);
        String result = getUrl(jointUrl(LOGIN_CONFIRM_URL + PARAMETER, body), JSON.toJSONString(request));
        FanDengLoginResponse response =
                (FanDengLoginResponse) exchange(result, FanDengLoginResponse.class);
//        Customer customer =
//                customerRepository.findByFanDengUserNoAndDelFlag(response.getUserNo(), DeleteFlag.NO);
//        customer = extractLogin(customer, response);
        return BaseResponse.success(response);
    }

    /**
     * 账号密码 登陆注册
     *
     * @param request
     * @return
     */
    public BaseResponse<FanDengLoginResponse> authLogin(@Valid FanDengAuthLoginRequest request) {
        String body = JSON.toJSONString(request);
        String result = getUrl(jointUrl(MEMBER_AUTH_LOGIN_URL + PARAMETER, body),
                body);
        log.info("authLogin param: {}", result);
        FanDengLoginResponse response =
                (FanDengLoginResponse) exchange(result, FanDengLoginResponse.class);
//        //实现商城登陆注册 逻辑
//        Customer customer =
//                customerRepository.findByFanDengUserNoAndDelFlag(response.getUserNo(), DeleteFlag.NO);
//
////        if (!response.getValidFlag()){
////           throw new SbcRuntimeException("K-120803");
////         }
//        response.setMobile(request.getMobile());
//        customer = extractLogin(customer, response);

        return BaseResponse.success(response);
    }

    public BaseResponse<FanDengWxAuthLoginResponse.WxAuthLoginData> wxAuthLogin(FanDengWxAuthLoginRequest request) {
        String body = JSON.toJSONString(request);
        String result = getUrlNew(jointUrl(WX_AUTH_LOGIN_URL + PARAMETER, body), body);
        log.info("wx authLogin param: {}", result);
        FanDengWxAuthLoginResponse response = JSONObject.parseObject(result, FanDengWxAuthLoginResponse.class);
        if(!"0000".equals(response.getStatus())){
            throw new SbcRuntimeException("K-120801", response.getMsg());
        }
        return BaseResponse.success(response.getData());
    }

/*    *//**
     * 抽取对接外部登陆公共方法
     *
     * @return
     *//*
    public Customer extractLogin(Customer customer, FanDengLoginResponse response) {
        if (customer == null) {
            customer =
                    customerRepository.findByCustomerAccountAndDelFlag(response.getMobile(), DeleteFlag.NO);
            if (customer != null) {
                if (StringUtils.isBlank(customer.getFanDengUserNo())) {
//                    customer.setFanDengUserNo(response.getUserNo());
//                    customer.setLoginTime(LocalDateTime.now());
                    customerRepository.updateLoginTimeAndFanDengUserNo(customer.getCustomerId(),
                            LocalDateTime.now(),
                            HttpUtil.getIpAddr(), response.getUserNo());
                } else {
                    //手机号码相同 但是樊登id  不一样 抛出异常
                    throw new SbcRuntimeException("K-120808");
                }
            } else {
                //实现商城登陆注册 逻辑
                customer = this.buildNewCustomer(response.getUserNo());
                customer.setCustomerAccount(response.getMobile());
                customer = customerRepository.save(customer);
                customer.setSafeLevel(SafeLevelUtil.getSafeLevel(customer.getCustomerPassword()));
                //生成盐值
                String saltVal = SecurityUtil.getNewPsw();
                //生成加密后的登陆密码
                String encryptPwd = SecurityUtil.getStoreLogpwd(String.valueOf(customer.getCustomerId()), customer
                        .getCustomerPassword(), saltVal);
                customer.setCustomerSaltVal(saltVal);
                customer.setCustomerPassword(encryptPwd);
                customer = customerRepository.save(customer);

                CustomerDetail customerDetail = this.buildNewCustomerDetail(response.getMobile());
                //手机号码
                customerDetail.setContactPhone(response.getMobile());
                customerDetail.setCustomerId(customer.getCustomerId());
                customerDetail = customerDetailRepository.save(customerDetail);
                //初始化会员资金信息
                producerService.initCustomerFunds(customer.getCustomerId(),
                        customerDetail.getCustomerName(), customer.getCustomerAccount());
                customer.setCustomerDetail(customerDetail);
            }
        } else {
            customer.setLoginTime(LocalDateTime.now());
            if (StringUtils.isBlank(customer.getCustomerAccount()) || !customer.getCustomerAccount().equals(response.getMobile())) {
                //如果樊登id 一样 并且手机号码不一样
                Customer repository =
                        customerRepository.findByCustomerAccountAndDelFlag(response.getMobile(), DeleteFlag.NO);
                if (repository != null) {
                    //手机号码有重复 抛出异常
                    throw new SbcRuntimeException("K-120808");
                }
                //并且新手机号码不存在用户库  修改手机号码
                CustomerAccountModifyRequest request = new CustomerAccountModifyRequest();
                request.setCustomerAccount(response.getMobile());
                request.setCustomerId(customer.getCustomerId());

                int result = customerRepository.updateNewCustomerAccount(request.getCustomerId(), request.getCustomerAccount(), LocalDateTime.now(), HttpUtil.getIpAddr());
                if (Constants.yes == result) {
                    //修改会员账号，同时修改会员资金-会员账号字段
                    producerService.modifyCustomerAccountWithCustomerFunds(request.getCustomerId(), request.getCustomerAccount());
                    //修改会员账号，同时修改分销员-会员账号字段
                    distributionCustomerRepository.updateCustomerAccountByCustomerId(request.getCustomerId(), request.getCustomerAccount());
                    //修改会员账号，同事修改会员提现管理-会员账号字段
                    producerService.modifyCustomerAccountWithCustomerDrawCash(request.getCustomerId(), request.getCustomerAccount());
                    //ares埋点-会员-会员修改绑定手机号
                    customerAresService.dispatchFunction(AresFunctionType.EDIT_CUSTOMER_PHONE, request.getCustomerAccount(),
                            request.getCustomerId());
                    resolver.resolveDestination(MQConstant.Q_ES_SERVICE_CUSTOMER_MODIFY_CUSTOMER_ACCOUNT)
                            .send(new GenericMessage<>(JSONObject.toJSONString(request)));

                }

            } else {
                customerRepository.updateLoginTime(customer.getCustomerId(), LocalDateTime.now(), HttpUtil.getIpAddr());
            }

        }

        String customerId = customer.getCustomerId();
        //0：未入会 1：体验中 2：体验过期 3：正式会员 4：会员过期 5：停用
        Integer userStatus = response.getUserStatus();

        //物理删除绑定关系
        paidCardCustomerRelRepository.deleteByCustomerId(Constants.no, customerId);
        Optional<FdPaidCast> fdPaidCast = fdPaidCastRepository.findByFdPayTypeAndDelFlag(userStatus, DeleteFlag.NO);
        if (fdPaidCast.isPresent()) {
            //查询对照表是否存在映射关系如果存在则需要去查关联表
            FdPaidCast paidCast = fdPaidCast.get();
            String paidCardId = paidCast.getPaidCardId();
            PaidCardCustomerRel paidCardCustomerRel = new PaidCardCustomerRel();
            paidCardCustomerRel.setId(UUIDUtil.getUUID());
            paidCardCustomerRel.setBeginTime(
                    LocalDateTime.ofInstant(Instant.ofEpochMilli(response.getVipStartTime()), ZoneId.systemDefault()));
            paidCardCustomerRel.setEndTime(
                    LocalDateTime.ofInstant(Instant.ofEpochMilli(response.getVipEndTime()), ZoneId.systemDefault()));
            paidCardCustomerRel.setCustomerId(customerId);
            paidCardCustomerRel.setDelFlag(DeleteFlag.NO);
            paidCardCustomerRel.setPaidSource(Constants.no);
            paidCardCustomerRel.setPaidCardId(paidCardId);
            paidCardCustomerRel.setSendExpireMsgFlag(Boolean.FALSE);
            paidCardCustomerRel.setSendMsgFlag(Boolean.FALSE);
            //TODO 目前先空着，后续在做修改
            paidCardCustomerRel.setCardNo(BusinessCodeGenUtils.genPaidCardBuyRecordCode());
            paidCardCustomerRelRepository.save(paidCardCustomerRel);
        }

        return customer;
    }*/


    @Transactional
    public CustomerVO saveFanDengCustomer(FanDengModifyCustomerRequest request){
        Customer customer = buildNewCustomer(request.getFanDengUserNo(), request.getCustomerAccount());
        customer = customerRepository.save(customer);
        customer.setSafeLevel(SafeLevelUtil.getSafeLevel(customer.getCustomerPassword()));
        //生成盐值
        String saltVal = SecurityUtil.getNewPsw();
        //生成加密后的登陆密码
        String encryptPwd = SecurityUtil.getStoreLogpwd(String.valueOf(customer.getCustomerId()), customer
                .getCustomerPassword(), saltVal);
        customer.setCustomerSaltVal(saltVal);
        customer.setCustomerPassword(encryptPwd);
        customer = customerRepository.save(customer);
        CustomerDetail customerDetail = this.buildNewCustomerDetail(request.getNickName());
        //手机号码
        customerDetail.setContactPhone(request.getCustomerAccount());
        customerDetail.setCustomerId(customer.getCustomerId());
        customerDetail = customerDetailRepository.save(customerDetail);
        //初始化会员资金信息
        producerService.initCustomerFunds(customer.getCustomerId(),
                customerDetail.getCustomerName(), customer.getCustomerAccount());
        customer.setCustomerDetail(customerDetail);

        if (StringUtils.isNotEmpty(request.getProfilePhoto())){
            //初始化头像
            List<ThirdLoginRelation> thirdLoginRelationList = thirdLoginRelationRepository.findAllByCustomerIdAndThirdTypeAndDelFlag(customer.getCustomerId(),
                    ThirdLoginType.WECHAT, DeleteFlag.NO);
            if (CollectionUtils.isEmpty(thirdLoginRelationList)){
                //初始化头像
                ThirdLoginRelation thirdLoginRelation = new ThirdLoginRelation();
                thirdLoginRelation.setCustomerId(customer.getCustomerId());
                thirdLoginRelation.setDelFlag(DeleteFlag.NO);
                thirdLoginRelation.setThirdLoginType(ThirdLoginType.WECHAT);
                thirdLoginRelation.setStoreId(Constants.BOSS_DEFAULT_STORE_ID);
                thirdLoginRelation.setHeadimgurl(request.getProfilePhoto());
                thirdLoginRelationRepository.save(thirdLoginRelation);
                }else {
                    ThirdLoginRelation loginRelation = thirdLoginRelationList.stream().findFirst().get();
                    loginRelation.setHeadimgurl(request.getProfilePhoto());
                    thirdLoginRelationRepository.save(loginRelation);
                }
        }
        return KsBeanUtil.convert(customer,CustomerVO.class);
    }

    @Transactional
    public BaseResponse  savePaidCard(FanDengModifyPaidCustomerRequest request){
        String customerId = request.getCustomerId();
        //0：未入会 1：体验中 2：体验过期 3：正式会员 4：会员过期 5：停用
        Integer userStatus = request.getUserStatus();
        //物理删除绑定关系
        paidCardCustomerRelRepository.deleteByCustomerId(Constants.no, customerId);
        Optional<FdPaidCast> fdPaidCast = fdPaidCastRepository.findByFdPayTypeAndDelFlag(userStatus, DeleteFlag.NO);
        if (fdPaidCast.isPresent()) {
            //查询对照表是否存在映射关系如果存在则需要去查关联表
            FdPaidCast paidCast = fdPaidCast.get();
            String paidCardId = paidCast.getPaidCardId();
            PaidCardCustomerRel paidCardCustomerRel = new PaidCardCustomerRel();
            paidCardCustomerRel.setId(UUIDUtil.getUUID());
            paidCardCustomerRel.setBeginTime(
                    LocalDateTime.ofInstant(Instant.ofEpochMilli(request.getVipStartTime()), ZoneId.systemDefault()));
            paidCardCustomerRel.setEndTime(
                    LocalDateTime.ofInstant(Instant.ofEpochMilli(request.getVipEndTime()), ZoneId.systemDefault()));
            paidCardCustomerRel.setCustomerId(customerId);
            paidCardCustomerRel.setDelFlag(DeleteFlag.NO);
            paidCardCustomerRel.setPaidSource(Constants.no);
            paidCardCustomerRel.setPaidCardId(paidCardId);
            paidCardCustomerRel.setSendExpireMsgFlag(Boolean.FALSE);
            paidCardCustomerRel.setSendMsgFlag(Boolean.FALSE);
            //TODO 目前先空着，后续在做修改
            paidCardCustomerRel.setCardNo(BusinessCodeGenUtils.genPaidCardBuyRecordCode());
            paidCardCustomerRelRepository.save(paidCardCustomerRel);
        }
        return BaseResponse.SUCCESSFUL();
    }

    @Transactional
    public BaseResponse modifyCustomerLoginTime(FanDengModifyCustomerLoginTimeRequest request){

/*        Customer customer =
                customerRepository.findByCustomerIdAndDelFlag(request.getCustomerId(), DeleteFlag.NO);
        // 修改昵称
        if (Objects.nonNull(request.getNickName())
                && !request.getNickName().equals(customer.getCustomerDetail().getCustomerName())){
            customer.getCustomerDetail().setCustomerName(request.getNickName());
            customerDetailRepository.save(customer.getCustomerDetail());

            Map<String,String>  param = new HashMap();
            param.put("customerName",request.getNickName());
            param.put("customerId",request.getCustomerId());
            resolver.resolveDestination(MQConstant.Q_ES_SERVICE_CUSTOMER_MODIFY_BASE_INFO).
                    send(new GenericMessage<>(JSONObject.toJSONString(param)));
        }

        if (StringUtils.isNotEmpty(request.getProfilePhoto())){
            //初始化头像
            List<ThirdLoginRelation> thirdLoginRelationList = thirdLoginRelationRepository.findAllByCustomerIdAndThirdTypeAndDelFlag(request.getCustomerId(),
                    ThirdLoginType.WECHAT, DeleteFlag.NO);
                if (CollectionUtils.isEmpty(thirdLoginRelationList)){
                    //初始化头像
                    ThirdLoginRelation thirdLoginRelation = new ThirdLoginRelation();
                    thirdLoginRelation.setCustomerId(request.getCustomerId());
                    thirdLoginRelation.setDelFlag(DeleteFlag.NO);
                    thirdLoginRelation.setThirdLoginType(ThirdLoginType.WECHAT);
                    thirdLoginRelation.setStoreId(Constants.BOSS_DEFAULT_STORE_ID);
                    thirdLoginRelation.setHeadimgurl(request.getProfilePhoto());
                    thirdLoginRelationRepository.save(thirdLoginRelation);
                }else {
                    ThirdLoginRelation loginRelation = thirdLoginRelationList.stream().findFirst().get();
                    if (!loginRelation.getHeadimgurl().equals(request.getProfilePhoto())){
                        loginRelation.setHeadimgurl(request.getProfilePhoto());
                        thirdLoginRelationRepository.save(loginRelation);
                    }
                }
        }*/
        customerRepository.updateLoginTime(request.getCustomerId(),LocalDateTime.now(),request.getLoginIp());
        return BaseResponse.SUCCESSFUL();
    }

    @Transactional
    public BaseResponse modifyCustomerNameAndImg(FanDengModifyCustomerLoginTimeRequest request){

        Customer customer =
                customerRepository.findByCustomerIdAndDelFlag(request.getCustomerId(), DeleteFlag.NO);
        // 修改昵称
        if (Objects.nonNull(request.getNickName())
                && !request.getNickName().equals(customer.getCustomerDetail().getCustomerName())){
            customer.getCustomerDetail().setCustomerName(request.getNickName());
            customerDetailRepository.save(customer.getCustomerDetail());

            Map<String,String>  param = new HashMap();
            param.put("customerName",request.getNickName());
            param.put("customerId",request.getCustomerId());
            resolver.resolveDestination(MQConstant.Q_ES_SERVICE_CUSTOMER_MODIFY_BASE_INFO).
                    send(new GenericMessage<>(JSONObject.toJSONString(param)));
        }
        if (StringUtils.isNotEmpty(request.getProfilePhoto())){
            //初始化头像
            List<ThirdLoginRelation> thirdLoginRelationList = thirdLoginRelationRepository.findAllByCustomerIdAndThirdTypeAndDelFlag(request.getCustomerId(),
                    ThirdLoginType.WECHAT, DeleteFlag.NO);
                if (CollectionUtils.isEmpty(thirdLoginRelationList)){
                    //初始化头像
                    ThirdLoginRelation thirdLoginRelation = new ThirdLoginRelation();
                    thirdLoginRelation.setCustomerId(request.getCustomerId());
                    thirdLoginRelation.setDelFlag(DeleteFlag.NO);
                    thirdLoginRelation.setThirdLoginType(ThirdLoginType.WECHAT);
                    thirdLoginRelation.setStoreId(Constants.BOSS_DEFAULT_STORE_ID);
                    thirdLoginRelation.setHeadimgurl(request.getProfilePhoto());
                    thirdLoginRelationRepository.save(thirdLoginRelation);
                }else {
                    ThirdLoginRelation loginRelation = thirdLoginRelationList.stream().findFirst().get();
                    if (Objects.nonNull(request.getProfilePhoto())
                            && !request.getProfilePhoto().equals(loginRelation.getHeadimgurl())){
                        loginRelation.setHeadimgurl(request.getProfilePhoto());
                        thirdLoginRelationRepository.save(loginRelation);
                    }
                }
        }
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 解决账号冲突 全部赋值为空
     * @param request
     * @return
     */
    @Transactional
    public BaseResponse modifyCustomerAccountFanDeng(FanDengModifyAccountFanDengRequest request){
        CustomerAccountModifyRequest modifyRequest = new CustomerAccountModifyRequest();
        modifyRequest.setCustomerId(request.getCustomerId());
        modifyRequest.setCustomerAccount("");
        customerService.updateCustomerAccount(modifyRequest);
        customerRepository.updateLoginTimeAndFanDengUserNo(request.getCustomerId(),LocalDateTime.now()
        ,HttpUtil.getIpAddr(),"");
        return BaseResponse.SUCCESSFUL();
    }
    /**
     * 发送验证码前置接口
     *
     * @param request
     * @return
     */
    public BaseResponse<FanDengPrepositionResponse> preposition(@Valid FanDengPrepositionRequest request) {
        String body = JSON.toJSONString(request);
        String result = getUrl(jointUrl(VALIDATION_CHECK_URL + PARAMETER, body),
                body);
        FanDengPrepositionResponse response =
                (FanDengPrepositionResponse) exchange(result, FanDengPrepositionResponse.class);
        //实现商城登陆注册 逻辑
        return BaseResponse.success(response);
    }

    public BaseResponse<FanDengVerifyResponse> verify(@Valid FanDengVerifyRequest request) {
        String body = JSON.toJSONString(request);
        String result = getUrl(jointUrl(VALIDATION_JY_URL + PARAMETER, body),
                body);
        FanDengVerifyResponse response =
                (FanDengVerifyResponse) exchange(result, FanDengVerifyResponse.class);

        return BaseResponse.success(response);
    }

    public BaseResponse<FanDengSengCodeResponse> sendCode(@Valid FanDengSendCodeRequest request) {
        String body = JSON.toJSONString(request);
        String result = getUrl(jointUrl(ACQUIRE_ACCESSTOKEN_URL + PARAMETER, body),
                body);
        FanDengSengCodeResponse response =
                (FanDengSengCodeResponse) exchange(result, FanDengSengCodeResponse.class);

        return BaseResponse.success(response);
    }

    public BaseResponse<FanDengPointResponse> getByUserNoPoint(@Valid FanDengPointRequest request) {
        String body = JSON.toJSONString(request);
        String result = getUrl(jointUrl(POINT_BALANCE_SEARCH_URL + PARAMETER, body),
                body);
        FanDengPointResponse response =
                (FanDengPointResponse) exchange(result, FanDengPointResponse.class);

        return BaseResponse.success(response);
    }

    public BaseResponse<FanDengKnowledgeResponse> getKnowledgeByFanDengNo(@Valid FanDengKnowledgeRequest request) {
        String body = JSON.toJSONString(request);
        String result = getUrl(jointUrl(KNOWLEDGE_BALANCE_SEARCH_URL + PARAMETER, body),
                body);
        FanDengKnowledgeResponse response =
                (FanDengKnowledgeResponse) exchangeAll(result, FanDengKnowledgeResponse.class);

        return BaseResponse.success(response);
    }

    public BaseResponse<FanDengLockResponse> pointLock(@Valid FanDengPointLockRequest request) {
        String body = JSON.toJSONString(request);
        String result = getUrl(jointUrl(POINT_LOCK_URL + PARAMETER, body),
                body);
        FanDengLockResponse response =
                (FanDengLockResponse) exchange(result, FanDengLockResponse.class);

        return BaseResponse.success(response);
    }

    public BaseResponse<FanDengLockResponse> knowledgeLock(@Valid FanDengKnowledgeLockRequest request) {
        String body = JSON.toJSONString(request);
        String result = getUrl(jointUrl(KNOWLEDGE_LOCK_URL + PARAMETER, body),
                body);
        FanDengLockResponse response =
                (FanDengLockResponse) exchange(result, FanDengLockResponse.class);

        return BaseResponse.success(response);
    }

    public BaseResponse<FanDengConsumeResponse> pointDeduct(@Valid FanDengPointDeductRequest request) {
        String body = JSON.toJSONString(request);
        String result = getUrl(jointUrl(POINT_DEDUCT_URL + PARAMETER, body),
                body);
        FanDengConsumeResponse response =
                (FanDengConsumeResponse) exchange(result, FanDengConsumeResponse.class);

        return BaseResponse.success(response);
    }
    public BaseResponse<FanDengConsumeResponse> knowledgeDeduct(@Valid FanDengPointDeductRequest request) {
        String body = JSON.toJSONString(request);
        String result = getUrl(jointUrl(KNOWLEDGE_DEDUCT_URL + PARAMETER, body),
                body);
        FanDengConsumeResponse response = new FanDengConsumeResponse();
        response.setSuccFlag(exchangeBoolean(result));

        return BaseResponse.success(response);
    }

    public BaseResponse<FanDengConsumeResponse> pointCancel(@Valid FanDengPointCancelRequest request) {
        String body = JSON.toJSONString(request);
        String result = getUrl(jointUrl(POINT_LOCK_FALLBACK_URL + PARAMETER, body),
                body);
        FanDengConsumeResponse response =
                (FanDengConsumeResponse) exchange(result, FanDengConsumeResponse.class);

        return BaseResponse.success(response);
    }
    public BaseResponse<FanDengConsumeResponse> knowledgeCancel(@Valid FanDengPointCancelRequest request) {
        String body = JSON.toJSONString(request);
        String result = getUrl(jointUrl(KNOWLEDGE_LOCK_FALLBACK_URL + PARAMETER, body),
                body);
        FanDengConsumeResponse response = new FanDengConsumeResponse();
        response.setSuccFlag(exchangeBoolean(result));
        return BaseResponse.success(response);
    }

    public BaseResponse<FanDengConsumeResponse> pointRefund(@Valid FanDengPointRefundRequest request) {
        String body = JSON.toJSONString(request);
        String result = getUrl(jointUrl(POINT_FALLBACK_URL + PARAMETER, body),
                body);
        FanDengConsumeResponse response =
                (FanDengConsumeResponse) exchange(result, FanDengConsumeResponse.class);

        return BaseResponse.success(response);
    }
    public BaseResponse<FanDengConsumeResponse> knowledgeRefund(@Valid FanDengKnowledgeRefundRequest request) {
        String body = JSON.toJSONString(request);
        String result = getUrl(jointUrl(KNOWLEDGE_FALLBACK_URL + PARAMETER, body),
                body);
        FanDengConsumeResponse response = new FanDengConsumeResponse();
        response.setSuccFlag(exchangeBoolean(result));
        return BaseResponse.success(response);
    }

    public BaseResponse<MaterialCheckResponse> materialCheck(@Valid MaterialCheckRequest request) throws Exception {
        MaterialCheckResponse parseObject = new MaterialCheckResponse();
        parseObject.setUrl(host + jointUrl(MATERIAL_CHENCK_URL + PARAMETER, request.getFileName()));
        return BaseResponse.success(parseObject);
    }

    /**
     * 除免登接口其他调用接口统一入口
     *
     * @param url
     * @param request
     * @return
     */
    private String getUrl(String url, String body) {
        try {
            log.info("ExternalService getUrl appId: {} url:{}", appid, url);
            log.info("ExternalService getUrl：{}, param{}", url, body);
            HttpResponse httpResponse = HttpUtils.doPost(host,
                    url, getMap(), null, body);
            if (HttpStatus.SC_UNAUTHORIZED ==  httpResponse.getStatusLine().getStatusCode()) {
                redisService.delete(appid);
                httpResponse = HttpUtils.doPost(host,
                        url, getMap(), null, body);
            }
            log.info("ExternalService getUrl 返回结果：{}", httpResponse);

            if (HttpStatus.SC_OK == httpResponse.getStatusLine().getStatusCode()) {
//                log.info("请求接口：{},请求参数：{}", host + url, body);
                String entity = EntityUtils.toString(httpResponse.getEntity());
                log.info("樊登请求接口：{},请求参数{},返回状态：{}", url, body, entity);
                return entity;
            }
        } catch (Exception e) {
            log.error("樊登接口调用异常：{}", e);
        }
        throw new SbcRuntimeException("K-120801", "樊登接口异常");
    }

    private String getUrlNew(String url, String body) {
        try {
            log.info("ExternalService getUrl appId: {} , url:{} , param: {}", appid, url, body);
            HttpResponse httpResponse = HttpUtils.doPost(host, url, getMap(), null, body);
            if (HttpStatus.SC_UNAUTHORIZED ==  httpResponse.getStatusLine().getStatusCode()) {
                redisService.delete(appid);
                httpResponse = HttpUtils.doPost(host, url, getMap(), null, body);
            }
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            String entity = EntityUtils.toString(httpResponse.getEntity());
            log.info("ExternalService getUrl status：{} , body : {}", statusCode, entity);
            if (HttpStatus.SC_OK == statusCode) {
                return entity;
            }
        } catch (Exception e) {
            log.error("樊登接口调用异常：{}", e);
        }
        throw new SbcRuntimeException("K-120801", "樊登接口异常");
    }

    /**
     * 获取樊登token
     *
     * @return
     */
    private String getAccessToken() {
        String accessToken = redisService.getString(appid);
        log.info("ExternalService getAccessToken appId: {} accessToken:{}", appid, accessToken);
//        String accessToken = "";
        if (StringUtils.isBlank(accessToken)) {
            String url = String.format(OAUTH_TOKEN_URL, appid, appsecret);
            try {
                log.info("ExternalService getAccessToken appId: {} url:{}", appid, url);
                HttpResponse httpResponse = HttpUtils.doPost(host, url, getMap(), null, null);
                int statusCode = httpResponse.getStatusLine().getStatusCode();
                String entity = EntityUtils.toString(httpResponse.getEntity());
                log.info("ExternalService getAccessToken appId: {} httpResponse:{}", appid, entity);
                if (200 == statusCode) {
                    JSONObject object = JSONObject.parseObject(entity);
                    String error = object.getString("error");
                    if (StringUtils.isBlank(error)) {
                        //获取到token
                        accessToken = object.getString("value");
                        redisService.setString(appid, accessToken, FANDENG_TIME);
                        return accessToken;
                    }
                    log.error("获取樊登AccessToken失败异常:{}", entity);
                }
            } catch (Exception e) {
                log.error("获取樊登token:{}", e);
            }
            throw new SbcRuntimeException("K-120802");
        }
        return accessToken;
    }

    /**
     * 拼接url参数  并生成签名
     *
     * @param url
     * @return
     */
    private String jointUrl(String url, String body) {
        try {
            String encryptSHA1 = SHAUtils.encryptSHA1(body + appid, appsecret);
            String realUrl = String.format(url, appid, encryptSHA1, getAccessToken());
            log.info("ExternalService jointUrl appId: {} url:{}, realUrl: {}", appid, url, realUrl);
            return realUrl;
        } catch (Exception e) {
            log.error("拼接加密参数:{}", e);
        }

        return null;
    }

    /**
     * 获取请求头参数
     *
     * @return
     */
    public Map<String, String> getMap() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json;charset=UTF-8");
        return headers;
    }

    /**
     * 转换成返回对象
     *
     * @param body
     * @param t
     * @return
     */
    private Object exchange(String body, Class t) {
        JSONObject object = JSONObject.parseObject(body);
        String code = (String) object.get("status");
        if (code == null || (code != null && code.equals("0000"))) {
            JSONObject data = (JSONObject) object.get("data");
            Object parseObject = JSON.parseObject(data.toString(), t);
            return parseObject;
        }
        throw new SbcRuntimeException("K-120801", (String) object.get("msg"));
    }

    /**
     * 转换成返回对象
     *
     * @param body
     * @param t
     * @return
     */
    private Object exchangeAll(String body, Class t) {
        JSONObject object = JSONObject.parseObject(body);
        String code = (String) object.get("status");
        if (code == null || (code != null && code.equals("0000"))) {
            JSONObject data = (JSONObject) object.get("data");
            Object parseObject = JSON.parseObject(data.toString(), t);
            return parseObject;
        }
        return null;
    }

    /**
     * 转换成返回对象
     *
     * @param body
     * @param t
     * @return
     */
    private Boolean exchangeBoolean(String body) {
        JSONObject object = JSONObject.parseObject(body);
        String code = (String) object.get("status");
        if (code == null || (code != null && code.equals("0000"))) {
            Boolean data = (Boolean) object.get("data");
            return data;
        }
        throw new SbcRuntimeException("K-120801", (String) object.get("msg"));
    }

    private Customer buildNewCustomer(String userNo,String customerAccount) {
        String password = SecurityUtil.getNewToken();
        Customer customer = new Customer();
        customer.setCustomerPassword(password);
        customer.setFanDengUserNo(userNo);
        customer.setCustomerType(CustomerType.PLATFORM);
        customer.setCustomerLevelId(customerLevelService.getDefaultLevel().getCustomerLevelId());
        customer.setGrowthValue(0L); // 成长值默认为0
        customer.setPointsAvailable(0L); // 可用积分默认为0
        customer.setPointsUsed(0L);// 已用积分默认为0
        customer.setSafeLevel(20);
        customer.setCustomerAccount(customerAccount);
        customer.setCustomerSaltVal("");
        customer.setDelFlag(DeleteFlag.NO);
        customer.setCheckState(CheckState.CHECKED);
        customer.setCheckTime(LocalDateTime.now());
        customer.setPayErrorTime(0);
        customer.setLoginErrorCount(0);
        customer.setCreateTime(LocalDateTime.now());
//        customer.setLoginTime(LocalDateTime.now());
        customer.setEnterpriseCheckState(EnterpriseCheckState.INIT); // 企业会员审核状态
        return customer;
    }

    private CustomerDetail buildNewCustomerDetail(String name) {
        CustomerDetail customerDetail = new CustomerDetail();
        customerDetail.setCustomerName(name);
        customerDetail.setDelFlag(DeleteFlag.NO);
        customerDetail.setCustomerStatus(CustomerStatus.ENABLE);
        customerDetail.setContactName(name);
        customerDetail.setContactPhone("");
//        customerDetail.setEmployeeId("2c8080815cd3a74a015cd3ae86850001");
        customerDetail.setCreateTime(LocalDateTime.now());
        customerDetail.setIsDistributor(DefaultFlag.NO);
        return customerDetail;
    }
}
