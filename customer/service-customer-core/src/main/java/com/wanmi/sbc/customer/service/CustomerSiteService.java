package com.wanmi.sbc.customer.service;

import io.seata.spring.annotation.GlobalTransactional;
import com.wanmi.sbc.common.annotation.MultiSubmit;
import com.wanmi.sbc.common.base.MessageMQRequest;
import com.wanmi.sbc.common.enums.AccountType;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.NodeType;
import com.wanmi.sbc.common.enums.node.AccountSecurityType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.redis.CacheKeyConstant;
import com.wanmi.sbc.common.util.*;
import com.wanmi.sbc.customer.api.constant.CustomerErrorCode;
import com.wanmi.sbc.customer.api.constant.EmployeeErrorCode;
import com.wanmi.sbc.customer.api.constant.ThirdLoginRelationErrorCode;
import com.wanmi.sbc.customer.api.request.growthvalue.CustomerGrowthValueAddRequest;
import com.wanmi.sbc.customer.api.request.loginregister.CustomerCheckPayPasswordRequest;
import com.wanmi.sbc.customer.api.request.loginregister.CustomerRegisterRequest;
import com.wanmi.sbc.customer.api.request.points.CustomerPointsDetailAddRequest;
import com.wanmi.sbc.customer.api.response.loginregister.ThirdLoginAndBindResponse;
import com.wanmi.sbc.customer.ares.CustomerAresService;
import com.wanmi.sbc.customer.bean.enums.*;
import com.wanmi.sbc.customer.detail.model.root.CustomerDetail;
import com.wanmi.sbc.customer.detail.repository.CustomerDetailRepository;
import com.wanmi.sbc.customer.employee.model.root.Employee;
import com.wanmi.sbc.customer.employee.repository.EmployeeRepository;
import com.wanmi.sbc.customer.enterpriseinfo.model.root.EnterpriseInfo;
import com.wanmi.sbc.customer.enterpriseinfo.repository.EnterpriseInfoRepository;
import com.wanmi.sbc.customer.enterpriseinfo.service.EnterpriseInfoService;
import com.wanmi.sbc.customer.growthvalue.service.GrowthValueIncreaseService;
import com.wanmi.sbc.customer.level.service.CustomerLevelService;
import com.wanmi.sbc.customer.model.entity.CustomerDetailQueryRequest;
import com.wanmi.sbc.customer.model.root.Customer;
import com.wanmi.sbc.customer.mq.ProducerService;
import com.wanmi.sbc.customer.points.service.CustomerPointsDetailService;
import com.wanmi.sbc.customer.quicklogin.model.root.ThirdLoginRelation;
import com.wanmi.sbc.customer.quicklogin.service.ThirdLoginRelationService;
import com.wanmi.sbc.customer.redis.RedisService;
import com.wanmi.sbc.customer.repository.CustomerRepository;
import com.wanmi.sbc.customer.sms.SmsSendUtil;
import com.wanmi.sbc.customer.util.SafeLevelUtil;
import com.wanmi.sbc.setting.api.provider.AuditQueryProvider;
import com.wanmi.sbc.setting.bean.enums.ConfigType;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 会员相关功能信息
 * Created by CHENLI on 2017/4/20.
 */
@Service
public class CustomerSiteService {
    @Autowired
    CustomerAresService customerAresService;

    @Autowired
    private CustomerDetailRepository customerDetailRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CustomerLevelService customerLevelService;

    @Autowired
    private AuditQueryProvider auditQueryProvider;

    @Autowired
    private SmsSendUtil smsSendUtil;

    @Autowired
    private OsUtil osUtil;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ThirdLoginRelationService thirdLoginRelationService;

    @Autowired
    private GrowthValueIncreaseService growthValueIncreaseService;

    @Autowired
    private CustomerPointsDetailService customerPointsDetailService;

    @Autowired
    private ProducerService producerService;

    @Autowired
    private EnterpriseInfoService enterpriseInfoService;

    @Autowired
    private EnterpriseInfoRepository enterpriseInfoRepository;


    /**
     * 密码错误超过6次后锁定的时间，单位：分钟
     */
    private static final int LOCK_MINUTES = 30;

    /**
     * 允许密码错误最大次数
     */
    private static final int PASS_WRONG_MAX_COUNTS = 5;

    /**
     * 注册时每个客户默认负责业务员是系统默认system
     */
    private static final String DEFAULT_EMPLOYEE = "system";

    /**
     * 登录
     * 遇SbcRuntimeException不回滚，该SbcRuntimeException本身是一种业务错误
     */
    @Transactional(noRollbackFor = SbcRuntimeException.class)
    @MultiSubmit
    public Customer login(String customerAccount, String password) throws SbcRuntimeException {
        CustomerDetail customerDetail = getCustomerDetailByAccount(customerAccount);
        if (Objects.isNull(customerDetail) || Objects.isNull(customerDetail.getCustomer())) {
            String errKey = CacheKeyConstant.LOGIN_ERR.concat(customerAccount);
            String lockTimeKey = CacheKeyConstant.S2B_BOSS_LOCK_TIME.concat(customerAccount);
            //登录错误次数
            String errCountStr = redisService.getString(errKey);
            //锁定时间
            String lockTimeStr = redisService.getString(lockTimeKey);
            int error = NumberUtils.toInt(errCountStr);

            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime lockTime = LocalDateTime.now();
            if (StringUtils.isNotBlank(lockTimeStr)) {
                lockTime = LocalDateTime.parse(lockTimeStr, df);
                if (LocalDateTime.now().isAfter(lockTime.plus(LOCK_MINUTES, ChronoUnit.MINUTES))) {
                    //如果锁定时间结束，则清空账户错误次数和锁定时间
                    error = 0;
                    redisService.delete(errKey);
                    redisService.delete(lockTimeKey);
                }
            }

            error = error + 1;
            redisService.setString(errKey, String.valueOf(error));
            if (error < PASS_WRONG_MAX_COUNTS) {
                //用户名或密码错误，还有几次机会
                throw new SbcRuntimeException(EmployeeErrorCode.ACCOUNT_PASSWORD_ERROR, new Object[]{PASS_WRONG_MAX_COUNTS - error});
            } else if (error == PASS_WRONG_MAX_COUNTS) {
                //连续输错密码5次，请30分钟后重试
                redisService.setString(lockTimeKey, df.format(LocalDateTime.now()));
                throw new SbcRuntimeException(EmployeeErrorCode.LOCKED_MINUTES, new Object[]{LOCK_MINUTES});
            } else {
                //连续输错密码5次，请{0}分钟后重试
                throw new SbcRuntimeException(EmployeeErrorCode.LOCKED_MINUTES, new Object[]{LocalDateTime.now().until
                        (lockTime.plus(LOCK_MINUTES, ChronoUnit.MINUTES), ChronoUnit.MINUTES)});
            }

        }

        Customer customer = customerDetail.getCustomer();

        if (CustomerStatus.DISABLE.toValue() == customerDetail.getCustomerStatus().toValue()) {
            throw new SbcRuntimeException(EmployeeErrorCode.ACCOUNT_DISABLED, new Object[]{"，原因为："+customerDetail
                    .getForbidReason()});
        }

        Integer currentErrCount = Objects.isNull(customer.getLoginErrorCount()) ? 0 : customer.getLoginErrorCount();
        LocalDateTime now = LocalDateTime.now();

        //已被锁定
        if (customer.getLoginLockTime() != null) {
            if (now.isBefore(customer.getLoginLockTime().plus(LOCK_MINUTES, ChronoUnit.MINUTES))) {
                long minutes = ChronoUnit.MINUTES.between(customer.getLoginLockTime().toLocalTime(), now.toLocalTime());
                minutes = LOCK_MINUTES - minutes;
                if (minutes < 1) {
                    minutes = 1;
                }
                throw new SbcRuntimeException(EmployeeErrorCode.LOCKED_MINUTES, new Object[]{minutes});
            } else {
                // 30分钟后解锁用户
                customerRepository.unlockCustomer(customer.getCustomerId());
                currentErrCount = 0;
            }
        }

        //生成加密后的登陆密码
        String encryptPwd = SecurityUtil.getStoreLogpwd(String.valueOf(customer.getCustomerId()), password, customer
                .getCustomerSaltVal());
        if (!customer.getCustomerPassword().equals(encryptPwd)) {
            if(currentErrCount + 1 == 3){
                this.sendMessage(NodeType.ACCOUNT_SECURITY, AccountSecurityType.LOGIN_PASSWORD_SUM_OUT.getType(), customer.getCustomerId(), customer.getCustomerAccount());
            }
            //累积一次错误
            // 记录失败次数
            customerRepository.updateloginErrorCount(customer.getCustomerId());
            // 超过最大错误次数，锁定用户; 否则错误次数+1
            if (currentErrCount + 1 >= PASS_WRONG_MAX_COUNTS) {
                customerRepository.updateLoginLockTime(customer.getCustomerId(), now);
                throw new SbcRuntimeException(EmployeeErrorCode.LOCKED_MINUTES, new Object[]{LOCK_MINUTES});
            } else {
                throw new SbcRuntimeException(EmployeeErrorCode.ACCOUNT_PASSWORD_ERROR, new Object[]{PASS_WRONG_MAX_COUNTS -
                        (currentErrCount + 1)});
            }
        }

        //如果是待审核，会员名称为空，设置为引导去完善信息的状态
//        if(CheckState.WAIT_CHECK.toValue() == customer.getCheckState().toValue() && StringUtils.isEmpty(customerDetail.getCustomerName())){
//            customer.setCheckState(CheckState.NOT_PASS);
//        }

        customer.setLoginIp(HttpUtil.getIpAddr());
        customerRepository.updateLoginTime(customer.getCustomerId(), now, customer.getLoginIp());
        customer.setCustomerDetail(customerDetail);
        return customer;
    }


    /**
     * 注册
     *
     * @param customer
     * @return
     */
    @Transactional/*(noRollbackFor = SbcRuntimeException.class)*/
   // @GlobalTransactional
    public Customer register(Customer customer, String employeeId) throws SbcRuntimeException {
        this.beforeRegister(customer);

        Optional<Employee> employee = Optional.empty();
        //存储基本信息
        if (StringUtils.isNotBlank(employeeId)) {
            employee = employeeRepository.findByEmployeeIdAndDelFlag(employeeId, DeleteFlag.NO);
            //employeeId 存在为商家客户 否则为平台客户
            customer.setCustomerType(!employee.isPresent() ? CustomerType.PLATFORM : CustomerType.SUPPLIER);
        }else {
            customer.setCustomerType(CustomerType.PLATFORM);
        }
        customer.setCustomerLevelId(customerLevelService.getDefaultLevel().getCustomerLevelId());
        customer.setGrowthValue(0L); // 成长值默认为0
        customer.setPointsAvailable(0L); // 可用积分默认为0
        customer.setPointsUsed(0L);// 已用积分默认为0
        customer.setDelFlag(DeleteFlag.NO);
        customer.setEnterpriseCheckState(EnterpriseCheckState.INIT); // 企业会员审核状态
        CustomerDetail customerDetail = new CustomerDetail();
        if (Objects.nonNull(customer.getCustomerDetail())) {
            customerDetail = customer.getCustomerDetail();
        }
        customer.setCustomerDetail(null);

        //平台端导入的会员则直接通过
        //审核开关关闭并且信息完善开关关闭，则直接审核通过
        if ( CheckState.CHECKED.equals(customer.getCheckState()) ||
                (!auditQueryProvider.isPerfectCustomerInfo().getContext().isPerfect()
                 && !auditQueryProvider.isCustomerAudit().getContext().isAudit()) ) {
            //不需要审核客户
            customer.setCheckState(CheckState.CHECKED);
            //加入审核时间
            customer.setCheckTime(LocalDateTime.now());
            //不需要完善信息
            customerDetail.setContactName(customer.getCustomerAccount());
            customerDetail.setContactPhone(customer.getCustomerAccount());
            customerDetail.setCustomerName(StringUtils.isNotBlank(customerDetail.getCustomerName()) ?
                    customerDetail.getCustomerName() : customer.getCustomerAccount());
        } else {
            //需要审核客户
            customer.setCheckState(CheckState.WAIT_CHECK);
        }

        //如果是扫码注册客户 默认设置 称呼、常用联系人、常用联系人手机号 为客户账户
        if (employee.isPresent()) {
            customer.setCheckState(CheckState.CHECKED);
            customerDetail.setContactName(customer.getCustomerAccount());
            customerDetail.setContactPhone(customer.getCustomerAccount());
            customerDetail.setCustomerName(customer.getCustomerAccount());
        }
        customer.setCreateTime(LocalDateTime.now());
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

        //存储详情信息
        AccountType accountType = AccountType.b2bBoss;
        if (osUtil.isS2b()) {
            accountType = AccountType.s2bBoss;
        }
        //负责的业务员
        String mainEmployeeId = employeeId;
        if (employee.isPresent()) {
            //如果不是业务员
            if (DefaultFlag.YES.toValue() == employee.get().getIsEmployee()) {
                //查询该店铺下商家主账号
                Employee mainEmployee = employeeRepository.findMainEmployee(employee.get().getCompanyInfoId(),
                        DeleteFlag.NO);
                if (Objects.nonNull(mainEmployee)) {
                    mainEmployeeId = mainEmployee.getEmployeeId();
                }
            }
        }

        customerDetail.setCustomerId(customer.getCustomerId());
        customerDetail.setEmployeeId(!employee.isPresent() ? employeeRepository
                .findByAccountNameAndDelFlagAndAccountType(DEFAULT_EMPLOYEE, DeleteFlag.NO, accountType).get()
                .getEmployeeId() : mainEmployeeId);
        customerDetail.setDelFlag(DeleteFlag.NO);
        customerDetail.setCustomerStatus(CustomerStatus.ENABLE);
        customerDetail.setCreateTime(LocalDateTime.now());
        customerDetail.setIsDistributor(DefaultFlag.NO);
        customerDetailRepository.save(customerDetail);
        //初始化会员资金信息
        producerService.initCustomerFunds(customer.getCustomerId(),customerDetail.getCustomerName(),customer.getCustomerAccount());
        //ares埋点-会员-会员注册
        customerAresService.dispatchFunction(AresFunctionType.REGISTER_CUSTOMER, customer, customerDetail);
        //如果自动审核了，发送审核通过埋点
        if (customer.getCheckState() == CheckState.CHECKED) {
            customerAresService.dispatchFunction(AresFunctionType.EDIT_CUSTOMER_CHECK_STATE, customer.getCheckState(), customer.getCustomerId());
        }
        customer.setCustomerDetail(customerDetail);
        return customer;
    }

    /**
     * 企业会员注册
     *
     * @param customer
     * @return
     */
    @Transactional(noRollbackFor = SbcRuntimeException.class)
    public Customer registerEnterprise(Customer customer, CustomerRegisterRequest customerRegisterRequest) throws SbcRuntimeException {
        this.beforeRegister(customer);

        //存储基本信息
        Optional<Employee> employee = employeeRepository.findByEmployeeIdAndDelFlag(customerRegisterRequest.getEmployeeId(),
                DeleteFlag.NO);
        //employeeId 存在为商家客户 否则为平台客户
        customer.setCustomerType(!employee.isPresent() ? CustomerType.PLATFORM : CustomerType.SUPPLIER);
        customer.setCustomerLevelId(customerLevelService.getDefaultLevel().getCustomerLevelId());
        customer.setGrowthValue(0L); // 成长值默认为0
        customer.setPointsAvailable(0L); // 可用积分默认为0
        customer.setPointsUsed(0L);// 已用积分默认为0
        customer.setDelFlag(DeleteFlag.NO);
        CustomerDetail customerDetail = new CustomerDetail();
        if (Objects.nonNull(customer.getCustomerDetail())) {
            customerDetail = customer.getCustomerDetail();
        }
        customer.setCustomerDetail(null);

        //不需要完善信息
        customerDetail.setContactName(customer.getCustomerAccount());
        customerDetail.setContactPhone(customer.getCustomerAccount());
        customerDetail.setCustomerName(StringUtils.isNotBlank(customerDetail.getCustomerName()) ?
                customerDetail.getCustomerName() : customer.getCustomerAccount());
        //审核开关关闭，则直接审核通过
        if (DefaultFlag.NO.equals(customerRegisterRequest.getEnterpriseCustomerAuditFlag())) {
            //不需要审核客户
            customer.setCheckState(CheckState.CHECKED);
            //企业会员审核状态：已审核
            customer.setEnterpriseCheckState(EnterpriseCheckState.CHECKED);
            //加入审核时间
            customer.setCheckTime(LocalDateTime.now());
        } else {
            //需要审核客户
            customer.setCheckState(CheckState.WAIT_CHECK);
            //企业会员审核状态：待审核
            customer.setEnterpriseCheckState(EnterpriseCheckState.WAIT_CHECK);
        }

        //如果是扫码注册客户 默认设置 称呼、常用联系人、常用联系人手机号 为客户账户
        if (employee.isPresent()) {
            customer.setCheckState(CheckState.CHECKED);
            customerDetail.setContactName(customer.getCustomerAccount());
            customerDetail.setContactPhone(customer.getCustomerAccount());
            customerDetail.setCustomerName(customer.getCustomerAccount());
        }
        customer.setCreateTime(LocalDateTime.now());
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

        //存储详情信息
        AccountType accountType = AccountType.b2bBoss;
        if (osUtil.isS2b()) {
            accountType = AccountType.s2bBoss;
        }
        //负责的业务员
        String mainEmployeeId = customerRegisterRequest.getEmployeeId();
        if (employee.isPresent()) {
            //如果不是业务员
            if (DefaultFlag.YES.toValue() == employee.get().getIsEmployee()) {
                //查询该店铺下商家主账号
                Employee mainEmployee = employeeRepository.findMainEmployee(employee.get().getCompanyInfoId(),
                        DeleteFlag.NO);
                if (Objects.nonNull(mainEmployee)) {
                    mainEmployeeId = mainEmployee.getEmployeeId();
                }
            }
        }

        customerDetail.setCustomerId(customer.getCustomerId());
        customerDetail.setEmployeeId(!employee.isPresent() ? employeeRepository
                .findByAccountNameAndDelFlagAndAccountType(DEFAULT_EMPLOYEE, DeleteFlag.NO, accountType).get()
                .getEmployeeId() : mainEmployeeId);
        customerDetail.setDelFlag(DeleteFlag.NO);
        customerDetail.setCustomerStatus(CustomerStatus.ENABLE);
        customerDetail.setCreateTime(LocalDateTime.now());
        customerDetail.setIsDistributor(DefaultFlag.NO);
        customerDetailRepository.save(customerDetail);

        //新增企业会员关联表信息
        EnterpriseInfo enterpriseInfo = new EnterpriseInfo();
        enterpriseInfo.setEnterpriseName(customerRegisterRequest.getEnterpriseName());
        enterpriseInfo.setSocialCreditCode(customerRegisterRequest.getSocialCreditCode());
        enterpriseInfo.setBusinessNatureType(customerRegisterRequest.getBusinessNatureType());
        enterpriseInfo.setBusinessLicenseUrl(customerRegisterRequest.getBusinessLicenseUrl());
        enterpriseInfo.setCustomerId(customer.getCustomerId());
        enterpriseInfo.setCreatePerson(customerRegisterRequest.getEmployeeId());
        enterpriseInfo.setCreateTime(LocalDateTime.now());
        enterpriseInfo.setDelFlag(DeleteFlag.NO);
        enterpriseInfoService.add(enterpriseInfo);

        //初始化会员资金信息
        producerService.initCustomerFunds(customer.getCustomerId(),customerDetail.getCustomerName(),customer.getCustomerAccount());
        customer.setCustomerDetail(customerDetail);
        return customer;
    }

    /**
     * 企业会员注册被驳回后重新提交注册
     * @param customerRegisterRequest
     * @return
     * @throws SbcRuntimeException
     */
    @Transactional(noRollbackFor = SbcRuntimeException.class)
    public Customer registerEnterpriseAgain(CustomerRegisterRequest customerRegisterRequest) throws SbcRuntimeException {

        Customer customer =
                customerRepository.findByCustomerIdAndDelFlag(customerRegisterRequest.getCustomerId(), DeleteFlag.NO);
        if(Objects.isNull(customer)){
            throw new SbcRuntimeException(SiteResultCode.ERROR_010107);
        }

        // 重新设置状态为待审核,更新会员信息
        customer.setEnterpriseCheckState(EnterpriseCheckState.WAIT_CHECK);
        customer.setEnterpriseCheckReason(null);
        customerRepository.save(customer);

        // 更新公司信息
        EnterpriseInfo enterpriseInfo = enterpriseInfoRepository.findByCustomerIdAndDelFlag(customer.getCustomerId(),
                DeleteFlag.NO).orElse(null);
        if(Objects.isNull(enterpriseInfo)){
            throw new SbcRuntimeException(SiteResultCode.ERROR_010108);
        }
        enterpriseInfo.setEnterpriseName(customerRegisterRequest.getEnterpriseName());
        enterpriseInfo.setSocialCreditCode(customerRegisterRequest.getSocialCreditCode());
        enterpriseInfo.setBusinessNatureType(customerRegisterRequest.getBusinessNatureType());
        enterpriseInfo.setBusinessLicenseUrl(customerRegisterRequest.getBusinessLicenseUrl());
        enterpriseInfo.setUpdatePerson(customerRegisterRequest.getEmployeeId());
        enterpriseInfo.setUpdateTime(LocalDateTime.now());
        enterpriseInfoRepository.save(enterpriseInfo);

        return customer;
    }


    /**
     * 注册客户前判断客户状态，是否存在
     *
     * @param customer
     */
    public void beforeRegister(Customer customer) {
        beforeRegister(customer.getCustomerAccount());
    }

    /**
     * 注册客户前判断客户状态，是否存在
     *
     * @param customerAccount
     */
    public void beforeRegister(String customerAccount) {
        Customer oldCustomer = getCustomerByAccount(customerAccount);
        if (oldCustomer != null) {
            CustomerDetail customerDetail = customerDetailRepository.findByCustomerId(oldCustomer.getCustomerId());
            if (Objects.isNull(customerDetail)) {
                throw new SbcRuntimeException(SiteResultCode.ERROR_010101);
            }

            if (CustomerStatus.DISABLE.toValue() == customerDetail.getCustomerStatus().toValue()) {
                throw new SbcRuntimeException(SiteResultCode.ERROR_010006);
            }

            throw new SbcRuntimeException(SiteResultCode.ERROR_010101);
        }
    }


    /**
     * 完善会员信息
     *
     * @param detail 信息
     * @return 成功标识
     */
    @Transactional(noRollbackFor = SbcRuntimeException.class)
    public Customer perfectCustomer(CustomerDetail detail) {
        Customer customer =
                customerRepository.findById(detail.getCustomerId()).orElseThrow(()->new SbcRuntimeException(SiteResultCode.ERROR_010103));

        CustomerDetail oldDetail = customerDetailRepository.findByCustomerId(detail.getCustomerId());
        if (Objects.isNull(oldDetail)) {
            throw new SbcRuntimeException(SiteResultCode.ERROR_010103);
        }

        customer.setUpdateTime(LocalDateTime.now());

        //S2B模式下的完善信息   不需要审核客户
//        if (osUtil.isS2b() && (!auditService.isCustomerAudit())) {
        if (osUtil.isS2b() && (!auditQueryProvider.isCustomerAudit().getContext().isAudit())) {
            customer.setCheckState(CheckState.CHECKED);
        } else {
            customer.setCheckState(CheckState.WAIT_CHECK);
        }

        customerRepository.save(customer);

        detail.setUpdateTime(LocalDateTime.now());
        KsBeanUtil.copyProperties(detail, oldDetail);
        customerDetailRepository.save(oldDetail);
        producerService.initCustomerFunds(customer.getCustomerId(),oldDetail.getCustomerName(),customer.getCustomerAccount());
        //ares埋点-会员-会员注册后完善会员信息
        customerAresService.dispatchFunction(AresFunctionType.EDIT_CUSTOMER, customer, oldDetail);
        customer.setCustomerDetail(oldDetail);
        return customer;
    }

    /**
     * 根据账号获取有效的Customer
     *
     * @param account 账号
     * @return 客户
     */
    public Customer getCustomerByAccount(String account) {
        return customerRepository.findByCustomerAccountAndDelFlag(account, DeleteFlag.NO);
    }

    /**
     * 修改密码
     *
     * @param customer 会员
     * @return 成功标识
     */
    @Transactional(noRollbackFor = SbcRuntimeException.class)
    public void editPassword(Customer customer) {
        customer.setSafeLevel(SafeLevelUtil.getSafeLevel(customer.getCustomerPassword()));
        customer.setCustomerPassword(SecurityUtil.getStoreLogpwd(String.valueOf(customer.getCustomerId()), customer
                .getCustomerPassword(), customer.getCustomerSaltVal()));
        customer.setUpdateTime(LocalDateTime.now());
        customerRepository.save(customer);
    }

    /**
     * 修改支付密码
     * @param customer
     */
    @Transactional(noRollbackFor = SbcRuntimeException.class)
    public void editPayPassword(Customer customer) {
        customer.setSafeLevel(SafeLevelUtil.getSafeLevel(customer.getCustomerPassword()));
        customer.setCustomerPayPassword(SecurityUtil.getStoreLogpwd(String.valueOf(customer.getCustomerId()), customer
                .getCustomerPayPassword(), customer.getCustomerSaltVal()));
        customer.setUpdateTime(LocalDateTime.now());
        customerRepository.save(customer);
    }

    /**
     * 根据账号获取有效的CustomerDetail
     *
     * @param account
     * @return
     */
    public CustomerDetail getCustomerDetailByAccount(String account) {
        List<CustomerDetail> customerList = customerDetailRepository.findAll(CustomerDetailQueryRequest.builder()
                .equalCustomerAccount(account)
                .delFlag(DeleteFlag.NO.toValue())
                .build().getWhereCriteria());
        return CollectionUtils.isNotEmpty(customerList) ? customerList.get(0) : null;
    }

    /**
     * 发送手机验证码
     *
     * @param redisKey    存入redis的验证码key
     * @param mobile      要发送短信的手机号码
     * @param smsTemplate 短信内容模版
     * @return
     */
    public Integer doMobileSms(String redisKey, String mobile, SmsTemplate smsTemplate) {
        //记录发送时间
        redisService.hset(CacheKeyConstant.YZM_MOBILE_LAST_TIME, mobile, DateUtil.nowTime());

        String verifyCode = RandomStringUtils.randomNumeric(6);
        smsSendUtil.send(smsTemplate, new String[]{mobile}, verifyCode);

        redisService.setString(redisKey.concat(mobile), verifyCode);
        redisService.expireByMinutes(redisKey.concat(mobile), Constants.SMS_TIME);

        return Constants.yes;
    }

    /**
     * 是否可以发送验证码
     *
     * @param mobile 要发送短信的手机号码
     * @return true:可以发送，false:不可以
     */
    public boolean isSendSms(String mobile) {
        String timeStr = redisService.hget(CacheKeyConstant.YZM_MOBILE_LAST_TIME, mobile);
        if (StringUtils.isBlank(timeStr)) {
            return true;
        }
        //如果当前时间 > 上一次发送时间+1分钟
        return LocalDateTime.now().isAfter(DateUtil.parse(timeStr, DateUtil.FMT_TIME_1).plusMinutes(1));
    }

    /**
     * 绑定会员与第三方账号的关系
     */
    public void bindThird(ThirdLoginRelation thirdLoginRelation) {
        // 校验customerId是否存在
        Customer customer = customerRepository.findByCustomerIdAndDelFlag(thirdLoginRelation.getCustomerId(),
                DeleteFlag.NO);
        if (customer == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        thirdLoginRelation.setStoreId(thirdLoginRelation.getStoreId()==null?-1L:thirdLoginRelation.getStoreId());
        thirdLoginRelationService.save(thirdLoginRelation);
    }

    @Transactional
    public ThirdLoginAndBindResponse thirdLoginAndBind(String phone, ThirdLoginRelation thirdLoginRelation, String shareUserId) {
        // 根据手机号查询客户信息
        Customer customer = this.getCustomerByAccount(phone);
        ThirdLoginAndBindResponse response;
        if (Objects.nonNull(customer)) {
            // 手机号未绑定微信 走绑定流程
            response = KsBeanUtil.convert(customer, ThirdLoginAndBindResponse.class);
            ThirdLoginRelation newThirdLoginRelation = thirdLoginRelationService.findAllByCustomerIdAndThirdTypeAndDelFlagAndStoreId(
                    customer.getCustomerId(), ThirdLoginType.WECHAT,DeleteFlag.NO,thirdLoginRelation.getStoreId());
            // 手机号已绑定微信 抛异常
            if (Objects.nonNull(newThirdLoginRelation)) {
                throw new SbcRuntimeException(ThirdLoginRelationErrorCode.PHONE_ALREADY_BINDING);
            }
            response.setIsNewCustomer(Boolean.FALSE);

            // 增加成长值
            growthValueIncreaseService.increaseGrowthValue(
                    CustomerGrowthValueAddRequest.builder()
                            .customerId(customer.getCustomerId())
                            .type(OperateType.GROWTH)
                            .serviceType(GrowthValueServiceType.BINDINGWECHAT)
                            .build());
            // 增加积分
            customerPointsDetailService.increasePoints(CustomerPointsDetailAddRequest.builder()
                            .customerId(customer.getCustomerId())
                            .type(OperateType.GROWTH)
                            .serviceType(PointsServiceType.BINDINGWECHAT)
                            .build(),
                    ConfigType.POINTS_BASIC_RULE_BIND_WECHAT);
        } else {
            //未注册 自动注册并且绑定微信
            customer = new Customer();
            customer.setCustomerAccount(phone);
            //自动生成密码 与前台加密方式一样
            String password = String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
            customer.setCustomerPassword(password);
            customer = this.register(customer, null);
            response = KsBeanUtil.convert(customer, ThirdLoginAndBindResponse.class);
            //发送密码到用户手机
            smsSendUtil.send(SmsTemplate.CUSTOMER_PASSWORD, new String[]{phone}, phone, password);
            response.setIsNewCustomer(Boolean.TRUE);
            // 注册成长值
            growthValueIncreaseService.increaseGrowthValue(
                    CustomerGrowthValueAddRequest.builder()
                            .customerId(customer.getCustomerId())
                            .type(OperateType.GROWTH)
                            .serviceType(GrowthValueServiceType.REGISTER)
                            .build());
            // 绑定微信成长值
            growthValueIncreaseService.increaseGrowthValue(
                    CustomerGrowthValueAddRequest.builder()
                            .customerId(customer.getCustomerId())
                            .type(OperateType.GROWTH)
                            .serviceType(GrowthValueServiceType.BINDINGWECHAT)
                            .build());

            // 注册增加积分
            customerPointsDetailService.increasePoints(CustomerPointsDetailAddRequest.builder()
                            .customerId(customer.getCustomerId())
                            .type(OperateType.GROWTH)
                            .serviceType(PointsServiceType.REGISTER)
                            .build(),
                    ConfigType.POINTS_BASIC_RULE_REGISTER);
            // 绑定微信增加积分
            customerPointsDetailService.increasePoints(CustomerPointsDetailAddRequest.builder()
                            .customerId(customer.getCustomerId())
                            .type(OperateType.GROWTH)
                            .serviceType(PointsServiceType.BINDINGWECHAT)
                            .build(),
                    ConfigType.POINTS_BASIC_RULE_BIND_WECHAT);
            // 分享注册增加成长值积分
            if(StringUtils.isNotBlank(shareUserId)){
                growthValueIncreaseService.increaseGrowthValue(
                        CustomerGrowthValueAddRequest.builder()
                                .customerId(shareUserId)
                                .type(OperateType.GROWTH)
                                .serviceType(GrowthValueServiceType.SHAREREGISTER)
                                .build());
                customerPointsDetailService.increasePoints(CustomerPointsDetailAddRequest.builder()
                                .customerId(shareUserId)
                                .type(OperateType.GROWTH)
                                .serviceType(PointsServiceType.SHAREREGISTER)
                                .build(),
                        ConfigType.POINTS_BASIC_RULE_SHARE_REGISTER);
            }

        }
        //绑定微信号
        thirdLoginRelation.setCustomerId(customer.getCustomerId());
        thirdLoginRelationService.save(thirdLoginRelation);
        return response;
    }

    public void checkCustomerPayPwd(CustomerCheckPayPasswordRequest customerCheckPayPasswordRequest){
        //获取会员信息
        Customer customer = customerRepository.findByCustomerIdAndDelFlag(customerCheckPayPasswordRequest.getCustomerId(),
                DeleteFlag.NO);
        String payPassword = SecurityUtil.getStoreLogpwd(String.valueOf(customerCheckPayPasswordRequest.getCustomerId()),
                customerCheckPayPasswordRequest.getPayPassword(), customer.getCustomerSaltVal());
        //判断密码是否正确
        if(StringUtils.isBlank(customer.getCustomerPayPassword())){
            throw new SbcRuntimeException(CustomerErrorCode.NO_CUSTOMER_PAY_PASSWORD);
        }
        if(!payPassword.equals(customer.getCustomerPayPassword())){
            if(customer.getPayErrorTime() != null && customer.getPayErrorTime()==3) {
                Duration duration = Duration.between(customer.getPayLockTime(), LocalDateTime.now());
                if (duration.toMinutes() < 30) {
                    //支付密码输错三次，并且锁定时间还未超过30分钟，返回账户冻结错误信息
                    throw new SbcRuntimeException(CustomerErrorCode.CUSTOMER_PAY_LOCK_ERROR);
                }
            }
            throw new SbcRuntimeException(CustomerErrorCode.CUSTOMER_PAY_PASSWORD_ERROR);
        }else {
            //密码输入正确--密码错误次数置为“0”
            customer.setPayErrorTime(0);
            customerRepository.save(customer);
        }
    }
    /**
     * 密码输入错误处理
     * @param customerCheckPayPasswordRequest
     */
    @Transactional
    public void checkCustomerPayPwdErrorEvent(CustomerCheckPayPasswordRequest customerCheckPayPasswordRequest){
        //获取会员信息
        Customer customer = customerRepository.findByCustomerIdAndDelFlag(customerCheckPayPasswordRequest.getCustomerId(),
                DeleteFlag.NO);
        //修改对应的密码错误次数以
        if(customer.getPayErrorTime()==null){
            customer.setPayErrorTime(1);
        }else if(customer.getPayErrorTime()<2){
            customer.setPayErrorTime(customer.getPayErrorTime()+1);
        } else if(customer.getPayErrorTime()==2){
            customer.setPayErrorTime(customer.getPayErrorTime()+1);
            //如果已经达到3次，修改支付锁定登录时间
            customer.setPayLockTime(LocalDateTime.now());
            this.sendMessage(NodeType.ACCOUNT_SECURITY, AccountSecurityType.PAY_PASSWORD_SUM_OUT.getType(), customer.getCustomerId(), customer.getCustomerAccount());
        } else if(customer.getPayErrorTime()==3){
            Duration duration = Duration.between(customer.getPayLockTime(),LocalDateTime.now());
            if(duration.toMinutes()>30){
                //支付密码输错三次，并且锁定时间超过30分钟，将错误次数重新计算
                customer.setPayErrorTime(1);
            }
        }
        customerRepository.save(customer);
    }

    /**
     * 消息中心通知节点发送消息
     * @param nodeType
     * @param nodeCode
     * @param customerId
     */
    private void sendMessage(NodeType nodeType, String nodeCode, String customerId, String mobile){
        MessageMQRequest request = new MessageMQRequest();
        request.setNodeType(nodeType.toValue());
        request.setNodeCode(nodeCode);
        request.setCustomerId(customerId);
        request.setMobile(mobile);
        producerService.sendMessage(request);
    }

    @Transactional
    public void updateInfo(ThirdLoginRelation thirdLoginRelation){
       thirdLoginRelationService.updateInfo(thirdLoginRelation);
    }
}
