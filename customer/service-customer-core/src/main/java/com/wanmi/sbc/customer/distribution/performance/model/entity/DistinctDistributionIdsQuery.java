package com.wanmi.sbc.customer.distribution.performance.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

/**
 * <p>去重后的分销员id查询参数Bean</p>
 * Created by of628-wenzhi on 2019-04-17-18:19.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DistinctDistributionIdsQuery {

    private Pageable pageable;

    /**
     * 开始日期
     */
    private LocalDate startDate;

    /**
     * 结束日期
     */
    private LocalDate endDate;
}
