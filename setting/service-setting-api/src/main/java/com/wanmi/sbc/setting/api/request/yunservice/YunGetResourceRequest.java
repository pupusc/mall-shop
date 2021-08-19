package com.wanmi.sbc.setting.api.request.yunservice;

import com.wanmi.sbc.setting.api.request.SettingBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>云获取文件参数</p>
 *
 * @author yang
 * @date 2019-11-21 16:33:04
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class YunGetResourceRequest extends SettingBaseRequest {
    private static final long serialVersionUID = 1L;

    /**
     * 素材KEY
     */
    @ApiModelProperty(value = "素材KEY")
    private String resourceKey;

}