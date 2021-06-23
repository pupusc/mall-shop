package com.wanmi.sbc.order.provider.impl.orderinvoice;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.order.api.provider.orderinvoice.OrderInvoiceQueryProvider;
import com.wanmi.sbc.order.api.request.orderinvoice.*;
import com.wanmi.sbc.order.api.response.orderinvoice.*;
import com.wanmi.sbc.order.bean.util.XssUtils;
import com.wanmi.sbc.order.bean.vo.OrderInvoiceVO;
import com.wanmi.sbc.order.orderinvoice.model.root.OrderInvoice;
import com.wanmi.sbc.order.orderinvoice.response.OrderInvoiceResponse;
import com.wanmi.sbc.order.orderinvoice.service.OrderInvoiceService;
import com.wanmi.sbc.order.payorder.model.root.PayOrder;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2018-12-03 10:44
 */
@Validated
@RestController
public class OrderInvoiceQueryController implements OrderInvoiceQueryProvider {

    @Autowired
    private OrderInvoiceService orderInvoiceService;


    /**
     * 生成开票
     * @param getByOrderNoRequest 订单开票查询参数 {@link OrderInvoiceGetByOrderNoRequest}
     * @return  开票信息 {@link OrderInvoiceGetByOrderNoResponse}
     */
    @Override
    public BaseResponse<OrderInvoiceGetByOrderNoResponse> getByOrderNo(@RequestBody @Valid OrderInvoiceGetByOrderNoRequest getByOrderNoRequest) {
        Optional<OrderInvoice> orderInvoice = orderInvoiceService.findByOrderNo(getByOrderNoRequest.getOrderNo());

        return BaseResponse.success(OrderInvoiceGetByOrderNoResponse.builder()
                .orderInvoiceVO(KsBeanUtil.convert( orderInvoice.isPresent()?orderInvoice.get():null,OrderInvoiceVO.class ))
                .build());
    }

    /**
     * 根据id查询开票信息
     * @param request
     * @return
     */
    @Override
    public BaseResponse<OrderInvoiceFindByOrderInvoiceIdResponse> findByOrderInvoiceId(OrderInvoiceFindByOrderInvoiceIdRequest request) {

        Optional<OrderInvoice> ret =  orderInvoiceService.findByOrderInvoiceId(request.getId());

        Optional<OrderInvoiceVO> retvo;

        if(ret.isPresent()){

            retvo =  Optional.ofNullable(KsBeanUtil.convert(ret.get(),OrderInvoiceVO.class));

        }else {

            retvo = Optional.ofNullable(null);
        }

        return BaseResponse.success(OrderInvoiceFindByOrderInvoiceIdResponse.builder().orderInvoiceVO(retvo).build());
    }


    @Override
    public BaseResponse<OrderInvoiceFindByOrderInvoiceIdAndDelFlagResponse> findByOrderInvoiceIdAndDelFlag(@RequestBody @Valid  OrderInvoiceFindByOrderInvoiceIdAndDelFlagRequest request) {

        Optional<OrderInvoice> ret =  orderInvoiceService.findByOrderInvoiceIdAndDelFlag(request.getId(),request.getFlag());

        OrderInvoiceVO  retvo = null;

        if(ret.isPresent()){

            retvo =   KsBeanUtil.convert(ret.get(),OrderInvoiceVO.class);

        }

        return BaseResponse.success(OrderInvoiceFindByOrderInvoiceIdAndDelFlagResponse.builder().orderInvoiceVO(retvo).build());
    }

    @Override
    public BaseResponse<OrderInvoiceFindAllResponse> findAll(@RequestBody @Valid  OrderInvoiceFindAllRequest request) {


        Specification<OrderInvoice>  arg = getWhereCriteria(request);

        Page<OrderInvoice>  ret =  orderInvoiceService.findAll(arg,request.getPageRequest());

        MicroServicePage<OrderInvoiceVO> result =

                KsBeanUtil.convertPage(ret,OrderInvoiceVO.class);

        return BaseResponse.success(OrderInvoiceFindAllResponse.builder().value(result).build());
    }


    public Specification<OrderInvoice> getWhereCriteria(@RequestBody @Valid  OrderInvoiceFindAllRequest request){
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();

            //联查
            Join<OrderInvoice, PayOrder> payOrderJoin = root.join("payOrder", JoinType.LEFT);

            //批量导出
            if(CollectionUtils.isNotEmpty(request.getOrderInvoiceIds())){
                predicates.add(root.get("orderInvoiceId").in(request.getOrderInvoiceIds()));
            }

            if(CollectionUtils.isNotEmpty(request.getCustomerIds())){
                predicates.add(root.get("customerId").in(request.getCustomerIds()));
            }

            if(CollectionUtils.isNotEmpty(request.getCompanyInfoIds())){
                predicates.add(root.get("companyInfoId").in(request.getCompanyInfoIds()));
            }

            if(Objects.nonNull(request.getOrderNo()) && StringUtils.isNotEmpty(request.getOrderNo().trim())){
                predicates.add(cbuild.like(root.get("orderNo"),buildLike(request.getOrderNo())));
            }
            if(request.getInvoiceState() != null){
                predicates.add(cbuild.equal(root.get("invoiceState"),request.getInvoiceState()));
            }

            if(Objects.nonNull(request.getStoreId())){
                predicates.add(cbuild.equal(root.get("storeId"),request.getStoreId()));
            }

            if(Objects.nonNull(request.getPayOrderStatus())){
                predicates.add(cbuild.equal(payOrderJoin.get("payOrderStatus"),request.getPayOrderStatus()));
            }

            if (!org.springframework.util.StringUtils.isEmpty(request.getBeginTime())) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                predicates.add(cbuild.greaterThanOrEqualTo(root.get("invoiceTime"), LocalDateTime.of(LocalDate
                        .parse(request.getBeginTime(), formatter), LocalTime.MIN)));
            }

            //收款
            if (!org.springframework.util.StringUtils.isEmpty(request.getEndTime())) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                predicates.add(cbuild.lessThan(root.get("invoiceTime"),
                        LocalDateTime.of(LocalDate
                                .parse(request.getEndTime(), formatter), LocalTime.MIN).plusDays(1)));
            }

            if (!org.springframework.util.CollectionUtils.isEmpty(request.getOrderInvoiceIds())) {
                predicates.add(root.get("orderInvoiceId").in(request.getOrderInvoiceIds()));
            }

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

    /**
     * 开票导出
     * @param exportRequest 开票导出查询参数 {@link OrderInvoiceExportRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @Override
    public BaseResponse export(@RequestBody @Valid OrderInvoiceExportRequest exportRequest) {
        orderInvoiceService.export(KsBeanUtil.convert(exportRequest.getOrderInvoiceExportRequestList(),
                OrderInvoiceResponse.class),exportRequest.getOutputStream());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 统计
     * @param countByStateRequest 统计参数 {@link OrderInvoiceCountByStateRequest}
     * @return 统计数量 {@link OrderInvoiceCountByStateResponse }
     */
    @Override
    public BaseResponse<OrderInvoiceCountByStateResponse> countByState(@RequestBody @Valid  OrderInvoiceCountByStateRequest countByStateRequest) {
        long count = orderInvoiceService.countInvoiceByState(countByStateRequest.getCompanyInfoId(), countByStateRequest.getStoreId(),
                countByStateRequest.getInvoiceState());
        return BaseResponse.success(OrderInvoiceCountByStateResponse.builder().count(count).build());
    }


}
