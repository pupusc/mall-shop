package com.wanmi.sbc.account.api.request.finance.record;

import com.wanmi.sbc.account.bean.enums.AccountRecordType;
import com.wanmi.sbc.account.bean.enums.PayWay;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * <p>对账明细查询参数分页结构</p>
 * Created by of628-wenzhi on 2017-12-08-下午5:02.
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class AccountDetailsPageRequest extends BasePageRequest {
    private static final long serialVersionUID = 828975362938588327L;

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    @NotNull
    private Long storeId;

    /**
     * 支付方式
     */
    @ApiModelProperty(value = "支付方式")
    private PayWay payWay;

    /**
     * 开始时间,非空，格式：yyyy-MM-dd HH:mm:ss
     */
    @ApiModelProperty(value = "开始时间")
    @NotNull
    private String beginTime;

    /**
     * 结束时间，非空，格式：yyyy-MM-dd HH:mm:ss
     */
    @ApiModelProperty(value = "结束时间")
    @NotNull
    private String endTime;

    /**
     * 对账类型 {@link AccountRecordType}
     */
    @ApiModelProperty(value = "对账类型")
    private AccountRecordType accountRecordType;

    /**
     * 交易流水号
     */
    @ApiModelProperty(value = "交易流水号")
    private String tradeNo;

    /**
     * 交易类型，0：收入 1：退款
     */
    @ApiModelProperty(value = "交易类型，0：收入 1：退款")
    private Integer type;

}
