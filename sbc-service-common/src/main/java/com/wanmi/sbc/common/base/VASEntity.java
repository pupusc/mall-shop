package com.wanmi.sbc.common.base;

import com.wanmi.sbc.common.enums.VASConstants;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: songhanlin
 * @Date: Created In 16:26 2020/2/28
 * @Description: 增值服务Entity
 */
@Data
public class VASEntity implements Serializable {

    private static final long serialVersionUID = -1L;

    @ApiModelProperty(value = "服务名")
    private VASConstants serviceName;

    @ApiModelProperty(value = "服务状态")
    private boolean serviceStatus;

}
