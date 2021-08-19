package com.wanmi.sbc.setting.api.response.yunservice;

import com.wanmi.sbc.setting.bean.vo.SystemConfigVO;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>根据id查询云配置信息response</p>
 *
 * @author yang
 * @date 2019-11-05 18:33:04
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
public class YunAvailableConfigResponse extends SystemConfigVO {

    private static final long serialVersionUID = 1L;

}
