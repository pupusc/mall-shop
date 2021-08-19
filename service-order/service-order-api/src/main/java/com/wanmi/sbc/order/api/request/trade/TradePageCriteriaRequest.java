package com.wanmi.sbc.order.api.request.trade;

import com.wanmi.sbc.order.bean.dto.TradeQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.query.Criteria;

import java.io.Serializable;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2018-12-04 11:47
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class TradePageCriteriaRequest implements Serializable {

    /**
     * 是否是可退单查询
     */
    @ApiModelProperty(value = "是否是可退单查询",dataType = "com.wanmi.sbc.common.enums.BoolFlag")
    private boolean isReturn;

    /**
     * 分页参数
     */
    @ApiModelProperty(value = "分页参数")
    private TradeQueryDTO tradePageDTO;
}
