package com.wanmi.sbc.setting.api.response.preserotation;

import com.wanmi.sbc.setting.bean.vo.PresetSearchRotationVO;
import com.wanmi.sbc.setting.bean.vo.PresetSearchTermsVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;


/**
 * <p>轮播词VO</p>
 * @author chenzhen
 * @date 2023-03-16
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PresetSearchRotationQueryResponse implements Serializable {
    /**
     * 新增的预置搜索词信息
     */
    @ApiModelProperty(value = "新增轮播词信息")
    private List<PresetSearchRotationVO> presetSearchTermsVO;

}
