package com.wanmi.sbc.customer.print.model.root;

import com.wanmi.sbc.customer.bean.enums.PrintDeliveryItem;
import com.wanmi.sbc.customer.bean.enums.PrintSize;
import com.wanmi.sbc.customer.bean.enums.PrintTradeItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrintSetting implements Serializable {

    private static final long serialVersionUID = 6490280932256489010L;
    /**
     * 主键
     */
    private String id;

    /**
     * 商户id
     */
    private Long storeId;

    /**
     * 打印尺寸
     */
    private PrintSize printSize;

    /**
     * 订单设置
     */
    private List<PrintTradeItem> tradeSettings;

    /**
     * 发货单设置
     */
    private List<PrintDeliveryItem> deliverySettings;

}
