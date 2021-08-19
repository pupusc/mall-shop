package com.wanmi.sbc.goods.api.response.livegoods;

import com.wanmi.sbc.goods.bean.vo.LiveGoodsVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>直播商品审核结果</p>
 * @author zwb
 * @date 2020-06-06 18:49:08
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LiveGoodsAuditResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 已新增的直播商品信息
     */
    @ApiModelProperty(value = "已新增的直播商品信息")
    private LiveGoodsVO liveGoodsVO;
}
