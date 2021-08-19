package com.wanmi.sbc.customer.api.response.liveroom;

import com.wanmi.sbc.customer.bean.vo.LiveRoomByWeChatVO;
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
public class LiveRoomListByWeChatResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 直播间列表结果
     */
    @ApiModelProperty(value = "直播间列表结果")
    private List<LiveRoomByWeChatVO> roomInfo;


    /**
     * errcode
     */
    @ApiModelProperty(value = "errcode")
    private Integer errcode;


    /**
     * errmsg
     */
    @ApiModelProperty(value = "errmsg")
    private String errmsg;



    /**
     * errcode
     */
    @ApiModelProperty(value = "errcode")
    private Integer total;

    /**
     * 直播间列表结果
     */
    @ApiModelProperty(value = "直播间列表结果")
    private List live_replay;


}
