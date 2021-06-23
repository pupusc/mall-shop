package com.wanmi.sbc.marketing.api.response.distributionrecord;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.marketing.bean.vo.DistributionRecordVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>DistributionRecord分页结果</p>
 * @author baijz
 * @date 2019-02-25 10:28:48
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DistributionRecordPageResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * DistributionRecord分页结果
     */
    private MicroServicePage<DistributionRecordVO> distributionRecordVOPage;
}
