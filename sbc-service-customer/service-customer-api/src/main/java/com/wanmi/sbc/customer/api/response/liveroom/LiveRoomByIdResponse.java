package com.wanmi.sbc.customer.api.response.liveroom;

import com.wanmi.sbc.customer.bean.vo.LiveGoodsByWeChatVO;
import com.wanmi.sbc.customer.bean.vo.LiveGoodsInfoVO;
import com.wanmi.sbc.customer.bean.vo.LiveRoomVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>根据id查询任意（包含已删除）直播间信息response</p>
 * @author zwb
 * @date 2020-06-06 18:28:57
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LiveRoomByIdResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 直播间信息
     */
    @ApiModelProperty(value = "直播间信息")
    private LiveRoomVO liveRoomVO;

    /**
     * 直播间商品
     */
    @ApiModelProperty(value = "直播间商品")
    private List<LiveGoodsByWeChatVO> liveGoodsList;

    /**
     * 直播间商品
     */
    @ApiModelProperty(value = "直播间商品")
    private  List<LiveGoodsInfoVO> goodsInfoVOList;

    /**
     * 所属店铺名称
     */
    @ApiModelProperty(value = "所属店铺名称")
    private String storeName;
}
