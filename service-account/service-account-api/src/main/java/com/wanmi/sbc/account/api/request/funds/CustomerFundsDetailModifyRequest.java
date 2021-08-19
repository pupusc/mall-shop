package com.wanmi.sbc.account.api.request.funds;

import com.wanmi.sbc.account.bean.enums.FundsStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

/**
 * 会员资金明细-更新对象
 * @createDate: 2019/2/19 11:06
 * @version: 1.0
 */
@ApiModel
@Data
public class CustomerFundsDetailModifyRequest implements Serializable {
    private static final long serialVersionUID = 1640260343601674437L;

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
     * 资金状态
     */
    @ApiModelProperty(value = "资金状态")
    private FundsStatus fundsStatus;

    /**
     * 佣金提现id
     */
    @ApiModelProperty(value = "佣金提现id")
    private String drawCashId;

    /**
     * Tab类型 0: 全部, 1: 收入, 2: 支出, 3:分销佣金&邀新记录
     */
    @ApiModelProperty(value = "ab类型 0: 全部, 1: 收入, 2: 支出, 3:分销佣金&邀新记录")
    private Integer tabType;
}
