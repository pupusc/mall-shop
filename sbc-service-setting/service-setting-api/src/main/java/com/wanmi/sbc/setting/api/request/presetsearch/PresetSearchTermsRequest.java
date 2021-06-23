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

}
