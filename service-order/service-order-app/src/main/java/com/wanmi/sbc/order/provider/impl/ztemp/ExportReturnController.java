package com.wanmi.sbc.order.provider.impl.ztemp;

import com.alibaba.fastjson.JSON;
import com.mongodb.client.result.UpdateResult;
import com.soybean.mall.order.dszt.TransferService;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.erp.api.provider.ShopCenterSaleAfterProvider;
import com.wanmi.sbc.erp.api.req.SaleAfterCreateNewReq;
import com.wanmi.sbc.order.api.enums.ThirdInvokeCategoryEnum;
import com.wanmi.sbc.order.api.enums.ThirdInvokePublishStatusEnum;
import com.wanmi.sbc.order.bean.enums.ReturnFlowState;
import com.wanmi.sbc.order.bean.enums.ReturnReason;
import com.wanmi.sbc.order.bean.enums.ReturnType;
import com.wanmi.sbc.order.provider.impl.ztemp.vo.ImportMallRefundParamVO;
import com.wanmi.sbc.order.provider.impl.ztemp.vo.ImportMallRefundParamVO$Detail;
import com.wanmi.sbc.order.provider.impl.ztemp.vo.ImportMallRefundParamVO$Item;
import com.wanmi.sbc.order.provider.impl.ztemp.vo.ImportMallRefundParamVO$Order;
import com.wanmi.sbc.order.provider.impl.ztemp.vo.ImportMallRefundParamVO$PostFee;
import com.wanmi.sbc.order.provider.impl.ztemp.vo.ImportMallRefundParamVO$Refund;
import com.wanmi.sbc.order.returnorder.model.entity.ReturnItem;
import com.wanmi.sbc.order.returnorder.model.root.ReturnOrder;
import com.wanmi.sbc.order.third.ThirdInvokeService;
import com.wanmi.sbc.order.third.model.ThirdInvokeDTO;
import com.wanmi.sbc.order.trade.model.root.Trade;
import com.wanmi.sbc.order.trade.repository.TradeRepository;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * @desc 导出售后推单到电商中台
 * @author Liang Jun
 * @date 2022-09-11 10:39:00
 */
@Validated
@Slf4j
@RestController
@RequestMapping("export/return")
public class ExportReturnController {
    private static final String versionField = "sVersion";
    @Autowired
    private DSZTService dsztService;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private TransferService transferService;
    @Autowired
    private ShopCenterSaleAfterProvider shopCenterSaleAfterProvider;
    @Autowired
    private ThirdInvokeService thirdInvokeService;
    @Autowired
    private TradeRepository tradeRepository;

    private AtomicBoolean inHand = new AtomicBoolean(false);
    private int selectVersion;
    private int updateVersion;

    private int countTotal;
    private int countExport;
    private int countIgnore;
    private int countError;
    private int countFailed;

    @GetMapping("stop")
    public Boolean stop() {
        boolean result = inHand.get() ? inHand.compareAndSet(true, false) : true;
        if (result) {
            this.countTotal = 0;
            this.countExport = 0;
            this.countIgnore = 0;
            this.countError = 0;
            this.countFailed = 0;
        }
        return result;
    }

    /**
     * 开始同步
     * @param selectVersion 同步的版本
     * @param updateVersion 更新的版本
     * @param pageSize 分页数量
     * @param errorStop 错误停止同步
     */
    @Valid
    @GetMapping("start")
    public String start(String id, Integer selectVersion, Integer updateVersion, Integer pageSize, Boolean errorStop,
                        @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date bgnTime, @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date endTime) {
        if (!inHand.compareAndSet(false, true)) {
            return "同步任务正在执行中，本次调用无效";
        }
        if (StringUtils.isBlank(id) && (bgnTime == null || endTime == null)) {
            stop();
            return "必须指定开始和结束时间";
        }

        if (selectVersion == null) {
            selectVersion = -1;
        }
        if (updateVersion == null) {
            updateVersion = 1;
        }
        if (pageSize == null) {
            pageSize = 1000;
        }
        if (errorStop == null) {
            errorStop = true;
        }

        log.info("开始同步商城退单数据到电商中台, selectVersion={}, updateVersion={}, pageSize={}, bgnTime, endTime", selectVersion, updateVersion, pageSize, bgnTime, endTime);
        this.selectVersion = selectVersion;
        this.updateVersion = updateVersion;

        int pageNo = 0;
        long start = System.currentTimeMillis();

        List<ReturnOrder> returnOrders;
        do {
            //指定id
            if (StringUtil.isNotBlank(id)) {
                Query query = new Query(Criteria.where("_id").is(id));
                returnOrders = mongoTemplate.find((query), ReturnOrder.class);
            } else {
                //查询
                Query query = new Query().with(Sort.by(Sort.Direction.ASC, "createTime")).skip((pageNo++) * pageSize).limit(pageSize);
                if (bgnTime != null && endTime != null) {
                    query.addCriteria(Criteria.where("createTime").gte(bgnTime).lt(endTime));
                }
                returnOrders = mongoTemplate.find(query, ReturnOrder.class);
            }

            //导出
            for (ReturnOrder item : returnOrders) {
                if (!inHand.get()) {
                    stop();
                    log.info("执行指令结束同步任务");
                    return "执行指令结束同步任务";
                }
                try {
                    export(item);
                } catch (Exception e) {
                    log.warn("同步数据发生异常", e);
                    if (errorStop) {
                        printResult("由于异常原因导致同步任务提前结束", start);
                        return "error";
                    }
                }
            }
        } while (returnOrders.size() >= pageSize);
        printResult("完成同步商城退单到电商中台", start);

        stop();
        return "success";
    }

    private void printResult(String msg, long start) {
        log.info("{}，耗时：{}ms，总计：{}, 成功：{}，失败：{}，忽略：{}，错误：{}，", msg, System.currentTimeMillis()-start, countTotal, countExport, countFailed, countIgnore, countError);
    }

    private List<ReturnFlowState> createStatus = Arrays.asList(
//            ReturnFlowState.INIT,
//            ReturnFlowState.AUDIT
    );
    private List<ReturnFlowState> finishStatus = Arrays.asList(
//            ReturnFlowState.REFUNDED,
//            ReturnFlowState.REJECT_REFUND,
//            ReturnFlowState.VOID,
//            ReturnFlowState.REFUND_FAILED,
            ReturnFlowState.COMPLETED
    );

    /**
     * 导出数据
     */
    private void export(ReturnOrder returnOrder) {
        if (Objects.isNull(returnOrder)) {
            return;
        }
        this.countTotal ++;
        //验证数据
        if (!checkVersion(returnOrder.getSVersion())) {
            this.countIgnore ++;
            return;
        }
        //初始状态
        if (!createStatus.contains(returnOrder.getReturnFlowState()) && !finishStatus.contains(returnOrder.getReturnFlowState())) {
            log.info("不支持的退单状态,跳过不做处理,id={},state={}", returnOrder.getId(), returnOrder.getReturnFlowState().getStateId());
            this.countIgnore ++;
            return;

        }
        //排除有赞订单
        Trade trade = tradeRepository.findById(returnOrder.getTid()).orElse(null);
        if (trade == null || trade.getYzTid() != null || Boolean.TRUE.equals(trade.getYzOrderFlag())) {
            log.info("有赞订单跳过不做处理, id={}", returnOrder.getId());
            this.countIgnore ++;
            return;
        }

        //同步电商中台
        try {
            if (!syncData(returnOrder)) {
                log.info("历史退单同步到电商中台,同步失败:id={}", returnOrder.getId());
                this.countFailed ++;
                return;
            }
        } catch (Exception e) {
            log.info("历史退单同步到电商中台,同步错误:id={}", returnOrder.getId());
            this.countError ++;
            throw new RuntimeException(e);
        }
        this.countExport ++;
        log.info("历史退单同步到电商中台,同步成功:id={}", returnOrder.getId());

        //更新本地版本
        UpdateResult updateResult = mongoTemplate.updateFirst(
                new Query(Criteria.where("_id").is(returnOrder.getId())),
                new Update().set(versionField, this.updateVersion),
                ReturnOrder.class);

//        if (updateResult.getModifiedCount() != 1) {
//            log.warn("更新退单的版本信息影响数量错误，主键:{}, 版本:{}, 更新数量:{}", returnOrder.getId(), this.updateVersion, updateResult.getModifiedCount());
//            throw new RuntimeException("更新退单的版本信息错误");
//        }
    }

    private boolean checkVersion(Integer sVersion) {
        if (selectVersion < 0 || sVersion == null) {
            return true;
        }
        return selectVersion == sVersion;
    }

    public boolean syncData(ReturnOrder returnOrder) {
        //创建售后订单
        ThirdInvokeDTO thirdInvokeDTO = thirdInvokeService.add(returnOrder.getId(), ThirdInvokeCategoryEnum.INVOKE_RETURN_ORDER);
        if (Objects.equals(thirdInvokeDTO.getPushStatus(), ThirdInvokePublishStatusEnum.SUCCESS.getCode())) {
            log.info("ProviderTradeService singlePushOrder businessId:{} 已经推送成功，重复提送", thirdInvokeDTO.getBusinessId());
            return true;
        }
        //调用推送接口
        SaleAfterCreateNewReq saleAfterCreateNewReq = transferService.changeSaleAfterCreateReq4Sync(returnOrder);
        if (saleAfterCreateNewReq == null) {
            throw new SbcRuntimeException(CommonErrorCode.FAILED, "创建退单的同步参数失败");
        }
        //saleAfterCreateNewReq.setSaleAfterCreateEnum(5);
        saleAfterCreateNewReq.getSaleAfterOrderBO().setImportFlag(1); //导入标记
        BaseResponse<Long> saleAfter = shopCenterSaleAfterProvider.createSaleAfter(saleAfterCreateNewReq);
        //推送成功
        if (Objects.equals(saleAfter.getCode(), CommonErrorCode.SUCCESSFUL)) {
            thirdInvokeService.update(thirdInvokeDTO.getId(), saleAfter.getContext().toString(), ThirdInvokePublishStatusEnum.SUCCESS, "SUCCESS");
            return true;
        }
        thirdInvokeService.update(thirdInvokeDTO.getId(), saleAfter.getContext().toString(), ThirdInvokePublishStatusEnum.FAIL, saleAfter.getMessage());
        return false;
    }

    private boolean sendData(ReturnOrder returnOrder) throws Exception {
        ImportMallRefundParamVO paramVO = new ImportMallRefundParamVO();
        paramVO.setSaleAfterCreateEnum(finishStatus.contains(returnOrder.getReturnFlowState()) ? "MALL_HISTORY" : "MALL");
        paramVO.setMallOrderId(returnOrder.getTid());
        //退款主单
        ImportMallRefundParamVO$Order orderBO = new ImportMallRefundParamVO$Order();
        orderBO.setRefundType(parseRefundType(returnOrder));
        orderBO.setPlatformRefundId(returnOrder.getId());
        orderBO.setApplyTime(returnOrder.getCreateTime());
        orderBO.setCloseTime(returnOrder.getFinishTime());
        orderBO.setMemo(returnOrder.getDescription());
        paramVO.setSaleAfterOrderBO(orderBO);
        paramVO.getRefundTypeList().add(parseRefundType(returnOrder));
        //退运费
        if (ReturnReason.PRICE_DELIVERY.equals(returnOrder.getReturnReason())) {
            return sendData4PostFee(paramVO, returnOrder);
        }
        return sendData4buyFee(paramVO, returnOrder);
    }

    private boolean sendData4PostFee(ImportMallRefundParamVO paramVO, ReturnOrder returnOrder) {
        int payType = 1; //1现金;2知豆;3积分
        int postFeeAmount = yuan2fen(returnOrder.getReturnPrice().getApplyPrice());

        ImportMallRefundParamVO$Detail detail = new ImportMallRefundParamVO$Detail();
        detail.setAmount(postFeeAmount);
        detail.setPayType(payType);
        paramVO.setSaleAfterPostFee(new ImportMallRefundParamVO$PostFee(Arrays.asList(detail)));
        //退款单
        if (finishStatus.contains(returnOrder.getReturnFlowState())) {
            ImportMallRefundParamVO$Refund refund = new ImportMallRefundParamVO$Refund();
            refund.setRefundNo(returnOrder.getId());
            refund.setAmount(postFeeAmount);
            refund.setPayType(String.valueOf(payType));
            refund.setRefundTime(returnOrder.getFinishTime());
            paramVO.setSaleAfterRefundBOList(Arrays.asList(refund));
        }
        return invoke(paramVO);
    }

    /**
     * 1现金;2知豆;3积分
     */
    private boolean sendData4buyFee(ImportMallRefundParamVO paramVO, ReturnOrder returnOrder) {
        paramVO.setSaleAfterItemBOList(new ArrayList<>());
        paramVO.setSaleAfterRefundBOList(new ArrayList<>());

        Integer refundType = parseRefundType(returnOrder);
        String returnReason = returnOrder.getReturnReason() == null ? null : returnOrder.getReturnReason().getDesc();
        //退款子单
        for (ReturnItem item : returnOrder.getReturnItems()) {
            ImportMallRefundParamVO$Item orderItem = new ImportMallRefundParamVO$Item();
            orderItem.setMallSkuId(item.getSkuId());
            //orderItem.setObjectId();
            orderItem.setObjectType("ORDER_ITEM");
            orderItem.setRefundType(refundType);
            orderItem.setRefundNum(item.getNum());
            orderItem.setSaleAfterRefundDetailBOList(new ArrayList<>());
            //现金
            Integer cashAmount = yuan2fen(item.getApplyRealPrice() != null ? item.getApplyRealPrice() : item.getSplitPrice());
            if (cashAmount != null && cashAmount > 0) {
                ImportMallRefundParamVO$Detail detail = new ImportMallRefundParamVO$Detail();
                detail.setPayType(1);
                detail.setAmount(cashAmount);
                detail.setRefundReason(returnReason);
                orderItem.getSaleAfterRefundDetailBOList().add(detail);

                ImportMallRefundParamVO$Refund refund = new ImportMallRefundParamVO$Refund();
                refund.setRefundNo(returnOrder.getId());
                refund.setPayType("1");
                refund.setAmount(cashAmount);
                refund.setRefundTime(returnOrder.getFinishTime());
                paramVO.getSaleAfterRefundBOList().add(refund);
            }
            //知豆
            Long knowAmount = item.getApplyKnowledge() != null ? item.getApplyKnowledge() : item.getSplitKnowledge();
            if (knowAmount != null && knowAmount > 0) {
                ImportMallRefundParamVO$Detail detail = new ImportMallRefundParamVO$Detail();
                detail.setPayType(2);
                detail.setAmount(knowAmount.intValue());
                detail.setRefundReason(returnReason);
                orderItem.getSaleAfterRefundDetailBOList().add(detail);

                ImportMallRefundParamVO$Refund refund = new ImportMallRefundParamVO$Refund();
                refund.setRefundNo(returnOrder.getId());
                refund.setPayType("2");
                refund.setAmount(knowAmount.intValue());
                refund.setRefundTime(returnOrder.getFinishTime());
                paramVO.getSaleAfterRefundBOList().add(refund);
            }
            //积分
            Long pointAmount = item.getApplyPoint() != null ? item.getApplyPoint() : item.getSplitPoint();
            if (pointAmount != null && pointAmount > 0) {
                ImportMallRefundParamVO$Detail detail = new ImportMallRefundParamVO$Detail();
                detail.setPayType(3);
                detail.setAmount(pointAmount.intValue());
                detail.setRefundReason(returnReason);
                orderItem.getSaleAfterRefundDetailBOList().add(detail);

                ImportMallRefundParamVO$Refund refund = new ImportMallRefundParamVO$Refund();
                refund.setRefundNo(returnOrder.getId());
                refund.setPayType("3");
                refund.setAmount(pointAmount.intValue());
                refund.setRefundTime(returnOrder.getFinishTime());
                paramVO.getSaleAfterRefundBOList().add(refund);
            }
            orderItem.setRefundFee(orderItem.getSaleAfterRefundDetailBOList().stream().mapToInt(i->i.getAmount()).sum());
            paramVO.getSaleAfterItemBOList().add(orderItem);
        }
        return invoke(paramVO);
    }

    /**
     * 金额元转分
     */
    private Integer yuan2fen(BigDecimal cashAmount) {
        if (cashAmount == null) {
            return null;
        }
        return cashAmount.multiply(BigDecimal.valueOf(100)).intValue();
    }

    /**
     * 售后类型：1退货退款 2仅退款 3换货 4补偿 5主订单退款 6赠品
     */
    private Integer parseRefundType(ReturnOrder returnOrder) {
        if (ReturnReason.PRICE_DELIVERY.equals(returnOrder.getReturnReason())) {
            return 5;
        }
        if (ReturnType.RETURN.equals(returnOrder.getReturnType())) {
            return  1;
        }
        if (ReturnType.REFUND.equals(returnOrder.getReturnType())) {
            return  2;
        }
        log.warn("错误的退款类型, id={}, returnType={}", returnOrder.getId(), returnOrder.getReturnType());
        throw new RuntimeException("不支持的退款类型");
    }

    private static final String exportUrl = "/platform-router/import/mallRefund";
    /**
     * HTTP调用
     */
    private boolean invoke(ImportMallRefundParamVO paramVO) {
        Map<String, Object> response = dsztService.doRequest(exportUrl, JSON.toJSONString(paramVO), Map.class);
        if (response == null || response.get("status") == null || !response.get("status").equals("0000")) {
            log.warn("调用数据同步接口结果失败, code={}, msg={}", response.get("status"), response.get("msg"));
            //throw new RuntimeException("数据导出失败");
            return false;
        }
        return true;
    }
}
