package com.wanmi.sbc.setting.api.response;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.setting.bean.vo.OperationLogVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@ApiModel
@Data
public class OperationLogPageResponse implements Serializable {
    private static final long serialVersionUID = 377638577274460833L;

    @ApiModelProperty(value = "操作日志列表")
    private MicroServicePage<OperationLogVO> opLogPage;
}

