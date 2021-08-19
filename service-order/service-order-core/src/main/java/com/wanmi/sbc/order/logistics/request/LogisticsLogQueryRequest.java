package com.wanmi.sbc.order.logistics.request;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.query.Criteria;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>物流信息查询参数结构</p>
 * Created by dyt on 2020-04-17.
 */
@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LogisticsLogQueryRequest extends BaseQueryRequest {

    private static final long serialVersionUID = 149142593703964072L;

    /**
     * id
     */
    private String id;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 快递单号
     */
    private String logisticNo;

    /**
     * 店铺id
     */
    private Long storeId;

    /**
     * 客户id
     */
    private String customerId;

    /**
     * 是否结束
     */
    private Boolean endFlag;

    /**
     * 是否签收标记
     */
    private String isCheck;

    /**
     * 本地物流公司标准编码
     */
    private String comOld;

    /**
     * 快递纠正新编码
     */
    private String comNew;

    /**
     * 是否有明细内容
     */
    private Boolean hasDetailsFlag;

    /**
     * 会员显示，未签收的 or 已签收未确认货，<3天
     */
    private Boolean customerShowLimit;

    /**
     * 封装公共条件
     *
     * @return
     */
    public Criteria getCriteria() {
        List<Criteria> criteriaList = new ArrayList<>();
        if (StringUtils.isNotBlank(id)) {
            criteriaList.add(Criteria.where("id").is(id));
        }
        if (StringUtils.isNotBlank(orderNo)) {
            criteriaList.add(Criteria.where("orderNo").is(orderNo));
        }
        if (StringUtils.isNotBlank(logisticNo)) {
            criteriaList.add(Criteria.where("logisticNo").is(logisticNo));
        }
        if (StringUtils.isNotBlank(customerId)) {
            criteriaList.add(Criteria.where("customerId").is(customerId));
        }
        if (Objects.nonNull(storeId)) {
            criteriaList.add(Criteria.where("storeId").is(storeId));
        }
        if (Objects.nonNull(endFlag)) {
            criteriaList.add(Criteria.where("endFlag").is(endFlag));
        }
        if (Objects.nonNull(isCheck)) {
            criteriaList.add(Criteria.where("isCheck").is(isCheck));
        }
        if (Objects.nonNull(comOld)) {
            criteriaList.add(Criteria.where("comOld").is(comOld));
        }
        if (Objects.nonNull(comNew)) {
            criteriaList.add(Criteria.where("comNew").is(comNew));
        }
        if(Boolean.TRUE.equals(hasDetailsFlag)){
            criteriaList.add(Criteria.where("logisticsLogDetails").exists(true));
        }
        //会员展示，当未确认收货且已签到的物流信息只能显示昨天的物流信息
        if(Boolean.TRUE.equals(customerShowLimit)){
            Criteria checkCriteria = new Criteria().orOperator(
                    new Criteria().andOperator(Criteria.where("isCheck").is("1"), Criteria.where("checkTime").gt(LocalDate.now().minusDays(1).atTime(LocalTime.MIN))),
                    Criteria.where("isCheck").is("0"));
            List<Criteria> customerShowCriteria = new ArrayList<>();
            //未确认收款状态
            customerShowCriteria.add(new Criteria().orOperator(Criteria.where("endFlag").is(false), Criteria.where("endFlag").exists(false)));
            customerShowCriteria.add(checkCriteria);
            criteriaList.add(new Criteria().andOperator(customerShowCriteria.toArray(new Criteria[customerShowCriteria.size()])));
        }
        return new Criteria().andOperator(criteriaList.toArray(new Criteria[criteriaList.size()]));
    }
}
