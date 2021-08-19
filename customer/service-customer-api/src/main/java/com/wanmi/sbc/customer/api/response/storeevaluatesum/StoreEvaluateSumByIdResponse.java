package com.wanmi.sbc.customer.api.response.storeevaluatesum;

import com.wanmi.sbc.customer.bean.vo.StoreEvaluateSumVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>根据id查询任意（包含已删除）店铺评价信息response</p>
 * @author liutao
 * @date 2019-02-23 10:59:09
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreEvaluateSumByIdResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 店铺评价信息
     */
    private StoreEvaluateSumVO storeEvaluateSumVO;
}
