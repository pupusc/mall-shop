package com.wanmi.sbc.customer.api.response.liveroomreplay;

import com.wanmi.sbc.customer.bean.vo.LiveRoomReplayVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>直播回放列表结果</p>
 * @author zwb
 * @date 2020-06-17 09:24:26
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LiveRoomReplayListResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 直播回放列表结果
     */
    @ApiModelProperty(value = "直播回放列表结果")
    private List<LiveRoomReplayVO> liveRoomReplayVOList;
}
