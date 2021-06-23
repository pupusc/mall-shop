package com.wanmi.sbc.goods.api.response.liveroomlivegoodsrel;

import com.wanmi.sbc.customer.bean.vo.LiveGoodsByWeChatVO;
import com.wanmi.sbc.goods.bean.vo.LiveRoomLiveGoodsRelVO;
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
 * <p>直播房间和直播商品关联表列表结果</p>
 * @author zwb
 * @date 2020-06-08 09:12:17
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LiveRoomLiveGoodsRelListByRoomIdsResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 直播房间和直播商品关联表列表结果
     */
    @ApiModelProperty(value = "直播房间和直播商品关联表列表结果")
    private Map<Long, List<LiveGoodsByWeChatVO>> result;
}
