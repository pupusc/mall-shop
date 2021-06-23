package com.wanmi.sbc.setting.api.response.baseconfig;

import com.wanmi.sbc.setting.bean.vo.BaseConfigVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>根据id查询任意（包含已删除）基本设置信息response</p>
 * @author lq
 * @date 2019-11-05 16:08:31
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseConfigByIdResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 基本设置信息
     */
    @ApiModelProperty(value = "基本设置信息")
    private BaseConfigVO baseConfigVO;
}
