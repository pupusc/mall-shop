package com.wanmi.sbc.setting.api.request.thirdaddress;

import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.setting.bean.dto.ThirdAddressDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * <p>第三方地址合并保存操作</p>
 * @author dyt
 * @date 2020-08-14 13:41:44
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ThirdAddressBatchMergeRequest extends BaseRequest {
	private static final long serialVersionUID = 1L;


	/**
	 * 第三方地址数据
	 */
	@ApiModelProperty(value = "第三方地址数据")
	@NotEmpty
	private List<ThirdAddressDTO> thirdAddressList;
}