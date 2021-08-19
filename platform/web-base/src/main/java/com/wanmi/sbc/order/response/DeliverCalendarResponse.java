package com.wanmi.sbc.order.response;

import com.wanmi.sbc.order.bean.vo.DeliverCalendarVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * 周期购发货日历
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class DeliverCalendarResponse {

    /**
     * 发货日历
     */
    @ApiModelProperty(value = "发货日历")
    private Map<String, List<DeliverCalendarVO>> deliverCalendarList;
}
