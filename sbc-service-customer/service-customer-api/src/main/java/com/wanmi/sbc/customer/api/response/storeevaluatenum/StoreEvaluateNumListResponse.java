package com.wanmi.sbc.customer.api.response.storeevaluatenum;

import com.wanmi.sbc.customer.bean.vo.StoreEvaluateNumVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.io.Serializable;

/**
 * <p>店铺统计评分等级人数统计列表结果</p>
 * @author liutao
 * @date 2019-03-04 10:55:28
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreEvaluateNumListResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 店铺统计评分等级人数统计列表结果
     */
    private List<StoreEvaluateNumVO> storeEvaluateNumVOList;
}
