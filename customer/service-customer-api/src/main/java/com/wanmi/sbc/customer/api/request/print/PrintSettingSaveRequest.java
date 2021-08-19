package com.wanmi.sbc.customer.api.request.print;

import com.wanmi.sbc.customer.bean.enums.PrintDeliveryItem;
import com.wanmi.sbc.customer.bean.enums.PrintSize;
import com.wanmi.sbc.customer.bean.enums.PrintTradeItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrintSettingSaveRequest implements Serializable {

    private static final long serialVersionUID = 532880731185256387L;

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private String id;

    /**
     * 商户id
     */
    @ApiModelProperty(value = "商户id")
    @NotNull
    private Long storeId;

    /**
     * 打印尺寸
     */
    @ApiModelProperty(value = "打印尺寸")
    private PrintSize printSize;

    /**
     * 订单设置
     */
    @ApiModelProperty(value = "订单设置")
    private List<PrintTradeItem> tradeSettings;

    /**
     * 发货单设置
     */
    @ApiModelProperty(value = "发货单设置")
    private List<PrintDeliveryItem> deliverySettings;

}