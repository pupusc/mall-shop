package com.wanmi.sbc.erp.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @program: sbc-background
 * @description: 仓库查询请求参数
 * @author: 0F3685-wugongjiang
 * @create: 2021-02-07 15:53
 **/
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WareHouseQueryRequest implements Serializable {

    /**
     * 是否附带返回已删除的仓库数据(true：返回
     * false:不返回
     * 默认false)
     */
    @ApiModelProperty(value = "是否包含删除仓库")
    private boolean hasDelData;
}
