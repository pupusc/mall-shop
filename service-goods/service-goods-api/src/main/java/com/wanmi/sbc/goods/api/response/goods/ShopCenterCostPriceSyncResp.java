package com.wanmi.sbc.goods.api.response.goods;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 库存同步结果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopCenterCostPriceSyncResp implements Serializable {

	private List<String> goodsInfoIds;

	private List<String> goodsIds;
}
