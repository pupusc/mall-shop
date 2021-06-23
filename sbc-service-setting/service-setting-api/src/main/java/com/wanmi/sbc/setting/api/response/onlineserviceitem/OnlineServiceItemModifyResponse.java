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
 * <p>onlineerviceItem修改结果</p>
 * @author lq
 * @date 2019-11-05 16:10:54
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OnlineServiceItemModifyResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 已修改的onlineerviceItem信息
     */
    @ApiModelProperty(value = "已修改的onlineerviceItem信息")
    private OnlineServiceItemVO onlineServiceItemVO;
}
