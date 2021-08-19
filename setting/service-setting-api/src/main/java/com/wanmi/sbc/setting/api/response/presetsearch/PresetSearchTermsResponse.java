package com.wanmi.sbc.setting.api.response.presetsearch;

import com.wanmi.sbc.setting.bean.vo.PresetSearchTermsVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * <p>预置搜索词VO</p>
 * @author weiwenhao
 * @date 2020-04-16
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PresetSearchTermsResponse implements Serializable {
    /**
     * 新增的预置搜索词信息
     */
    @ApiModelProperty(value = "新增预置搜索词信息")
    private PresetSearchTermsVO presetSearchTermsVO;

}
