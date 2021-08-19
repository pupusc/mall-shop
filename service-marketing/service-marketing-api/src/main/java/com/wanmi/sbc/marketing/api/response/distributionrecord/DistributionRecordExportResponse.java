package com.wanmi.sbc.marketing.api.response.distributionrecord;

import com.wanmi.sbc.marketing.bean.vo.DistributionRecordVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p>DistributionRecord导出结果</p>
 * @author of2975
 * @date 2019-04-30 10:28:48
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DistributionRecordExportResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * DistributionRecord导出结果
     */
    private List<DistributionRecordVO> distributionRecordVOList;
}
