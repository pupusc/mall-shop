package com.wanmi.sbc.setting.api.response.operatedatalog;

import com.wanmi.sbc.setting.bean.vo.OperateDataLogVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>根据id查询任意（包含已删除）系统日志信息response</p>
 * @author guanfl
 * @date 2020-04-21 14:57:15
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OperateDataLogByIdResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 系统日志信息
     */
    @ApiModelProperty(value = "系统日志信息")
    private OperateDataLogVO operateDataLogVO;
}
