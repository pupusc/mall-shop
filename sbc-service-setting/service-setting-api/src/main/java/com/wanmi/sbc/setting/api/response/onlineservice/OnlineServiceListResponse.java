package com.wanmi.sbc.setting.api.response.onlineservice;

import com.wanmi.sbc.setting.bean.vo.OnlineServiceItemVO;
import com.wanmi.sbc.setting.bean.vo.OnlineServiceVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>onlineService座席列表结果</p>
 * @author lq
 * @date 2019-11-05 16:10:28
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OnlineServiceListResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * onlineService客服信息
     */
    @ApiModelProperty(value = "onlineService客服信息")
    private OnlineServiceVO qqOnlineServerRop = new OnlineServiceVO();

    /**
     * onlineerviceItem座席列表结果
     */
    @ApiModelProperty(value = "onlineerviceItem座席列表结果")
    private List<OnlineServiceItemVO> qqOnlineServerItemRopList = new ArrayList<>();
}
