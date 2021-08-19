package com.wanmi.sbc.marketing.api.response.distributionrecord;

import com.wanmi.sbc.marketing.bean.vo.DistributionRecordVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>DistributionRecord修改结果</p>
 * @author baijz
 * @date 2019-02-25 10:28:48
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DistributionRecordModifyResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 返回状态
     */
    private DistributionRecordVO distributionRecordVO;
}
