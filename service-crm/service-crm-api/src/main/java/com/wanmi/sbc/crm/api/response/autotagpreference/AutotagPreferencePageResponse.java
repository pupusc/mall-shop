package com.wanmi.sbc.crm.api.response.autotagpreference;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.crm.bean.vo.AutotagPreferenceVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>偏好标签明细分页结果</p>
 * @author dyt
 * @date 2020-03-11 14:58:07
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AutotagPreferencePageResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 偏好标签明细分页结果
     */
    @ApiModelProperty(value = "偏好标签明细分页结果")
    private MicroServicePage<AutotagPreferenceVO> autotagPreferenceVOPage;

    @ApiModelProperty(value = "总人数")
    private Long count;
}
