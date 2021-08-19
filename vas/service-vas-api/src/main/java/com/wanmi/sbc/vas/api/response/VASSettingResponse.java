package com.wanmi.sbc.vas.api.response;

import com.wanmi.sbc.common.base.VASEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: songhanlin
 * @Date: Created In 17:22 2020/2/28
 * @Description: 购买的增值服务
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VASSettingResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 购买的增值服务列表
     */
    @ApiModelProperty(value = "购买的增值服务列表")
    private List<VASEntity> services = new ArrayList<>();

}

