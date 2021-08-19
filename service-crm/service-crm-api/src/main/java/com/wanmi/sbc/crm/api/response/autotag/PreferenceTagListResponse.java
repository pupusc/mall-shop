package com.wanmi.sbc.crm.api.response.autotag;

import com.wanmi.sbc.crm.bean.vo.AutoTagVO;
import com.wanmi.sbc.crm.bean.vo.PreferenceTagListVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p>自动标签列表结果</p>
 * @author dyt
 * @date 2020-03-11 14:47:32
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PreferenceTagListResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 自动标签列表结果
     */
    @ApiModelProperty(value = "自动标签列表结果")
    private List<PreferenceTagListVo> autoTagVOList;
}
