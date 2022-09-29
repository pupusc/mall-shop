package com.wanmi.sbc.order.provider.impl.ztemp;

import com.alibaba.fastjson.JSON;
import com.mongodb.client.result.UpdateResult;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
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
    private AtomicBoolean inHand = new AtomicBoolean(false);
    private List<String> ids = new ArrayList<>();
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
            this.ids.clear();
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
    public String start(String[] ids, @NotNull Integer selectVersion, @NotNull Integer updateVersion, @NotNull Integer pageSize, @NotNull Boolean errorStop) {
        if (!inHand.compareAndSet(false, true)) {
            return "同步任务正在执行中，本次调用无效";
        }

        log.info("开始同步商城退单数据到电商中台, selectVersion={}, updateVersion={}, pageSize={}", selectVersion, updateVersion, pageSize);
        this.selectVersion = selectVersion;
        this.updateVersion = updateVersion;

        if (Objects.nonNull(ids)) {
            this.ids.addAll(Arrays.asList(ids));
        }

        int pageNo = 0;
        long start = System.currentTimeMillis();

        List<ReturnOrder> returnOrders;
        do {
            //查询
            Query query = new Query().with(Sort.by(Sort.Direction.ASC, "createTime")).skip((pageNo++) * pageSize).limit(pageSize);
            returnOrders = mongoTemplate.find(query, ReturnOrder.class);
            //导出
            for (ReturnOrder item : returnOrders) {
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
        //指定ids
        if (!this.ids.isEmpty() && !this.ids.contains(returnOrder.getId())) {
            this.countIgnore ++;
            return;
        }
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
        //同步电商中台
        try {
            if (!sendData(returnOrder)) {
                this.countFailed ++;
                return;
            }
        } catch (Exception e) {
            this.countError ++;
            throw new RuntimeException(e);
        }
        this.countExport ++;
        log.info("成功同步到电商中台, 主键:{}, tid:{}", returnOrder.getId(), returnOrder.getTid());

        //更新本地版本
        UpdateResult updateResult = mongoTemplate.updateFirst(
                new Query(Criteria.where("_id").is(returnOrder.getId())),
                new Update().set(versionField, this.updateVersion),
                ReturnOrder.class);

        if (updateResult.getModifiedCount() != 1) {
            log.warn("更新退单的版本信息影响数量错误，主键:{}, 版本:{}, 更新数量:{}", returnOrder.getId(), this.updateVersion, updateResult.getModifiedCount());
            throw new RuntimeException("更新退单的版本信息错误");
        }
    }

    private boolean checkVersion(Integer sVersion) {
        if (sVersion == null) {
            return true;
        }
        if (selectVersion < 0) {
            return sVersion < this.updateVersion;
        }
        return selectVersion == sVersion;
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
