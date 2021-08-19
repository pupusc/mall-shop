package com.wanmi.sbc.customer.api.request.mq;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author yangzhen
 * @Description //邀新奖励
 * @Date 11:09 2020/12/16
 * @Param
 * @return
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EsCustomerFundsAddRequest implements Serializable {

    /**
     * 主键
     */
    private String customerFundsId;

    /**
     * 会员ID
     */
    private String customerId;

    /**
     * 会员名称
     */
    private String customerName;

    /**
     * 会员账号
     */
    private String customerAccount;

}
