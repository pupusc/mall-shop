package com.wanmi.sbc.goods.api.response.livegoods;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.LiveGoodsVO;
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
 * <p>直播商品分页结果</p>
 * @author zwb
 * @date 2020-06-06 18:49:08
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LiveGoodsPageResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 直播商品分页结果
     */
    @ApiModelProperty(value = "直播商品分页结果")
    private MicroServicePage<LiveGoodsVO> liveGoodsVOPage;

    /**
     * 直播间所属店铺名字
     */
    @ApiModelProperty(value = "直播间所属店铺名字")
    private Map<Long, String> storeName;

    /**
     * 规格信息
     */
    @ApiModelProperty(value = "规格信息")
   private  List<GoodsInfoVO> goodsInfoList;


}
