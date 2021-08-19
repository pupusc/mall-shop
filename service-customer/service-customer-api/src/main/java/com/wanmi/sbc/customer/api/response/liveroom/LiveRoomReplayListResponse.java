package com.wanmi.sbc.customer.api.response.liveroom;

import com.wanmi.sbc.customer.bean.vo.LiveReplayVO;
import com.wanmi.sbc.customer.bean.vo.LiveRoomVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p>直播间列表结果</p>
 * @author zwb
 * @date 2020-06-06 18:28:57
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LiveRoomReplayListResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 视频id
     */
    @ApiModelProperty(value = "视频ID")
    private Long id;

    /**
     * 回放视频信息
     */
    @ApiModelProperty(value = "回放视频信息")
    private List<LiveReplayVO> liveReplay;

    /**
     * 视频总条数
     */
    @ApiModelProperty(value = "视频总条数")
    private Integer total;
    /**
     * 错误信息
     */
    @ApiModelProperty(value = "错误信息")
    private String errmsg;

    /**
     * 返回状态
     */
    @ApiModelProperty(value = "返回状态")
    private Integer errcode;
}
