package com.wanmi.sbc.order.api.request.orderinvoice;

import com.wanmi.sbc.account.bean.enums.InvoiceState;
import com.wanmi.sbc.account.bean.enums.PayOrderStatus;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.order.bean.util.XssUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class OrderInvoiceFindAllRequest extends BaseQueryRequest {

    /**
     * 客户名称
     */
    @ApiModelProperty(value = "客户名称")
    private String customerName;

    /**
     * 订单号
     */
    @ApiModelProperty(value = "订单号")
    private String orderNo;

    /**
     * 付款状态0:已付款 1.未付款 2.待确认
     */
    private PayOrderStatus payOrderStatus;

    /**
     * 开票状态 0待开票 1 已开票
     */
    @ApiModelProperty(value = "开票状态")
    private InvoiceState invoiceState;

    /**
     * 订单开票IDs
     */
    @ApiModelProperty(value = "订单开票IDs")
    private List<String> orderInvoiceIds;

    /**
     * 查询退款开始时间，精确到天
     */
    @ApiModelProperty(value = "查询退款开始时间，精确到天")
    private String beginTime;

    /**
     * 查询退款结束时间，精确到天
     */
    @ApiModelProperty(value = "查询退款结束时间，精确到天")
    private String endTime;

    @ApiModelProperty(value = "token")
    private String token;

    /**
     * 商家id
     */
    @ApiModelProperty(value = "商家id")
    private Long companyInfoId;

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private Long storeId;

    /**
     * 商家名称
     */
    @ApiModelProperty(value = "商家名称")
    private String supplierName;

    /**
     * 负责业务员
     */
    @ApiModelProperty(value = "负责业务员")
    private String employeeId;

    /**
     * 负责业务员ID集合
     */
    @ApiModelProperty(value = "负责业务员ID集合")
    private List<String> employeeIds;

    /**
     * 批量会员Ids
     */
    @ApiModelProperty(value = "批量会员Ids")
    private List<String> customerIds;

    /**
     * 商家id
     */
    @ApiModelProperty(value = "商家id")
    private List<Long> companyInfoIds;

    /**
     * 封装公共条件
     * @return
     */
    private static String buildLike(String field) {
        StringBuilder stringBuilder = new StringBuilder();
        return stringBuilder.append("%").append(XssUtils.replaceLikeWildcard(field)).append("%").toString();
    }
}