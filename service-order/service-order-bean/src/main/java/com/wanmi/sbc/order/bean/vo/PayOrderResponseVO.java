package com.wanmi.sbc.order.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.account.bean.enums.PayOrderStatus;
import com.wanmi.sbc.account.bean.enums.PayType;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.Convert;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 支付单返回对象
 * Created by zhangjin on 2017/4/27.
 */
@Data
@ApiModel
public class PayOrderResponseVO implements Serializable {

    /**
     * 支付单Id
     */
    @ApiModelProperty(value = "支付单Id")
    private String payOrderId;

    /**
     *子订单列表
     */
    @ApiModelProperty(value = "子订单列表")
    private List<TradeVO> tradeVOList;

    /**
     * 订单编号
     */
    @ApiModelProperty(value = "订单编号")
    private String orderCode;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    /**
     * 会员名称
     */
    @ApiModelProperty(value = "会员名称")
    private String customerName;

    /**
     * 会员id
     */
    @ApiModelProperty(value = "会员id")
    private String customerId;

    /**
     * 支付类型
     */
    @ApiModelProperty(value = "支付类型")
    private PayType payType;

    /**
     * 付款状态
     */
    @ApiModelProperty(value = "付款状态")
    private PayOrderStatus payOrderStatus;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String comment;

    /**
     * 收款单账号
     * 收款账户前端展示 农业银行6329***7791 支付宝支付189**@163.com
     */
    @ApiModelProperty(value = "收款单账号")
    private String receivableAccount;

    /**
     * 流水号
     */
    @ApiModelProperty(value = "流水号")
    private String receivableNo;

    /**
     * 收款金额
     */
    @ApiModelProperty(value = "收款金额")
    private BigDecimal payOrderPrice;

    /**
     * 支付单积分
     */
    @ApiModelProperty(value = "支付单积分")
    private Long payOrderPoints;

    /**
     * 应付金额
     */
    @ApiModelProperty(value = "应付金额")
    private BigDecimal totalPrice;

    /**
     * 收款时间
     */
    @ApiModelProperty(value = "收款时间")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime receiveTime;

    /**
     * 收款在线渠道
     */
    @ApiModelProperty(value = "收款在线渠道")
    private String payChannel;

    @ApiModelProperty(value = "收款在线渠道")
    private Long payChannelId;

    /**
     * 商家编号
     */
    @ApiModelProperty(value = "商家编号")
    private Long companyInfoId;

    /**
     * 商家名称
     */
    @ApiModelProperty(value = "商家名称")
    private String supplierName;

    /**
     * 附件
     */
    @ApiModelProperty(value = "附件")
    private String encloses;

    /**
     * 支付单对应的订单状态
     */
    @ApiModelProperty(value = "订单状态")
    private TradeStateVO tradeState;

}
