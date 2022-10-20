package com.wanmi.sbc.setting.api.request.presetsearch;

import com.wanmi.sbc.setting.api.request.SettingBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;


/**
 * <p>预置搜索词</p>
 * @author weiwenhao
 * @date 2020-04-16
 */

@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
@Data
public class PresetSearchTermsRequest extends SettingBaseRequest {

    private static final long serialVersionUID = 6607049145772092738L;


    /**
     * 预置搜索词
     */
    @ApiModelProperty(value = "预置搜索词")
    @Length(max = 10)
    private String presetSearchKeyword;

    /**
     * 预置搜索词类型 0-H5  1-小程序
     */
    @ApiModelProperty(value = "预置搜索词类型")
    private Integer presetChannel = 0;


    /**
     * 预置搜索词类型0-搜索结果页面 1-指定跳转页
     */
    private Integer presetSearchType;

    /**
     * 预置搜索词跳转地址
     */
    @Length(max = 400)
    private String presetSearchKeywordPageUrl;

}
