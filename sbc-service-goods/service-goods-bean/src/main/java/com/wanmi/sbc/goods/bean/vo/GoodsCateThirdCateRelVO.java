package com.wanmi.sbc.goods.bean.vo;

import com.wanmi.sbc.goods.bean.dto.GoodsCateThirdCateRelDTO;
import lombok.Data;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>平台类目和第三方平台类目映射VO</p>
 * @author 
 * @date 2020-08-18 19:51:55
 */
@ApiModel
@Data
public class GoodsCateThirdCateRelVO extends GoodsCateThirdCateRelDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@ApiModelProperty(value = "id")
	private Long id;

}