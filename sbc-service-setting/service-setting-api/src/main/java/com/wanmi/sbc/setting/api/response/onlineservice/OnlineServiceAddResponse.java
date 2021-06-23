package com.wanmi.sbc.setting.api.response.onlineservice;

import com.wanmi.sbc.setting.bean.vo.OnlineServiceVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>onlineService新增结果</p>
 * @author lq
 * @date 2019-11-05 16:10:28
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OnlineServiceAddResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 已新增的onlineService信息
     */
    @ApiModelProperty(value = "已新增的onlineService信息")
    private OnlineServiceVO onlineServiceVO;
}
