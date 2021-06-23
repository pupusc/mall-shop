package com.wanmi.sbc.convert;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * com.wanmi.sbc.goods.api.provider.info.GoodsInfo
 *
 * @author lipeng
 * @dateTime 2018/11/8 下午5:45
 */
@Data
@ToString
public class GoodsInfo implements Serializable {

    private Integer goodsInfoId;

    private String goodsInfoName;

    private List<GoodsMarket> goodsMarketDTOList;

    private GoodsMarket goodsMarketDTO;
}
