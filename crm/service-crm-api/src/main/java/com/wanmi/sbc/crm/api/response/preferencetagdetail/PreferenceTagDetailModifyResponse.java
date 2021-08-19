package com.wanmi.sbc.crm.api.response.preferencetagdetail;

import com.wanmi.sbc.crm.bean.vo.PreferenceTagDetailVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>偏好标签明细修改结果</p>
 * @author dyt
 * @date 2020-03-11 14:58:07
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PreferenceTagDetailModifyResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 已修改的偏好标签明细信息
     */
    @ApiModelProperty(value = "已修改的偏好标签明细信息")
    private PreferenceTagDetailVO preferenceTagDetailVO;
}
