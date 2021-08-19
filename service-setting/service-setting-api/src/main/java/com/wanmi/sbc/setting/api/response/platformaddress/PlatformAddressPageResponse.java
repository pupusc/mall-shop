package com.wanmi.sbc.setting.api.response.platformaddress;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.setting.bean.vo.PlatformAddressVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>平台地址信息分页结果</p>
 * @author dyt
 * @date 2020-03-30 14:39:57
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlatformAddressPageResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 平台地址信息分页结果
     */
    @ApiModelProperty(value = "平台地址信息分页结果")
    private MicroServicePage<PlatformAddressVO> platformAddressVOPage;
}
