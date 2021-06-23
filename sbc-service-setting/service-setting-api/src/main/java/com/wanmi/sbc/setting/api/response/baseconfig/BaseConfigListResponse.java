package com.wanmi.sbc.setting.api.response.baseconfig;

import com.wanmi.sbc.setting.bean.vo.BaseConfigVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>基本设置列表结果</p>
 * @author lq
 * @date 2019-11-05 16:08:31
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseConfigListResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 基本设置列表结果
     */
    @ApiModelProperty(value = "基本设置列表结果")
    private List<BaseConfigVO> baseConfigVOList;
}
