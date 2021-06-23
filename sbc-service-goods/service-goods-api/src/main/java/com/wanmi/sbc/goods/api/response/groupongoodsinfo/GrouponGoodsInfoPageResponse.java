package com.wanmi.sbc.goods.api.response.groupongoodsinfo;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.goods.bean.vo.GrouponGoodsInfoVO;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>拼团活动商品信息表分页结果</p>
 * @author groupon
 * @date 2019-05-15 14:49:12
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrouponGoodsInfoPageResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 拼团活动商品信息表分页结果
     */
    private MicroServicePage<GrouponGoodsInfoVO> grouponGoodsInfoVOPage;
}
