package com.wanmi.sbc.account.api.request.finance.record;

import com.wanmi.sbc.account.bean.enums.AccountRecordType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * <p>对账列表汇总请求参数</p>
 * Created by of628-wenzhi on 2018-10-13-上午11:48.
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountGatherListRequest extends AccountBaseRequest {
    private static final long serialVersionUID = 7795837829113110055L;

    /**
     * 商家id
     */
    @ApiModelProperty(value = "商家id")
    private Long supplierId;

    /**
     * 检索关键字
     */
    @ApiModelProperty(value = "检索关键字")
    private String keywords;

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
     * 需要查询的对账记录类型 {@link AccountRecordType}
     */
    @ApiModelProperty(value = "需要查询的对账记录类型")
    private AccountRecordType accountRecordType;
}
