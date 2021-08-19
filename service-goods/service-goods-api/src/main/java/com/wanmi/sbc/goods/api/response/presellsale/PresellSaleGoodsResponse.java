package com.wanmi.sbc.goods.api.response.presellsale;

import com.wanmi.sbc.goods.bean.vo.PresellSaleGoodsVO;
import lombok.Data;

import java.io.Serializable;

/**
 * 预售活动关联商品信息
 */
@Data
public class PresellSaleGoodsResponse extends PresellSaleGoodsVO implements Serializable {
    private static final long serialVersionUID = 4614077699001075323L;
}
