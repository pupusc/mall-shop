package com.wanmi.sbc.elastic.api.response.goods;

import com.wanmi.sbc.elastic.bean.vo.goods.EsGoodsInfoVO;
import lombok.Data;

import java.util.List;

@Data
public class EsGoodsInfoListResponse {

    private List<EsGoodsInfoVO> goodsInfos;
}
