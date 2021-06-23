package com.wanmi.sbc.order.api.response.trade;

import com.wanmi.sbc.order.bean.vo.TradeCommitResultVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author: gaomuwei
 * @createDate: 2018/12/18 10:06
 * @version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class TradeCommitResponse implements Serializable {


    private static final long serialVersionUID = -8353082327240263253L;
    /**
     * 订单提交结果
     */
    @ApiModelProperty(value = "订单提交结果列表")
    private List<TradeCommitResultVO> tradeCommitResults;

}
