package com.wanmi.sbc.order.api.response.distribution;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: 更新消费记录返回体
 * @Autho qiaokang
 * @Date：2019-03-06 11:02:41
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class ConsumeRecordModifyResponse implements Serializable {

    private static final long serialVersionUID = -3819095798113373718L;

    @ApiModelProperty(value = "更新数量")
    private int count;
}
