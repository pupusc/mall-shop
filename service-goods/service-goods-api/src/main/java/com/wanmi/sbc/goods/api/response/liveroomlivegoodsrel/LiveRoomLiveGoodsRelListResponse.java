package com.wanmi.sbc.goods.api.response.liveroomlivegoodsrel;

import com.wanmi.sbc.goods.bean.vo.LiveRoomLiveGoodsRelVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>直播房间和直播商品关联表列表结果</p>
 * @author zwb
 * @date 2020-06-08 09:12:17
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LiveRoomLiveGoodsRelListResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 直播房间和直播商品关联表列表结果
     */
    @ApiModelProperty(value = "直播房间和直播商品关联表列表结果")
    private List<LiveRoomLiveGoodsRelVO> liveRoomLiveGoodsRelVOList;
}
