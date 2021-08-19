package com.wanmi.sbc.account.api.request.finance.record;

import com.wanmi.sbc.account.bean.enums.AccountRecordType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * <p>对账数据导出Excel 参数结构</p>
 * Created by chenli on 2018-12-21-下午14:39.
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class AccountRecordToExcelRequest extends BasePageRequest {

    private static final long serialVersionUID = -2709202223618975083L;

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
     * token
     */
    @ApiModelProperty(value = "token")
    private String token;

    /**
     * 需要查询的对账记录类型 {@link AccountRecordType}
     */
    @ApiModelProperty(value = "需要查询的对账记录类型")
    private AccountRecordType accountRecordType;

}
