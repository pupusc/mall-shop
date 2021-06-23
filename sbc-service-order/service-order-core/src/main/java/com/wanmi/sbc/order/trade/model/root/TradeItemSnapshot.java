package com.wanmi.sbc.order.trade.model.root;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

/**
 * <p>订单商品快照，为保证商品的即时性TradeItem只暂存skuId与商品数量，
 * 用户下单后在订单生成前的校验使用，做持久化处理
 * </p>
 * Created by of628-wenzhi on 2017-07-11-下午5:25.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "tradeItemSnapshot")
public class TradeItemSnapshot implements Serializable {

    private static final long serialVersionUID = -3575489933840114170L;

    @Id
    private String id;

    /**
     * 客户id
     */
    private String buyerId;

    /**
     * 按商家，店铺分组的订单商品快照
     */
    private List<TradeItemGroup> itemGroups;

    /**
     * 快照类型--秒杀活动抢购商品订单快照："FLASH_SALE"
     */
    private String snapshotType;

    /**
     * 用户终端token
     */
    private String terminalToken;

    /**
     * 是否购物车购买
     */
    private Boolean purchaseBuy = Boolean.TRUE;

}
