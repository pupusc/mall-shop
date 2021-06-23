package com.wanmi.sbc.goods.api.response.marketing;

import com.wanmi.sbc.goods.bean.vo.GoodsMarketingVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>商品营销</p>
 * author: sunkun
 * Date: 2018-11-02
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class GoodsMarketingModifyResponse extends GoodsMarketingVO {

    private static final long serialVersionUID = -9221303025882761507L;

}
