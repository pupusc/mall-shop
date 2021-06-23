package com.wanmi.sbc.login;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.AccountType;
import com.wanmi.sbc.common.enums.Platform;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.redis.CacheKeyConstant;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.HttpUtil;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.util.SecurityUtil;
import com.wanmi.sbc.customer.api.constant.EmployeeErrorCode;
import com.wanmi.sbc.customer.api.constant.StoreErrorCode;
import com.wanmi.sbc.customer.api.provider.employee.EmployeeProvider;
import com.wanmi.sbc.customer.api.provider.employee.EmployeeQueryProvider;
import com.wanmi.sbc.customer.api.provider.employee.RoleInfoQueryProvider;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.request.employee.*;
import com.wanmi.sbc.customer.api.request.store.NoDeleteStoreByIdRequest;
import com.wanmi.sbc.customer.api.request.store.StoreByCompanyInfoIdRequest;
import com.wanmi.sbc.customer.api.response.employee.*;
import com.wanmi.sbc.customer.bean.enums.AccountState;
import com.wanmi.sbc.customer.bean.vo.EmployeeVO;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import com.wanmi.sbc.redis.RedisService;
import com.wanmi.sbc.setting.bean.enums.ConfigKey;
import com.wanmi.sbc.sso.SSOConstant;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * 登录
 * Created by CHENLI on 2017/11/3.
 */
@Api(tags = "LoginController", description = "登录服务API")
@RestController("supplierLoginController")
@RequestMapping("/employee")
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private EmployeeQueryProvider employeeQueryProvider;

    @Autowired
    private EmployeeProvider employeeProvider;

    @Autowired
    private StoreQueryProvider storeQueryProvider;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private RedisService redisService;

    @Autowired
    private RoleInfoQueryProvider roleInfoQueryProvider;

    @Value("${jwt.secret-key}")
    private String jwtSecretKey;

    @Value("${cookie.secure}")
    private boolean secure;

    @Value("${cookie.maxAge}")
    private Integer maxAge;

    @Value("${cookie.path}")
    private String path;

    @Value("${cookie.domain}")
    private String domain;

    // 密码错误超过5次后锁定的时间，单位：分钟
    private static final int LOCK_MINUTES = 30;

    // 允许密码错误最大次数
    private static final int PASS_WRONG_MAX_COUNTS = 5;


    /**
     * 会员登录
     *
     * @param loginRequest
     * @return ResponseEntity<LoginResponse>
     */
    @ApiOperation(value = "会员登录")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public BaseResponse<LoginResponse> login(@Valid @RequestBody EmployeeLoginRequest loginRequest,
                                             HttpServletResponse httpServletResponse) {
        String account = new String(Base64.getUrlDecoder().decode(loginRequest.getAccount().getBytes()));
        String password = new String(Base64.getUrlDecoder().decode(loginRequest.getPassword().getBytes()));
        //商家登录
        return loginByAccount(account, password, AccountType.s2bSupplier.toValue(),httpServletResponse);
    }

    /**
     * 按账号查找登录
     *
     * @param account  账号
     * @param password 密码
     * @return
     */
    private BaseResponse<LoginResponse> loginByAccount(String account, String password,int type ,HttpServletResponse httpServletResponse) {
        return Optional.ofNullable(employeeQueryProvider.getByAccountName(
                EmployeeByAccountNameRequest.builder()
                        .accountName(account)
                        .accountType(AccountType.fromValue(type)).build()
        ).getContext().getEmployee())
                .map(response -> {
                    EmployeeVO employee = new EmployeeVO();
                    KsBeanUtil.copyPropertiesThird(response, employee);
                    return customerValidate(employee, password,httpServletResponse);
                })
                .orElseThrow(() -> validateNull(account));
    }


    /**
     * 当账号不存在时
     *
     * @param account
     * @return
     */
    public SbcRuntimeException validateNull(String account) {
        //登录错误次数
        String errKey = CacheKeyConstant.S2B_SUPPLIER_LOGIN_ERR.concat(account);
        //锁定时间
        String lockTimeKey = CacheKeyConstant.S2B_SUPPLIER_LOCK_TIME.concat(account);

        String errCountStr = redisService.getString(errKey);
        String lockTimeStr = redisService.getString(lockTimeKey);
        int error = NumberUtils.toInt(errCountStr);

        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime lockTime = LocalDateTime.now();
        if (StringUtils.isNotBlank(lockTimeStr)) {
            lockTime = LocalDateTime.parse(lockTimeStr, df);
            if (LocalDateTime.now().isAfter(lockTime.plus(LOCK_MINUTES, ChronoUnit.MINUTES))) {
                error = 0;
                redisService.delete(errKey);
                redisService.delete(lockTimeKey);
            }
        }

        boolean isRedisErr = false;
        if ("error".equals(errCountStr)) {
            isRedisErr = true;
        }

        if (!isRedisErr) {
            error = error + 1;
            redisService.setString(errKey, String.valueOf(error));
            //错误次数小于5次
            if (error < PASS_WRONG_MAX_COUNTS) {
                //用户名或密码错误，还有几次机会
                throw new SbcRuntimeException(EmployeeErrorCode.ACCOUNT_PASSWORD_ERROR,
                        new Object[]{PASS_WRONG_MAX_COUNTS - error});
            } else if (error == PASS_WRONG_MAX_COUNTS) {
                redisService.setString(lockTimeKey, df.format(LocalDateTime.now()));
                //连续输错密码5次，请30分钟后重试
                throw new SbcRuntimeException(EmployeeErrorCode.LOCKED_MINUTES, new Object[]{LOCK_MINUTES});
            } else {
                //连续输错密码5次，请{0}分钟后重试
                throw new SbcRuntimeException(EmployeeErrorCode.LOCKED_MINUTES,
                        new Object[]{LocalDateTime.now().until(lockTime.plus(LOCK_MINUTES, ChronoUnit.MINUTES),
                                ChronoUnit.MINUTES)});
            }
        } else {
            throw new SbcRuntimeException(CommonErrorCode.FAILED);
        }
    }

    /**
     * 验证登录账户
     *
     * @param employee
     * @param password
     * @return
     */
    private BaseResponse<LoginResponse> customerValidate(EmployeeVO employee, String password, HttpServletResponse
            httpServletResponse) {
        Integer currentErrCount = employee.getLoginErrorTime();

        //账号禁用
        if (AccountState.DISABLE.equals(employee.getAccountState())) {
            logger.info("员工[{}]已被锁定或禁用", employee.getAccountName());
            // 账户被锁定或禁用
            throw new SbcRuntimeException(EmployeeErrorCode.ACCOUNT_DISABLED,
                    new Object[]{"，原因为：" + employee.getAccountDisableReason() + "，如有疑问请联系平台"});
        }
        //输错密码，账号被锁定
        if (Objects.nonNull(employee.getLoginLockTime())) {
            //如果已经过了30分钟
            if (LocalDateTime.now().isAfter(employee.getLoginLockTime().plus(LOCK_MINUTES, ChronoUnit.MINUTES))) {
                // 30分钟后解锁用户
                BaseResponse<EmployeeUnlockResponse> response = employeeProvider.unlockById(
                        EmployeeUnlockByIdRequest.builder().employeeId(employee.getEmployeeId()).build()
                );
                if (!response.getCode().equals(CommonErrorCode.SUCCESSFUL)) {
                    throw new SbcRuntimeException(response.getCode(), response.getMessage());
                }
                currentErrCount = 0;
            }
        }

        //根据规则加密密码
        String encodePwd = SecurityUtil.getStoreLogpwd(String.valueOf(employee.getEmployeeId()), password,
                employee.getEmployeeSaltVal());
        if (employee.getAccountPassword().equals(encodePwd)) {
            //在锁定账号的30分钟内，就算密码正确，也提示用户30分钟后再试
            if (Objects.nonNull(employee.getLoginLockTime()) && LocalDateTime.now().isBefore(employee.getLoginLockTime().plus(LOCK_MINUTES, ChronoUnit.MINUTES))) {
                //请{0}分钟后重试
                throw new SbcRuntimeException(EmployeeErrorCode.TRY_AGAIN,
                        new Object[]{LocalDateTime.now().until(employee.getLoginLockTime().plus(LOCK_MINUTES,
                                ChronoUnit.MINUTES), ChronoUnit.MINUTES)});
            }

            Long storeId = null;
            Long companyInfoId = employee.getCompanyInfo().getCompanyInfoId();
//            Store store = storeService.queryStoreByCompanyInfoId(companyInfoId);
            StoreVO store = storeQueryProvider.getStoreByCompanyInfoId(new StoreByCompanyInfoIdRequest(companyInfoId))
                    .getContext().getStoreVO();
            if (Objects.nonNull(store)) {
                storeId = store.getStoreId();
            }

            Map<String, Object> claims = Maps.newHashMap();
            claims.put("employeeId", employee.getEmployeeId());
            claims.put("EmployeeName", employee.getAccountName());
            claims.put("adminId", companyInfoId);
            claims.put("companyInfoId", companyInfoId);
            claims.put("storeId", storeId);
            //商家
            claims.put("platform", Platform.SUPPLIER.toValue());
            claims.put("ip", HttpUtil.getIpAddr());
            //以下为魔方建站需要的固定参数(目前userId写死)
            claims.put("envCode", SSOConstant.ENV_CODE);
            claims.put("systemCode", SSOConstant.SYSTEM_CODE);
            claims.put("userId", SSOConstant.PLAT_SHOP_ID);
            if (Objects.nonNull(store.getCompanyType())){
                claims.put("companyType",store.getCompanyType().toValue());
            }
            claims.put("realEmployeeName", StringUtils.isNotBlank(employee.getEmployeeName()) ? employee.getEmployeeName() : employee.getAccountName());

            Map<String, String> vasList = redisService.hgetall(ConfigKey.VALUE_ADDED_SERVICES.toString());
            claims.put(ConfigKey.VALUE_ADDED_SERVICES.toString(), JSONObject.toJSONString(vasList));
            if (Objects.isNull(employee.getRoleIds())) {
                claims.put("roleName", org.apache.commons.lang3.StringUtils.EMPTY);
            } else {
                if (!StringUtils.isNotBlank(employee.getRoleIds())){
                    throw new SbcRuntimeException(EmployeeErrorCode.ACCESS_LACK_ROLE, new Object[]{"，请联系您的管理员"});
                }
                String roleName = org.apache.commons.lang3.StringUtils.EMPTY;
                String[] roleIds = employee.getRoleIds().split(",");
                for (String roleId: roleIds) {
                    RoleInfoQueryRequest roleInfoQueryRequest = new RoleInfoQueryRequest();
                    roleInfoQueryRequest.setRoleInfoId(Long.valueOf(roleId));
                    RoleInfoQueryResponse roleInfoQueryResponse =
                            roleInfoQueryProvider.getRoleInfoById(roleInfoQueryRequest).getContext();
                    if (Objects.nonNull(roleInfoQueryResponse) && Objects.nonNull(roleInfoQueryResponse.getRoleInfoVO())
                            && StringUtils.isNotBlank(roleInfoQueryResponse.getRoleInfoVO().getRoleName())) {
                        roleName += roleInfoQueryResponse.getRoleInfoVO().getRoleName() + ";";
                    }
                    claims.put("roleName", roleName);
                }

            }

            Date now = new Date();
            String token = Jwts.builder().setSubject(employee.getAccountName())
                    .signWith(SignatureAlgorithm.HS256, jwtSecretKey)
                    .setIssuedAt(now)
                    .setClaims(claims)
                    .setExpiration(Date.from(Instant.now().plus(7, ChronoUnit.DAYS))) // 有效期一个星期
                    .compact();

            // 更新登录时间
            BaseResponse<EmployeeLoginTimeModifyResponse> response = employeeProvider.modifyLoginTimeById(
                    EmployeeLoginTimeModifyByIdRequest.builder().employeeId(employee.getEmployeeId()).build()
            );
            if (!response.getCode().equals(CommonErrorCode.SUCCESSFUL)) {
                throw new SbcRuntimeException(response.getCode(), response.getMessage());
            }
            Cookie cookie = new Cookie(storeId.toString(), token);
            cookie.setSecure(secure);
            cookie.setMaxAge(maxAge);
            cookie.setPath(path);
            if(StringUtils.isNotEmpty(domain)){
                cookie.setDomain(domain);
            }
            httpServletResponse.addCookie(cookie);
            logger.info("魔方domian配置为:[{}]", cookie.getDomain());
            //操作日志记录
            operateLogMQUtil.convertAndSend("登录", "登录", "登录S2B 商家端", new DefaultClaims(claims));

            return BaseResponse.success(LoginResponse.builder()
                    .accountName(employee.getAccountName())
                    .companyInfoId(companyInfoId)
                    .storeId(storeId)
                    .mobile(employee.getEmployeeMobile())
                    .auditState(Objects.isNull(store.getAuditState()) ? -1 : store.getAuditState().toValue())
                    .isMasterAccount(employee.getIsMasterAccount())
                    .isEmployee(employee.getIsEmployee())
                    .employeeId(employee.getEmployeeId())
                    .employeeName(StringUtils.isNotBlank(employee.getEmployeeName()) ? employee.getEmployeeName() : employee.getAccountName())
                    .companyType(store.getCompanyType())
                    .supplierName(Objects.nonNull(store) ? store.getSupplierName() : "")
                    .storeName(Objects.nonNull(store) ? store.getStoreName() : "")
                    .storeLogo(Objects.nonNull(store) ? store.getStoreLogo() : "")
                    .token(token)
                    .build());
        } else {
            logger.info("员工[{}]密码校验失败", employee.getEmployeeName());

            // 记录失败次数
            BaseResponse<EmployeeLoginErrorCountModifyByIdResponse> response =
                    employeeProvider.modifyLoginErrorCountById(
                            EmployeeLoginErrorCountModifyByIdRequest.builder().employeeId(employee.getEmployeeId())
                                    .build()
                    );
            if (!response.getCode().equals(CommonErrorCode.SUCCESSFUL)) {
                throw new SbcRuntimeException(response.getCode(), response.getMessage());
            }

            // 超过最大错误次数，锁定用户; 否则错误次数+1
            currentErrCount = currentErrCount + 1;
            //错误次数小于5次
            if (currentErrCount < PASS_WRONG_MAX_COUNTS) {
                //用户名或密码错误，还有几次机会
                throw new SbcRuntimeException(EmployeeErrorCode.ACCOUNT_PASSWORD_ERROR,
                        new Object[]{PASS_WRONG_MAX_COUNTS - currentErrCount});
            } else if (currentErrCount == PASS_WRONG_MAX_COUNTS) {
                BaseResponse<EmployeeLoginLockTimeModifyByIdResponse> modifyResponse =
                        employeeProvider.modifyLoginLockTimeById(
                        EmployeeLoginLockTimeModifyByIdRequest.builder().employeeId(employee.getEmployeeId()).build()
                );
                if (!modifyResponse.getCode().equals(CommonErrorCode.SUCCESSFUL)) {
                    throw new SbcRuntimeException(modifyResponse.getCode(), modifyResponse.getMessage());
                }

                //连续输错密码5次，请30分钟后重试
                throw new SbcRuntimeException(EmployeeErrorCode.LOCKED_MINUTES, new Object[]{LOCK_MINUTES});
            } else {
                //连续输错密码5次，请{0}分钟后重试
                throw new SbcRuntimeException(EmployeeErrorCode.LOCKED_MINUTES,
                        new Object[]{LocalDateTime.now().until(employee.getLoginLockTime().plus(LOCK_MINUTES,
                                ChronoUnit.MINUTES), ChronoUnit.MINUTES)});
            }
        }
    }

    /**
     * 登录后验证商家店铺的各种状态
     *
     * @return
     */
    @ApiOperation(value = "登录后验证商家店铺的各种状态")
    @RequestMapping(value = "/store/state", method = RequestMethod.GET)
    public BaseResponse<LoginStoreResponse> queryStoreState() {
        LoginStoreResponse storeResponse = new LoginStoreResponse();
        StoreVO store = storeQueryProvider.getNoDeleteStoreById(new NoDeleteStoreByIdRequest(commonUtil.getStoreId()))
                .getContext().getStoreVO();
        //店铺不存在
        if (Objects.isNull(store)) {
            throw new SbcRuntimeException(StoreErrorCode.NOT_EXIST);
        }
        storeResponse.setStoreState(store.getStoreState());
        storeResponse.setStoreClosedReason(store.getStoreClosedReason());
        storeResponse.setContractEndDate(store.getContractEndDate());
        //店铺已过期(当前时间超过了截止日期)
        int overDueDay = -1;
        if (Objects.nonNull(store.getContractEndDate())) {
            Period period = Period.between(LocalDate.from(LocalDateTime.now()),
                    LocalDate.from(store.getContractEndDate()));
            overDueDay = period.getYears() * 365 + period.getMonths() * 30 + period.getDays();
        }
        storeResponse.setOverDueDay(overDueDay);
        storeResponse.setStoreLogo(store.getStoreLogo());

        return BaseResponse.success(storeResponse);
    }

}
