package com.wanmi.sbc.goods.api.response.groupongoodsinfo;

import com.wanmi.sbc.goods.bean.vo.GrouponGoodsInfoVO;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>根据id查询任意（包含已删除）拼团活动商品信息表信息response</p>
 * @author groupon
 * @date 2019-05-15 14:49:12
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrouponGoodsInfoByIdResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 拼团活动商品信息表信息
     */
    private GrouponGoodsInfoVO grouponGoodsInfoVO;
}
