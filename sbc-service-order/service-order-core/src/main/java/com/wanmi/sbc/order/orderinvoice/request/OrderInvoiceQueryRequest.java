package com.wanmi.sbc.order.orderinvoice.request;


import com.wanmi.sbc.account.bean.enums.InvoiceState;
import com.wanmi.sbc.account.bean.enums.PayOrderStatus;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.order.orderinvoice.model.root.OrderInvoice;
import com.wanmi.sbc.order.payorder.model.root.PayOrder;
import com.wanmi.sbc.order.util.XssUtils;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 订单开票查询条件
 * Created by CHENLI on 2017/5/5.
 */
@Data
public class OrderInvoiceQueryRequest extends BaseQueryRequest {

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 付款状态0:已付款 1.未付款 2.待确认
     */
    private PayOrderStatus payOrderStatus;

    /**
     * 开票状态 0待开票 1 已开票
     */
    private InvoiceState invoiceState;

    /**
     * 订单开票IDs
     */
    private List<String> orderInvoiceIds;

    /**
     * 查询退款开始时间，精确到天
     */
    private String beginTime;

    /**
     * 查询退款结束时间，精确到天
     */
    private String endTime;

    private String token;

    /**
     * 商家id
     */
    private Long companyInfoId;

    /**
     * 店铺id
     */
    private Long storeId;

    /**
     * 商家名称
     */
    private String supplierName;

    /**
     * 负责业务员
     */
    private String employeeId;

    /**
     * 批量会员Ids
     */
    private List<String> customerIds;

    /**
     * 商家id
     */
    private List<Long> companyInfoIds;

    /**
     * 封装公共条件
     * @return
     */
    public Specification<OrderInvoice> getWhereCriteria(){
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();

            //联查
//            Join<OrderInvoice,CustomerDetail> customerDetailJoin = root.join("customerDetail", JoinType.LEFT);
            Join<OrderInvoice,PayOrder> payOrderJoin = root.join("payOrder",JoinType.LEFT);
//            Join<OrderInvoice, CompanyInfo> companyInfoJoin = root.join("companyInfo", JoinType.LEFT);

            //批量导出
            if(CollectionUtils.isNotEmpty(orderInvoiceIds)){
                predicates.add(root.get("orderInvoiceId").in(orderInvoiceIds));
            }

//            if(Objects.nonNull(customerName) && StringUtils.isNoneBlank(customerName.trim())){
//                predicates.add(cbuild.like(customerDetailJoin.get("customerName"),buildLike(customerName)));
//            }

            //商家名称
//            if (Objects.nonNull(supplierName) && StringUtils.isNoneBlank( supplierName.trim())) {
//                predicates.add(cbuild.like(companyInfoJoin.get("supplierName"),buildLike(supplierName)));
//            }

            if(CollectionUtils.isNotEmpty(customerIds)){
                predicates.add(root.get("customerId").in(customerIds));
            }

            if(CollectionUtils.isNotEmpty(companyInfoIds)){
                predicates.add(root.get("companyInfoId").in(companyInfoIds));
            }

            if(Objects.nonNull(orderNo) && StringUtils.isNotEmpty(orderNo.trim())){
                predicates.add(cbuild.like(root.get("orderNo"),buildLike(orderNo)));
            }
            if(invoiceState != null){
                predicates.add(cbuild.equal(root.get("invoiceState"),invoiceState));
            }

            if(Objects.nonNull(companyInfoId)){
                predicates.add(cbuild.equal(root.get("companyInfoId"),companyInfoId));
            }

            if(Objects.nonNull(storeId)){
                predicates.add(cbuild.equal(root.get("storeId"),storeId));
            }

            if(Objects.nonNull(payOrderStatus)){
                predicates.add(cbuild.equal(payOrderJoin.get("payOrderStatus"),payOrderStatus));
            }

            if (!org.springframework.util.StringUtils.isEmpty(beginTime)) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("invoiceTime"), LocalDateTime.of(LocalDate
                        .parse(beginTime, formatter), LocalTime.MIN)));
            }

            //收款
            if (!org.springframework.util.StringUtils.isEmpty(endTime)) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                predicates.add(cbuild.lessThan(root.get("invoiceTime"),
                        LocalDateTime.of(LocalDate
                                .parse(endTime, formatter), LocalTime.MIN).plusDays(1)));
            }

            if (!org.springframework.util.CollectionUtils.isEmpty(this.getOrderInvoiceIds())) {
                predicates.add(root.get("orderInvoiceId").in(this.getOrderInvoiceIds()));
            }

            //业务员
//            if(StringUtils.isNotBlank(employeeId)){
//                predicates.add(cbuild.equal(customerDetailJoin.get("employeeId"), employeeId));
//            }

            //删除标记
            predicates.add(cbuild.equal(root.get("delFlag"), DeleteFlag.NO));
            predicates.add(cbuild.equal(payOrderJoin.get("delFlag"), DeleteFlag.NO));

            cquery.orderBy(cbuild.desc(root.get("createTime")));
            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }

    private static String buildLike(String field) {
        StringBuilder stringBuilder = new StringBuilder();
        return stringBuilder.append("%").append(XssUtils.replaceLikeWildcard(field)).append("%").toString();
    }
}
