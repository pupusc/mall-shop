package com.wanmi.sbc.account.api.response.offline;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 线下账户禁用响应
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfflineDisableByIdResponse implements Serializable {
    private static final long serialVersionUID = -4678098526172341403L;

    /**
     * 影响行数
     */
    @ApiModelProperty(value = "影响行数")
    private int count;
}
