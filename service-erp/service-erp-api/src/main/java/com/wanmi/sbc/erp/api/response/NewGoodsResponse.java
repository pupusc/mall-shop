package com.wanmi.sbc.erp.api.response;

import com.sbc.wanmi.erp.bean.vo.NewGoodsInfoVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class NewGoodsResponse implements Serializable {
	/**
	 * 库存信息
	 */
	@ApiModelProperty(value = "商品库存信息")
	private List<NewGoodsInfoVO> goodsInfoList;
}
