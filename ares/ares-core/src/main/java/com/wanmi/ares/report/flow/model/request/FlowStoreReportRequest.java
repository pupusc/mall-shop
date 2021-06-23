package com.wanmi.ares.report.flow.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @ClassName FlowStoreReportRequest
 * @Description TODO
 * @Author lvzhenwei
 * @Date 2019/9/11 10:08
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FlowStoreReportRequest implements Serializable {

    private static final long serialVersionUID = 3321313805255602819L;

    /**
     * 开始时候
     * 20170925
     */
    private LocalDate beginDate;

    /**
     * 结束时间
     * 20170926
     */
    private LocalDate endDate;

}
