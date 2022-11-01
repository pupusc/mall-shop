package com.wanmi.sbc.setting.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

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
    private Long id;

    /**
     * 预置搜索词字
     */
    private String presetSearchKeyword;

    /**
     * 预置搜索词类型 0-H5  1-小程序
     */
    private Integer presetChannel = 0;

    /**
     * 预置搜索词类型0-搜索结果页面 1-指定跳转页
     */
    private Integer presetSearchType;

    /**
     * 预置搜索词跳转地址
     */
    private String presetSearchKeywordPageUrl;
}
