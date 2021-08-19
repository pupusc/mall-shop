package com.wanmi.sbc.setting.api.response.onlineserviceitem;

import com.wanmi.sbc.setting.bean.vo.OnlineServiceItemVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>onlineerviceItem列表结果</p>
 * @author lq
 * @date 2019-11-05 16:10:54
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OnlineServiceItemListResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * onlineerviceItem列表结果
     */
    @ApiModelProperty(value = "onlineerviceItem列表结果")
    private List<OnlineServiceItemVO> onlineServiceItemVOList;
}
