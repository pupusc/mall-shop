package com.wanmi.sbc.erp.api.response;

import com.sbc.wanmi.erp.bean.vo.MetaStockInfoVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class MetaStockResponse implements Serializable {
	/**
	 * 库存信息
	 */
	@ApiModelProperty(value = "商品库存信息")
	private List<MetaStockInfoVO> goodsInfoList;
}
