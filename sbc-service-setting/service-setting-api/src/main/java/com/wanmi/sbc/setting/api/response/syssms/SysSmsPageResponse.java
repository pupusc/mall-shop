package com.wanmi.sbc.setting.api.response.syssms;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.setting.bean.vo.SysSmsVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>系统短信配置分页结果</p>
 * @author lq
 * @date 2019-11-05 16:13:47
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysSmsPageResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 系统短信配置分页结果
     */
    @ApiModelProperty(value = "系统短信配置分页结果")
    private MicroServicePage<SysSmsVO> sysSmsVOPage;
}
