package com.wanmi.sbc.elastic.api.response.operationlog;

import com.wanmi.sbc.elastic.bean.vo.operationlog.EsOperationLogVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author houshuai
 * @date 2020/12/16 10:02
 * @description <p> </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EsOperationLogListResponse {

    /**
     * 操作日志列表
     */
    @ApiModelProperty(value = "操作日志列表")
    private List<EsOperationLogVO> logVOList = new ArrayList<>();
}
