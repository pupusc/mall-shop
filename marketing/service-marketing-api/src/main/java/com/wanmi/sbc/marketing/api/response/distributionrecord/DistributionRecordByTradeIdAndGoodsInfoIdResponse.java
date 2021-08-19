package com.wanmi.sbc.marketing.api.response.distributionrecord;

import com.wanmi.sbc.marketing.bean.vo.DistributionRecordVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>根据id查询任意（包含已删除）DistributionRecord信息response</p>
 * @author baijz
 * @date 2019-02-25 10:28:48
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DistributionRecordByTradeIdAndGoodsInfoIdResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * DistributionRecord信息
     */
    private DistributionRecordVO distributionRecordVO;
}
