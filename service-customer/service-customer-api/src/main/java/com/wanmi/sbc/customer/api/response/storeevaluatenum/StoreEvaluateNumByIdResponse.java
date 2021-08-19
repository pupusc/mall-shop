package com.wanmi.sbc.customer.api.response.storeevaluatenum;

import com.wanmi.sbc.customer.bean.vo.StoreEvaluateNumVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>根据id查询任意（包含已删除）店铺统计评分等级人数统计信息response</p>
 * @author liutao
 * @date 2019-03-04 10:55:28
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreEvaluateNumByIdResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 店铺统计评分等级人数统计信息
     */
    private StoreEvaluateNumVO storeEvaluateNumVO;
}
