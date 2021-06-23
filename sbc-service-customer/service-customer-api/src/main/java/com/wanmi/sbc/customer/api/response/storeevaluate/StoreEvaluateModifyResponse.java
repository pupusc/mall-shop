package com.wanmi.sbc.customer.api.response.storeevaluate;

import com.wanmi.sbc.customer.bean.vo.StoreEvaluateVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>店铺评价修改结果</p>
 * @author liutao
 * @date 2019-02-26 10:23:32
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreEvaluateModifyResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 已修改的店铺评价信息
     */
    private StoreEvaluateVO storeEvaluateVO;
}
