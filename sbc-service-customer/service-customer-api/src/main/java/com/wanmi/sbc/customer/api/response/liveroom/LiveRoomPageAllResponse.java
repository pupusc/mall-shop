package com.wanmi.sbc.customer.api.response.liveroom;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.customer.bean.vo.LiveGoodsByWeChatVO;
import com.wanmi.sbc.customer.bean.vo.LiveRoomVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

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
public class LiveRoomPageAllResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 直播间分页结果
     */
    @ApiModelProperty(value = "直播间分页结果")
    private MicroServicePage<LiveRoomVO> liveRoomVOPage;

    /**
     * 直播间所属店铺名字
     */
    @ApiModelProperty(value = "直播间所属店铺名字")
    private  Map<Long, String> storeName;

    /**
     * 直播间商品
     */
    @ApiModelProperty(value = "直播间商品")
    private   Map<Long, List<LiveGoodsByWeChatVO>> liveGoodsList;
}
