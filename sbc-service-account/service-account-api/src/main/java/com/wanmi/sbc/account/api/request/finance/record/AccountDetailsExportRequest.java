package com.wanmi.sbc.account.api.request.finance.record;

import com.wanmi.sbc.account.bean.enums.AccountRecordType;
import com.wanmi.sbc.account.bean.enums.PayWay;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * <p>对账明细导出请求参数结构</p>
 * Created by of628-wenzhi on 2017-12-13-下午5:03.
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class AccountDetailsExportRequest extends AccountBaseRequest {

    private static final long serialVersionUID = -7885604503879260262L;

    /**
     * jwt token
     */
    @ApiModelProperty(value = "jwt token")
    private String token;

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
     * 交易单号
     */
    @ApiModelProperty(value = "交易单号")
    private String tradeNo;
}
