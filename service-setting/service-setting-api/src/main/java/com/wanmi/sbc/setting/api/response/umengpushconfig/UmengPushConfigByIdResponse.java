package com.wanmi.sbc.setting.api.response.umengpushconfig;

import com.wanmi.sbc.setting.bean.vo.UmengPushConfigVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>根据id查询任意（包含已删除）友盟push接口配置信息response</p>
 * @author bob
 * @date 2020-01-07 10:34:04
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UmengPushConfigByIdResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 友盟push接口配置信息
     */
    @ApiModelProperty(value = "友盟push接口配置信息")
    private UmengPushConfigVO umengPushConfigVO;
}
