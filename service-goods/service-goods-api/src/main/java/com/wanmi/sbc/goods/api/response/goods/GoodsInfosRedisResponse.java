package com.wanmi.sbc.goods.api.response.goods;


import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
@Data
public class GoodsInfosRedisResponse implements Serializable {

    private List<GoodsInfoVO> goodsInfoVOList;

    private List<NewBookPointRedisResponse> newBookPointResponseList;
}
