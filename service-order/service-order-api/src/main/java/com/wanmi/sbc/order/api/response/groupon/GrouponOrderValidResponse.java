package com.wanmi.sbc.order.api.response.groupon;

import com.wanmi.sbc.goods.bean.vo.GrouponGoodsInfoVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: gaomuwei
 * @Date: Created In 上午11:41 2019/5/27
 * @Description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrouponOrderValidResponse implements Serializable {

    private static final long serialVersionUID = -1147003148832055281L;

    /**
     * 拼团活动商品
     */
    private GrouponGoodsInfoVO grouponGoodsInfo;

}
