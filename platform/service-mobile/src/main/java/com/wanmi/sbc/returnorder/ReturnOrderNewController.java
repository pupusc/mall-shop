package com.wanmi.sbc.returnorder;
import com.google.common.collect.Lists;
import com.wanmi.sbc.common.enums.ChannelType;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.order.api.provider.trade.TradeQueryProvider;
import com.wanmi.sbc.order.api.request.trade.TradeGetByIdRequest;
import com.wanmi.sbc.order.api.response.trade.TradeGetByIdResponse;
import com.wanmi.sbc.order.bean.dto.CompanyDTO;
import com.wanmi.sbc.order.bean.enums.DeliverStatus;
import com.wanmi.sbc.order.bean.vo.SupplierVO;
import com.wanmi.sbc.order.bean.vo.TradeStateVO;
import com.wanmi.sbc.order.bean.vo.TradeVO;
import com.wanmi.sbc.returnorder.request.AddReturnOrderRequest;
import com.wanmi.sbc.returnorder.request.DistributionAddProviderRequest;
import com.wanmi.sbc.setting.api.provider.AuditQueryProvider;
import com.wanmi.sbc.setting.api.request.TradeConfigGetByTypeRequest;
import com.wanmi.sbc.setting.api.response.TradeConfigGetByTypeResponse;
import com.wanmi.sbc.setting.bean.enums.ConfigType;
import com.wanmi.sbc.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;


/**
 * Description:  新退单
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/12/23 1:15 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@RestController
@RequestMapping("/mobile/return")
public class ReturnOrderNewController {

    @Autowired
    private TradeQueryProvider tradeQueryProvider;

    @Autowired
    private AuditQueryProvider auditQueryProvider;

    @Autowired
    private CommonUtil commonUtil;

    /**
     * 新增退单
     * @param addReturnOrderRequest
     * @return
     */
    public BaseResponse create(@Validated @RequestBody AddReturnOrderRequest addReturnOrderRequest) {
        TradeGetByIdRequest tradeGetByIdRequest = TradeGetByIdRequest.builder().tid(addReturnOrderRequest.getOrderId()).build();
        BaseResponse<TradeGetByIdResponse> tradeGetByIdResponseBaseResponse = tradeQueryProvider.getById(tradeGetByIdRequest);
        TradeGetByIdResponse context = tradeGetByIdResponseBaseResponse.getContext();
        if (context == null || context.getTradeVO() == null || StringUtils.isEmpty(context.getTradeVO().getId()) || CollectionUtils.isEmpty(context.getProviderTradeVOs())) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        //查看订单信息和用户信息是否一致
        if (!context.getTradeVO().getBuyer().getId().equals(commonUtil.getOperatorId())) {
            throw new SbcRuntimeException("K-050204");
        }
        //如果部分发货，需要联系客服
        //验证订单信息
        TradeStateVO tradeState = context.getTradeVO().getTradeState();
        if (tradeState == null || tradeState.getDeliverStatus() == null){
            throw new SbcRuntimeException("K-050451");
        }
        //如果为部分发货，则提示联系客服，当前系统不支持部分发货退款退货
        if (Objects.equals(tradeState.getDeliverStatus(), DeliverStatus.PART_SHIPPED)) {
            throw new SbcRuntimeException("K-050452");
        }
        //获取商家信息
        SupplierVO supplier = context.getTradeVO().getSupplier();
        if (supplier == null) {
            throw new SbcRuntimeException("K-110102");
        }



        //公司 商家店铺信息
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setCompanyInfoId(supplier.getSupplierId());
        companyDTO.setSupplierName(supplier.getSupplierName());
        companyDTO.setCompanyCode(supplier.getSupplierCode());
//        companyDTO.setAccountName(); 商家账号，
        companyDTO.setStoreId(supplier.getStoreId());
        companyDTO.setStoreName(supplier.getStoreName());
        companyDTO.setCompanyType(supplier.getIsSelf() ? BoolFlag.NO : BoolFlag.YES);

        //分销信息
        DistributionAddProviderRequest distributionAddProviderRequest = new DistributionAddProviderRequest();
        distributionAddProviderRequest.setChannelType(context.getTradeVO().getChannelType());
        distributionAddProviderRequest.setDistributorId(context.getTradeVO().getDistributorId());
        distributionAddProviderRequest.setDistributorName(context.getTradeVO().getDistributorName());
        distributionAddProviderRequest.setDistributeItems(context.getTradeVO().getDistributeItems());



        //已经发货的情况需要审核验证
//        if (Objects.nonNull(trade.getTradeState().getDeliverStatus()) && (trade.getTradeState().getDeliverStatus() == DeliverStatus.SHIPPED || trade.getTradeState().getDeliverStatus() == DeliverStatus.PART_SHIPPED)) {
//            TradeConfigGetByTypeRequest request = new TradeConfigGetByTypeRequest();
//            request.setConfigType(ConfigType.ORDER_SETTING_APPLY_REFUND);
//            TradeConfigGetByTypeResponse config = auditQueryProvider.getTradeConfigByType(request).getContext();
//            if (config.getStatus() == 0) {
//                throw new SbcRuntimeException("K-050208");
//            }
//            JSONObject content = JSON.parseObject(config.getContext());
//            Integer day = content.getObject("day", Integer.class);
//
//            if (!trade.getCycleBuyFlag()) {
//                if (Objects.isNull(trade.getTradeState().getEndTime())) {
//                    throw new SbcRuntimeException("K-050002");
//                }
//                if (trade.getTradeState().getEndTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() < LocalDateTime.now().minusDays(day).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()) {
//                    throw new SbcRuntimeException("K-050208");
//                }
//            }
//        }
        return BaseResponse.SUCCESSFUL();
    }
}
