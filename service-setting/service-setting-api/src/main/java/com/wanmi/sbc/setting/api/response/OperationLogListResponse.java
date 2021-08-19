package com.wanmi.sbc.setting.api.response;

import com.wanmi.sbc.setting.bean.vo.OperationLogVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
@ApiModel
@Data
public class OperationLogListResponse implements Serializable {
    private static final long serialVersionUID = -239731168693023516L;
    /**
     * 操作日志列表
     */
    @ApiModelProperty(value = "操作日志列表")
    private List<OperationLogVO> logVOList = new ArrayList<>();
}
