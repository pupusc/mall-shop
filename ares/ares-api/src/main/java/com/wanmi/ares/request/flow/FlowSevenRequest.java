package com.wanmi.ares.request.flow;


import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @ClassName FlowSevenRequest
 * @Description TODO
 * @Author lvzhenwei
 * @Date 2019/8/27 16:29
 **/
@Data
public class FlowSevenRequest implements Serializable {

    private static final long serialVersionUID = 7283867528220499592L;

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
