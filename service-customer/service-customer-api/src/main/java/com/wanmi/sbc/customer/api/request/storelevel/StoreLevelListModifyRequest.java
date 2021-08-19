package com.wanmi.sbc.customer.api.request.storelevel;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import com.wanmi.sbc.customer.bean.vo.StoreLevelVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <p>店铺等级批量修改参数</p>
 * @author yinxianzhi
 * @date 2019-05-08 19:51:30
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreLevelListModifyRequest extends CustomerBaseRequest {

	private static final long serialVersionUID = -2271113436710243093L;

	@ApiModelProperty(value = "店铺等级更新请求信息")
	List<StoreLevelVO> storeLevelVOList;
}