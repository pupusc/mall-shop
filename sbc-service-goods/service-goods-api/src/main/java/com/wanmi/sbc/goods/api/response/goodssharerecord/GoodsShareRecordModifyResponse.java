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
 * <p>商品分享修改结果</p>
 * @author zhangwenchang
 * @date 2020-03-06 13:46:24
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsShareRecordModifyResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 已修改的商品分享信息
     */
    @ApiModelProperty(value = "已修改的商品分享信息")
    private GoodsShareRecordVO goodsShareRecordVO;
}
