package com.wanmi.sbc.goods.api.response.groupongoodsinfo;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.goods.bean.vo.GrouponGoodsVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>拼团活动spu列表返回对象</p>
 *
 * @author chenli
 * @date 2019-05-21 14:49:12
 */

@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrouponGoodsPageResponse implements Serializable {

    private static final long serialVersionUID = -4863055961805354652L;

    /**
     * 我的优惠券列表
     */
    @ApiModelProperty(value = "拼团活动spu列表")
    private MicroServicePage<GrouponGoodsVO> grouponGoodsVOS = new MicroServicePage<>(new ArrayList<>());
}