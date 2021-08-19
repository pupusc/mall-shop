package com.wanmi.sbc.customer.api.response.liveroomreplay;

import com.wanmi.sbc.customer.bean.vo.LiveRoomReplayVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>根据id查询任意（包含已删除）直播回放信息response</p>
 * @author zwb
 * @date 2020-06-17 09:24:26
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LiveRoomReplayByIdResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 直播回放信息
     */
    @ApiModelProperty(value = "直播回放信息")
    private LiveRoomReplayVO liveRoomReplayVO;
}
