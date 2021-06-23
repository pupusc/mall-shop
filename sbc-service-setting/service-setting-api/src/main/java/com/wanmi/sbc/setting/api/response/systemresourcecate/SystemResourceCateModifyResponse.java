package com.wanmi.sbc.setting.api.response.systemresourcecate;

import com.wanmi.sbc.setting.bean.vo.SystemResourceCateVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>平台素材资源分类修改结果</p>
 * @author lq
 * @date 2019-11-05 16:14:55
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemResourceCateModifyResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 已修改的平台素材资源分类信息
     */
    @ApiModelProperty(value = "已修改的平台素材资源分类信息")
    private SystemResourceCateVO systemResourceCateVO;
}
