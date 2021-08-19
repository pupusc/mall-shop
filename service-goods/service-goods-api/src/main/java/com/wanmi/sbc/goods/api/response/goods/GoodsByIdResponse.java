package com.wanmi.sbc.goods.api.response.goods;

import com.wanmi.sbc.goods.bean.vo.GoodsVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * com.wanmi.sbc.goods.api.response.goods.GoodsByIdResponse
 * 根据编号查询商品信息响应对象
 * @author lipeng
 * @dateTime 2018/11/5 上午9:39
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class GoodsByIdResponse extends GoodsVO implements Serializable {

    private static final long serialVersionUID = -6641896293423917872L;
}
