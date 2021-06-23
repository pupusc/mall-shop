package com.wanmi.sbc.order.api.response.payorder;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.account.bean.enums.PayOrderStatus;
import com.wanmi.sbc.account.bean.enums.PayType;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.order.bean.enums.FlowState;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.Convert;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

@Data
@ApiModel
public class FindPayOrderResponse implements Serializable {

    private static int STR_LENGTH = 4;
    private static Pattern NUMBER_PATERN = Pattern.compile("[^0-9]");

    /**
     * 支付单Id
     */
    @ApiModelProperty(value = "支付单Id")
    private String payOrderId;

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
    @ApiModelProperty(value = "支付类型" )
    private PayType payType;

    /**
     * 付款状态
     */
    @ApiModelProperty(value = "支付单状态")
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
     * 收款单账号
     * 收款账户前端正常直接展示
     */
    @ApiModelProperty(value = "收款单账号")
    private String normalReceivableAccount;


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

    @ApiModelProperty(value = "收款在线渠道id")
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
     * 团编号
     */
    private String grouponNo;

    /**
     * 店铺名称
     */
    @ApiModelProperty(value = "店铺名称")
    private String storeName;

    /**
     * 是否平台自营
     */
    @ApiModelProperty(value = "是否平台自营",dataType = "com.wanmi.sbc.common.enums.BoolFlag")
    private Boolean isSelf;

    /**
     * 应付积分
     */
    @ApiModelProperty(value = "应付积分")
    private Long buyPoints;

    /**
     * 流程状态
     */
    @ApiModelProperty(value = "流程状态")
    private FlowState flowState;

    /**
     * 收款账户脱敏 example :622000000000008888  -->  6220**********8888
     * @return
     */
    public String getReceivableAccount(){
        if(StringUtils.isNotEmpty(receivableAccount)){
            int postion = receivableAccount.indexOf(" ");
            //提取银行卡号
            String bankAccount = NUMBER_PATERN.matcher(receivableAccount).replaceAll("");
            //脱敏
            if(bankAccount.length() > STR_LENGTH) {
                bankAccount = bankAccount.substring(0, 4) + "**********" + bankAccount.substring(bankAccount.length() - 4);
            }
            receivableAccount = receivableAccount.substring(0,postion) + " " + bankAccount;
        }
        return receivableAccount;
    }
}
