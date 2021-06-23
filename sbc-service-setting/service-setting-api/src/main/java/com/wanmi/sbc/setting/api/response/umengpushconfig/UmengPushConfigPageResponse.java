package com.wanmi.sbc.setting.api.response.umengpushconfig;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.setting.bean.vo.UmengPushConfigVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>友盟push接口配置分页结果</p>
 * @author bob
 * @date 2020-01-07 10:34:04
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UmengPushConfigPageResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 友盟push接口配置分页结果
     */
    @ApiModelProperty(value = "友盟push接口配置分页结果")
    private MicroServicePage<UmengPushConfigVO> umengPushConfigVOPage;
}
