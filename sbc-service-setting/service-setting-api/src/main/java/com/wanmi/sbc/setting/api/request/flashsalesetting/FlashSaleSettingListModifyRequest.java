package com.wanmi.sbc.setting.api.request.flashsalesetting;

import com.wanmi.sbc.setting.api.request.SettingBaseRequest;
import com.wanmi.sbc.setting.bean.vo.FlashSaleSettingVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

/**
 * <p>秒杀设置修改参数</p>
 * @author yxz
 * @date 2019-06-11 13:48:53
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlashSaleSettingListModifyRequest extends SettingBaseRequest {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "秒杀设置更新请求信息")
	List<FlashSaleSettingVO> flashSaleSettingVOS;

}