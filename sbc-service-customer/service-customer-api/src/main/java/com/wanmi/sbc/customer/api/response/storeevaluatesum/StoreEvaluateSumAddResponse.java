package com.wanmi.sbc.customer.api.response.storeevaluatesum;

import com.wanmi.sbc.customer.bean.vo.StoreEvaluateSumVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>店铺评价新增结果</p>
 * @author liutao
 * @date 2019-02-23 10:59:09
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreEvaluateSumAddResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 已新增的店铺评价信息
     */
    private StoreEvaluateSumVO storeEvaluateSumVO;
}
