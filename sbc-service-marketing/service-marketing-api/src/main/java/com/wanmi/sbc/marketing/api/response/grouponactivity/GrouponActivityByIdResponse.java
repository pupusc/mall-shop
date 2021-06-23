package com.wanmi.sbc.marketing.api.response.grouponactivity;

import com.wanmi.sbc.goods.bean.vo.GrouponGoodsInfoVO;
import com.wanmi.sbc.marketing.bean.vo.GrouponActivityVO;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p>根据id查询任意（包含已删除）拼团活动信息表信息response</p>
 *
 * @author groupon
 * @date 2019-05-15 14:02:38
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrouponActivityByIdResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 拼团活动
     */
    private GrouponActivityVO grouponActivity;

    /**
     * 拼团活动商品列表
     */
    private List<GrouponGoodsInfoVO> grouponGoodsInfos;
}
