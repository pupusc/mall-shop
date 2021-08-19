package com.wanmi.sbc.order.api.response.exceptionoftradepoints;

import com.wanmi.sbc.order.bean.vo.ExceptionOfTradePointsVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>积分订单抵扣异常表列表结果</p>
 * @author caofang
 * @date 2021-03-10 18:54:25
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionOfTradePointsListResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 积分订单抵扣异常表列表结果
     */
    @ApiModelProperty(value = "积分订单抵扣异常表列表结果")
    private List<ExceptionOfTradePointsVO> exceptionOfTradePointsVOList;
}
