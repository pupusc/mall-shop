package com.wanmi.sbc.setting.api.response.searchterms;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: weiwenhao
 * @Description:
 * @Date: 2020-04-16
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchAssociationalWordDeleteResponse {

    /**
     * 数据操作结果
     */
    @ApiModelProperty(value = "数据操作结果")
    private Integer resultNum;

}
