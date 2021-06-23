package com.wanmi.sbc.goods.bean.vo;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.goods.bean.dto.ThirdGoodsCateRelDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.stream.StreamFilter;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>VO</p>
 * @author 
 * @date 2020-08-17 14:46:43
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ThirdGoodsCateRelVO extends ThirdGoodsCateRelDTO implements Serializable {
	/**
	 * 类目名称全路径
	 */
	@ApiModelProperty("类目名称全路径")
	private String path;
	@ApiModelProperty("子类目")
	private List<ThirdGoodsCateRelVO> children;

}