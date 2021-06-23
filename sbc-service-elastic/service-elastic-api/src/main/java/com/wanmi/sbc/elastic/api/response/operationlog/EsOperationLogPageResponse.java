package com.wanmi.sbc.elastic.api.response.operationlog;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.elastic.bean.vo.operationlog.EsOperationLogVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@ApiModel
@Data
public class EsOperationLogPageResponse implements Serializable {
    private static final long serialVersionUID = 377638577274460833L;

    @ApiModelProperty(value = "操作日志列表")
    private MicroServicePage<EsOperationLogVO> opLogPage;
}
