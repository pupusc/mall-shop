package com.wanmi.sbc.customer.api.response.liveroom;

import com.wanmi.sbc.customer.bean.vo.LiveRoomVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>直播间新增结果</p>
 * @author zwb
 * @date 2020-06-06 18:28:57
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LiveRoomAddResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 已新增的直播间信息
     */
    @ApiModelProperty(value = "已新增的直播间信息")
    private LiveRoomVO liveRoomVO;

}
