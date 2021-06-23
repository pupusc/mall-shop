package com.wanmi.sbc.customer.api.response.liveroom;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.customer.bean.vo.LiveRoomVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>直播间分页结果</p>
 * @author zwb
 * @date 2020-06-06 18:28:57
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LiveRoomPageResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 直播间分页结果
     */
    @ApiModelProperty(value = "直播间分页结果")
    private MicroServicePage<LiveRoomVO> liveRoomVOPage;


}
