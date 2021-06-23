package com.wanmi.sbc.order.api.request.trade;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.order.bean.dto.LogisticsDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.query.Criteria;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>物流信息重复查询参数结构</p>
 */
@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LogisticsRepeatRequest implements Serializable {

    private static final long serialVersionUID = 149142593703964072L;

    /**
     * 物流信息
     */
    @ApiModelProperty(value = "物流信息")
    @NotEmpty
    private List<LogisticsDTO> logisticsDTOList;
}
