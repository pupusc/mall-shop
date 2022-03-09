package com.soybean.mall.user.controller;

import com.soybean.mall.common.CommonUtil;
import com.soybean.mall.common.LoginResponse;
import com.soybean.mall.user.mq.WebBaseProducerService;
import com.soybean.mall.wx.mini.user.bean.request.WxGetUserPhoneAndOpenIdRequest;
import com.soybean.mall.wx.mini.user.bean.response.WxGetUserPhoneAndOpenIdResponse;
import com.soybean.mall.wx.mini.user.controller.WxUserApiController;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.HttpUtil;
import com.wanmi.sbc.customer.api.provider.customer.CustomerProvider;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.provider.distribution.DistributionCustomerSaveProvider;
import com.wanmi.sbc.customer.api.provider.fandeng.ExternalProvider;
import com.wanmi.sbc.customer.api.request.customer.*;
import com.wanmi.sbc.customer.api.request.distribution.DistributionCustomerModifyCustomerIdRequest;
import com.wanmi.sbc.customer.api.request.fandeng.FanDengModifyCustomerLoginTimeRequest;
import com.wanmi.sbc.customer.api.request.fandeng.FanDengModifyCustomerRequest;
import com.wanmi.sbc.customer.api.request.fandeng.FanDengModifyPaidCustomerRequest;
import com.wanmi.sbc.customer.api.request.fandeng.FanDengWxAuthLoginRequest;
import com.wanmi.sbc.customer.api.response.customer.NoDeleteCustomerGetByAccountResponse;
import com.wanmi.sbc.customer.api.response.fandeng.FanDengLoginResponse;
import com.wanmi.sbc.customer.api.response.fandeng.FanDengWxAuthLoginResponse;
import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import com.wanmi.sbc.order.api.provider.trade.TradeQueryProvider;
import com.wanmi.sbc.order.api.request.trade.TradePageCriteriaRequest;
import com.wanmi.sbc.order.api.response.trade.TradePageCriteriaResponse;
import com.wanmi.sbc.order.bean.dto.TradeQueryDTO;
import com.wanmi.sbc.order.bean.vo.TradeVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@RestController
public class UserController {

    @Value("${jwt.secret-key}")
    private String jwtSecretKey;

    @Autowired
    private WxUserApiController wxUserApiController;
    @Autowired
    private CustomerQueryProvider customerQueryProvider;
    @Autowired
    private ExternalProvider externalProvider;
    @Autowired
    private CustomerProvider customerProvider;
    @Autowired
    private CommonUtil commonUtil;
    @Autowired
    private WebBaseProducerService webBaseProducerService;
    @Autowired
    private TradeQueryProvider tradeQueryProvider;
    @Autowired
    private DistributionCustomerSaveProvider distributionCustomerSaveProvider;

    /**
     * 微信小程序授权登录
     */
    @ApiOperation(value = "微信授权登陆接口")
    @RequestMapping(value = "/wxAuthLogin", method = RequestMethod.POST)
//    @MultiSubmit
//    @GlobalTransactional
    public BaseResponse<LoginResponse> wxAuthLogin(@RequestBody WxGetUserPhoneAndOpenIdRequest wxGetUserPhoneAndOpenIdRequest) {
        String codeForOpenid = wxGetUserPhoneAndOpenIdRequest.getCodeForOpenid();
        wxGetUserPhoneAndOpenIdRequest.setCodeForOpenid(null);
        BaseResponse<WxGetUserPhoneAndOpenIdResponse> phoneAndOpenid = wxUserApiController.getPhoneAndOpenid(wxGetUserPhoneAndOpenIdRequest);

        NoDeleteCustomerGetByAccountRequest accountRequest = new NoDeleteCustomerGetByAccountRequest();
        accountRequest.setCustomerAccount(phoneAndOpenid.getContext().getPhoneNumber());
        NoDeleteCustomerGetByAccountResponse newCustomerVO = customerQueryProvider.getNoDeleteCustomerByAccount(accountRequest).getContext();

        String openId, unionId;
        boolean newUser = false;
        if(newCustomerVO == null){
            //如果是新用户，就调微信接口获取opeid
            wxGetUserPhoneAndOpenIdRequest.setCodeForPhone(null);
            wxGetUserPhoneAndOpenIdRequest.setCodeForOpenid(codeForOpenid);
            BaseResponse<WxGetUserPhoneAndOpenIdResponse> phoneAndOpenid2 = wxUserApiController.getPhoneAndOpenid(wxGetUserPhoneAndOpenIdRequest);
            unionId = phoneAndOpenid2.getContext().getUnionId();
            openId = phoneAndOpenid2.getContext().getOpenId();
            newUser = true;
        }else {
            unionId = newCustomerVO.getWxMiniUnionId();
            openId = newCustomerVO.getWxMiniOpenId();
        }
        if(!newUser && (openId == null || unionId == null)){
            // 如果不是新用户，但是没有openid，先调微信获取，再保存到用户信息中
            wxGetUserPhoneAndOpenIdRequest.setCodeForPhone(null);
            wxGetUserPhoneAndOpenIdRequest.setCodeForOpenid(codeForOpenid);
            BaseResponse<WxGetUserPhoneAndOpenIdResponse> phoneAndOpenid2 = wxUserApiController.getPhoneAndOpenid(wxGetUserPhoneAndOpenIdRequest);
            unionId = phoneAndOpenid2.getContext().getUnionId();
            openId = phoneAndOpenid2.getContext().getOpenId();
            customerProvider.modifyCustomerOpenIdAndUnionId(newCustomerVO.getCustomerId(), openId, unionId);
        }
        FanDengWxAuthLoginRequest authLoginRequest = FanDengWxAuthLoginRequest.builder().unionId(unionId).openId(openId).areaCode("+86").registerSource("IntegralMall")
                .mobile(phoneAndOpenid.getContext().getPhoneNumber()).build();
        BaseResponse<FanDengWxAuthLoginResponse.WxAuthLoginData> wxAuthLoginDataBaseResponse = externalProvider.wxAuthLogin(authLoginRequest);
        LoginResponse loginResponse = afterWxAuthLogin(wxAuthLoginDataBaseResponse.getContext(), phoneAndOpenid.getContext().getPhoneNumber(), openId, unionId);
        return BaseResponse.success(loginResponse);
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
                //todo 发送埋点
                webBaseProducerService.sendMQForCustomerRegister(customerVO);
                loginResponse.setNewFlag(Boolean.TRUE);
            }
        }
        loginResponse.setPhoto(resData.getProfilePhoto());
        loginResponse.setOpenId(openId);
        return loginResponse;
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

                    CustomersDeleteRequest deleteRequest = new CustomersDeleteRequest();
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
}
