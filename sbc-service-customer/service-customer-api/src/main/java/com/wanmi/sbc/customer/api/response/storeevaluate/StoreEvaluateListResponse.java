package com.wanmi.sbc.customer.api.response.storeevaluate;

import com.wanmi.sbc.customer.bean.vo.StoreEvaluateVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.io.Serializable;

/**
 * <p>店铺评价列表结果</p>
 * @author liutao
 * @date 2019-02-26 10:23:32
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreEvaluateListResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 店铺评价列表结果
     */
    private List<StoreEvaluateVO> storeEvaluateVOList;
}
