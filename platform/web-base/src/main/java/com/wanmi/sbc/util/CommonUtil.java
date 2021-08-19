package com.wanmi.sbc.util;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.ms.autoconfigure.JwtProperties;
import com.wanmi.sbc.common.base.DistributeChannel;
import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.common.base.VASEntity;
import com.wanmi.sbc.common.constant.VASStatus;
import com.wanmi.sbc.common.enums.ChannelType;
import com.wanmi.sbc.common.enums.Platform;
import com.wanmi.sbc.common.enums.VASConstants;
import com.wanmi.sbc.common.enums.TerminalSource;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.redis.CacheKeyConstant;
import com.wanmi.sbc.common.util.*;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.provider.detail .CustomerDetailQueryProvider;
import com.wanmi.sbc.customer.api.provider.distribution.DistributionCustomerQueryProvider;
import com.wanmi.sbc.customer.api.provider.enterpriseinfo.EnterpriseInfoQueryProvider;
import com.wanmi.sbc.customer.api.request.customer.CustomerGetByIdRequest;
import com.wanmi.sbc.customer.api.request.detail.CustomerDetailByCustomerIdRequest;
import com.wanmi.sbc.customer.api.request.distribution.DistributionCustomerByCustomerIdRequest;
import com.wanmi.sbc.customer.api.request.enterpriseinfo.EnterpriseInfoByCustomerIdRequest;
import com.wanmi.sbc.customer.api.response.customer.CustomerGetByIdResponse;
import com.wanmi.sbc.customer.bean.enums.CustomerStatus;
import com.wanmi.sbc.customer.bean.vo.CustomerDetailVO;
import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import com.wanmi.sbc.customer.bean.vo.DistributionCustomerVO;
import com.wanmi.sbc.customer.bean.vo.EnterpriseInfoVO;
import com.wanmi.sbc.customer.response.LoginResponse;
import com.wanmi.sbc.redis.RedisService;
import com.wanmi.sbc.setting.bean.enums.ConfigKey;
import com.wanmi.sbc.vas.api.constants.iep.IepServiceErrorCode;
import com.wanmi.sbc.vas.api.provider.iepsetting.IepSettingQueryProvider;
import com.wanmi.sbc.vas.bean.vo.IepSettingVO;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.compression.CompressionCodecs;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

/**
 * BFF公共工具类
 * Created by daiyitian on 15/12/29.
 */
@Component
@Slf4j
public final class CommonUtil {

    @Autowired
    private CustomerQueryProvider customerQueryProvider;

    @Autowired
    private CustomerDetailQueryProvider customerDetailQueryProvider;

    @Autowired
    private RedisService redisService;

    @Autowired
    private IepSettingQueryProvider iepSettingQueryProvider;

    @Autowired
    private EnterpriseInfoQueryProvider enterpriseInfoQueryProvider;

    @Autowired
    private DistributionCustomerQueryProvider distributionCustomerQueryProvider;

    @Value("${jwt.secret-key}")
    private String key;

    @Autowired
    private JwtProperties jwtProperties;


    /**
     * 获取当前登录编号
     *
     * @return
     */
    public String getOperatorId() {
        return getOperator().getUserId();
    }


    /**
     * 正则表达式：验证手机号 匹配最新的正则表达式
     */
    public String REGEX_MOBILE = "^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[1589])\\d{8}$";

    /**
     * 正则表达式：验证邮箱
     */
    public String REGEX_EMAIL = "^([a-z0-9A-Z_]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

    /**
     * 正则表达式：银行卡
     */
    public String REGEX_BANK_CARD = "^([1-9]{1})(\\d{14}|\\d{15}|\\d{16}|\\d{18})$";

    /**
     * 图片格式后缀大全
     */
    public static String[] IMAGE_SUFFIX = new String[]{"bmp", "jpg", "jpeg", "heif", "png", "tif", "gif", "pcx", "tga",
            "exif", "fpx", "svg", "psd", "cdr", "pcd", "dxf", "ufo", "eps", "ai", "raw", "WMF", "webp"};


    /**
     * 常见视频格式后缀大全
     */
    public static String[] VIDEO_SUFFIX = new String[]{"avi", "wmv", "rm", "rmvb", "mpeg1", "mpeg2", "mpeg4(mp4)",
            "3gp", "asf", "swf"
            , "vob", "dat", "mov", "m4v", "flv", "f4v", "mkv", "mts", "ts", "imv", "amv", "xv", "qsv"};


    /**
     * 获取登录客户信息
     *
     * @return
     */
    public CustomerVO getCustomer() {
        //获取会员和等级
        CustomerGetByIdResponse customer = customerQueryProvider.getCustomerById(new CustomerGetByIdRequest
                (getOperatorId())).getContext();
        if (customer == null) {
            throw new SbcRuntimeException(CommonErrorCode.PERMISSION_DENIED);
        }
        return customer;
    }
    /**
     * 获取登录客户信息
     *
     * @return
     */
    public CustomerVO getCanNullCustomer() {
        //获取会员和等级
        CustomerGetByIdResponse customer = customerQueryProvider.getCustomerById(new CustomerGetByIdRequest
                (getOperatorId())).getContext();
        return customer;
    }

    /**
     * 获取分销渠道对象
     *
     * @return
     */
    public DistributeChannel getDistributeChannel() {
        DistributeChannel distributeChannel = JSONObject.parseObject(
                HttpUtil.getRequest().getHeader("distribute-channel"), DistributeChannel.class);
        if (Objects.isNull(distributeChannel)) {
            distributeChannel = new DistributeChannel();
        }
        return distributeChannel;
    }


    /**
     * 获取终端
     *
     * @return
     */
    public TerminalSource getTerminal() {
        String terminal = HttpUtil.getRequest().getHeader("terminal");
        return TerminalSource.getTerminalSource(terminal);
    }

    /**
     * 获取购物车归属
     * 当且仅当为店铺精选时，需要根据InviteeId区分购物车
     */
    public String getPurchaseInviteeId() {

        if (null != this.getDistributeChannel() && Objects.equals(this.getDistributeChannel().getChannelType(),
                ChannelType.SHOP)) {
            return this.getDistributeChannel().getInviteeId();
        }
        return Constants.PURCHASE_DEFAULT;
    }


    /**
     * 获取当前登录对象
     *
     * @return
     */
    public Operator getOperator() {
        Claims claims = (Claims) (HttpUtil.getRequest().getAttribute("claims"));
        if (claims == null) {
            return new Operator();
        }
        Object vasObject = claims.get(ConfigKey.VALUE_ADDED_SERVICES.toString());
        List<VASEntity> services = new ArrayList<>();
        if (Objects.nonNull(vasObject)) {
            String vasJson = vasObject.toString();
            Map<String, String> map = (Map<String, String>) JSONObject.parse(vasJson);
            services = map.entrySet().stream().map(m -> {
                VASEntity vasEntity = new VASEntity();
                vasEntity.setServiceName(VASConstants.fromValue(m.getKey()));
                vasEntity.setServiceStatus(StringUtils.equals(VASStatus.ENABLE.toValue(), m.getValue()));
                return vasEntity;
            }).collect(Collectors.toList());
        }
        return Operator.builder()
                .account(ObjectUtils.toString(claims.get("customerAccount")))
                .platform(Platform.CUSTOMER)
                .adminId(ObjectUtils.toString(claims.get("adminId")))
                .ip(String.valueOf(claims.get("ip")))
                .name(String.valueOf(claims.get("customerName")))
                .userId(String.valueOf(claims.get("customerId")))
                .firstLogin(claims.get("firstLogin") == null ? Boolean.FALSE : Boolean.valueOf(Objects.toString(claims.get("firstLogin"))))
                .services(services)
                //生成用户token，用于同一用户不同终端登陆区别
                //.terminalToken(String.valueOf(claims.get("terminalToken")))
                .terminalToken(claims.get("terminalToken") == null ? String.valueOf(claims.get("customerId")) : String.valueOf(claims.get("terminalToken")))
                .build();
    }

    /**
     * 获取用户的登陆的终端token
     *
     * @return
     */
    public String getTerminalToken() {
        String terminalToken = getOperator().getTerminalToken();
        if (StringUtils.isEmpty(terminalToken) || "null".equals(terminalToken)) {
            terminalToken = getOperator().getUserId();
        }
        return terminalToken;

    }

    /**
     * 获取当前登录对象(JWT忽略的时候使用)
     *
     * @return
     */
    public Operator getUserInfo() {
        Claims claims = (Claims) (HttpUtil.getRequest().getAttribute("claims"));
        if (claims == null) {
            //从header中直接获取token解析 —— 解决需要在被过滤的请求中获取当前登录人信息
            String token = this.getToken(HttpUtil.getRequest());
            if (StringUtils.isNotBlank(token)) {
                claims = this.validate(token);
            } else {
                return new Operator();
            }
        }
        //已登录会员，需要再次比对storeid 加强校验，防止携带其他店铺登录的token，越权查询操作数据
//        checkIfStore(Long.valueOf(claims.get("storeId").toString()));
        return Operator.builder()
                .account(ObjectUtils.toString(claims.get("customerAccount")))
                .platform(Platform.CUSTOMER)
                .adminId(ObjectUtils.toString(claims.get("adminId")))
                .ip(String.valueOf(claims.get("ip")))
                .name(String.valueOf(claims.get("customerName")))
                .userId(String.valueOf(claims.get("customerId")))
                .storeId(String.valueOf(claims.get("storeId")))
                .build();
    }

    /**
     * 获取jwtToken
     *
     * @return
     */
    private String getToken(HttpServletRequest request) {

        String jwtHeaderKey = org.apache.commons.lang3.StringUtils.isNotBlank(jwtProperties.getJwtHeaderKey()) ? jwtProperties.getJwtHeaderKey
                () : "Authorization";
        String jwtHeaderPrefix = org.apache.commons.lang3.StringUtils.isNotBlank(jwtProperties.getJwtHeaderPrefix()) ? jwtProperties
                .getJwtHeaderPrefix() : "Bearer ";

        String authHeader = request.getHeader(jwtHeaderKey);

        //当token失效,直接返回失败
        if (authHeader != null && authHeader.length() > 16) {
            return authHeader.substring(jwtHeaderPrefix.length());
        }
        return null;
    }

    /**
     * 验证转换为Claims
     *
     * @param token
     * @return
     */
    private Claims validate(String token) {
        try {
            final Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
            log.debug("JwtFilter out ['Authorization success']");
            return claims;
        } catch (final SignatureException | MalformedJwtException | ExpiredJwtException e) {
            log.info("JwtFilter exception, exMsg:{}", e.getMessage());
            return null;
        }
    }

    public LoginResponse loginByCustomerId(String customerId, String jwtSecretKey) {
//        Customer customer = customerService.findInfoById(customerId);
        CustomerGetByIdResponse customer = customerQueryProvider.getCustomerById(new CustomerGetByIdRequest
                (customerId)).getContext();
        if (customer == null) {
            throw new SbcRuntimeException(SiteResultCode.ERROR_010005);
        }

        CustomerDetailVO customerDetail = customerDetailQueryProvider.getCustomerDetailByCustomerId(
                CustomerDetailByCustomerIdRequest.builder().customerId(customer.getCustomerId()).build()).getContext();

        if (customerDetail == null) {
            throw new SbcRuntimeException(SiteResultCode.ERROR_010005);
        }
        //是否禁用
        if (CustomerStatus.DISABLE.toValue() == customerDetail.getCustomerStatus().toValue()) {
            throw new SbcRuntimeException(SiteResultCode.ERROR_010002, new Object[]{"，原因为：" + customerDetail
                    .getForbidReason()});
        }
        //组装登录信息
        return this.getLoginResponse(customer, jwtSecretKey);
    }

    /**
     * 拼接登录后返回值
     *
     * @param customer
     * @return
     */
    public LoginResponse getLoginResponse(CustomerVO customer, String jwtSecretKey) {
        Date date = new Date();
        Map<String, String> vasList = redisService.hgetall(ConfigKey.VALUE_ADDED_SERVICES.toString());
        String token = Jwts.builder().setSubject(customer.getCustomerAccount())
                .compressWith(CompressionCodecs.DEFLATE)
                .signWith(SignatureAlgorithm.HS256, jwtSecretKey)
                .setIssuedAt(date)
                .claim("customerId", customer.getCustomerId())
                .claim("customerAccount", customer.getCustomerAccount())
                .claim("customerName", customer.getCustomerDetail().getCustomerName())
                .claim("ip", customer.getLoginIp())
                .claim("terminalToken", MD5Util.md5Hex(customer.getCustomerId() + date.getTime() + RandomStringUtils.randomNumeric(4)))
                .claim("firstLogin", Objects.isNull(customer.getLoginTime()))//是否首次登陆
                .claim(ConfigKey.VALUE_ADDED_SERVICES.toString(), JSONObject.toJSONString(vasList))
                .setExpiration(DateUtils.addMonths(date, 1))
                .compact();

        // 如果是企业会员，则增加公司信息与审核信息/不通过原因
        EnterpriseInfoVO enterpriseInfoVO =
                enterpriseInfoQueryProvider.getByCustomerId(EnterpriseInfoByCustomerIdRequest.builder()
                        .customerId(customer.getCustomerId()).build()).getContext().getEnterpriseInfoVO();

        // 登陆时查询是否是分销员邀请注册，若是则增加邀请码返回前台展示使用
        DistributionCustomerVO distributionCustomerVO =
                distributionCustomerQueryProvider.getByCustomerId(DistributionCustomerByCustomerIdRequest.builder()
                        .customerId(customer.getCustomerId()).build()).getContext().getDistributionCustomerVO();
        String inviteCode = StringUtils.EMPTY;
        if (Objects.nonNull(distributionCustomerVO)) {
            String inviteId = StringUtils.isEmpty(distributionCustomerVO.getInviteCustomerIds()) ? StringUtils.EMPTY :
                    distributionCustomerVO.getInviteCustomerIds().split(",")[0];
            DistributionCustomerVO inviteCustomer =
                    distributionCustomerQueryProvider.getByCustomerId(DistributionCustomerByCustomerIdRequest.builder()
                            .customerId(inviteId).build()).getContext().getDistributionCustomerVO();
            if (Objects.nonNull(inviteCustomer)) {
                inviteCode = inviteCustomer.getInviteCode();
            }
        }


        return LoginResponse.builder()
                .accountName(customer.getCustomerAccount())
                .customerId(customer.getCustomerId())
                .token(token)
                .checkState(customer.getCheckState().toValue())
                .enterpriseCheckState(customer.getEnterpriseCheckState())
                .enterpriseCheckReason(customer.getEnterpriseCheckReason())
                .customerDetail(customer.getCustomerDetail())
                .enterpriseInfoVO(enterpriseInfoVO)
                .inviteCode(inviteCode)
                .build();
    }





    /**
     * 查询指定增值服务是否购买
     *
     * @param constants
     * @return
     */
    public boolean findVASBuyOrNot(VASConstants constants) {
        boolean flag = false;
        List<VASEntity> list = this.getAllServices();
        VASEntity vasEntity =
                list.stream().filter(f -> StringUtils.equals(f.getServiceName().toValue(), constants.toValue()) && f.isServiceStatus()).findFirst().orElse(null);
        if (Objects.nonNull(vasEntity)) {
            flag = vasEntity.isServiceStatus();
        }
        return flag;
    }

    /**
     * 获取所有增值服务
     *
     * @return
     */
    public List<VASEntity> getAllServices() {
        Claims claims = (Claims) HttpUtil.getRequest().getAttribute("claims");
        if (claims == null) {
            Map<String, String> vasList = redisService.hgetall(ConfigKey.VALUE_ADDED_SERVICES.toString());
            return vasList.entrySet().stream().map(m -> {
                VASEntity vasEntity = new VASEntity();
                vasEntity.setServiceName(VASConstants.fromValue(m.getKey()));
                vasEntity.setServiceStatus(StringUtils.equals(VASStatus.ENABLE.toValue(), m.getValue()));
                return vasEntity;
            }).collect(Collectors.toList());
        } else {
            return this.getOperator().getServices();
        }
    }

    /**
     * 获取企业购配置信息
     *
     * @return
     */
    public IepSettingVO getIepSettingInfo() {
        if (!this.findVASBuyOrNot(VASConstants.VAS_IEP_SETTING)) {
            throw new SbcRuntimeException(IepServiceErrorCode.DID_NOT_BUY_IEP_SERVICE);
        }
        if (redisService.hasKey(CacheKeyConstant.IEP_SETTING)) {
            IepSettingVO iepSettingVO = JSONObject.parseObject(redisService.getString(CacheKeyConstant.IEP_SETTING),
                    IepSettingVO.class);
            return iepSettingVO;
        } else {
            return iepSettingQueryProvider.cacheIepSetting().getContext().getIepSettingVO();
        }
    }

    /**
     * 获取分享id
     * @return
     */
    public String getShareId(String customerId){
        //防止刷分行为，每次分享生成一个id，存在redis中
        long milli = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        String key;
        if(StringUtils.isNotBlank(customerId)) {
            key = customerId.concat(String.valueOf(milli));
        } else {
            key = this.getOperatorId().concat(String.valueOf(milli));
        }

        //存放一周
        redisService.setString(key, "0", 604800);
        return key;
    }

}
