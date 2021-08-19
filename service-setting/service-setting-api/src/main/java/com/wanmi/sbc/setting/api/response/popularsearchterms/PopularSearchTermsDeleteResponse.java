package com.wanmi.sbc.setting.api.response.popularsearchterms;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>热门搜索VO</p>
 * @author weiwenhao
 * @date 2020-04-17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class PopularSearchTermsDeleteResponse implements Serializable {

    private static final long serialVersionUID = -557547322294109773L;

    /**
     * 热门搜索ID
     */
    @ApiModelProperty(value = "删除结果")
    private Integer resultNum;

}
