package com.wanmi.sbc.convert;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * com.wanmi.sbc.goods.api.provider.info.GoodsInfoRequest
 *
 * @author lipeng
 * @dateTime 2018/11/8 下午5:45
 */
@Data
public class GoodsInfoDTO implements Serializable {

    private Integer goodsInfoId;

    private String goodsInfoName;

    private List<GoodsMarketDTO> goodsMarketDTOList;

    private GoodsMarketDTO goodsMarketDTO;
}
