package com.wanmi.sbc.goods.api.response.goodssharerecord;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.goods.bean.vo.GoodsShareRecordVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>商品分享分页结果</p>
 * @author zhangwenchang
 * @date 2020-03-06 13:46:24
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsShareRecordPageResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 商品分享分页结果
     */
    @ApiModelProperty(value = "商品分享分页结果")
    private MicroServicePage<GoodsShareRecordVO> goodsShareRecordVOPage;
}
