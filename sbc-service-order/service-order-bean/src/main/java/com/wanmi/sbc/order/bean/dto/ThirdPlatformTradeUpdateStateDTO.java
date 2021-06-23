package com.wanmi.sbc.order.bean.dto;

import com.wanmi.sbc.order.bean.enums.DeliverStatus;
import com.wanmi.sbc.order.bean.enums.FlowState;
import com.wanmi.sbc.order.bean.enums.PayState;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

/**
 * @Description: 第三方平台订单信息
 * @Autho yuhuiyu
 * @Date： 2020-8-21 10:19:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class ThirdPlatformTradeUpdateStateDTO implements Serializable {

    private static final long serialVersionUID = 4576910388614459987L;

    /**
     * 子订单id
     */
    @Id
    private String id;

    /**
     * 父订单id
     */
    private String parentId;

    /**
     * 主订单id(商户)
     */
    private String tradeId;

    /**
     * 流程状态
     */
    @ApiModelProperty(value = "流程状态")
    private FlowState flowState;

    /**
     * 发货状态
     */
    @ApiModelProperty(value = "发货状态")
    private DeliverStatus deliverStatus;

    /**
     * 支付状态
     */
    @ApiModelProperty(value = "支付状态")
    private PayState payState;

}
