package com.wanmi.sbc.order.api.response.exceptionoftradepoints;

import com.wanmi.sbc.order.bean.vo.ExceptionOfTradePointsVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>根据id查询任意（包含已删除）积分订单抵扣异常表信息response</p>
 * @author caofang
 * @date 2021-03-10 18:54:25
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionOfTradePointsByIdResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 积分订单抵扣异常表信息
     */
    @ApiModelProperty(value = "积分订单抵扣异常表信息")
    private ExceptionOfTradePointsVO exceptionOfTradePointsVO;
}
