package com.wanmi.sbc.order.api.response.returnorder;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 根据动态条件统计退单响应结构
 * Created by jinwei on 6/5/2017.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class ReturnCountByConditionResponse implements Serializable {

    private static final long serialVersionUID = 4079600894275807085L;

    /**
     * 统计个数
     */
    @ApiModelProperty(value = "统计个数")
    private Long count;


}
