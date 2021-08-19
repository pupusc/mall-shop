package com.wanmi.sbc.goods.api.request.info;

import com.wanmi.sbc.goods.bean.dto.DistributionGoodsInfoModifyDTO;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * com.wanmi.sbc.goods.api.request.info.DistributionGoodsModifyRequest
 * 编辑分销商品请求对象
 *
 * @author CHENLI
 * @dateTime 2019/2/19 上午9:33
 */
@ApiModel
@Data
public class DistributionGoodsModifyRequest extends DistributionGoodsInfoModifyDTO implements Serializable {

    private static final long serialVersionUID = -5609005002056735583L;
}
