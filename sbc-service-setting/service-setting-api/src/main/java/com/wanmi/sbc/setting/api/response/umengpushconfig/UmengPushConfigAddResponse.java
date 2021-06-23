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
 * <p>友盟push接口配置新增结果</p>
 * @author bob
 * @date 2020-01-07 10:34:04
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UmengPushConfigAddResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 已新增的友盟push接口配置信息
     */
    @ApiModelProperty(value = "已新增的友盟push接口配置信息")
    private UmengPushConfigVO umengPushConfigVO;
}
