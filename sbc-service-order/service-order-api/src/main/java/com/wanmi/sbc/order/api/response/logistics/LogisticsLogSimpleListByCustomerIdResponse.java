package com.wanmi.sbc.order.api.response.logistics;

import com.wanmi.sbc.order.bean.vo.LogisticsLogSimpleVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p>根物流信息简化列表响应结果</p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogisticsLogSimpleListByCustomerIdResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "物流信息列表")
    private List<LogisticsLogSimpleVO> logisticsList;
}