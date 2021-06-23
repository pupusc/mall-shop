package com.wanmi.sbc.setting.api.response.syssms;

import com.wanmi.sbc.setting.bean.vo.SysSmsVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>系统短信配置修改结果</p>
 * @author lq
 * @date 2019-11-05 16:13:47
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysSmsModifyResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 已修改的系统短信配置信息
     */
    @ApiModelProperty(value = "已修改的系统短信配置信息")
    private SysSmsVO sysSmsVO;
}
