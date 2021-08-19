package com.wanmi.sbc.setting.api.response.systemresource;

import com.wanmi.sbc.setting.bean.vo.SystemResourceVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>平台素材资源修改结果</p>
 * @author lq
 * @date 2019-11-05 16:14:27
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemResourceMoveResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 已修改的平台素材资源信息
     */
    @ApiModelProperty(value = "已修改的平台素材资源信息")
    private SystemResourceVO systemResourceVO;
}
