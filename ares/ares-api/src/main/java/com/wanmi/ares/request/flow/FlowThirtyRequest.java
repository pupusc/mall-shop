package com.wanmi.ares.request.flow;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @ClassName FlowThirtyRequest
 * @Description TODO
 * @Author lvzhenwei
 * @Date 2019/8/27 16:44
 **/
@Data
public class FlowThirtyRequest implements Serializable {

    private static final long serialVersionUID = -6292173514936981978L;

    private Long id;

    /**
     * 店铺id
     */
    private String companyId;

    /**
     * 统计开始时间
     */
    private LocalDate beginDate;

    /**
     * 统计结束时间
     */
    private LocalDate endDate;

    /**
     * 统计时间
     */
    private LocalDate flowDate;
}
