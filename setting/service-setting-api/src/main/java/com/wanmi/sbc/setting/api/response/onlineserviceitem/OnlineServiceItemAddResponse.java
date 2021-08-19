package com.wanmi.sbc.setting.api.response.onlineserviceitem;

import com.wanmi.sbc.setting.bean.vo.OnlineServiceItemVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>onlineerviceItem新增结果</p>
 * @author lq
 * @date 2019-11-05 16:10:54
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OnlineServiceItemAddResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 已新增的onlineerviceItem信息
     */
    @ApiModelProperty(value = "已新增的onlineerviceItem信息")
    private OnlineServiceItemVO onlineServiceItemVO;
}
