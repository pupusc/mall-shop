package com.wanmi.sbc.pay.api.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>根据单号获取交易记录数返回结果Bean</p>
 * Created by of628-wenzhi on 2018-08-10-下午3:19.
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayTradeRecordCountResponse implements Serializable{
    private static final long serialVersionUID = 6879403044996693563L;

    /**
     * 结果数
     */
    @ApiModelProperty(value = "结果数")
    private Long count;
}
