package com.wanmi.sbc.elastic.api.response.customer;

import com.wanmi.sbc.common.base.MicroServicePage;

import com.wanmi.sbc.customer.bean.vo.StoreEvaluateSumVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EsStoreEvaluateSumPageResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 店铺评价分页结果
     */
    private MicroServicePage<StoreEvaluateSumVO> storeEvaluateSumVOPage;
}
