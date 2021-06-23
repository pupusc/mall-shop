package com.wanmi.sbc.account.api.request.funds;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.account.bean.enums.FundsStatus;
import com.wanmi.sbc.account.bean.enums.FundsSubType;
import com.wanmi.sbc.account.bean.enums.FundsType;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Enumerated;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 会员资金明细-新增对象
 *
 * @author: Geek Wang
 * @createDate: 2019/2/19 11:06
 * @version: 1.0
 */
@ApiModel
@Data
public class CustomerFundsDetailAddRequest implements Serializable {

    /**
     * 会员ID
     */
    @ApiModelProperty(value = "会员ID")
    private String customerId;

    /**
     * 业务编号
     */
    @ApiModelProperty(value = "业务编号")
    private String businessId;

    /**
     * 资金类型
     */
    @ApiModelProperty(value = "资金类型")
    private FundsType fundsType;

    /**
     * 佣金提现id
     */
    @ApiModelProperty(value = "佣金提现id")
    private String drawCashId;

    /**
     * 收支金额
     */
    @ApiModelProperty(value = "收支金额")
    private BigDecimal receiptPaymentAmount;

    /**
     * 资金是否成功入账 0:否，1：成功
     */
    @ApiModelProperty(value = "资金是否成功入账 0:否，1：成功")
    @Enumerated
    private FundsStatus fundsStatus;

    /**
     * 账户余额
     */
    @ApiModelProperty(value = "账户余额")
    private BigDecimal accountBalance;

    /**
     * 资金账务子类型
     */
    @ApiModelProperty(value = "资金账务子类型")
    @Enumerated
    private FundsSubType subType;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;

}
