package com.wanmi.sbc.account.api.response.offline;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 线下账户统计响应
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfflineCountResponse implements Serializable {
    private static final long serialVersionUID = 5039964425176182590L;

    /**
     * 总数
     */
    @ApiModelProperty(value = "总数")
    private int count;
}
