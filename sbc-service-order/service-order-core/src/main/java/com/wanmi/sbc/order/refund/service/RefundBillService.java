package com.wanmi.sbc.order.refund.service;

import io.seata.spring.annotation.GlobalTransactional;
import com.google.common.collect.Lists;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.GeneratorService;
import com.wanmi.sbc.order.api.request.refund.RefundBillAddRequest;
import com.wanmi.sbc.order.refund.model.root.RefundBill;
import com.wanmi.sbc.order.refund.repository.RefundBillRepository;
import com.wanmi.sbc.order.returnorder.service.ReturnOrderService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * 退款单流水
 * Created by zhangjin on 2017/4/21.
 */
@Service
@Transactional(readOnly = true, timeout = 10)
public class RefundBillService {

    @Autowired
    private RefundBillRepository refundBillRepository;

    @Autowired
    private RefundOrderService refundOrderService;

    @Autowired
    private GeneratorService generatorService;

    @Autowired
    private ReturnOrderService returnOrderService;

    /**
     * 新增流水单
     *
     * @param refundBillRequest refundBillRequest
     * @return Optional<RefundBill>
     */
    @Transactional
    @GlobalTransactional
    public void save(RefundBillAddRequest refundBillRequest) {
        RefundBill bill = new RefundBill();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        bill.setCreateTime(LocalDateTime.of(LocalDate.parse(refundBillRequest.getCreateTime(), formatter),
                LocalTime.MIN));
        bill.setDelFlag(DeleteFlag.NO);
        BeanUtils.copyProperties(refundBillRequest, bill);
        bill.setOfflineAccountId(refundBillRequest.getOfflineAccountId());
        bill.setRefundBillCode(generatorService.generateRF());
        refundOrderService.batchConfirm(Lists.newArrayList(refundBillRequest.getRefundId()));
        refundBillRepository.save(bill);

        refundOrderService.findById(refundBillRequest.getRefundId()).ifPresent(refundOrder ->
                returnOrderService.refund(refundOrder.getReturnOrderCode(), refundBillRequest.getOperator(),
                        refundOrder.getRefundBill().getActualReturnPrice()));
    }

    /**
     * 新增流水单
     *
     * @param refundBill refundBill
     * @return Optional<RefundBill>
     */
    @Transactional
    public Optional<RefundBill> save(RefundBill refundBill) {
        refundBill.setDelFlag(DeleteFlag.NO);
        refundBill.setRefundBillCode(generatorService.generateRF());

        if (StringUtils.isNotBlank(refundBill.getRefundComment())) {
            // refundOrder的refuseReason字段实际是备注，这里更新一下
            refundOrderService.updateRefundOrderReason(refundBill.getRefundId(), refundBill.getRefundComment());
        }

        refundOrderService.batchConfirm(Lists.newArrayList(refundBill.getRefundId()));
        return Optional.ofNullable(refundBillRepository.save(refundBill));
    }

    @Transactional
    public Optional<RefundBill> saveAndFlush(RefundBill refundBill) {
        return Optional.ofNullable(refundBillRepository.saveAndFlush(refundBill));
    }

    /**
     * 根据退款单删除流水
     *
     * @param refundId refundId
     * @return rows
     */
    @Transactional
    public int deleteByRefundId(String refundId) {
        return refundBillRepository.deleteBillByRefundId(refundId);
    }
}
