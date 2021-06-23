package com.wanmi.sbc.goods.api.response.goodssharerecord;

import com.wanmi.sbc.goods.bean.vo.GoodsShareRecordVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>根据id查询任意（包含已删除）商品分享信息response</p>
 * @author zhangwenchang
 * @date 2020-03-06 13:46:24
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsShareRecordByIdResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 商品分享信息
     */
    @ApiModelProperty(value = "商品分享信息")
    private GoodsShareRecordVO goodsShareRecordVO;
}
