package com.wanmi.sbc.setting.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * <p>预置搜索词VO</p>
 * @author weiwenhao
 * @date 2020-04-16 11:40:28
 */
@ApiModel
@Data
public class PresetSearchTermsVO implements Serializable {

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    private Long id;

    /**
     * 预置搜索词字
     */
    @ApiModelProperty(value = "预置搜索词字")
    private String presetSearchKeyword;
}
