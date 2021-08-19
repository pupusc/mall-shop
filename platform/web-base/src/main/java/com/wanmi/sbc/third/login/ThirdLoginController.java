package com.wanmi.sbc.third.login;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.SpecialSymbols;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.redis.CacheKeyConstant;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.SiteResultCode;
import com.wanmi.sbc.common.util.ValidateUtil;
import com.wanmi.sbc.customer.api.constant.ThirdLoginRelationErrorCode;
import com.wanmi.sbc.customer.api.provider.loginregister.CustomerSiteProvider;
import com.wanmi.sbc.customer.api.provider.loginregister.CustomerSiteQueryProvider;
import com.wanmi.sbc.customer.api.provider.quicklogin.ThirdLoginRelationProvider;
import com.wanmi.sbc.customer.api.provider.quicklogin.ThirdLoginRelationQueryProvider;
import com.wanmi.sbc.customer.api.request.loginregister.CustomerByAccountRequest;
import com.wanmi.sbc.customer.api.request.loginregister.CustomerSendMobileCodeRequest;
import com.wanmi.sbc.customer.api.request.loginregister.CustomerValidateSendMobileCodeRequest;
import com.wanmi.sbc.customer.api.request.quicklogin.ThirdLoginRelationByCustomerRequest;
import com.wanmi.sbc.customer.api.request.quicklogin.ThirdLoginRelationDeleteByCustomerRequest;
import com.wanmi.sbc.customer.api.response.loginregister.CustomerByAccountResponse;
import com.wanmi.sbc.customer.bean.enums.SmsTemplate;
import com.wanmi.sbc.customer.bean.enums.ThirdLoginType;
import com.wanmi.sbc.customer.bean.vo.ThirdLoginRelationVO;
import com.wanmi.sbc.redis.RedisService;
import com.wanmi.sbc.third.login.response.LinkedAccountFlagsQueryResponse;
import com.wanmi.sbc.third.login.response.ThirdLoginSendCodeResponse;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import jodd.util.StringUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@Api(tags = "ThirdLoginController", description = "第三方登录 API")
@RestController
@RequestMapping("/third/login")
public class ThirdLoginController {

    private static String USER_INFO_KEY = CacheKeyConstant.WE_CHAT + SpecialSymbols.COLON.toValue() + "USER_INFO" +
            SpecialSymbols.COLON.toValue();

    @Autowired
    private RedisService redisService;

    @Autowired
    private ThirdLoginRelationProvider thirdLoginRelationProvider;

    @Autowired
    private ThirdLoginRelationQueryProvider thirdLoginRelationQueryProvider;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private CustomerSiteProvider customerSiteProvider;


    @Autowired
    private CustomerSiteQueryProvider customerSiteQueryProvider;

    @Value("${jwt.secret-key}")
    private String jwtSecretKey;

    /**
     * 绑定第三方账号 发送验证码
     *
     * @return
     */
    @ApiOperation(value = "绑定第三方账号 发送验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "String", name = "phone", value = "手机号", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "String", name = "id", value = "id", required = true)
    })
    @RequestMapping(value = "/bind/sendCode/{phone}/{id}", method = RequestMethod.GET)
    public BaseResponse<ThirdLoginSendCodeResponse> sendCode(@PathVariable String phone, @PathVariable String id) {
        // 校验输入信息是否合法
        if (StringUtil.isBlank(phone) || StringUtil.isBlank(id) || !ValidateUtil.isPhone(phone)) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        // 判断是否在10分钟之内验证 为了确保在验证验证码绑定商城账户时，能获取到在/wetchLogin接口中存入redis的微信userInfo
        boolean wxFlag = redisService.hasKey(USER_INFO_KEY + id);
        if (!wxFlag) {
            throw new SbcRuntimeException(ThirdLoginRelationErrorCode.TIME_OUT);
        }
        //是否可以发送
        CustomerValidateSendMobileCodeRequest customerValidateSendMobileCodeRequest =
                new CustomerValidateSendMobileCodeRequest();
        customerValidateSendMobileCodeRequest.setMobile(phone);
        if (!customerSiteProvider.validateSendMobileCode(customerValidateSendMobileCodeRequest).getContext().getResult()) {
            throw new SbcRuntimeException(SiteResultCode.ERROR_000016);
        }

        //账号是否注册
        CustomerByAccountRequest request = new CustomerByAccountRequest();
        request.setCustomerAccount(phone);
        BaseResponse<CustomerByAccountResponse> responseBaseResponse =
                customerSiteQueryProvider.getCustomerByCustomerAccount(request);
        CustomerByAccountResponse response = responseBaseResponse.getContext();
        // 已注册的情况下, 需要走的流程
        Boolean isRegister = false;
        if (Objects.nonNull(response)) {
            ThirdLoginRelationVO phoneRelation = thirdLoginRelationQueryProvider
                    .thirdLoginRelationByCustomerAndDelFlag(
                            ThirdLoginRelationByCustomerRequest.builder()
                                    .customerId(response.getCustomerId())
                                    .thirdLoginType(ThirdLoginType.WECHAT)
                                    .delFlag(DeleteFlag.NO)
                                    .storeId(Constants.BOSS_DEFAULT_STORE_ID)
                                    .build()
                    ).getContext().getThirdLoginRelation();
            // 手机号是否已经被绑定
            if (Objects.nonNull(phoneRelation)) {
                throw new SbcRuntimeException(ThirdLoginRelationErrorCode.PHONE_ALREADY_BINDING);
            }
        } else {
            isRegister = true;
        }
        //发送验证码
        CustomerSendMobileCodeRequest customerSendMobileCodeRequest = new CustomerSendMobileCodeRequest();
        customerSendMobileCodeRequest.setMobile(phone);
        customerSendMobileCodeRequest.setRedisKey(CacheKeyConstant.WX_BINDING_LOGIN);
        customerSendMobileCodeRequest.setSmsTemplate(SmsTemplate.WX_CUSTOMER_LOGIN);
        if (Constants.yes.equals(customerSiteProvider.sendMobileCode(customerSendMobileCodeRequest).getContext().getResult())) {
            return BaseResponse.success(ThirdLoginSendCodeResponse.builder()
                    .isRegister(isRegister)
                    .build());
        }
        return BaseResponse.FAILED();
    }

    /**
     * 解绑
     *
     * @param thirdLoginType 第三方登录方式
     * @return
     */
    @ApiOperation(value = "解绑", notes = "thirdLoginType: 0微信")
    @ApiImplicitParam(paramType = "path", name = "thirdLoginType", value = "登录方式", required = true)
    @RequestMapping(value = "/remove/bind/{thirdLoginType}", method = RequestMethod.DELETE)
    public BaseResponse removeBind(@PathVariable ThirdLoginType thirdLoginType) {
        return thirdLoginRelationProvider.deleteThirdLoginRelationByCustomerIdAndStoreId(
                ThirdLoginRelationDeleteByCustomerRequest.builder()
                        .customerId(commonUtil.getOperatorId())
                        .thirdLoginType(thirdLoginType)
                        .storeId(Constants.BOSS_DEFAULT_STORE_ID)
                        .build()
        );
    }

    /**
     * 查询登录用户关联账号的状态
     */
    @ApiOperation(value = "查询登录用户关联账号的状态")
    @RequestMapping(value = "/linked-account-flags", method = RequestMethod.GET)
    public BaseResponse<LinkedAccountFlagsQueryResponse> queryLinkedAccountFlags() {
        ThirdLoginRelationVO phoneRelation = thirdLoginRelationQueryProvider
                .thirdLoginRelationByCustomerAndDelFlag(
                        ThirdLoginRelationByCustomerRequest.builder()
                                .customerId(commonUtil.getOperatorId())
                                .thirdLoginType(ThirdLoginType.WECHAT)
                                .delFlag(DeleteFlag.NO)
                                .storeId(Constants.BOSS_DEFAULT_STORE_ID)
                                .build()
                ).getContext().getThirdLoginRelation();
        String nickName = "";
        String headImgUrl = "";
        if (Objects.nonNull(phoneRelation)) {
            headImgUrl = phoneRelation.getHeadimgurl();
            nickName = phoneRelation.getNickname();
        }
        return BaseResponse.success(LinkedAccountFlagsQueryResponse.builder()
                .wxFlag(phoneRelation != null)
                .nickname(nickName)
                .headimgurl(headImgUrl)
                .build());
    }
}
