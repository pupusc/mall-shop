package com.wanmi.sbc.finance;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.account.api.provider.customerdrawcash.CustomerDrawCashProvider;
import com.wanmi.sbc.account.api.provider.customerdrawcash.CustomerDrawCashQueryProvider;
import com.wanmi.sbc.account.api.provider.customerdrawcash.CustomerDrawCashSaveProvider;
import com.wanmi.sbc.account.api.provider.funds.CustomerFundsDetailQueryProvider;
import com.wanmi.sbc.account.api.provider.funds.CustomerFundsProvider;
import com.wanmi.sbc.account.api.provider.funds.CustomerFundsQueryProvider;
import com.wanmi.sbc.account.api.request.customerdrawcash.*;
import com.wanmi.sbc.account.api.request.funds.*;
import com.wanmi.sbc.account.api.response.customerdrawcash.CustomerDrawCashModifyResponse;
import com.wanmi.sbc.account.api.response.customerdrawcash.CustomerDrawCashStatusResponse;
import com.wanmi.sbc.account.api.response.funds.CustomerFundsByCustomerFundsIdForEsResponse;
import com.wanmi.sbc.account.api.response.funds.CustomerFundsByCustomerIdForEsResponse;
import com.wanmi.sbc.account.api.response.funds.CustomerFundsByCustomerIdResponse;
import com.wanmi.sbc.account.bean.enums.*;
import com.wanmi.sbc.account.bean.vo.CustomerDrawCashVO;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.*;
import com.wanmi.sbc.common.util.excel.Column;
import com.wanmi.sbc.common.util.excel.ExcelHelper;
import com.wanmi.sbc.common.util.excel.impl.SpelColumnRender;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.provider.detail.CustomerDetailQueryProvider;
import com.wanmi.sbc.customer.api.provider.quicklogin.ThirdLoginRelationQueryProvider;
import com.wanmi.sbc.customer.api.request.customer.CustomerGetByIdRequest;
import com.wanmi.sbc.customer.api.request.customer.DisableCustomerDetailGetByAccountRequest;
import com.wanmi.sbc.customer.api.request.detail.CustomerDetailStatusByCustomerIdsRequest;
import com.wanmi.sbc.customer.api.response.customer.CustomerGetByIdResponse;
import com.wanmi.sbc.customer.api.response.detail.CustomerDetailStatusResponse;
import com.wanmi.sbc.customer.bean.vo.CustomerStatusBaseVO;
import com.wanmi.sbc.elastic.api.provider.customerFunds.EsCustomerFundsProvider;
import com.wanmi.sbc.elastic.api.request.customerFunds.EsCustomerFundsRequest;
import com.wanmi.sbc.pay.api.provider.WxPayProvider;
import com.wanmi.sbc.pay.api.request.WxPayCompanyPaymentInfoRequest;
import com.wanmi.sbc.pay.api.response.WxPayCompanyPaymentRsponse;
import com.wanmi.sbc.pay.bean.enums.WxPayTradeType;
import com.wanmi.sbc.pay.weixinpaysdk.WXPayConstants;
import com.wanmi.sbc.setting.api.provider.WechatAuthProvider;
import com.wanmi.sbc.setting.api.response.MiniProgramSetGetResponse;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 会员提现管理
 *
 * @author chenyufei
 */
@Slf4j
@RestController(value = "CustomerDrawCashController")
@RequestMapping("/draw/cash")
@Api(tags = "CustomerDrawCashController", description = "S2B 平台端-会员提现API")
public class CustomerDrawCashController {

    @Autowired
    private CustomerDrawCashQueryProvider customerDrawCashQueryProvider;

    @Autowired
    private CustomerFundsQueryProvider customerFundsQueryProvider;

    @Autowired
    private CustomerQueryProvider customerQueryProvider;

    @Autowired
    private CustomerDetailQueryProvider customerDetailQueryProvider;

    @Autowired
    private CustomerDrawCashProvider customerDrawCashProvider;

    @Autowired
    private CustomerDrawCashSaveProvider customerDrawCashSaveProvider;

    @Autowired
    private WxPayProvider wxPayProvider;

    @Autowired
    private ThirdLoginRelationQueryProvider thirdLoginRelationQueryProvider;

    @Autowired
    private CustomerFundsProvider customerFundsProvider;

    @Autowired
    private CustomerFundsDetailQueryProvider customerFundsDetailQueryProvider;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    @Autowired
    private WechatAuthProvider wechatAuthProvider;

    @Autowired
    private EsCustomerFundsProvider esCustomerFundsProvider;

    private AtomicInteger exportCount = new AtomicInteger(0);


    /**
     * S2B 平台端-获取会员提现管理列表数据统计(待审核、已完成、提现失败、审核未通过、已取消)
     *
     * @return
     */
    @ApiOperation(value = "S2B 平台端-获取会员提现管理列表数据统计(待审核、已完成、提现失败、审核未通过、已取消)")
    @RequestMapping(value = "/gather", method = RequestMethod.POST)
    public BaseResponse<CustomerDrawCashStatusResponse> gather() {
        //TODO 改为count方法
        return this.customerDrawCashQueryProvider.countDrawCashTabNum();
    }

    /**
     * S2B 平台端-获取会员提现管理分页列表
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "S2B 平台端-获取会员提现管理分页列表")
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public BaseResponse<MicroServicePage<CustomerDrawCashVO>> page(@RequestBody CustomerDrawCashPageRequest request) {

        request.setDelFlag(DeleteFlag.NO);
        request.setSourceFromPlatForm(Boolean.TRUE);
        MicroServicePage<CustomerDrawCashVO> cashVOS = this.customerDrawCashQueryProvider.page(request).getContext()
                .getCustomerDrawCashVOPage();
        // 设置账户信息
        cashVOS.forEach(this::setAccountInfo);
        return BaseResponse.success(cashVOS);
    }


    /**
     * S2B 平台端-审核会员提现请求(0 待审核  1审核不通过 2审核通过)
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "S2B 平台端-审核会员提现请求(0 待审核  1审核不通过 2审核通过)")
    @RequestMapping(value = "/audit", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse audit(@RequestBody CustomerDrawCashModifyAuditStatusRequest request) {

        CustomerDrawCashVO cash = this.customerDrawCashQueryProvider.getById(CustomerDrawCashByIdRequest
                .builder()
                .drawCashId(request.getDrawCashId())
                .build()
        ).getContext().getCustomerDrawCashVO();

        if(cash.getCustomerOperateStatus() == CustomerOperateStatus.CANCEL){
            throw new SbcRuntimeException("K-120011",new Object[]{cash.getDrawCashNo()});
        }

        BigDecimal accountBalance = cash.getAccountBalance();
        //如果审核通过调用微信提现接口  //这边是页面操作 点击审核成功的
        if (request.getAuditStatus() == AuditStatus.PASS) {
            CustomerDrawCashModifyAuditStatusRequest customerDrawCashModifyAuditStatusRequest =
                    this.wxPayCompanyPayment(cash);
            request.setDrawCashFailedReason(customerDrawCashModifyAuditStatusRequest.getDrawCashFailedReason());
            request.setFinishStatus(customerDrawCashModifyAuditStatusRequest.getFinishStatus());
            request.setDrawCashStatus(customerDrawCashModifyAuditStatusRequest.getDrawCashStatus());
            //这边如果审核通过  同时微信提现成功的要入已提现字段
            if (request.getDrawCashStatus() == DrawCashStatus.SUCCESS) {
                accountBalance = this.increaseAlreadyDrawCashAmount(cash);
                //this.descendAccountBalance(cash);
                //this.descendBlockedBalance(cash);
                withdrawAmount(cash,WithdrawAmountStatus.AGREE);
                //同时更新账户明细表
                agreeAmountPaidAndExpenditure(cash);
                //更新会员账户明细表数据
                CustomerFundsDetailModifyRequest customerFundsDetailModifyRequest = new CustomerFundsDetailModifyRequest();
                customerFundsDetailModifyRequest.setBusinessId(customerDrawCashModifyAuditStatusRequest.getBusinessId());
                customerFundsDetailModifyRequest.setCustomerId(cash.getCustomerId());
                customerFundsDetailModifyRequest.setDrawCashId(cash.getDrawCashId());
                customerFundsDetailModifyRequest.setFundsStatus(FundsStatus.YES);
                customerFundsDetailModifyRequest.setTabType(2);
                modifyCustomerFundsDetail(customerFundsDetailModifyRequest);
            }
        } else if(request.getAuditStatus() == AuditStatus.REJECT){
            //审核拒绝操作
            request.setFinishStatus(FinishStatus.UNSUCCESS);
            request.setDrawCashStatus(DrawCashStatus.WAIT);
            withdrawAmount(cash,WithdrawAmountStatus.REJECT);
        }
        //修改 返回
        CustomerDrawCashBatchModifyAuditStatusRequest customerDrawCashBatchModifyAuditStatusRequest = CustomerDrawCashBatchModifyAuditStatusRequest.builder()
                .drawCashIdList(new ArrayList<String>() {{
                    add(request.getDrawCashId());
                }})
                .drawCashFailedReason(request.getDrawCashFailedReason())
                .drawCashStatus(request.getDrawCashStatus())
                .finishStatus(request.getFinishStatus())
                .auditStatus(request.getAuditStatus())
                .rejectReason(request.getRejectReason())
                .accountBalance(accountBalance)
                .build();

        BaseResponse response = this.customerDrawCashProvider.batchModifyAuditStatus(customerDrawCashBatchModifyAuditStatusRequest);

        String statusTxt = "待审核";
        if (Objects.equals(customerDrawCashBatchModifyAuditStatusRequest.getAuditStatus(), AuditStatus.PASS)) {
            statusTxt = "通过";
        } else if (Objects.equals(customerDrawCashBatchModifyAuditStatusRequest.getAuditStatus(), AuditStatus.REJECT)) {
            statusTxt = "不通过";
        }
        // 操作日志
        operateLogMQUtil.convertAndSend("财务", "审核会员提现", "提现单号: "+cash.getDrawCashNo() +" 审核状态：" + statusTxt);
        return response;
    }

    /**
     * S2B 平台端-审核会员提现请求重新请求支付接口
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "S2B 平台端-审核会员提现请求重新请求支付接口")
    @RequestMapping(value = "/try/again", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse<CustomerDrawCashModifyResponse> tryAgain(@RequestBody CustomerDrawCashPageRequest request) {

        CustomerDrawCashVO cash = this.customerDrawCashQueryProvider.getById(CustomerDrawCashByIdRequest.builder()
                .drawCashId(request.getDrawCashId())
                .build())
                .getContext().getCustomerDrawCashVO();

        //判断提现申请是否已经取消
        if(cash.getCustomerOperateStatus() == CustomerOperateStatus.CANCEL){
            throw new SbcRuntimeException("K-120011",new Object[]{cash.getDrawCashNo()});
        }

        BigDecimal accountBalance = cash.getAccountBalance();

        //如果审核通过调用微信提现接口  //这边是取数据库中查出的数据  审核成功的
        if (cash.getAuditStatus() == AuditStatus.PASS) {
            CustomerDrawCashModifyAuditStatusRequest customerDrawCashModifyAuditStatusRequest =
                    this.wxPayCompanyPayment(cash);
            request.setDrawCashFailedReason(customerDrawCashModifyAuditStatusRequest.getDrawCashFailedReason());
            request.setFinishStatus(customerDrawCashModifyAuditStatusRequest.getFinishStatus());
            request.setDrawCashStatus(customerDrawCashModifyAuditStatusRequest.getDrawCashStatus());
            request.setAuditStatus(cash.getAuditStatus());

            //这边如果审核通过  同时微信提现成功的要入已提现字段
            if (request.getDrawCashStatus() == DrawCashStatus.SUCCESS) {
                accountBalance = this.increaseAlreadyDrawCashAmount(cash);
//                this.descendAccountBalance(cash);
//                this.descendBlockedBalance(cash);
                withdrawAmount(cash,WithdrawAmountStatus.AGREE);
                //同时更新账户明细表
                agreeAmountPaidAndExpenditure(cash);
                //更新会员账户明细表数据
                CustomerFundsDetailModifyRequest customerFundsDetailModifyRequest = new CustomerFundsDetailModifyRequest();
                customerFundsDetailModifyRequest.setBusinessId(customerDrawCashModifyAuditStatusRequest.getBusinessId());
                customerFundsDetailModifyRequest.setCustomerId(cash.getCustomerId());
                customerFundsDetailModifyRequest.setDrawCashId(cash.getDrawCashId());
                customerFundsDetailModifyRequest.setFundsStatus(FundsStatus.YES);
                customerFundsDetailModifyRequest.setTabType(2);
                modifyCustomerFundsDetail(customerFundsDetailModifyRequest);
            }
        } else if(request.getAuditStatus() == AuditStatus.REJECT){
            //审核拒绝操作
            request.setFinishStatus(FinishStatus.UNSUCCESS);
            request.setDrawCashStatus(DrawCashStatus.WAIT);
            withdrawAmount(cash,WithdrawAmountStatus.REJECT);
        }

        //处理 返回
        BaseResponse<CustomerDrawCashModifyResponse> response = this.customerDrawCashSaveProvider.modify(
                CustomerDrawCashModifyRequest.builder()
                        .drawCashId(request.getDrawCashId())
                        .drawCashNo(cash.getDrawCashNo())
                        .applyTime(cash.getApplyTime())
                        .customerId(cash.getCustomerId())
                        .customerName(cash.getCustomerName())
                        .customerAccount(cash.getCustomerAccount())
                        .drawCashChannel(cash.getDrawCashChannel())
                        .drawCashAccountName(cash.getDrawCashAccountName())
                        .drawCashAccount(cash.getDrawCashAccount())
                        .drawCashSum(cash.getDrawCashSum())
                        .drawCashRemark(cash.getDrawCashRemark())
                        .auditStatus(request.getAuditStatus())
                        .rejectReason(cash.getRejectReason())
                        .drawCashStatus(request.getDrawCashStatus())
                        .drawCashFailedReason(request.getDrawCashFailedReason())
                        .customerOperateStatus(cash.getCustomerOperateStatus())
                        .finishStatus(request.getFinishStatus())
                        .finishTime(LocalDateTime.now())
                        .supplierOperateId(cash.getSupplierOperateId())
                        .updateTime(cash.getUpdateTime())
                        .delFlag(cash.getDelFlag())
                        .drawCashSource(cash.getDrawCashSource())
                        .openId(cash.getOpenId())
                        .accountBalance(accountBalance)
                        .build()
        );

        if(Objects.nonNull(response.getContext())){
            //新增更改会员资金es
            CustomerFundsByCustomerIdRequest byCustomerIdRequest = new CustomerFundsByCustomerIdRequest();
            byCustomerIdRequest.setCustomerId(cash.getCustomerId());
            BaseResponse<CustomerFundsByCustomerIdForEsResponse> forEsResponseBaseResponse = customerFundsQueryProvider.getByCustomerIdForEs(byCustomerIdRequest);
            if(Objects.nonNull(forEsResponseBaseResponse.getContext())){
                esCustomerFundsProvider.initCustomerFunds(KsBeanUtil.convert(forEsResponseBaseResponse.getContext(), EsCustomerFundsRequest.class));
            }
        }


        return response;
    }

    /**
     * S2B 平台端-批量审核会员提现单
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "S2B 平台端-批量审核会员提现单")
    @RequestMapping(value = "/batch/audit", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse batchAudit(@RequestBody CustomerDrawCashBatchModifyAuditStatusRequest request) {

        List<String> drawCashNos = new ArrayList<>();

        request.getDrawCashIdList().forEach(drawCashId -> {

            //单个对象
            CustomerDrawCashVO cash = this.customerDrawCashQueryProvider.getById(CustomerDrawCashByIdRequest
                    .builder()
                    .drawCashId(drawCashId)
                    .build()
            ).getContext().getCustomerDrawCashVO();
            drawCashNos.add(cash.getDrawCashNo());
            BigDecimal accountBalance = cash.getAccountBalance();

            //批量处理时，判断提现申请是否已经取消，未取消才进行审核操作
            if(cash.getCustomerOperateStatus() != CustomerOperateStatus.CANCEL){
                //调用微信
                CustomerDrawCashModifyAuditStatusRequest customerDrawCashModifyAuditStatusRequest =
                        this.wxPayCompanyPayment(cash);
                cash.setDrawCashFailedReason(customerDrawCashModifyAuditStatusRequest.getDrawCashFailedReason());
                cash.setFinishStatus(customerDrawCashModifyAuditStatusRequest.getFinishStatus());
                cash.setDrawCashStatus(customerDrawCashModifyAuditStatusRequest.getDrawCashStatus());

                //这边如果审核通过  同时微信提现成功的要入已提现字段  账户余额   冻结资金金额
                if (cash.getDrawCashStatus() == DrawCashStatus.SUCCESS) {
                    accountBalance =  this.increaseAlreadyDrawCashAmount(cash);
                    //this.descendAccountBalance(cash);
                   // this.descendBlockedBalance(cash);
                    withdrawAmount(cash,WithdrawAmountStatus.AGREE);
                    //同时更新账户明细表
                    agreeAmountPaidAndExpenditure(cash);
                    //更新会员账户明细表数据
                    CustomerFundsDetailModifyRequest customerFundsDetailModifyRequest = new CustomerFundsDetailModifyRequest();
                    customerFundsDetailModifyRequest.setBusinessId(customerDrawCashModifyAuditStatusRequest.getBusinessId());
                    customerFundsDetailModifyRequest.setCustomerId(cash.getCustomerId());
                    customerFundsDetailModifyRequest.setDrawCashId(cash.getDrawCashId());
                    customerFundsDetailModifyRequest.setFundsStatus(FundsStatus.YES);
                    customerFundsDetailModifyRequest.setTabType(2);
                    modifyCustomerFundsDetail(customerFundsDetailModifyRequest);
                }

                //循环塞值
                this.customerDrawCashProvider.batchModifyAuditStatus(CustomerDrawCashBatchModifyAuditStatusRequest.builder()
                        .drawCashIdList(new ArrayList<String>() {{
                            add(drawCashId);
                        }})
                        .drawCashFailedReason(cash.getDrawCashFailedReason())
                        .drawCashStatus(cash.getDrawCashStatus())
                        .finishStatus(cash.getFinishStatus())
                        .auditStatus(request.getAuditStatus())
                        .rejectReason(cash.getRejectReason())
                        .accountBalance(accountBalance)
                        .build()
                );
            }
        });
        if(CollectionUtils.isNotEmpty(drawCashNos)) {
            // 操作日志
            operateLogMQUtil.convertAndSend("财务", "批量审核会员提现", "提现单号: "+drawCashNos.toString());
        }
        return BaseResponse.success("批量审核成功");
    }


    /**
     * 更新提现字段
     * @param customerDrawCashVO
     */
    private BigDecimal increaseAlreadyDrawCashAmount(CustomerDrawCashVO customerDrawCashVO){

        CustomerFundsByCustomerIdRequest customerIdRequest = new CustomerFundsByCustomerIdRequest();
        customerIdRequest.setCustomerId(customerDrawCashVO.getCustomerId());

        BaseResponse<CustomerFundsByCustomerIdResponse> customerIdResponseBaseResponse =
                this.customerFundsQueryProvider.getByCustomerId(customerIdRequest);

        if(Objects.isNull(customerIdResponseBaseResponse.getContext())){
            throw new SbcRuntimeException("当前会员没有会员资金账户,已提现入账失败");
        }

        BigDecimal nextAlreadyDrawCashAmount =
                customerIdResponseBaseResponse.getContext().getAlreadyDrawAmount().add(customerDrawCashVO.getDrawCashSum());

        this.customerFundsProvider.modifyAlreadyDrawCashAmountByCustomerId(
                CustomerFundsModifyAlreadyDrawCashAmountByCustomerIdRequest
                        .builder()
                        .customerId(customerDrawCashVO.getCustomerId())
                        .alreadyDrawCashAmount(nextAlreadyDrawCashAmount)
                        .build());

        return customerIdResponseBaseResponse.getContext().getAccountBalance().subtract(customerDrawCashVO.getDrawCashSum());
    }

    /**
     * 提现成功更新
     * @param customerDrawCashVO
     */
    private void agreeAmountPaidAndExpenditure(CustomerDrawCashVO customerDrawCashVO){
        CustomerFundsByCustomerIdRequest customerIdRequest = new CustomerFundsByCustomerIdRequest();
        customerIdRequest.setCustomerId(customerDrawCashVO.getCustomerId());
        BaseResponse<CustomerFundsByCustomerIdResponse> customerIdResponseBaseResponse =
                this.customerFundsQueryProvider.getByCustomerId(customerIdRequest);
        if(Objects.isNull(customerIdResponseBaseResponse.getContext())){
            throw new SbcRuntimeException("当前会员没有会员资金账户,已提现入账失败");
        }
        CustomerFundsModifyRequest customerFundsModifyRequest = new CustomerFundsModifyRequest();
        customerFundsModifyRequest.setCustomerFundsId(customerIdResponseBaseResponse.getContext().getCustomerFundsId());
        customerFundsModifyRequest.setWithdrawAmount(customerDrawCashVO.getDrawCashSum());
        customerFundsProvider.agreeAmountPaidAndExpenditure(customerFundsModifyRequest);
    }

    /**
     * 更新账户明细表对应字段
     * @param customerFundsDetailModifyRequest
     */
    private void modifyCustomerFundsDetail(CustomerFundsDetailModifyRequest customerFundsDetailModifyRequest){
        customerFundsDetailQueryProvider.modifyCustomerFundsDetail(customerFundsDetailModifyRequest);
    }

    /**
     * 更新可提现字段
     * @param customerDrawCashVO
     */
    private void withdrawAmount(CustomerDrawCashVO customerDrawCashVO,WithdrawAmountStatus withdrawAmountStatus){
        CustomerFundsByCustomerIdRequest customerIdRequest = new CustomerFundsByCustomerIdRequest();
        customerIdRequest.setCustomerId(customerDrawCashVO.getCustomerId());
        BaseResponse<CustomerFundsByCustomerIdResponse> customerIdResponseBaseResponse =
                this.customerFundsQueryProvider.getByCustomerId(customerIdRequest);
        if(Objects.isNull(customerIdResponseBaseResponse.getContext())){
            throw new SbcRuntimeException("当前会员没有会员资金账户,已提现入账失败");
        }
        CustomerFundsModifyRequest customerFundsModifyRequest = new CustomerFundsModifyRequest();
        customerFundsModifyRequest.setCustomerFundsId(customerIdResponseBaseResponse.getContext().getCustomerFundsId());
        customerFundsModifyRequest.setWithdrawAmount(customerDrawCashVO.getDrawCashSum());
        customerFundsModifyRequest.setWithdrawAmountStatus(withdrawAmountStatus);
        customerFundsProvider.modifyCustomerFundsByIdAndWithdrawAmountStatus(customerFundsModifyRequest);

        //新增更改会员资金es
        CustomerFundsByCustomerFundsIdRequest byCustomerIdRequest = new CustomerFundsByCustomerFundsIdRequest();
        byCustomerIdRequest.setCustomerFundsId(customerIdResponseBaseResponse.getContext().getCustomerFundsId());
        BaseResponse<CustomerFundsByCustomerFundsIdForEsResponse> response = customerFundsQueryProvider.getByCustomerFundsIdForEs(byCustomerIdRequest);
        if(Objects.nonNull(response.getContext())){
            esCustomerFundsProvider.initCustomerFunds(KsBeanUtil.convert(response.getContext(), EsCustomerFundsRequest.class));
        }


    }

    /**
     * 递减账户余额
     * @param customerDrawCashVO
     */
    private void descendAccountBalance(CustomerDrawCashVO customerDrawCashVO){

        CustomerFundsByCustomerIdRequest customerIdRequest = new CustomerFundsByCustomerIdRequest();
        customerIdRequest.setCustomerId(customerDrawCashVO.getCustomerId());

        BaseResponse<CustomerFundsByCustomerIdResponse> customerIdResponseBaseResponse =
                this.customerFundsQueryProvider.getByCustomerId(customerIdRequest);

        if(Objects.isNull(customerIdResponseBaseResponse.getContext())){
            throw new SbcRuntimeException("当前会员没有会员资金账户,账户余额入账失败");
        }

        BigDecimal nextAccountBalance =
                customerIdResponseBaseResponse.getContext().getAccountBalance().subtract(customerDrawCashVO.getDrawCashSum());

        this.customerFundsProvider.modifyAccountBalanceByCustomerId(
                CustomerFundsModifyAccountBalanceByCustomerIdRequest
                        .builder()
                        .customerId(customerDrawCashVO.getCustomerId())
                        .accountBalance(nextAccountBalance)
                        .build());


        //新增更改会员资金es
        CustomerFundsByCustomerIdRequest byCustomerIdRequest = new CustomerFundsByCustomerIdRequest();
        byCustomerIdRequest.setCustomerId(customerDrawCashVO.getCustomerId());
        BaseResponse<CustomerFundsByCustomerIdForEsResponse> response = customerFundsQueryProvider.getByCustomerIdForEs(byCustomerIdRequest);
        if(Objects.nonNull(response.getContext())){
            esCustomerFundsProvider.initCustomerFunds(KsBeanUtil.convert(response.getContext(), EsCustomerFundsRequest.class));
        }
    }

    /**
     * 递减冻结金额
     * @param customerDrawCashVO
     */
    private void descendBlockedBalance(CustomerDrawCashVO customerDrawCashVO){

        CustomerFundsByCustomerIdRequest customerIdRequest = new CustomerFundsByCustomerIdRequest();
        customerIdRequest.setCustomerId(customerDrawCashVO.getCustomerId());

        BaseResponse<CustomerFundsByCustomerIdResponse> customerIdResponseBaseResponse =
                this.customerFundsQueryProvider.getByCustomerId(customerIdRequest);

        if(Objects.isNull(customerIdResponseBaseResponse.getContext())){
            throw new SbcRuntimeException("当前会员没有会员资金账户,冻结金额入账失败");
        }

        BigDecimal nextBlockedBalance =
                customerIdResponseBaseResponse.getContext().getBlockedBalance().subtract(customerDrawCashVO.getDrawCashSum());

        this.customerFundsProvider.modifyBlockedBalanceByCustomerId(
                CustomerFundsModifyBlockedBalanceByCustomerIdRequest
                        .builder()
                        .customerId(customerDrawCashVO.getCustomerId())
                        .blockedBalance(nextBlockedBalance)
                        .build());

        //新增更改会员资金es
        CustomerFundsByCustomerIdRequest byCustomerIdRequest = new CustomerFundsByCustomerIdRequest();
        byCustomerIdRequest.setCustomerId(customerDrawCashVO.getCustomerId());
        BaseResponse<CustomerFundsByCustomerIdForEsResponse> response = customerFundsQueryProvider.getByCustomerIdForEs(byCustomerIdRequest);
        if(Objects.nonNull(response.getContext())){
            esCustomerFundsProvider.initCustomerFunds(KsBeanUtil.convert(response.getContext(), EsCustomerFundsRequest.class));
        }


    }



    /**
     * 余额提现--微信付款到零钱接口
     *
     * @author lvzhengwei
     */
    private CustomerDrawCashModifyAuditStatusRequest wxPayCompanyPayment(CustomerDrawCashVO cash) {
        log.info("go into wxPayCompanyPayment method......");
        log.info("charge withdraw start......");
        CustomerDrawCashModifyAuditStatusRequest customerDrawCashModifyAuditStatusRequest =
                new CustomerDrawCashModifyAuditStatusRequest();

//        ThirdLoginRelationByCustomerRequest thirdLoginRelationByCustomerRequest =
//                new ThirdLoginRelationByCustomerRequest();
//        thirdLoginRelationByCustomerRequest.setCustomerId(cash.getCustomerId());
//        thirdLoginRelationByCustomerRequest.setThirdLoginType(ThirdLoginType.WECHAT);
//        ThirdLoginRelationResponse thirdLoginRelationResponse = thirdLoginRelationQueryProvider.
//                listThirdLoginRelationByCustomer(thirdLoginRelationByCustomerRequest).getContext();
        // 封装微信企业付款到零钱请求参数
        WxPayCompanyPaymentInfoRequest wxPayCompanyPaymentInfoRequest = new WxPayCompanyPaymentInfoRequest();
        // 标准版余额提现
        wxPayCompanyPaymentInfoRequest.setStoreId(Constants.BOSS_DEFAULT_STORE_ID);
        wxPayCompanyPaymentInfoRequest.setPartner_trade_no(cash.getDrawCashNo());
        wxPayCompanyPaymentInfoRequest.setOpenid(cash.getOpenId());
        wxPayCompanyPaymentInfoRequest.setCheck_name("NO_CHECK");
        wxPayCompanyPaymentInfoRequest.setAmount(cash.getDrawCashSum().multiply(new BigDecimal(100)).
                setScale(0, BigDecimal.ROUND_DOWN).toString());
        wxPayCompanyPaymentInfoRequest.setDesc("余额提现");
        // 微信支付交易类型
        if (Objects.equals(DrawCashSource.APP,cash.getDrawCashSource())){
            // app支付
            wxPayCompanyPaymentInfoRequest.setPayType(WxPayTradeType.APP);
        } else if (Objects.equals(DrawCashSource.MOBILE,cash.getDrawCashSource())) {
            // H5支付
            wxPayCompanyPaymentInfoRequest.setPayType(WxPayTradeType.MWEB);
        } else if (Objects.equals(DrawCashSource.MINI,cash.getDrawCashSource())) {
            // 小程序
            MiniProgramSetGetResponse response = wechatAuthProvider.getMiniProgramSet().getContext();
            JSONObject configJson = JSONObject.parseObject(response.getContext());
            //小程序被禁用
            if (response.getStatus() == 0) {
                throw new SbcRuntimeException(CommonErrorCode.WEAPP_FORBIDDEN);
            }
            String appId = configJson.getString("appId");
            wxPayCompanyPaymentInfoRequest.setPayType(WxPayTradeType.JSAPI);
            wxPayCompanyPaymentInfoRequest.setMiniAppId(appId);
        }
        wxPayCompanyPaymentInfoRequest.setSpbill_create_ip(HttpUtil.getIpAddr());

        // 微信支付--微信企业付款到零钱
        BaseResponse<WxPayCompanyPaymentRsponse> response =
                wxPayProvider.wxPayCompanyPayment(wxPayCompanyPaymentInfoRequest);
        // 付款请求成功
        if (response.getCode().equals(BaseResponse.SUCCESSFUL().getCode())) {
            WxPayCompanyPaymentRsponse wxPayCompanyPaymentRsponse = response.getContext();
            DrawCashStatus drawCashStatus = cash.getDrawCashStatus();
            FinishStatus finishStatus = cash.getFinishStatus();
            String drawCashFailedReason = "";
            //返回状态码：SUCCESS/FAIL；此字段是通信标识，非交易标识，交易是否成功需要查看result_code来判断
            if (wxPayCompanyPaymentRsponse.getReturn_code().equals(WXPayConstants.SUCCESS)) {
                // 业务结果
                if (wxPayCompanyPaymentRsponse.getResult_code().equals(WXPayConstants.SUCCESS)) {
                    // 提现成功
                    drawCashStatus = DrawCashStatus.SUCCESS;
                    // 提现已完成
                    finishStatus = FinishStatus.SUCCESS;
                    customerDrawCashModifyAuditStatusRequest.setBusinessId(cash.getDrawCashNo());
                } else {
                    // 错误代码描述
                    drawCashFailedReason = wxPayCompanyPaymentRsponse.getErr_code_des();
                    // 提现失败
                    drawCashStatus = DrawCashStatus.FAIL;
                }
            } else {
                // 为错误原因
                drawCashFailedReason = wxPayCompanyPaymentRsponse.getReturn_msg();
                // 提现失败
                drawCashStatus = DrawCashStatus.FAIL;
            }
            customerDrawCashModifyAuditStatusRequest.setDrawCashFailedReason(drawCashFailedReason);
            customerDrawCashModifyAuditStatusRequest.setDrawCashStatus(drawCashStatus);
            customerDrawCashModifyAuditStatusRequest.setFinishStatus(finishStatus);
            log.info("drawCashFailedReason============="+drawCashFailedReason);
        }
        log.info("charge withdraw end......");
        return customerDrawCashModifyAuditStatusRequest;
    }

    /**
     * 导出会员提现记录
     * @param encrypted
     * @param response
     */
    @ApiOperation(value = "导出会员提现记录")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "encrypted", value = "解密", required = true)
    @RequestMapping(value = "/export/params/{encrypted}", method = RequestMethod.GET)
    public void export(@PathVariable String encrypted, HttpServletResponse response) {
        try {
            if (exportCount.incrementAndGet() > 1) {
                throw new SbcRuntimeException(SiteResultCode.ERROR_000016);
            }

            String decrypted = new String(Base64.getUrlDecoder().decode(encrypted.getBytes()));
            CustomerDrawCashExportRequest queryReq = JSON.parseObject(decrypted, CustomerDrawCashExportRequest.class);
            queryReq.setDelFlag(DeleteFlag.NO);
            MicroServicePage<CustomerDrawCashVO> cashVOS = this.customerDrawCashQueryProvider.export(queryReq).getContext().getCustomerDrawCashVOPage();
            // 设置账号信息
            cashVOS.forEach(this::setAccountInfo);
            List<CustomerDrawCashVO> dataRecords = cashVOS.getContent();

            String headerKey = "Content-Disposition";
            LocalDateTime dateTime = LocalDateTime.now();
            String fileName = String.format("批量导出会员提现记录_%s.xls", dateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmm")));
            String fileNameNew = fileName;
            try {
                fileName = URLEncoder.encode(fileName, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                log.error("/draw/cash/export/params, fileName={},", fileName, e);
            }
            String headerValue = String.format("attachment;filename=\"%s\";filename*=\"utf-8''%s\"", fileName, fileName);
            response.setHeader(headerKey, headerValue);


            try {
                exportCash(dataRecords, response.getOutputStream(),queryReq);
                response.flushBuffer();
            } catch (IOException e) {
                throw new SbcRuntimeException(e);
            }
            operateLogMQUtil.convertAndSend("财务", "批量导出会员提现记录", fileNameNew);

        } catch (Exception e) {
            log.error("/draw/cash/export/params error: ", e);
            throw new SbcRuntimeException(SiteResultCode.ERROR_000001);
        } finally {
            exportCount.set(0);
        }

    }

    /**
     * 设置账户信息
     * @param customerDrawCashVO
     */
    private void setAccountInfo(CustomerDrawCashVO customerDrawCashVO) {
        AccountStatus status = AccountStatus.DISABLE;
        CustomerGetByIdResponse response = this.customerQueryProvider.getCustomerById(
                new CustomerGetByIdRequest(customerDrawCashVO.getCustomerId())).getContext();
        if(response!=null && response.getCustomerDetail() != null && response.getCustomerDetail().getCustomerStatus() != null){
            status = AccountStatus.valueOf(response.getCustomerDetail().getCustomerStatus().name());
        }
        customerDrawCashVO.setAccountStatus(status);

        DisableCustomerDetailGetByAccountRequest byAccountRequest = new DisableCustomerDetailGetByAccountRequest();
        byAccountRequest.setCustomerAccount(customerDrawCashVO.getCustomerAccount());
        customerDrawCashVO.setForbidReason(this.customerQueryProvider.getDisableCustomerDetailByAccount(byAccountRequest)
                .getContext().getForbidReason());

        // 会员账号中间4位*号
        customerDrawCashVO.setCustomerAccount(customerDrawCashVO.getCustomerAccount()
                .replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));

        CustomerFundsByCustomerIdRequest customerIdReques = new CustomerFundsByCustomerIdRequest();
        customerIdReques.setCustomerId(customerDrawCashVO.getCustomerId());

        CustomerFundsByCustomerIdResponse customerFundsByCustomerIdResponse = this.customerFundsQueryProvider
                .getByCustomerId(customerIdReques).getContext();

//        if (customerFundsByCustomerIdResponse != null) {
//            customerDrawCashVO.setAccountBalance(customerFundsByCustomerIdResponse.getAccountBalance());
//        }
        if (customerFundsByCustomerIdResponse != null) {
            customerDrawCashVO.setCustomerFundsId(customerFundsByCustomerIdResponse.getCustomerFundsId()
            );
        }
    }

    /**
     * 设置账户信息 优化，合并请求
     * @param customerDrawCashVOs
     */
    private void setAccountInfoByCustomers(List<CustomerDrawCashVO> customerDrawCashVOs) {

        CustomerDetailStatusByCustomerIdsRequest request = new CustomerDetailStatusByCustomerIdsRequest();
        request.setCustomerIds(customerDrawCashVOs.stream().map(CustomerDrawCashVO::getCustomerId).collect(Collectors.toList()));
        CustomerDetailStatusResponse response = customerDetailQueryProvider.getCustomerStatus(request).getContext();
        Map<String, CustomerStatusBaseVO> customerStatusMap
                = response.getList().stream().collect(Collectors.toMap(CustomerStatusBaseVO::getCustomerId, Function.identity(),(k1, k2) -> k1));

        //填充账号状态以及禁用理由
        customerDrawCashVOs.forEach(v -> {
            AccountStatus status = AccountStatus.DISABLE;
            CustomerStatusBaseVO customerStatusBaseVO = customerStatusMap.get(v.getCustomerId());
            if(Objects.nonNull(customerStatusBaseVO) && Objects.nonNull(customerStatusBaseVO.getCustomerStatus())){
                status = AccountStatus.valueOf(customerStatusBaseVO.getCustomerStatus().name());
                v.setForbidReason(customerStatusBaseVO.getForbidReason());
            }
            v.setAccountStatus(status);
            // 会员账号中间4位*号
            v.setCustomerAccount(v.getCustomerAccount()
                    .replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
        });
    }


    /**
     * 会员提现记录导出
     * @param dataRecords
     * @param outputStream
     * @param queryReq
     */
    private void exportCash(List<CustomerDrawCashVO> dataRecords, OutputStream outputStream, CustomerDrawCashExportRequest queryReq){
        ExcelHelper excelHelper = new ExcelHelper();
        Column[] columns = {
                new Column("提现单号", new SpelColumnRender<CustomerDrawCashVO>("drawCashNo")),
                new Column("申请时间", new SpelColumnRender<CustomerDrawCashVO>("applyTime")),
        };
        Column[] finishColumn = {
                new Column("完成时间", new SpelColumnRender<CustomerDrawCashVO>("finishTime")),
        };
        Column[] baseColumn = {
                new Column("会员名称", new SpelColumnRender<CustomerDrawCashVO>("customerName")),
                new Column("会员账号", (cell, object) -> {
                    CustomerDrawCashVO customerDrawCashVO = (CustomerDrawCashVO) object;
                    // 账号脱敏
                    cell.setCellValue(SensitiveUtils.handlerMobilePhone(customerDrawCashVO.getCustomerAccount()));
                }),
                new Column("账号状态", (cell, object) -> {
                    CustomerDrawCashVO cashVO = (CustomerDrawCashVO) object;
                    if(Objects.nonNull(cashVO.getAccountStatus())) {
                        cell.setCellValue(cashVO.getAccountStatus().getDesc());
                    }
                }),
                new Column("提现渠道", (cell, object) -> {
                    CustomerDrawCashVO cashVO = (CustomerDrawCashVO) object;
                    if(Objects.nonNull(cashVO.getDrawCashChannel())) {
                        cell.setCellValue(cashVO.getDrawCashChannel().getDesc());
                    }
                }),
                new Column("提现账户名称", new SpelColumnRender<CustomerDrawCashVO>("drawCashAccountName")),
                new Column("账户余额", new SpelColumnRender<CustomerDrawCashVO>("accountBalance")),
                new Column("本次提现", new SpelColumnRender<CustomerDrawCashVO>("drawCashSum")),
                new Column("提现备注", new SpelColumnRender<CustomerDrawCashVO>("drawCashRemark")),
        };
        Column[] failColumn = {
                new Column("提现失败原因", new SpelColumnRender<CustomerDrawCashVO>("drawCashFailedReason")),
        };

        Column[] auditColumn = {
                new Column("驳回原因", new SpelColumnRender<CustomerDrawCashVO>("rejectReason")),
        };

        Column[] newColumns={};
        String sheetName="会员提现导出";
        if(Objects.nonNull(queryReq.getCheckState())) {
            sheetName = queryReq.getCheckState().getDesc();
            if (queryReq.getCheckState().equals(CheckState.CHECK)) {
                newColumns =  ArrayUtils.addAll(columns,baseColumn);
            } else if (queryReq.getCheckState().equals(CheckState.FINISH)) {
                newColumns =  ArrayUtils.addAll(columns,finishColumn);
                newColumns =  ArrayUtils.addAll(newColumns,baseColumn);
            } else if(queryReq.getCheckState().equals(CheckState.FAIL)) {
                newColumns =  ArrayUtils.addAll(columns,baseColumn);
                newColumns =  ArrayUtils.addAll(newColumns,failColumn);
            } else if(queryReq.getCheckState().equals(CheckState.NOT_AUDIT)) {
                newColumns =  ArrayUtils.addAll(columns,baseColumn);
                newColumns =  ArrayUtils.addAll(newColumns,auditColumn);
            } else if(queryReq.getCheckState().equals(CheckState.CANCEL)) {
                newColumns =  ArrayUtils.addAll(columns,baseColumn);
            }
        }

        excelHelper.addSheet(
                sheetName,
                newColumns,
                dataRecords
        );
        excelHelper.write(outputStream);
    }
}
