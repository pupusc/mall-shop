package com.wanmi.sbc.crm.api.response.autotag;

import com.wanmi.sbc.crm.bean.vo.AutoTagVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>自动标签修改结果</p>
 * @author dyt
 * @date 2020-03-11 14:47:32
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AutoTagModifyResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 已修改的自动标签信息
     */
    @ApiModelProperty(value = "已修改的自动标签信息")
    private AutoTagVO autoTagVO;
}
