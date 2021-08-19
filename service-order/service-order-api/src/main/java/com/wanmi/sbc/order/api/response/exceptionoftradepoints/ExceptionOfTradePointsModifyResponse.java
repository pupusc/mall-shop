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
 * <p>积分订单抵扣异常表修改结果</p>
 * @author caofang
 * @date 2021-03-10 18:54:25
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionOfTradePointsModifyResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 已修改的积分订单抵扣异常表信息
     */
    @ApiModelProperty(value = "已修改的积分订单抵扣异常表信息")
    private ExceptionOfTradePointsVO exceptionOfTradePointsVO;
}
