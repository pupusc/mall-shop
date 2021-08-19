package com.wanmi.sbc.account.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.account.bean.enums.FundsSubType;
import com.wanmi.sbc.account.bean.enums.FundsType;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 会员资金明细VO
 * @author: Geek Wang
 * @createDate: 2019/2/19 9:42
 * @version: 1.0
 */
@Data
public class CustomerFundsDetailVO implements Serializable {

    /**
     * 主键
     */
    private String customerFundsDetailId;

    /**
     * 业务编号
     */
    private String businessId;

    /**
     * 资金类型
     */
    private FundsType fundsType;

    /**
     * 资金账务子类型
     */
    @ApiModelProperty(value = "资金账务子类型")
    private FundsSubType subType;

    /**
     * 收支金额
     */
    private BigDecimal receiptPaymentAmount;

    /**
     * 账户余额
     */
    private BigDecimal accountBalance;

    /**
     * 入账时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

}
