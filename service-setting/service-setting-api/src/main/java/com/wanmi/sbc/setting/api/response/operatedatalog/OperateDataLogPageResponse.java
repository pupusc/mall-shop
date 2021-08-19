package com.wanmi.sbc.setting.api.response.operatedatalog;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.setting.bean.vo.OperateDataLogVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>系统日志分页结果</p>
 * @author guanfl
 * @date 2020-04-21 14:57:15
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OperateDataLogPageResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 系统日志分页结果
     */
    @ApiModelProperty(value = "系统日志分页结果")
    private MicroServicePage<OperateDataLogVO> operateDataLogVOPage;
}
