package com.wanmi.sbc.setting.api.response.systemresource;

import com.wanmi.sbc.setting.bean.vo.SystemResourceVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>平台素材资源新增结果</p>
 * @author lq
 * @date 2019-11-05 16:14:27
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemResourceAddResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 已新增的平台素材资源信息
     */
    @ApiModelProperty(value = "已新增的平台素材资源信息")
    private SystemResourceVO systemResourceVO;
}
