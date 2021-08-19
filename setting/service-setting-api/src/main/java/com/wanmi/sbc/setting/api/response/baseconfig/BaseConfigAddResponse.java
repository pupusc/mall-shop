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
 * <p>基本设置新增结果</p>
 * @author lq
 * @date 2019-11-05 16:08:31
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseConfigAddResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 已新增的基本设置信息
     */
    @ApiModelProperty(value = "已新增的基本设置信息")
    private BaseConfigVO baseConfigVO;
}
