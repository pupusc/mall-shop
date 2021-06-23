package com.wanmi.sbc.setting.api.response.yunservice;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>获取文件response</p>
 *
 * @author yang
 * @date 2019-11-05 18:33:04
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class YunGetResourceResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 文件内容
     */
    @ApiModelProperty(value = "文件内容")
    private byte[] content;
}
