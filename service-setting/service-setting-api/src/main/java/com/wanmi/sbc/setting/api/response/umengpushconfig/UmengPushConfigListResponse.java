package com.wanmi.sbc.setting.api.response.umengpushconfig;

import com.wanmi.sbc.setting.bean.vo.UmengPushConfigVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p>友盟push接口配置列表结果</p>
 * @author bob
 * @date 2020-01-07 10:34:04
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UmengPushConfigListResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 友盟push接口配置列表结果
     */
    @ApiModelProperty(value = "友盟push接口配置列表结果")
    private List<UmengPushConfigVO> umengPushConfigVOList;
}
